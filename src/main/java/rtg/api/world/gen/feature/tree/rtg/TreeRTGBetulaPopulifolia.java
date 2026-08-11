package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.util.BlockCenteredPresence;
import rtg.api.util.Direction;
import rtg.api.world.terrain.TerrainBase;

public class TreeRTGBetulaPopulifolia extends TreeRTG {

    /**
     * <b>Betula Populifolia (Gray Birch)</b>
     */
	
    public TreeRTGBetulaPopulifolia() {

        super();
    }
    
	public float estimatedSize() {

    	float branchLength= (float)crownSize/3f;
    	return branchLength*branchLength/16f;
	}
	
	@Override
    public int furthestLikelyExtension() {

    	int branchLength= (crownSize+2)/3;
    	return branchLength;
	}
	
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {

        if (!this.isGroundValid(world, pos)) {
            return false;
        }
        
        return new BirchGenerator(world,rand,pos).generate();
    }
    
    private class BirchGenerator {
    	
    	World world;
    	Random rand;
    	BlockPos pos;
    	SkylightTracker lightTracker;
    	float extension ;
    	int lastCertainPlacement;
    	boolean biasLeft;// whether corner squares are assigned to branches which have them on the left.
        float bayesianShift = 1.5f;
        float leafSuccess = .9f;
        final int maxDistance = 5;
        BlockCenteredPresence below;
        BlockCenteredPresence current;
        float growth;
    	
    	BirchGenerator(World _world, Random _rand, BlockPos _pos) {
    		world = _world;
    		rand  = _rand;
    		pos = _pos;
    		current =  new BlockCenteredPresence(maxDistance,pos);
    		
    	}
    
    	boolean generate() {


            lightTracker = new SkylightTracker(furthestLikelyExtension(),pos,world);
            
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            
            int i, j;
            for (i = 0; i < trunkSize; i++) {
                placeTrunkBlock(world, new BlockPos(x, y, z),  generateFlag,lightTracker);
                y++;
            }
            
            
            for (i = 0; i < crownSize; i++) {
            	below = current;
            	current = new BlockCenteredPresence(maxDistance,pos);
            	BlockPos trunkBlock = new BlockPos(x, y+i, z);
            	if ((crownSize -i)> 2 ) {
                   current.set(trunkBlock,placeTrunkBlock(world, trunkBlock,  generateFlag,lightTracker));
            	} else {
            		current.set(trunkBlock,placeLeavesBlock(world, trunkBlock,  leavesBlock, generateFlag,lightTracker));
             	}
            	float heightFraction = (float)i/(float)crownSize;

            	growth = .35f - heightFraction*.15f;
            	// bias upwards 
            	// extra spread on top
            	heightFraction *= .9;
            	heightFraction = TerrainBase.bayesianAdjustment(heightFraction, bayesianShift + rand.nextFloat());
            	extension = heightFraction*(1f-heightFraction)*5f*2f + 1f;
            	lastCertainPlacement = (int)Math.floor((double)extension);
            	biasLeft = (rand.nextFloat()< 0.5f);
            	// build base branches
            	Direction.cardinalList().forEach(direction -> {buildBranch(direction,trunkBlock);});
            	// build initial corners
            	if (biasLeft) {
	            	Direction.cardinalList().forEach(direction -> {
	            		BlockPos leafPos = direction.moved(direction.rightAngleLeft().moved(trunkBlock));
	            		tryBuildLeaves(direction,direction.rightAngleLeft(),leafPos,rand);
	            	});
            	} else {
	            	Direction.cardinalList().forEach(direction -> {
	            		BlockPos leafPos = direction.moved(direction.rightAngleRight().moved(trunkBlock));
	            		tryBuildLeaves(direction,direction.rightAngleRight(),leafPos,rand);
	            	});
            	}
            	// build side branches
            	for (int out = 2; out <= lastCertainPlacement +1; out ++) {
            		final int ring  = out;
            		
            		float leftStop = extension + rand.nextFloat();
            		final float leftStopSquared = leftStop*leftStop;
            		

            		float rightStop = extension + rand.nextFloat();
            		final float rightStopSquared = rightStop*rightStop;
            		
            		
            		for (int branchout = 1; branchout < ring; branchout ++) {
            			final int branchRing = branchout;
    	            	Direction.cardinalList().forEach(direction -> {
    	            		BlockPos leafPos;
    	            		if (rightStopSquared >ring*ring + branchRing*branchRing) {// not too far from trunk
    	            		    leafPos = direction.moved(direction.rightAngleRight().moved(trunkBlock,branchRing),ring);
    	            		    tryBuildLeaves(direction,direction.rightAngleRight(),leafPos,rand);
    	            		}

    	            		if (leftStopSquared >ring*ring + branchRing*branchRing) {// not too far from trunk
	    	            		leafPos = direction.moved(direction.rightAngleLeft().moved(trunkBlock,branchRing),ring);
	    	            		tryBuildLeaves(direction,direction.rightAngleLeft(),leafPos,rand);
    	            		}
    	            	});
            		}
            		
            		// then the corners
            		if (biasLeft) {

	            		if (rightStopSquared >2*ring*ring ) {// not too far from trunk
    	            	Direction.cardinalList().forEach(direction -> {
    	            		BlockPos leafPos = direction.moved(direction.rightAngleLeft().moved(trunkBlock,ring),ring);
    	            		tryBuildLeaves(direction,direction.rightAngleLeft(),leafPos,rand);
    	            	});
	            		}
    	            	
            		} else {
	            		if (leftStopSquared >2*ring*ring ) {// not too far from trunk
	    	            	Direction.cardinalList().forEach(direction -> {
	    	            		BlockPos leafPos = direction.moved(direction.rightAngleRight().moved(trunkBlock,ring),ring);
	    	            		tryBuildLeaves(direction,direction.rightAngleRight(),leafPos,rand);
	    	            	});
	            		}
            			
            		}
            	}
            }

            BlockPos top = new BlockPos(x, y+crownSize, z);
            if (rand.nextFloat() <0.4f) {
                //current.set(top,placeLeavesBlock(world, top, leavesBlock, generateFlag,lightTracker));
            }
            lightTracker.checkLighting(world);
            return true;
    	}
    	
