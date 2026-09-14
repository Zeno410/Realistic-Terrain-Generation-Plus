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
import rtg.api.util.noise.SimplexNoise;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.deco.DecoLeafVines;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.gen.feature.tree.bop.BOPTreeMaterials;
import rtg.api.world.gen.feature.tree.bop.TreeBOPMediumFir;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGBetulaPopulifolia;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGResizable;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.event.EventHandlerCommon;


public class RealisticBiomeBOPTemperateRainforest extends RealisticBiomeBase {

    public RealisticBiomeBOPTemperateRainforest(final Biome biome) { super(biome); }


    @Override
    public void initDecos() {

    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(this.baseBiome());
    	
    	TreeBOPMediumFir redwoodTree = new  TreeBOPMediumFir();
    	redwoodTree.setMaterials(BOPTreeMaterials.redwood);
    	redwoodTree.setWideningRate(.2f);
    	redwoodTree.setLowestVariableTrunkProportion(.4f);
    	redwoodTree.setTrunkProportionVariability(.15f);
    	TreeRTGResizable variableRedwoodTree = new TreeRTGResizable(redwoodTree);
    	variableRedwoodTree.changeAverageHeightSqrt(1.5f);
    	variableRedwoodTree.changeHeightNoiseVariability(.2f);
    	
        DecoTree redwoodPopulation = new DecoTree(variableRedwoodTree);
        
        TreeRTG substoryTree = new TreeRTGBetulaPopulifolia();
        substoryTree.setMaxCrownSize(7);
        substoryTree.setTrunkProportionVariability(.5f);
        substoryTree.setMaterials(TreeMaterials.Picker.oak);
        substoryTree.setAbsoluteMinimumTrunk(0);
        TreeRTGResizable variableSubstoryTree = new TreeRTGResizable(substoryTree);
        variableSubstoryTree.changeLocalNoiseVariability(1f);
        
        DecoTree substoryPopulation = new DecoTree(variableSubstoryTree);
        
        DecoTreeSet forest = new DecoTreeSet();
        forest.setDistribution(new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 3.5f, 12.0f) ); //denser than usual
        
        forest.add(redwoodPopulation, 8);
        forest.add(substoryPopulation, 3);
        
		// have to add our own vines to the trees
		DecoLeafVines vines = new DecoLeafVines();
		vines.setLoops(10);
		
		DecoCollectionBase vinedForest = new DecoCollectionBase(this.getConfig());
		vinedForest.addDeco(forest);
		vinedForest.addDeco(vines);
		
		treeGenerator = vinedForest;
    	
    }

    @Override
    public Biome preferredBeach() {
    	return BOPBiomes.gravel_beach.orNull();
    }

    @Override
    public void initConfig() {
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPTemperateRainforest();
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceBOPTemperateRainforest(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock, 0.45f);
    }

    public static class TerrainBOPTemperateRainforest extends TerrainBase {

        public TerrainBOPTemperateRainforest() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainPlains(x, y, rtgWorld, river, 160f, 10f, 60f, 100f, 65f);
        }
    }

    public static class SurfaceBOPTemperateRainforest extends SurfaceBase {

        private float min;

        private float sCliff = 1.5f;
        private float sHeight = 60f;
        private float sStrength = 65f;
        private float cCliff = 1.5f;

        public SurfaceBOPTemperateRainforest(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff) {

            super(config, top, fill);
            min = minCliff;
        }

        public SurfaceBOPTemperateRainforest(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff, float stoneCliff, float stoneHeight, float stoneStrength, float clayCliff) {

            this(config, top, fill, minCliff);

            sCliff = stoneCliff;
            sHeight = stoneHeight;
            sStrength = stoneStrength;
            cCliff = clayCliff;
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand();
            SimplexNoise simplex = rtgWorld.simplexInstance(0);
            float c = TerrainBase.calcCliff(x, z, noise, river);
            int cliff = 0;

            Block b;
            for (int k = 255; k > -1; k--) {
                b = primer.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                }
                else if (b == Blocks.STONE) {
                    depth++;

                    if (depth == 0) {

                        float p = simplex.noise3f(i / 8f, j / 8f, k / 8f) * 0.5f;
                        if (c > min && c > sCliff - ((k - sHeight) / sStrength) + p) {
                            cliff = 1;
                        }
                        if (c > cCliff) {
                            cliff = 2;
                        }

                        if (cliff == 1) {
                            if (rand.nextInt(3) == 0) {

                                primer.setBlockState(x, k, z, hcCobble());
                            }
                            else {

                                primer.setBlockState(x, k, z, hcStone());
                            }
                        }
                        else if (cliff == 2) {
                            primer.setBlockState(x, k, z, getShadowStoneBlock());
                        }
                        else if (k < 63) {
                            if (k < 62) {
                                primer.setBlockState(x, k, z, fillerBlock);
                            }
                            else {
                                primer.setBlockState(x, k, z, topBlock);
                            }
                        }
                        else {
                            primer.setBlockState(x, k, z, topBlock);
                        }
                    }
                    else if (depth < 6) {
                        if (cliff == 1) {
                            primer.setBlockState(x, k, z, hcStone());
                        }
                        else if (cliff == 2) {
                            primer.setBlockState(x, k, z, getShadowStoneBlock());
                        }
                        else {
                            primer.setBlockState(x, k, z, fillerBlock);
                        }
                    }
                }
            }
        }
    }
}
