package rtg.world.biome.realistic.biomesoplenty;

import java.util.Random;

import biomesoplenty.api.biome.BOPBiomes;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.config.BiomeConfig;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.gen.feature.tree.bop.BOPTreeMaterials;
import rtg.api.world.gen.feature.tree.bop.TreeBOPJacaranda;
import rtg.api.world.gen.feature.tree.bop.TreeBOPMediumFir;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGBetulaUtilis;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGQuercusNigra;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGResizable;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.event.EventHandlerCommon;


public class RealisticBiomeBOPMeadow extends RealisticBiomeBase {

    public RealisticBiomeBOPMeadow(final Biome biome) { super(biome); }

    @Override
    public void initDecos() {
    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(baseBiome());
    	
    	TreeRTGBetulaUtilis ballonTree = new TreeRTGBetulaUtilis();
    	ballonTree.setMaterials(BOPTreeMaterials.fir);
    	TreeRTGResizable variableMaple = new TreeRTGResizable(ballonTree);
    	variableMaple.changeAverageHeightSqrt(-1f);
    	variableMaple.changeHeightNoiseVariability(-.75f);
    	DecoTree mapleDeco = new DecoTree(variableMaple);
    	
    	TreeBOPJacaranda babyOak = new TreeBOPJacaranda();
    	babyOak.setMaterials(TreeMaterials.Picker.oak);
    	TreeRTGResizable variableOak = new TreeRTGResizable(babyOak);
    	variableOak.changeAverageHeightSqrt(-1f);
    	variableOak.changeHeightNoiseVariability(-.75f);
    	DecoTree oakDeco = new DecoTree(variableOak);
    	 
    	TreeBOPMediumFir scatteredPines = new TreeBOPMediumFir();
    	scatteredPines.setTrunkProportionVariability(.15f);
    	scatteredPines.setMaterials(TreeMaterials.Picker.spruce);
    	TreeRTGResizable variablePines = new TreeRTGResizable(scatteredPines);
    	variablePines.changeAverageHeightSqrt(-1f);
    	variablePines.changeHeightNoiseVariability(-.75f);
    	DecoTree pineDeco = new DecoTree(variablePines);
    	
    	DecoTreeSet trees = new DecoTreeSet();
    	trees.setDistribution(new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), .5f, .5f) ); //sparse
    	trees.add(pineDeco, 3);
    	trees.add(mapleDeco, 1);
    	trees.add(oakDeco, 1);
    	
    	this.treeGenerator = trees;
    }

    @Override
    public Biome preferredBeach() {
    	return BOPBiomes.gravel_beach.orNull();
    }

    @Override
    public void initConfig() {
        this.getConfig().ALLOW_VILLAGES.set(true);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPMeadow();
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceBOPMeadow(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock);
    }

    public static class TerrainBOPMeadow extends TerrainBase {

        public TerrainBOPMeadow() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainPlains(x, y, rtgWorld, river, 160f, 10f, 60f, 200f, 66f);
        }
    }

    public static class SurfaceBOPMeadow extends SurfaceBase {

        public SurfaceBOPMeadow(BiomeConfig config, IBlockState top, IBlockState filler) {

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
                }
                else if (b == Blocks.STONE) {
                    depth++;

                    if (cliff) {
                        if (depth > -1 && depth < 2) {
                            if (rand.nextInt(3) == 0) {

                                primer.setBlockState(x, k, z, hcCobble());
                            }
                            else {

                                primer.setBlockState(x, k, z, hcStone());
                            }
                        }
                        else if (depth < 10) {
                            primer.setBlockState(x, k, z, hcStone());
                        }
                    }
                    else {
                        if (depth == 0 && k > 61) {
                            primer.setBlockState(x, k, z, topBlock);
                        }
                        else if (depth < 4) {
                            primer.setBlockState(x, k, z, fillerBlock);
                        }
                    }
                }
            }
        }
    }
}
