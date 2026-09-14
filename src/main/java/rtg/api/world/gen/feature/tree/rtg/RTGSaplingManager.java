package rtg.api.world.gen.feature.tree.rtg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import rtg.api.util.Direction;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoVariableAcacia;
import rtg.api.world.deco.DecoVariableBirch;
import rtg.api.world.deco.DecoVariableDarkOak;
import rtg.api.world.deco.DecoVariableOak;
import rtg.api.world.deco.DecoVariableSpruce;

public class RTGSaplingManager {
	
	/* algorithm: REGISTRY has the actions to be performed on a growing sapling 
	 * SAPLINGS stores the tree materials
	 * a sapling block can be in SAPLINGS but not REGISTRY, meaning RTG can use that sapling's
	 * material in an RTG tree, but cannot make an RTG tree of that sapling
	 * Something should not be in REGISTRY but not SAPLINGS hence private;
	 */
	private static HashMap<IBlockState,RTGSaplingAction> REGISTRY = vanillaActions();
	
	private static HashMap<IBlockState, TreeMaterials> SAPLINGS = vanillaMaterials();
	
	public static void register(IBlockState sapling, TreeMaterials materials, RTGSaplingAction action) {
		REGISTRY.put(sapling, action);
		SAPLINGS.put(sapling, materials);
	}
	
	public static void setSapling(IBlockState sapling, TreeMaterials materials)  {
		if (materials == null) throw new RuntimeException();
		if (sapling == null) throw new RuntimeException();
		
		SAPLINGS.put(sapling, materials);
	}
	
	public static boolean isSapling(IBlockState possible) {
		IBlockState possibleUngrown = possible;
		if (possible.getPropertyKeys().contains(BlockSapling.STAGE)) {
		    possibleUngrown = possible.withProperty(BlockSapling.STAGE, Integer.valueOf(0));
		}
		return SAPLINGS.containsKey(possibleUngrown);
	}
	
	public static boolean manages(IBlockState possible) {
		IBlockState possibleUngrown = possible;
		if (possible.getPropertyKeys().contains(BlockSapling.STAGE)) {
		    possibleUngrown = possible.withProperty(BlockSapling.STAGE, Integer.valueOf(0));
		}
		return REGISTRY.containsKey(possibleUngrown);
	}
	
	public static TreeMaterials materialsFor(IBlockState sapling) {
		IBlockState saplingUngrown = sapling;
		if (sapling.getPropertyKeys().contains(BlockSapling.STAGE)) {
		    saplingUngrown = sapling.withProperty(BlockSapling.STAGE, Integer.valueOf(0));
		}
		return SAPLINGS.get(saplingUngrown);
	}
	
	public static RTGSaplingAction actionFor(IBlockState sapling) {
		IBlockState saplingUngrown = sapling;
		if (sapling.getPropertyKeys().contains(BlockSapling.STAGE)) {
		    saplingUngrown = sapling.withProperty(BlockSapling.STAGE, Integer.valueOf(0));
		}
		return REGISTRY.get(saplingUngrown);
	}
	

	private static HashMap<IBlockState, RTGSaplingAction> vanillaActions() {
		HashMap<IBlockState, RTGSaplingAction> result = new HashMap<>();
		result.put(Blocks.SAPLING.getDefaultState(),new OakSaplingAction());
		result.put(Blocks.SAPLING.getStateFromMeta(1),new SpruceSaplingAction());
		result.put(Blocks.SAPLING.getStateFromMeta(2),new RTGSaplingAction(new DecoVariableBirch()));
		result.put(Blocks.SAPLING.getStateFromMeta(4),new RTGSaplingAction(new DecoVariableAcacia()));
		result.put(Blocks.SAPLING.getStateFromMeta(5),new DarkOakSaplingAction());
		return result;
	}
	
	private static HashMap<IBlockState,TreeMaterials> vanillaMaterials() {
		HashMap<IBlockState, TreeMaterials> result = new HashMap<>();
		result.put(Blocks.SAPLING.getDefaultState(),TreeMaterials.Picker.oak);
		result.put(Blocks.SAPLING.getStateFromMeta(1),TreeMaterials.spruce);
		result.put(Blocks.SAPLING.getStateFromMeta(2),TreeMaterials.Picker.birch);
		result.put(Blocks.SAPLING.getStateFromMeta(3),TreeMaterials.jungle);
		result.put(Blocks.SAPLING.getStateFromMeta(4),TreeMaterials.Picker.acacia);
		result.put(Blocks.SAPLING.getStateFromMeta(5),TreeMaterials.Picker.darkOak);
		return result;
	}
	
	
	 public static boolean manage(SaplingGrowTreeEvent event) {
		 
		final World world = event.getWorld();
	    final BlockPos pos = event.getPos();
	    IBlockState saplingBlock = world.getBlockState(pos);
	        
		 // is this a sapling we know anything about

	    if (!isSapling(saplingBlock)) return false;
	    // big enough to use
        int groupSize = countSaplingGroup(world,pos);
        
        if (groupSize == 1) return false; // lone saplings grow as vanilla


        
        
        //now check to see if this is not in the center of its group
        //by looking for adjacent directions with more around it
        
        BlockPos trunkLocation = pos;
        // if the sapling is not one that can generate a tree make it ineligible to be the center
        if (!manages(world.getBlockState(trunkLocation))) {groupSize = 1;}
        
        
        // find the adjacent block with a managed sapling and the largest number adjacent
        for (Direction direction: Direction.list()) {
        	BlockPos testLocation = direction.moved(pos);
        	int testCount = countSaplingGroup(world,testLocation);

            //Logger.info("test size {} {} {}", testCount, pos, testLocation);
        	if (testCount > groupSize) {
        		if (manages(world.getBlockState(testLocation))) {
        		    groupSize = testCount;
        		    trunkLocation = testLocation;
        		}
        		
        	}
        }
        // center sapling is the controller
        saplingBlock = world.getBlockState(trunkLocation);
        // return if there is no action
        
        if (!manages(saplingBlock)) return false;
        
        
        // Determine height
        int actualHeight = 6;
        actualHeight += (groupSize -2)*5 + event.getRand().nextInt(5);///;
        
        // determine log and leaves

        SaplingCounter saplingCounter = new SaplingCounter(world,trunkLocation);
        
        IBlockState log = saplingCounter.log();
        IBlockState leaves = saplingCounter.leaves();
        // DecoTree set branches by default when the log is set
        boolean success = actionFor(saplingBlock).generate(world, event.getRand(), trunkLocation, actualHeight, log, leaves);
        if (!success) return false; // didn't work; let vanilla handle it
        event.setResult(Event.Result.DENY);
        // clean up adjacent saplings
        RTGSaplingManager.finishGeneration(event, world, trunkLocation, saplingBlock);
        return true;
        
	 }

