package rtg.world.biome.realistic.biomesoplenty;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.Distribution;
import rtg.api.util.noise.SimplexNoise;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.deco.DecoShrub;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.gen.feature.tree.bop.BOPTreeMaterials;
import rtg.api.world.gen.feature.tree.bop.TreeBOPPalm;
import rtg.api.world.gen.feature.tree.bop.TreeBOPTropicalRainforest;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGBetulaPopulifolia;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGCocosNucifera;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGQuercusRobur;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.event.EventHandlerCommon;


public class RealisticBiomeBOPRainforest extends RealisticBiomeBase {

    public RealisticBiomeBOPRainforest(final Biome biome) { super(biome); }

    @Override
    public void initDecos() {
    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(baseBiome());
    	TreeRTG coconutTree = new TreeRTGCocosNucifera();
    	coconutTree.setMinCrownSize(9);
    	coconutTree.setMaxCrownSize(11);
    	coconutTree.setMinTrunkSize(9);
    	coconutTree.setMaxTrunkSize(9);
    	DecoTree coconutDeco = new DecoTree(coconutTree);
    	

    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(baseBiome());
    	TreeRTG palmTree = new TreeBOPPalm();
    	palmTree.setMinCrownSize(9);
    	palmTree.setMaxCrownSize(11);
    	palmTree.setMinTrunkSize(9);
    	palmTree.setMaxTrunkSize(9);
    	DecoTree palmDeco = new DecoTree(palmTree);
    	
    	TreeRTG jungleTree = new TreeRTGBetulaPopulifolia();
    	jungleTree.setLogBlock(BlockUtil.getStateLog(EnumType.JUNGLE));
    	jungleTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.JUNGLE));
    	jungleTree.setBranchBlock(BlockUtil.getStateLog(EnumType.JUNGLE).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
    	jungleTree.setMinCrownSize(8);
    	jungleTree.setMaxCrownSize(11);
    	jungleTree.setMinTrunkSize(4);
    	jungleTree.setMaxTrunkSize(6);
    	
    	DecoTree jungleDeco = new DecoTree(jungleTree);
    	
    	TreeRTG floweringTree = new TreeRTGBetulaPopulifolia();
    	floweringTree.setMaterials(TreeMaterials.Picker.oak);
    	floweringTree.setLogBlock(BlockUtil.getStateLog(EnumType.DARK_OAK));
    	floweringTree.setBranchBlock(BlockUtil.getStateLog(EnumType.DARK_OAK).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
    	floweringTree.setMinCrownSize(8);
    	floweringTree.setMaxCrownSize(11);
    	floweringTree.setMinTrunkSize(4);
    	floweringTree.setMaxTrunkSize(6);
    	floweringTree.setLeafChoice(BOPTreeMaterials.floweringOak());
    	
    	DecoTree floweringDeco = new DecoTree(floweringTree);
    	
    	TreeRTG oakTree = new TreeRTGQuercusRobur();
    	oakTree.setMinCrownSize(9);
    	oakTree.setMaxCrownSize(12);
    	oakTree.setMinTrunkSize(4);
    	oakTree.setMaxTrunkSize(7);
    	floweringTree.setLeafChoice(BOPTreeMaterials.floweringOak());
    	
    	DecoTree oakDeco = new DecoTree(oakTree);
    	

    	DecoTreeSet treeCombination = new DecoTreeSet();
    	treeCombination.add(coconutDeco,1);
    	treeCombination.add(palmDeco,1);
    	treeCombination.add(jungleDeco,2);
    	treeCombination.add(floweringDeco,3);
    	treeCombination.add(floweringDeco,6);
		Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 6.5f); 
		treeCombination.setDistribution(treeFrequencyDistribution);
		
    	DecoShrub shrubs = new DecoShrub();
    	shrubs.setLeafChoice(BOPTreeMaterials.floweringOak());
    	shrubs.setLoops(4);
		
		DecoCollectionBase allDecos = new DecoCollectionBase(getConfig());
		allDecos.addDeco(treeCombination);
		allDecos.addDeco(shrubs);
    	treeGenerator = allDecos;
    }

    @Override
    public Biome preferredBeach() {
        return baseBiome();
    }

    @Override
    public void initConfig() {
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPRainforest();
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceBOPRainforest(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock, 1.3f);
    }

    public static class TerrainBOPRainforest extends TerrainBase {

        public TerrainBOPRainforest() {}

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainHighland(x, y, rtgWorld, river, 10f, 68f, 40f, 10f);
        }
    }

    public static class SurfaceBOPRainforest extends SurfaceBase {

        private float min;

        private float sCliff = 1.5f;
        private float sHeight = 60f;
        private float sStrength = 65f;
        private float cCliff = 1.5f;

        public SurfaceBOPRainforest(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff) {

            super(config, top, fill);
            min = minCliff;
        }

        public SurfaceBOPRainforest(BiomeConfig config, IBlockState top, IBlockState fill, float minCliff, float stoneCliff, float stoneHeight, float stoneStrength, float clayCliff) {

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
