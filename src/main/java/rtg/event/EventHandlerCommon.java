package rtg.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.WeakHashMap;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenLiquids;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.ChunkEvent;

import rtg.RTGConfig;
import rtg.api.RTGAPI;
import rtg.api.event.DecorateBiomeEventRTG;
import rtg.api.util.BlockUtil;
import rtg.api.util.ChunkInfo;
import rtg.api.util.Direction;
import rtg.api.util.Logger;
import rtg.api.util.UtilityClass;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;
import rtg.api.world.deco.DecoVariableTree;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGCeibaPentandra;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGRhizophoraMucronata;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGSalixMyrtilloides;
import rtg.api.world.gen.feature.tree.rtg.RTGSaplingManager;
import rtg.api.world.gen.feature.tree.rtg.TreeDensityLimiter;
import rtg.world.biome.BiomeProviderRTG;
import rtg.world.gen.ChunkGeneratorRTG;


@UtilityClass
public final class EventHandlerCommon
{
    private EventHandlerCommon() {}
    public static TreeGenerationManager treeGenerationManager = new TreeGenerationManager();

    public static void init() {
        MinecraftForge.TERRAIN_GEN_BUS.register(EventHandlerCommon.class);
        MinecraftForge.EVENT_BUS.register(EventHandlerCommon.class);
    }

    private static boolean alreadyFixing = false;
    @SubscribeEvent
    public static void fixLightingOnLoad(ChunkEvent.Load loadEvent) {
    	if (loadEvent.getWorld().isRemote) return;
        if (!(loadEvent.getWorld().getBiomeProvider() instanceof BiomeProviderRTG)) return; 
        if (alreadyFixing) return;
        alreadyFixing = true;
    	for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16 ; z++) {
				fixLighting(loadEvent.getChunk(),x,z);
			}
		}
    	alreadyFixing = false;
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
    
    public static boolean allowNextGeneration;
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void takeoverTreeGeneration(DecorateBiomeEvent.Decorate event)
    {
    	if (!RTGConfig.treesInNonRTGWorlds()) return;// not doing this
        if (event.getType() == Decorate.EventType.TREE){
        	if (event instanceof DecorateBiomeEventRTG.DecorateRTG) return;// don't intervene when RTG is calling
        	
            if (event.getWorld().isRemote) return;// just in case;
            
            WorldChunkPos chunkID = new WorldChunkPos(event.getWorld(),event.getChunkPos());
            if (chunkIDs.contains(chunkID)) {
            	event.setResult(Event.Result.DENY);
            	return;// we're already working on this chunk;
            }
            //Logger.info(event.getWorld().toString() + " " +event.getChunkPos().toString() + chunkID.hashCode());
        	// get biome
            BlockPos center = event.getChunkPos().getBlock(8, 64, 8);
            Biome biome = event.getWorld().getBiomeProvider().getBiome(center);
            if (!treeGenerationManager.managingBiome(biome)) return; // we're not doing this biome.
        	IRealisticBiome rtgBiome = RTGAPI.getRTGBiome(biome);
        	if (rtgBiome.getConfig().DISABLE_RTG_DECORATIONS.get()) return;// user has disabled this biome
        	chunkIDs.add(chunkID);
        	
        	boolean rtgIsGenerating = false;
        	IChunkProvider iProvider = event.getWorld().getChunkProvider();
        	if (iProvider instanceof ChunkProviderServer) {
        		ChunkProviderServer provider = (ChunkProviderServer)iProvider;
        		IChunkGenerator generator = provider.chunkGenerator;
        		rtgIsGenerating = (generator instanceof ChunkGeneratorRTG);
        	}
            if (rtgIsGenerating) {
                //the chunk generator calls RTG trees
            } else {
            	ChunkInfo chunkInfo = new ChunkInfo(event.getChunkPos(), RTGWorld.getInstance(event.getWorld()));
        	    rtgBiome.getTreeDecos().generate(rtgBiome, RTGWorld.getInstance(event.getWorld()), event.getRand(), event.getChunkPos(), 0, false, chunkInfo);
            }
        	chunkIDs.remove(chunkID);
        	//Logger.info("done");
        	event.setResult(Event.Result.DENY);
        }
        if (event.getType() == Decorate.EventType.BIG_SHROOM) {
        	if (allowNextGeneration) {
        		allowNextGeneration = false;
        		return;
        	}
        	// Roofed Forests make their trees in the big mushroom event, so we have to block that.
            BlockPos center = event.getChunkPos().getBlock(8, 64, 8);
            Biome biome = event.getWorld().getBiomeProvider().getBiome(center);
            if (biome == Biomes.ROOFED_FOREST||biome == Biomes.MUTATED_ROOFED_FOREST) {
                if (treeGenerationManager.managingBiome(biome)) {
                	event.setResult(Event.Result.DENY);
                }
            }
        }
    }
    
    private static HashSet<WorldChunkPos> chunkIDs =new HashSet<>();
    
    private static class WorldChunkPos {
    	final World world;
    	final ChunkPos chunkPos;
    	WorldChunkPos(World _world, ChunkPos _chunkPos) {
    		world = _world;
    		chunkPos = _chunkPos;
    	}
    	
    	public int hashCode() {return world.hashCode() + chunkPos.hashCode();}
    	
    	public boolean equals(Object candidate) {
    		if (!(candidate instanceof WorldChunkPos)) return false;
    		WorldChunkPos compared = (WorldChunkPos)candidate;
    		return (world.equals(compared.world)&&chunkPos.equals(compared.chunkPos));
    	}
    }
    
    //private static ChunkTracker lightChecked = new ChunkTracker(1500);
    
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
        if (!RTGConfig.rtgTreesFromSaplings() ) {
            Logger.debug("[SaplingGrowTreeEvent] Aborting: RTG trees are disabled, or not an RTG dimension");
            return;
        }

        if (RTGSaplingManager.manage(event)) {
        	EventHandlerCommon.fixLighting(world, new ChunkPos(event.getPos()));
        };
    }
    
}