        private void buildBranch(Direction branch,BlockPos base) {
        	boolean previousPlaced = true;
        	BlockPos placement = branch.moved(base);
        	for (int i = 0; i < lastCertainPlacement; i++) {
        		float success = 0f;
        		if (previousPlaced) {success = 0.8f;}
        		if (below.present(placement)) success += 0.2f;
        		success *= leafSuccess;
        		if (success>0f &&rand.nextFloat()<success) {
        			previousPlaced = placeLeavesBlock(world, placement,  leavesBlock, generateFlag,lightTracker);
        			current.set(placement, previousPlaced);
        			
        		} else {
        			previousPlaced = false;
        			// current is set false at start and currently no redo;
        		}

            	placement = branch.moved(placement);
        	}
        	if (rand.nextFloat() + lastCertainPlacement > extension) {// then try to place one more
        		float success = 0f;
        		if (previousPlaced) {success = 0.5f;}
        		if (below.present(placement)) success += 0.5f;
        		success *= leafSuccess;
        		if (success>0f &&rand.nextFloat()<success) {
        			previousPlaced = placeLeavesBlock(world, placement,  leavesBlock, generateFlag,lightTracker);
        			current.set(placement, previousPlaced);
        			
        		} else {
        			previousPlaced = false;
        			// current is set false at start and currently no redo;
        		}
        	}
        }
        
        private void tryBuildLeaves(Direction branch, Direction sideBranch, BlockPos position, Random rand) {
        	float success = 0f;
    		if (below.present(position)) success += growth;
    		if (current.present(position,branch.reversed())) success += growth;
    		if (current.present(position,sideBranch.reversed())) success += growth;
    		if (success > 1f) success = 1f;
    		success *= leafSuccess;
    		if (success>0f &&rand.nextFloat()<success) {
    			current.set(position, placeLeavesBlock(world, position,  getLeaves(rand), generateFlag,lightTracker));
    		}
        }
    	
    }
}