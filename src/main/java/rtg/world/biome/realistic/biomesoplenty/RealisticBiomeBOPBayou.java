package rtg.world.biome.realistic.biomesoplenty;

import java.util.Random;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.config.BiomeConfig;
import rtg.api.util.Distribution;
import rtg.api.util.noise.ISimplexData2D;
import rtg.api.util.noise.SimplexData2D;
import rtg.api.util.noise.SimplexNoise;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.deco.DecoFallenTree;
import rtg.api.world.deco.DecoMushrooms;
import rtg.api.world.deco.DecoShrub;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.deco.DecoLeafVines;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGCeibaRosea;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGQuercusFalcata;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGSalixMyrtilloides;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.event.EventHandlerCommon;


public class RealisticBiomeBOPBayou extends RealisticBiomeBase {

    private static IBlockState mudBlock = BOPBlocks.mud.getDefaultState();
    private static IBlockState willowLogBlock = BlockBOPLog.paging.getVariantState(BOPWoods.WILLOW)
        .withProperty(BlockLog.LOG_AXIS, EnumAxis.Y);
    private static IBlockState willowBranchBlock = BlockBOPLog.paging.getVariantState(BOPWoods.WILLOW)
            .withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE);
    private static IBlockState willowLeavesBlock = BlockBOPLeaves
        .paging.getVariantState(BOPTrees.WILLOW)
        .withProperty(BlockLeaves.CHECK_DECAY, false)
        .withProperty(BlockLeaves.DECAYABLE, true);
    //private double lakeWaterLevel = 0.2;// the lakeStrength below which things should be below water
    //private double lakeDepressionLevel = 0.6;// the lakeStrength below which land should start to be lowered
    private float defaultMixHeight = .25f;// higher numbers mean less mud

    public RealisticBiomeBOPBayou(final Biome biome) { 
    	super(biome);  
    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(biome);
    }

    @Override
    public Biome preferredBeach() {
        return baseBiome();
    }

    @Override
    public void initConfig() {
        this.getConfig().SURFACE_WATER_LAKE_MULT.set(0.0f);
        this.getConfig().addProperty(this.getConfig().ALLOW_LOGS).set(true);
        this.getConfig().addProperty(this.getConfig().FALLEN_LOG_DENSITY_MULTIPLIER);
        this.getConfig().addProperty(this.getConfig().SURFACE_MIX_BLOCK).set("");
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPBayou();
    }

    @Override
    public SurfaceBase initSurface() {
    	SurfaceBase result =  new SurfaceBOPBayou(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock, 0f, 1.5f, 60f, 65f, 1.5f, baseBiome().topBlock, defaultMixHeight);
    	result.shadowStoneBlock = Blocks.DIRT.getDefaultState();
    	//BOPBlocks.mud.getStateFromMeta(0);
    	return result;
    }

    @Override
    public float lakePressure(RTGWorld rtgWorld, int x, int y, float border, float lakeInterval, float largeBendSize, float mediumBendSize, float smallBendSize) {

        double pX = x;
        double pY = y;
        ISimplexData2D jitterData = SimplexData2D.newDisk();
        // rather than lakes, we have a bayou network
        rtgWorld.simplexInstance(1).multiEval2D(x / 40.0d, y / 40.0d, jitterData);
        pX += jitterData.getDeltaX() * 35d;
        pY += jitterData.getDeltaY() * 35d;
        //return (float) rtgWorld.cellularInstance(0).eval2D(pX / 150.0, pY / 150.0).interiorValue();
        
        return TerrainBase.bayesianAdjustment((float) rtgWorld.cellularInstance(0).eval2D(pX / 75.0, pY / 75.0).interiorValue(), 0.12f);
    }

    @Override
    public float erodedNoise(RTGWorld rtgWorld, int x, int y, float river, float border, float biomeHeight) {
        // Bayou has a higher river bottom
        float r;
        // river of actualRiverProportions now maps to 1; TODO
        float riverFlattening = 1f - river;
        riverFlattening = riverFlattening - (1 - RTGWorld.ACTUAL_RIVER_PROPORTION);
        // return biomeHeight if no river effect
        if (riverFlattening < 0) {
            return biomeHeight;
        }
        // what was 1 set back to 1;
        riverFlattening /= (RTGWorld.ACTUAL_RIVER_PROPORTION);

        // back to usual meanings: 1 = no river 0 = river
        r = 1f - riverFlattening;
        // flat spot in middle;
        riverFlattening = riverFlattening * 1.4f - 0.4f;
        if (riverFlattening < 0) {
            riverFlattening = 0;
        }

        if ((r < 1f && biomeHeight > 57f)) {
            float irregularity = rtgWorld.simplexInstance(0).noise2f(x / 12f, y / 12f) * 2f + rtgWorld.simplexInstance(0).noise2f(x / 8f, y / 8f);
            // less on the bottom and more on the sides
            irregularity = irregularity * (1 + r);
            return (biomeHeight * (r)) + ((57f + irregularity) * 1.0f) * (1f - r);
        }
        else {
            return biomeHeight;
        }
    }

    @Override
    public float lakeToRiverProportions(float pressure, float bottomLevel, float topLevel) {

        // these are rivers so not necessary to fake the lake values as river
        return pressure;
        // this number indicates a multiplier to height
        /*if (pressure > lakeDepressionLevel) return 1;
        if (pressure<lakeWaterLevel) return 0;
        return (float)((pressure-lakeWaterLevel)/(lakeDepressionLevel-lakeWaterLevel));*/
    }

    @Override
    public void initDecos() {
    	
    	treeGenerator  = new DecoCollectionBOPBayou(getConfig());

        // Shrubs to fill in the blanks.
        DecoShrub decoShrubOak = new DecoShrub();
        decoShrubOak.setMaxY(90);
        decoShrubOak.setLoopMultiplier(4f);
        decoShrubOak.setChance(3);
        decoShrubOak.setLoops(decoShrubOak.getLoops()*3);
        this.addDeco(decoShrubOak);
        

        DecoShrub decoShrubWillow = new DecoShrub();
        decoShrubWillow.setLeavesBlock(willowLeavesBlock);
        decoShrubWillow.setLogBlock(willowLogBlock);
        decoShrubWillow.setMaxY(90);
        decoShrubWillow.setLoopMultiplier(4f);
        decoShrubWillow.setChance(3);
        decoShrubWillow.setLoops(decoShrubWillow.getLoops()*3);
        this.addDeco(decoShrubWillow);

        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.getDistribution().setNoiseDivisor(80f);
        decoFallenTree.getDistribution().setNoiseFactor(60f);
        decoFallenTree.getDistribution().setNoiseAddend(-15f);
        decoFallenTree.setLogConditionChance(4);
        decoFallenTree.setLogBlock(willowLogBlock);
        decoFallenTree.setLeavesBlock(willowLeavesBlock);
        decoFallenTree.setMinSize(3);
        decoFallenTree.setMaxSize(6);
        this.addDeco(decoFallenTree, this.getConfig().ALLOW_LOGS.get());

        DecoMushrooms decoMushrooms = new DecoMushrooms();
        decoMushrooms.setMaxY(90);
        decoMushrooms.setRandomType(DecoMushrooms.RandomType.ALWAYS_GENERATE);
        this.addDeco(decoMushrooms);
    }
    private static class DecoCollectionBOPBayou extends DecoCollectionBase {
        
        public DecoCollectionBOPBayou(BiomeConfig config) {
			super(config); 
			
			// uses willow, mangrove, and spreading oak
			// currently uses area density variability but not area height variability
			
			// Define trees
			TreeRTG myrtilloidesTree = new TreeRTGSalixMyrtilloides();
			myrtilloidesTree.setLogBlock(willowLogBlock);
	        myrtilloidesTree.setLeavesBlock(willowLeavesBlock);
	        myrtilloidesTree.setBranchBlock(willowBranchBlock);
	        //myrtilloidesTree.validGroundBlocks.add(mudBlock);\        
	        DecoTree decomyrtilloides = new DecoTree(myrtilloidesTree);
	        decomyrtilloides.setTreeCondition(DecoTree.TreeCondition.RANDOM_CHANCE);
	        decomyrtilloides.setTreeConditionChance(4);
	        decomyrtilloides.setLogBlock(willowLogBlock);
	        decomyrtilloides.setLeavesBlock(willowLeavesBlock);
	        decomyrtilloides.setMaxY(90);
	        //this.addDeco(decoTrees);


	        TreeRTG roseaTree = new TreeRTGCeibaRosea(16f, 5, 0.32f, 0.1f);
	        roseaTree.setLogBlock(willowLogBlock);
	        roseaTree.setLeavesBlock(willowLeavesBlock);
	        roseaTree.setBranchBlock(willowBranchBlock);
	        //roseaTree.validGroundBlocks.add(mudBlock);
	        roseaTree.setMinTrunkSize(2);
	        roseaTree.setMaxTrunkSize(5);
	        roseaTree.setMinCrownSize(5);
	        roseaTree.setMaxCrownSize(8);
	        roseaTree.setNoLeaves(false);
	        DecoTree ceibaRoseaTree = new DecoTree(roseaTree);
	        ceibaRoseaTree.setTreeCondition(DecoTree.TreeCondition.RANDOM_CHANCE);
	        ceibaRoseaTree.setTreeConditionChance(4);
	        ceibaRoseaTree.setMaxY(90);
	        //this.addDeco(ceibaRoseaTree);
	        
	        //Quercus Falcata
	        DecoTree oakTree = new DecoTree(new TreeRTGQuercusFalcata());
	        oakTree.setTreeCondition(DecoTree.TreeCondition.NOISE_GREATER_AND_RANDOM_CHANCE);
	        oakTree.setMinSize(2);
	        oakTree.setMaxSize(4);
	        oakTree.setMinTrunkSize(3);
	        oakTree.setMaxTrunkSize(6);
	        oakTree.setMinCrownSize(4);
	        oakTree.setMaxCrownSize(10);
	        oakTree.setDistribution(new Distribution(100f, 6f, 0.8f));
	        oakTree.setTreeConditionNoise(0f);
	        oakTree.setTreeConditionChance(4);
	        //this.addDeco(decoTree);
    
	        // combine into TreeSet, and add it as sole entry to the deco.
			Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 5.5f); 
			DecoTreeSet treeChooser = new DecoTreeSet();
			treeChooser.setDistribution(treeFrequencyDistribution);
			treeChooser.add(oakTree,2);
			treeChooser.add(ceibaRoseaTree,4);
			treeChooser.add(decomyrtilloides,2);

			this.addDeco(treeChooser);
			
			// have to add our own vines to the trees
			DecoLeafVines vines = new DecoLeafVines();
			vines.setLoops(20);
			
			this.addDeco(vines);
		}
		
    }
    
    public static class TerrainBOPBayou extends TerrainBase {

        public TerrainBOPBayou() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainPlains(x, y, rtgWorld, river, 80f, 1f, 40f, 20f, 62f);
        }
    }

    public static class SurfaceBOPBayou extends SurfaceBase {

        private float min;

        private float sCliff = 1.5f;
        private float sHeight = 60f;
        private float sStrength = 65f;
        private float cCliff = 1.5f;

        private IBlockState mixBlock;
        private float mixHeight;

        public SurfaceBOPBayou(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff, float stoneCliff,
                               float stoneHeight, float stoneStrength, float clayCliff, IBlockState mix, float mixSize) {

            super(config, top, fill);
            min = minCliff;

            sCliff = stoneCliff;
            sHeight = stoneHeight;
            sStrength = stoneStrength;
            cCliff = clayCliff;

            mixBlock = this.getConfigBlock(config.SURFACE_MIX_BLOCK.get(), mix);
            //mixBlock = mudBlock;
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
                        else if (k < 62) {
                            if (k < 62) {
                                primer.setBlockState(x, k, z, fillerBlock);
                            }
                            else {
                                primer.setBlockState(x, k, z, topBlock);
                            }
                        }
                        else if ((simplex.noise2f(i / 12f, j / 12f) > mixHeight)&&k<66) {
                        	// mud not on hills
                            primer.setBlockState(x, k, z, mixBlock);
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
