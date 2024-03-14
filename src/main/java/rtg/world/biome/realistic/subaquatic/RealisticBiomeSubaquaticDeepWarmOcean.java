package rtg.world.biome.realistic.subaquatic;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.config.BiomeConfig;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;

import java.util.Random;

public class RealisticBiomeSubaquaticDeepWarmOcean extends RealisticBiomeBase {

    public RealisticBiomeSubaquaticDeepWarmOcean(final Biome biome) {

        super(biome);
    }

    @Override
    public void initDecos() {

    }

    @Override
    public void initConfig() {
        this.getConfig().SURFACE_WATER_LAKE_MULT.set(0.0f);
        this.getConfig().ALLOW_RIVERS.set(false);
        this.getConfig().ALLOW_SCENIC_LAKES.set(false);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainSubaquaticDeepWarmOcean(false, -10f, 8f, 0f, 0f, 30f);
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceSubaquaticDeepWarmOcean(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock);
    }

    public static class TerrainSubaquaticDeepWarmOcean extends TerrainBase {

        private final boolean booRiver;
        private final float[] height;
        private final int heightLength;
        private final float strength;
        private final float cWidth;
        private final float cHeigth;
        private final float cStrength;
        private final float base;

        public TerrainSubaquaticDeepWarmOcean(boolean riverGen, float heightStrength, float canyonWidth, float canyonHeight, float canyonStrength, float baseHeight) {

            booRiver = riverGen;
            height = new float[]{5.0f, 0.5f, 12.5f, 0.5f};
            strength = heightStrength;
            heightLength = height.length;
            cWidth = canyonWidth;
            cHeigth = canyonHeight;
            cStrength = canyonStrength;
            base = baseHeight;
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainOceanCanyon(x, y, rtgWorld, river, height, border, strength, heightLength, booRiver);
        }
    }

    public static class SurfaceSubaquaticDeepWarmOcean extends SurfaceBase {

        public SurfaceSubaquaticDeepWarmOcean(BiomeConfig config, IBlockState top, IBlockState filler) {

            super(config, top, filler);
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand();
            float c = TerrainBase.calcCliff(x, z, noise, river);
            boolean cliff = c > 1.4f;

            for (int k = 255; k > -1; k--) {
                Block b = primer.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                } else if (b == Blocks.STONE) {
                    depth++;

                    if (cliff) {
                        if (depth > -1 && depth < 2) {
                            if (rand.nextInt(3) == 0) {

                                primer.setBlockState(x, k, z, hcCobble());
                            } else {

                                primer.setBlockState(x, k, z, hcStone());
                            }
                        } else if (depth < 10) {
                            primer.setBlockState(x, k, z, hcStone());
                        }
                    } else {
                        if (depth == 0 && k > 61) {
                            primer.setBlockState(x, k, z, topBlock);
                        } else if (depth < 4) {
                            primer.setBlockState(x, k, z, fillerBlock);
                        }
                    }
                }
            }
        }
    }
}
