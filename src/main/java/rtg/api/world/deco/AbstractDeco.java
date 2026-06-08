package rtg.api.world.deco;

import java.util.Random;

import net.minecraft.util.math.ChunkPos;
import rtg.api.util.ChunkInfo;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;

public abstract class AbstractDeco {
	
	public abstract void generate(final IRealisticBiome biome, final RTGWorld rtgWorld, final Random rand, final ChunkPos chunkPos, final float river, final boolean hasVillage, ChunkInfo chunkInfo);


}
