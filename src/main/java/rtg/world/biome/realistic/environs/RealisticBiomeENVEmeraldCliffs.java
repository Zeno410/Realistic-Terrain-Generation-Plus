package rtg.world.biome.realistic.environs;

import net.minecraft.world.biome.Biome;
import rtg.api.world.RTGWorld;
import rtg.api.world.terrain.TerrainBase;


public class RealisticBiomeENVEmeraldCliffs extends RealisticBiomeENVBase {

    public RealisticBiomeENVEmeraldCliffs(Biome biome) {
        super(biome);
    }

    @Override
    public void initConfig() {

    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPOvergrownCliffs(300f, 100f, 0f);
    }

    public static class TerrainBOPOvergrownCliffs extends TerrainBase {

        private float width;
        private float strength;
        private float lakeDepth;
        private float lakeWidth;
        private float terrainHeight;

	/*
     * width = 230f
	 * strength = 120f
	 * lake = 50f;
	 *
	 * 230f, 120f, 50f
	 */

        public TerrainBOPOvergrownCliffs(float mountainWidth, float mountainStrength, float depthLake) {

            this(mountainWidth, mountainStrength, depthLake, 260f, 68f);
        }

        public TerrainBOPOvergrownCliffs(float mountainWidth, float mountainStrength, float depthLake, float widthLake, float height) {

            width = mountainWidth;
            strength = mountainStrength;
            lakeDepth = depthLake;
            lakeWidth = widthLake;
            terrainHeight = height;
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainLonelyMountain(x, y, rtgWorld, river, strength, width, terrainHeight);
        }
    }
}