	private static class OakSaplingAction extends RTGSaplingAction{
		// can also produce a Swamp Willow
		
		private DecoTree swampWillow = new DecoTree(new TreeRTGSalixMyrtilloides());

		public OakSaplingAction() {
			super(new DecoVariableOak());
		}
		
		//@SuppressWarnings("unused")  
		public boolean generate(World world, Random rand, BlockPos pos, int actualHeight, IBlockState log, IBlockState leaves) {
			if (obtuseAngle(world,pos)) {
				swampWillow.setLogBlock(log);
				swampWillow.setLeavesBlock(leaves);
				return swampWillow.doGenerate(world, rand, pos, actualHeight);
			}
			
			return super.generate(world, rand, pos, actualHeight, log, leaves);
			
		}
	}
	
	private static class SpruceSaplingAction extends RTGSaplingAction{
		// skips 2x2
		
		public SpruceSaplingAction() {
			super(new DecoVariableSpruce());
		}
		
		//@SuppressWarnings("unused")  
		public boolean generate(World world, Random rand, BlockPos pos, int actualHeight,  IBlockState log, IBlockState leaves) {
			if (is2x2(world,pos,Blocks.SAPLING.getStateFromMeta(1))) {
				return false;// hand back to vanilla
			}

			return super.generate(world, rand, pos, actualHeight, log, leaves);
		}
	}
	
	private static class DarkOakSaplingAction extends RTGSaplingAction{
		// skips 2x2
		
		public DarkOakSaplingAction() {
			super(new DecoVariableDarkOak());
		}
		
		public boolean generate(World world, Random rand, BlockPos pos, int actualHeight,  IBlockState log, IBlockState leaves) {
			if (is2x2(world,pos,Blocks.SAPLING.getStateFromMeta(5))) {
				return false;// hand back to vanilla
			}

			return super.generate(world, rand, pos, actualHeight, log, leaves);
		}
	}
	
    private static int countSaplingGroup(World world, BlockPos pos) {
    	if (!isSapling(world.getBlockState(pos))) return  0;
    	int found = 1;
    	for (Direction direction: Direction.list()) {
    		if (isSapling((world.getBlockState(direction.moved(pos))))) {
    			found++;
    		}
    	}
    	return found;
    }
    
     static boolean obtuseAngle(World world, BlockPos pos) {
    	ArrayList<Direction> found = new ArrayList<>();
    		
    	for (Direction direction: Direction.list()) {
    		if (isSapling(world.getBlockState(direction.moved(pos)))) {
    			found.add(direction);
    		}
    	}
    	if (found.size() == 2) {
    		//must be two
    		int different = found.get(1).index - found.get(0).index;
    		if (different==3) return true;// obtuse angle
    		if (different==5) return true;// obtuse the other way
    	}
    	return false;
    }
     
     private static boolean is2x2(World world, BlockPos pos,IBlockState saplingBlock) {
    	 
    	 if (countSaplingGroup(world,pos)!=4) return false;
     	
     	// northeast
     	if (world.getBlockState(pos.north()) == saplingBlock) 
     		if (world.getBlockState(pos.east()) == saplingBlock)
         		if (world.getBlockState(pos.east().north()) == saplingBlock) return true;

     	// northwest
     	if (world.getBlockState(pos.north()) == saplingBlock) 
     		if (world.getBlockState(pos.west()) == saplingBlock)
         		if (world.getBlockState(pos.west().north()) == saplingBlock) return true;
     	// southeast
     	if (world.getBlockState(pos.south()) == saplingBlock) 
     		if (world.getBlockState(pos.east()) == saplingBlock)
         		if (world.getBlockState(pos.east().south()) == saplingBlock) return true;

     	// southwest
     	if (world.getBlockState(pos.south()) == saplingBlock) 
     		if (world.getBlockState(pos.west()) == saplingBlock)
         		if (world.getBlockState(pos.west().south()) == saplingBlock) return true;
     	
     	// none of the above
     	return false;
     }
     
     private static void finishGeneration(SaplingGrowTreeEvent event,World world, BlockPos trunkLocation, IBlockState saplingBlock) {
         event.setResult(Event.Result.DENY);
         // Sometimes we have to remove the sapling manually because some trees grow around it, leaving the original sapling.
         if (isSapling(world.getBlockState(trunkLocation))) {
             world.setBlockState(trunkLocation, Blocks.AIR.getDefaultState(), 2);
         }
     	for (Direction direction: Direction.list()) {
     		BlockPos adjacent = direction.moved(trunkLocation);
             if (isSapling(world.getBlockState(adjacent))) {
                 world.setBlockState(adjacent, Blocks.AIR.getDefaultState(), 2);
             }
     	}
     }
     
}
