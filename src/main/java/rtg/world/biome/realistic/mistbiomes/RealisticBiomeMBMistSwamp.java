package rtg.world.biome.realistic.mistbiomes;

import net.minecraft.world.biome.Biome;
import rtg.api.world.RTGWorld;
import rtg.api.world.terrain.TerrainBase;


public class RealisticBiomeMBMistSwamp extends RealisticBiomeMBBase {

    public RealisticBiomeMBMistSwamp(Biome biome) {

        super(biome);
    }

    @Override
    public void initConfig() {

    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainVanillaSwampland();
    }

    public static class TerrainVanillaSwampland extends TerrainBase {

        public TerrainVanillaSwampland() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainMarsh(x, y, rtgWorld, 61.5f, river);
        }
    }
}
