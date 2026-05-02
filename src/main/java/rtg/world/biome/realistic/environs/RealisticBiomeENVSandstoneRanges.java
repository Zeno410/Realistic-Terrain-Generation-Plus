package rtg.world.biome.realistic.environs;

import net.minecraft.world.biome.Biome;
import rtg.api.world.terrain.TerrainBase;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaExtremeHills.RidgedExtremeHills;

public class RealisticBiomeENVSandstoneRanges extends RealisticBiomeENVBase {
	
	public RealisticBiomeENVSandstoneRanges(Biome baseBiome) {
		super(baseBiome);
	}

    @Override
    public TerrainBase initTerrain() {
        return new RidgedExtremeHills(150f, 64f, 200f);
        //return new TerrainVanillaExtremeHills(10f, 120f, 10f, 200f);
    }

}
