package rtg.world.gen;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
//import rtg.api.util.Logger;

public class LatePopulatingChunk extends Chunk {

	public LatePopulatingChunk(World worldIn, int x, int z) {
		super(worldIn,x,z);
	}
	
	 public LatePopulatingChunk(World worldIn, ChunkPrimer primer, int x, int z) {
		 super(worldIn,primer,x,z);
	 }

	public void populate(IChunkProvider chunkProvider, IChunkGenerator chunkGenerator)
    {
		//if (this.getWorld().isChunkGeneratedAt(this.x, this.z)) return;
		new ChunkPresence(this,chunkGenerator,chunkProvider).populateAllNeeded();
    }

	// duplicating private and protected code from
	static ChunkPos prev;
	static ChunkPos populating;
    protected static void populate(Chunk chunk, IChunkGenerator generator)
    {
    	//Logger.info("Considering "+ chunk.getPos().toString() + " " + chunk.isTerrainPopulated() + " " + chunk.isLightPopulated() + chunk.isPopulated());
        if (chunk.isTerrainPopulated())
        {
            /*if (generator.generateStructures(chunk, chunk.x, chunk.z))
            {
                	chunk.markDirty();
            }*/
        }
        else
        {
            if (populating != null && net.minecraftforge.common.ForgeModContainer.logCascadingWorldGeneration) logCascadingWorldGeneration(chunk);
            ChunkPos prev = populating;
            populating = chunk.getPos();
        	chunk.checkLight();
        	//Logger.info("starting "+chunk.getPos().toString());
            generator.populate(chunk.x, chunk.z);
        	//Logger.info("done with "+chunk.getPos().toString());
            net.minecraftforge.fml.common.registry.GameRegistry.generateWorld(chunk.x, chunk.z, chunk.getWorld(), generator, chunk.getWorld().getChunkProvider());
            chunk.markDirty();
            populating = prev;
        }
    }


	
    private static void logCascadingWorldGeneration(Chunk chunk)
    {
        net.minecraftforge.fml.common.ModContainer activeModContainer = net.minecraftforge.fml.common.Loader.instance().activeModContainer();
        String format = "{} loaded a new chunk {} in dimension {} ({}) while populating chunk {}, causing cascading worldgen lag.";

        if (activeModContainer == null) { // vanilla minecraft has problems too (MC-114332), log it at a quieter level.
            net.minecraftforge.fml.common.FMLLog.log.debug(format, "Minecraft", chunk.getPos(), chunk.getWorld().provider.getDimension(), chunk.getWorld().provider.getDimensionType().getName(), populating);
            net.minecraftforge.fml.common.FMLLog.log.debug("Consider setting 'fixVanillaCascading' to 'true' in the Forge config to fix many cases where this occurs in the base game.");
        } else {
            net.minecraftforge.fml.common.FMLLog.log.warn(format, activeModContainer.getName(), chunk.getPos(), chunk.getWorld().provider.getDimension(), chunk.getWorld().provider.getDimensionType().getName(), populating);
            net.minecraftforge.fml.common.FMLLog.log.warn("Please report this to the mod's issue tracker. This log can be disabled in the Forge config.");
        }
    }

    private static int depth;
    
    private static class ChunkPresence {
    	
    	boolean [] [] generated = new boolean [8] [8];
    	Chunk center;
    	IChunkGenerator chunkGenerator;
    	IChunkProvider chunkProvider;
    	private static final int OFFSET = 3;
    	private static final int OFFSETDOWN = -3;
    	
    	
    	ChunkPresence(Chunk _center, IChunkGenerator _chunkGenerator, IChunkProvider _chunkProvider) {
    		this.center = _center;
    		chunkGenerator = _chunkGenerator;
    		chunkProvider = _chunkProvider;
    		int centerX = center.getPos().x;
    		int centerZ = center.getPos().z;
    		//Logger.info("creating "+center.getPos().toString());
    		for (int dx = OFFSETDOWN; dx <= OFFSET + 1; dx ++) {
    			//StringBuilder line = new StringBuilder();
    			for (int dz = OFFSETDOWN; dz <= OFFSET + 1; dz ++) {
    			    generated [dx + OFFSET][dz + OFFSET] =center.getWorld().isChunkGeneratedAt(centerX + dx, centerZ + dz);
    			    //line.append((generated [dx + OFFSET][dz + OFFSET])?"+":"=");
    			}
    			//Logger.info(line.toString());
    		}
    	}
    	
    	void populateAllNeeded() {
    		if (depth++>100) throw new RuntimeException();
    		for (int dx = -2; dx <= 2; dx ++) {
    			for (int dz = -2; dz <=2; dz ++) {
    				populateIfNeeded(dx,dz);
    			}
    		}
    		depth--;
    	}
    	
    	void populateIfNeeded(final int posX, final int posZ) {
    		int centerX = center.getPos().x;
    		int centerZ = center.getPos().z;
    		// check all adjacent chunks exist;
    		for (int dx = -1; dx <= 2; dx ++) {
    			for (int dz = -1; dz <=2; dz ++) {
    				/*Logger.info(")"+(centerX + posX + dx)+"," + (centerZ +posZ + dz) + ") " + generated[ posX + dx + OFFSET][ posZ + dz + OFFSET] +
    						center.getWorld().isChunkGeneratedAt(centerX +posX + dx, centerZ + posZ + dz));*/
    				if (!generated[ posX + dx + OFFSET][ posZ + dz + OFFSET]) return;
    			}
    		}
    		// they do;

    		Chunk toPopulate;
    		if (posX==0&&posZ==0) {
    			// we are looking at the chunk being generated
    			toPopulate = center;
    		} else {
    			toPopulate = this.chunkProvider.getLoadedChunk(centerX + posX, centerZ + posZ);
    			
    			//Logger.info("Chunk (" + (centerX + posX) + "," + (centerZ +posZ ));
    	    	//Logger.info("Considering "+ toPopulate.getPos().toString() + " " + toPopulate.isTerrainPopulated() + " " +
    	    	//toPopulate.isLightPopulated() + toPopulate.isPopulated());
    			if (toPopulate == null) toPopulate = center.getWorld().getChunk(centerX + posX, centerZ + posZ);
    		}
    		LatePopulatingChunk.populate(toPopulate,chunkGenerator);
    	}
    }
}
