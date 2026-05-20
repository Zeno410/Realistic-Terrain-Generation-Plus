package rtg.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenLiquids;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkEvent.Load;

import rtg.RTGConfig;
import rtg.api.RTGAPI;
import rtg.api.util.BlockUtil;
import rtg.api.util.ChunkTracker;
import rtg.api.util.Direction;
import rtg.api.util.Logger;
import rtg.api.util.UtilityClass;
import rtg.api.world.biome.IRealisticBiome;
import rtg.api.world.deco.DecoBase;
import rtg.api.world.deco.DecoVariableTree;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGCeibaPentandra;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGRhizophoraMucronata;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGSalixMyrtilloides;
import rtg.api.world.gen.feature.tree.rtg.RTGSaplingManager;
import rtg.api.world.gen.feature.tree.rtg.TreeDensityLimiter;
import rtg.world.biome.BiomeProviderRTG;


@UtilityClass
public final class EventHandlerCommon
{
    private EventHandlerCommon() {}

    public static void init() {
        MinecraftForge.TERRAIN_GEN_BUS.register(EventHandlerCommon.class);
        MinecraftForge.EVENT_BUS.register(EventHandlerCommon.class);
    }

    @SubscribeEvent
    public static void fixLightingOnLoad(ChunkEvent.Load loadEvent) {
    	if (loadEvent.getWorld().isRemote) return;
    	for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16 ; z++) {
				//fixLighting(loadEvent.getChunk(),x,z);
			}
		}
	}
	    
    
    // TERRAIN_GEN_BUS
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDecorateBiome(final DecorateBiomeEvent.Decorate event) {

        final World world = event.getWorld();
    	if (world.isRemote) return;
        if (world.getBiomeProvider() instanceof BiomeProviderRTG) {
            final Decorate.EventType eventType = event.getType();
            if (eventType == Decorate.EventType.LAKE_WATER || eventType == Decorate.EventType.LAKE_LAVA) {
                event.setResult(Event.Result.DENY);
                generateFalls(world, event.getRand(), event.getChunkPos(), eventType);
            }
        }
    }
    
    private static ChunkTracker lightChecked = new ChunkTracker(1500);
    
    private static void fixLighting(World world, ChunkPos chunkPos) {
    	
    	// abort if we've done this chunk before
    	//if (!lightChecked.addIfNeeded(world, chunkPos)) return;
        Chunk chunk = world.getChunk(chunkPos.x,chunkPos.z);
        //chunk.generateSkylightMap();
        //chunk.checkLight();
    	//Logger.error("Relighting ChunkPos "+ chunkPos.x + "," + chunkPos.z, "Relighting ChunkPos "+ chunkPos.x + "," + chunkPos.z);
    	for (int x = 0; x < 16; x++) {
    		for (int z = 0; z < 16 ; z++) {
    			fixLighting(chunk,x,z);
    		}
    	}
    	//chunk.markDirty();
    	//chunk.setModified(true);
    }
    
    private static void fixLighting(Chunk chunk, int x, int z) {
    	BlockPos start = new BlockPos(x,255,z);
    	if (chunk.getBlockState(x, 255, z).getBlock() != Blocks.AIR) return;
    	boolean lastChecked = false;
    	int lastAir = 255;
    	for (int y = 255; y > 48; y--) {
    		IBlockState state  = chunk.getBlockState(x, y, z);
    		Block block = state.getBlock();
    		if (block == Blocks.AIR) {
    			lastAir = y;
    			lastChecked = true;
    			continue;
    		}
    		if (state.isOpaqueCube()) {
    	    	BlockPos lastAirLocation = new BlockPos(x,lastAir,z);
    	    	int lighting = chunk.getLightFor(EnumSkyBlock.SKY, lastAirLocation);
    	    	if (lighting < getAdjacentBlockLight(chunk,x,lastAir,z) -1 ) {
    	    		chunk.getWorld().setBlockState(chunk.getPos().getBlock(x, lastAir, z), Blocks.WOOL.getDefaultState(), 3);
    	    		chunk.getWorld().setBlockToAir(chunk.getPos().getBlock(x, lastAir, z));
    	    	} 
    	    	lastChecked = false;
    			continue;
    		}
    	}
    }
    
    private static int getAdjacentBlockLight(Chunk chunk, int x, int y, int z) {
    	int result = getBlockLight(chunk,x+1,y,z);
    	result = Math.max(getBlockLight(chunk,x+1,y,z+1), result);
    	result = Math.max(getBlockLight(chunk,x-1,y,z+1), result);
    	result = Math.max(getBlockLight(chunk,x-1,y,z-1), result);
    	return result;
    }
    
    private static int getBlockLight(Chunk chunk, int x, int y, int z) {
    	if ((x <0)||(x>15)||(z <0)||(z>15)) return 0;
    	return chunk.getLightFor(EnumSkyBlock.SKY, new BlockPos(x,y,z));
    }
    
    public static void fixLightingAround(World world, ChunkPos pos) {

        if (world.getBiomeProvider() instanceof BiomeProviderRTG) {
        	Chunk targeted;
        	if (world.isChunkGeneratedAt(pos.x+1,pos.z+1)) {
        		targeted = world.getChunk(pos.x+1,pos.z+1);
        		if (targeted.isPopulated())
            	    fixLighting(world,new ChunkPos(pos.x+1,pos.z+1));
        	}
        	if (world.isChunkGeneratedAt(pos.x-1,pos.z+1)) {
            	targeted = world.getChunk(pos.x-1,pos.z+1);
        		if (targeted.isPopulated())
            	    fixLighting(world,new ChunkPos(pos.x-1,pos.z+1));
    	    }
        	if (world.isChunkGeneratedAt(pos.x-1,pos.z-1)) {
            	targeted = world.getChunk(pos.x-1,pos.z-1);
        		if (targeted.isPopulated())
            	    fixLighting(world,new ChunkPos(pos.x-1,pos.z-1));
    	    }
        	if (world.isChunkGeneratedAt(pos.x+1,pos.z-1)) {
            	targeted = world.getChunk(pos.x+1,pos.z-1);
        		if (targeted.isPopulated())
            	    fixLighting(world,new ChunkPos(pos.x+1,pos.z-1));
    	    }
        }
    }

    private static void generateFalls(final World world, final Random rand, final ChunkPos chunkPos, final Decorate.EventType type) {
        final BlockPos offsetpos = new BlockPos(chunkPos.x * 16 + 8, 0, chunkPos.z * 16 + 8);
        switch (type) {
            case LAKE_WATER:
                // reduced chance due to reduced random y level
                for (int i = 0; i < 20; i++) {
                    (new WorldGenLiquids(Blocks.FLOWING_WATER))
                        .generate(world, rand, offsetpos.add(rand.nextInt(16), rand.nextInt(64) + 8, rand.nextInt(16)));
                }
                break;
            case LAKE_LAVA:
                // reduced chance due to reduced random y level
                for (int i = 0; i < 5; i++) {
                    (new WorldGenLiquids(Blocks.FLOWING_LAVA))
                        .generate(world, rand, offsetpos.add(rand.nextInt(16), rand.nextInt(rand.nextInt(rand.nextInt(64) + 8) + 8), rand.nextInt(16)));
                }
                break;
            default:
        }
    }

 // TERRAIN_GEN_BUS
    @SubscribeEvent
    public static void variableSaplingGrowTreeRTG(SaplingGrowTreeEvent event) {

        final World world = event.getWorld();
        
        //Logger.info("trying RTG trees", "");

        // skip if RTG saplings are disabled or this world does not use BiomeProviderRTG
        if (!RTGConfig.rtgTreesFromSaplings() || !(world.getBiomeProvider() instanceof BiomeProviderRTG)) {
            Logger.debug("[SaplingGrowTreeEvent] Aborting: RTG trees are disabled, or not an RTG dimension");
            return;
        }

        final BlockPos pos = event.getPos();
        final IBlockState saplingBlock = world.getBlockState(pos);
        //Logger.trace("Handling SaplingGrowTreeEvent in dim: {}, at: {}, for: {}", world.provider.getDimension(), pos, saplingBlock);

        // Are we dealing with a sapling? Sounds like a silly question, but apparently it's one that needs to be asked.
        if (!(saplingBlock.getBlock() instanceof BlockSapling)) {
            Logger.debug("[SaplingGrowTreeEvent] Aborting: Sapling is not a sapling block ({})", saplingBlock.getBlock().getClass().getName());
            return;
        }

        final Random rand = event.getRand();

        // Should we generate a vanilla tree instead?
        // TODO: RTGConfig.rtgTreeChance() is obsolete with this system
        
        RTGSaplingManager saplingManager = new RTGSaplingManager();//not saving this due to sapling growth being so infrequent
        
        
        if (!saplingManager.manages(saplingBlock)) return;// do nothing if weren't not handling this sapling type
        
        int groupSize = countSaplingGroup(world,pos,saplingBlock);
        
        //Logger.info("group size{}", groupSize);
        if (groupSize == 1) return; // lone saplings grow as vanilla
        
        if (groupSize ==4) {
        	// check for 2x2, which we will hand to vanilla for some trees
        	if (saplingManager.rejectIf2x2(saplingBlock)) {
        		if (is2x2(world,pos,saplingBlock)) return;
        	}
        }
        
        //now check to see if this is not in the center of its group
        //by looking for adjacent directions with more around it
        
        BlockPos trunkLocation = pos;
        for (Direction direction: Direction.list()) {
        	BlockPos testLocation = direction.moved(pos);
        	int testCount = countSaplingGroup(world,testLocation,saplingBlock);

            //Logger.info("test size {} {} {}", testCount, pos, testLocation);
        	if (testCount > groupSize) {
        		groupSize = testCount;
        		trunkLocation = testLocation;
        	}
        }

        //Logger.info("group size{}", groupSize);
        
        // Swamp Willow special code
        if (saplingManager.couldBeSwampWillow(saplingBlock)) {
        	if (groupSize == 3) {
            	if (obtuseAngle(world,trunkLocation,saplingBlock)) {
            		new TreeRTGSalixMyrtilloides().generate(world, rand, pos);
            		finishGeneration(event,world,trunkLocation,saplingBlock);
            		return;
            	}
        	}
        }
        
        // Roofed Forest Tree Special Code
        if (saplingManager.darkOak(saplingBlock)) {
        	if (groupSize == 2) {        
        		TreeRTG pentandraTree = new TreeRTGCeibaPentandra();;
	            pentandraTree.setLogBlock(BlockUtil.getStateLog(EnumType.DARK_OAK));
	            pentandraTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.DARK_OAK));
	            pentandraTree.setMaxAllowedObstruction(TreeRTG.ROOFED_FOREST_LIGHT_OBSTRUCTION_LIMIT);
	            pentandraTree.setMinTrunkSize(2);
	            pentandraTree.setMaxTrunkSize(3);
	            pentandraTree.setMinCrownSize(5);
	            pentandraTree.setMaxCrownSize(8);
	            pentandraTree.setNoLeaves(false);
	            pentandraTree.randomizeTreeSize(rand);
	            pentandraTree.generate(world, rand, pos);
        		finishGeneration(event,world,trunkLocation,saplingBlock);
        		return;
        	}
        	if (groupSize == 3) {
                TreeRTG mucronataTree = new TreeRTGRhizophoraMucronata();
                mucronataTree.setLogBlock(BlockUtil.getStateLog(EnumType.DARK_OAK));
                mucronataTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.DARK_OAK));
                mucronataTree.setMaxAllowedObstruction(TreeRTG.ROOFED_FOREST_LIGHT_OBSTRUCTION_LIMIT);
                mucronataTree.setMinTrunkSize(2);
                mucronataTree.setMaxTrunkSize(3);
                mucronataTree.setMinCrownSize(5);
                mucronataTree.setMaxCrownSize(8);
                mucronataTree.setNoLeaves(false);
                mucronataTree.randomizeTreeSize(rand);
                mucronataTree.generate(world, rand, pos);
        		finishGeneration(event,world,trunkLocation,saplingBlock);
        		return;
        	}
        	// otherwise we're going to ignore this
        	return;
        }
        DecoVariableTree variableTree = saplingManager.tree(saplingBlock);
        
        // Determine height
        int actualHeight = variableTree.largestVanillaTree() + 1;
        if (groupSize > 2) {
        	// 2 is minimum height
        	actualHeight += (groupSize -3)*5 + rand.nextInt(5);
        }
        
        variableTree.doGenerate(world, rand, trunkLocation, actualHeight, new TreeDensityLimiter(1000000));
        finishGeneration(event,world,trunkLocation,saplingBlock);
    }
    
    private static void finishGeneration(SaplingGrowTreeEvent event,World world, BlockPos trunkLocation, IBlockState saplingBlock) {
        event.setResult(Event.Result.DENY);
        // Sometimes we have to remove the sapling manually because some trees grow around it, leaving the original sapling.
        if (RTGSaplingManager.similar(world.getBlockState(trunkLocation), saplingBlock)) {
            world.setBlockState(trunkLocation, Blocks.AIR.getDefaultState(), 2);
        }
    	for (Direction direction: Direction.list()) {
    		BlockPos adjacent = direction.moved(trunkLocation);
            if (RTGSaplingManager.similar(world.getBlockState(adjacent), saplingBlock)) {
                world.setBlockState(adjacent, Blocks.AIR.getDefaultState(), 2);
            }
    	}
    	ChunkPos chunkPos = new ChunkPos(trunkLocation);
    	fixLighting(world,chunkPos);
    }
    
    private static int countSaplingGroup(World world, BlockPos pos,IBlockState saplingBlock) {
    	if (!RTGSaplingManager.similar(world.getBlockState(pos), saplingBlock)) return  0;
    	int found = 1;
    	for (Direction direction: Direction.list()) {
    		if (RTGSaplingManager.similar(world.getBlockState(direction.moved(pos)), saplingBlock)) {
    			found++;
    		}
    	}
    	return found;
    }
    
    private static boolean obtuseAngle(World world, BlockPos pos,IBlockState saplingBlock) {
    	ArrayList<Direction> found = new ArrayList<>();
    			
    	for (Direction direction: Direction.list()) {
    		if (RTGSaplingManager.similar(world.getBlockState(direction.moved(pos)), saplingBlock)) {
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
}
