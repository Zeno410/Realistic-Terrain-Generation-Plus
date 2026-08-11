package rtg.world.biome.realistic.biomesoplenty;

import java.util.Random;

import biomesoplenty.api.biome.BOPBiomes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.Distribution;
import rtg.api.util.noise.SimplexNoise;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.deco.DecoBoulder;
import rtg.api.world.deco.DecoFallenTree;
import rtg.api.world.deco.DecoShrub;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.deco.DecoVariableOak;
import rtg.api.world.deco.DecoVariableSpruce;
import rtg.api.world.deco.DecoVariableTree;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.deco.helper.DecoHelper5050;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGBetulaPapyrifera;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGPiceaSitchensis;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.api.world.terrain.heighteffect.HeightVariation;
import rtg.api.world.terrain.heighteffect.JitterEffect;
import rtg.event.EventHandlerCommon;


public class RealisticBiomeBOPLandOfLakes extends RealisticBiomeBase {

    public RealisticBiomeBOPLandOfLakes(final Biome biome) { 
    	super(biome); 
    	}

    @Override
    public Biome preferredBeach() {
    	return Biomes.STONE_BEACH;
    }

    @Override
    public void initConfig() {
        this.getConfig().addProperty(this.getConfig().ALLOW_LOGS).set(true);
        this.getConfig().addProperty(this.getConfig().FALLEN_LOG_DENSITY_MULTIPLIER);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPLandOfLakes();//(58f, 76f, 36f);
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceBOPLandOfLakes(getConfig(), Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState(), 0f, 1.5f, 60f, 65f, 1.5f, Blocks.GRASS.getDefaultState(), 0.10f);
    }

    @Override
    public void initDecos() {

    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(baseBiome());
    	treeGenerator = new DecoTreeCollectionBOPLandOfLakes(getConfig());
    	
        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.getDistribution().setNoiseDivisor(100f);
        decoFallenTree.getDistribution().setNoiseFactor(6f);
        decoFallenTree.getDistribution().setNoiseAddend(0.8f);
        decoFallenTree.setLogConditionChance(12);
        decoFallenTree.setRandomLogBlocks(new IBlockState[]{Blocks.LOG.getDefaultState(), BlockUtil.getStateLog(EnumType.SPRUCE), BlockUtil.getStateLog(EnumType.BIRCH)});
        decoFallenTree.setMinSize(8);
        decoFallenTree.setMaxSize(12);
        this.addDeco(decoFallenTree, this.getConfig().ALLOW_LOGS.get());

        DecoShrub decoShrub = new DecoShrub();
        decoShrub.setMaxY(110);
        decoShrub.setLoopMultiplier(3f);
        this.addDeco(decoShrub);

        DecoBoulder decoBoulder = new DecoBoulder();
        decoBoulder.setBoulderBlock(Blocks.COBBLESTONE.getDefaultState());
        decoBoulder.setMaxY(80);
        decoBoulder.setChance(12);
        decoBoulder.setStrengthFactor(1f);
        this.addDeco(decoBoulder);
    }

    
    private static class DecoTreeCollectionBOPLandOfLakes extends DecoCollectionBase {

		public DecoTreeCollectionBOPLandOfLakes(BiomeConfig config) {
			super(config);
			
			DecoTreeSet trees = new DecoTreeSet();
			
			{DecoVariableTree spruceTree = new DecoVariableSpruce();
			spruceTree.changeAverageHeightSqrt(-1);// short
			trees.add(spruceTree,4);}
			
			
			{DecoVariableTree oakSpruceTree = new DecoVariableSpruce();
			oakSpruceTree.changeAverageHeightSqrt(-1);// short
			TreeMaterials oakSpruceMaterials = new TreeMaterials("Oak Spruce",
					Blocks.LOG.getStateFromMeta(0),
					Blocks.LEAVES.getStateFromMeta(1),
					Blocks.LOG.getStateFromMeta(12));
			oakSpruceTree.setMaterials(oakSpruceMaterials);
			trees.add(oakSpruceTree,4);}
			
			{DecoVariableTree oakTree = new DecoVariableOak();
			oakTree.changeAverageHeightSqrt(-1.5f);// shorter
			trees.add(oakTree,3);}
			
			{DecoVariableTree oakBirchTree = new DecoVariableOak();
			oakBirchTree.changeAverageHeightSqrt(-1.5f);// shorter
			oakBirchTree.setMaterials(TreeMaterials.Picker.birch);
			trees.add(oakBirchTree,3);}

			Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 5.5f); 
			trees.setDistribution(treeFrequencyDistribution);
			
			this.addDeco(trees);
			
			
		}
    	
    }
    public static class TerrainBOPLandOfLakes extends TerrainBase {

        private float minHeight;
        private float maxHeight;
        private float hillStrength;
        private HeightVariation small;
        private HeightVariation large;
        private JitterEffect largeJitter;
        private JitterEffect smallJitter;


        public TerrainBOPLandOfLakes() {
            super(63f);
            small = new HeightVariation();
            small.height = 2.5f;
            small.octave = 1;
            small.wavelength = 10;

            large = new HeightVariation();
            large.height = 5;
            large.octave = 2;
            large.wavelength = 20;

            smallJitter = new JitterEffect();
            smallJitter.amplitude = 2;
            smallJitter.wavelength = 9;
            smallJitter.jittered = large.plus(small);

            largeJitter = new JitterEffect();
            largeJitter.amplitude = 4;
            largeJitter.wavelength = 18;
            largeJitter.jittered = smallJitter;

        }

        public TerrainBOPLandOfLakes(float minHeight, float maxHeight, float hillStrength) {

            this.minHeight = minHeight;
            this.maxHeight = (maxHeight > rollingHillsMaxHeight) ? rollingHillsMaxHeight : ((maxHeight < this.minHeight) ? rollingHillsMaxHeight : maxHeight);
            this.hillStrength = hillStrength;
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {
            return riverized(largeJitter.added(rtgWorld, x, y) + this.base, river);
            //return terrainRollingHills(x, y, rtgWorld.simplex, river, hillStrength, maxHeight, groundNoise, groundNoiseAmplitudeHills, 0f);
        }
    }

    public static class SurfaceBOPLandOfLakes extends SurfaceBase {

        private float min;

        private float sCliff = 1.5f;
        private float sHeight = 60f;
        private float sStrength = 65f;
        private float cCliff = 1.5f;

        private IBlockState mix;
        private float mixHeight;

        public SurfaceBOPLandOfLakes(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff, float stoneCliff,
                                     float stoneHeight, float stoneStrength, float clayCliff, IBlockState mixBlock, float mixSize) {

            super(config, top, fill);
            min = minCliff;

            sCliff = stoneCliff;
            sHeight = stoneHeight;
            sStrength = stoneStrength;
            cCliff = clayCliff;

            mix = mixBlock;
            mixHeight = mixSize;
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand();
            SimplexNoise simplex = rtgWorld.simplexInstance(0);
            float c = TerrainBase.calcCliff(x, z, noise, river);
            int cliff = 0;
            boolean m = false;

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
                        else if (simplex.noise2f(i / 12f, j / 12f) > mixHeight) {
                            primer.setBlockState(x, k, z, mix);
                            m = true;
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
