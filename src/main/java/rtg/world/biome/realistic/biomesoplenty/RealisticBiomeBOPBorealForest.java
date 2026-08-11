package rtg.world.biome.realistic.biomesoplenty;

import java.util.Random;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt.DirtType;
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
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.deco.DecoVariableBirch;
import rtg.api.world.deco.DecoVariableOak;
import rtg.api.world.deco.DecoVariableSpruce;
import rtg.api.world.deco.DecoVariableTree;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.gen.feature.tree.bop.BOPTreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGPinusPonderosa;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.api.world.terrain.heighteffect.BumpyHillsEffect;
import rtg.event.EventHandlerCommon;


public class RealisticBiomeBOPBorealForest extends RealisticBiomeBase {

    public RealisticBiomeBOPBorealForest(final Biome biome) { 
    	super(biome);
    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(biome);
    }

    @Override
    public void initDecos() {
    	treeGenerator = new DecoCollectionBOPBorealForest(getConfig());
    }

    @Override
    public Biome preferredBeach() {
        return Biomes.STONE_BEACH;
    }

    @Override
    public void initConfig() {
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPBorealForest();
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceBOPBorealForest(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock, 0f, 1.5f, 60f, 65f, 1.5f, BlockUtil.getStateDirt(DirtType.PODZOL), 0.15f);
    }

    public static class TerrainBOPBorealForest extends TerrainBase {

        private float baseHeight = 64f;
        private float hillStrength = 50f;
        private BumpyHillsEffect hillEffect;
        private float hillWidth = 60f;
        private float hillBumpyness = 10f;
        private float hillBumpynessWidth = 20f;

        public TerrainBOPBorealForest() {

            hillEffect = new BumpyHillsEffect();
            hillEffect.hillHeight = hillStrength;
            hillEffect.hillWavelength = hillWidth;
            hillEffect.spikeHeight = hillBumpyness;
            hillEffect.spikeWavelength = this.hillBumpynessWidth;
        }

        public TerrainBOPBorealForest(float bh, float hs) {

            this();
            baseHeight = bh;
            hillStrength = hs;
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            groundNoise = groundNoise(x, y, groundNoiseAmplitudeHills, rtgWorld);


            float m = hillEffect.added(rtgWorld, x, y);

            return riverized(baseHeight, river) + (groundNoise + m) * river;
        }
    }
    
    public static class DecoCollectionBOPBorealForest extends DecoCollectionBase {
        
        private Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 5.5f); 
        // the BoP version is pretty crowded
        // the trees are a bit stunted too

    	public DecoCollectionBOPBorealForest(BiomeConfig config) {
    		super(config);     
    		DecoTreeSet treeChooser = new DecoTreeSet();
    		treeChooser.setDistribution(treeFrequencyDistribution);
    		treeChooser.add(evergreenTree(),9);
    		treeChooser.add(yellowAutumnTree(), 9);
    		treeChooser.add(stuntedOakTree(), 2);
    		this.addDeco(treeChooser);
    	}

    	private DecoTree evergreenTree() {
    		DecoVariableTree result = new DecoVariableSpruce();
    		result.changeAverageHeightSqrt(-.5f);
    		return result;
    	}
    	
    	private DecoTree yellowAutumnTree() {
    		DecoVariableTree result = new DecoVariableBirch();
    		result.changeAverageHeightSqrt(-.5f);
    		result.setMaterials(BOPTreeMaterials.yellowAutumn);
    		return result;
    	}
  
    	
    	private DecoTree stuntedOakTree() {
    		DecoVariableTree result = new DecoVariableOak();
    		result.changeAverageHeightSqrt(-1.5f);
    		result.changeHeightNoiseVariability(-.5f);
    		return result;
    	}	
    	
    }

    public static class SurfaceBOPBorealForest extends SurfaceBase {

        private float min;

        private float sCliff = 1.5f;
        private float sHeight = 60f;
        private float sStrength = 65f;
        private float cCliff = 1.5f;

        private IBlockState mix;
        private float mixHeight;

        public SurfaceBOPBorealForest(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff, float stoneCliff,
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
