package rtg.world.biome.realistic.vanilla;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import rtg.api.config.BiomeConfig;
import rtg.api.util.noise.SimplexNoise;
import rtg.api.world.RTGWorld;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.api.world.biome.RealisticBiomeBase;


public class RealisticBiomeVanillaFrozenOcean extends RealisticBiomeBase {

    public static Biome biome = Biomes.FROZEN_OCEAN;
    public static Biome river = Biomes.FROZEN_RIVER;

    public RealisticBiomeVanillaFrozenOcean() {

        super(biome, RiverType.FROZEN, BeachType.COLD);
    }

    @Override
    public void initConfig() {
        this.getConfig().SURFACE_WATER_LAKE_MULT.set(0.0f);
        this.getConfig().ALLOW_SCENIC_LAKES.set(false);
        this.getConfig().addProperty(this.getConfig().SURFACE_MIX_BLOCK).set("");
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainVanillaFrozenOcean();
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceVanillaFrozenOcean(getConfig(), Blocks.SAND.getDefaultState(), Blocks.SAND.getDefaultState(), Blocks.GRAVEL.getDefaultState(), 20f, 0.2f);
    }

    @Override
    public void initDecos() {
    }

    public static class TerrainVanillaFrozenOcean extends TerrainBase {

        public TerrainVanillaFrozenOcean() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainOcean(x, y, rtgWorld, river, 50f);
        }
    }

    public static class SurfaceVanillaFrozenOcean extends SurfaceBase {

        private IBlockState mixBlock;
        private float width;
        private float height;
        private float mixCheck;

        public SurfaceVanillaFrozenOcean(BiomeConfig config, IBlockState top, IBlockState filler, IBlockState mix, float mixWidth, float mixHeight) {

            super(config, top, filler);

            mixBlock = this.getConfigBlock(config.SURFACE_MIX_BLOCK.get(), mix);

            width = mixWidth;
            height = mixHeight;
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand();
            SimplexNoise simplex = rtgWorld.simplexInstance(0);

            for (int k = 255; k > -1; k--) {
                Block b = primer.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                }
                else if (b == Blocks.STONE) {
                    depth++;

                    if (depth == 0 && k > 0 && k < 63) {
                        mixCheck = simplex.noise2f(i / width, j / width);

                        if (mixCheck > height) // > 0.27f, i / 12f
                        {
                            primer.setBlockState(x, k, z, mixBlock);
                        }
                        else {
                            primer.setBlockState(x, k, z, topBlock);
                        }
                    }
                    else if (depth < 4 && k < 63) {
                        primer.setBlockState(x, k, z, fillerBlock);
                    }
                    else if (depth == 0 && k < 69) {
                        primer.setBlockState(x, k, z, Blocks.SAND.getDefaultState());

                    }
                }
            }
        }
    }
}
