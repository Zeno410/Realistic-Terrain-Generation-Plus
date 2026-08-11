package rtg.world.biome.realistic.biomesoplenty;

import java.util.Random;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
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
import rtg.api.world.gen.feature.tree.bop.TreeBOPTropicalRainforest;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGBetulaPopulifolia;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.event.EventHandlerCommon;


public class RealisticBiomeBOPTropicalRainforest extends RealisticBiomeBase {

    public RealisticBiomeBOPTropicalRainforest(final Biome biome) { 
    	super(biome); 
    }

    @Override
    public void initDecos() {

    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(baseBiome());
    	TreeRTG mahoganyTree = new TreeBOPTropicalRainforest();
    	mahoganyTree.setMinCrownSize(6);
    	mahoganyTree.setMaxCrownSize(8);
    	mahoganyTree.setMinTrunkSize(12);
    	mahoganyTree.setMaxTrunkSize(14);
    	DecoTree mahoganyDeco = new DecoTree(mahoganyTree);
    	
    	TreeRTG jungleTree = new TreeRTGBetulaPopulifolia();
    	jungleTree.setLogBlock(BlockUtil.getStateLog(EnumType.JUNGLE));
    	jungleTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.JUNGLE));
    	jungleTree.setBranchBlock(BlockUtil.getStateLog(EnumType.JUNGLE).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
    	jungleTree.setMinCrownSize(6);
    	jungleTree.setMaxCrownSize(8);
    	jungleTree.setMinTrunkSize(12);
    	jungleTree.setMaxTrunkSize(14);
    	
    	DecoTree jungleDeco = new DecoTree(jungleTree);
    	
    	TreeRTG darkTree = new TreeRTGBetulaPopulifolia();
    	darkTree.setLogBlock(BlockUtil.getStateLog(EnumType.JUNGLE));
    	darkTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.JUNGLE));
    	darkTree.setBranchBlock(BlockUtil.getStateLog(EnumType.JUNGLE).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
    	darkTree.setMinCrownSize(8);
    	darkTree.setMaxCrownSize(10);
    	darkTree.setMinTrunkSize(10);
    	darkTree.setMaxTrunkSize(14);
    	
    	DecoTree darkDeco = new DecoTree(darkTree);

    	DecoTreeSet treeCombination = new DecoTreeSet();
    	treeCombination.add(mahoganyDeco,5);
    	treeCombination.add(jungleDeco,3);
    	treeCombination.add(darkDeco,2);
		Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 6.5f); 
		treeCombination.setDistribution(treeFrequencyDistribution);
		
    	treeGenerator = treeCombination;
    }

    @Override
    public void initConfig() {
        this.getConfig().ALLOW_RIVERS.set(false);
        this.getConfig().ALLOW_SCENIC_LAKES.set(false);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPTropicalRainforest(0f, 60f, 68f, 200f);
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceBOPTropicalRainforest(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock);
    }

    public static class TerrainBOPTropicalRainforest extends TerrainBase {

        private float start;
        private float height;
        private float width;

        public TerrainBOPTropicalRainforest(float hillStart, float landHeight, float baseHeight, float hillWidth) {

            start = hillStart;
            height = landHeight;
            base = baseHeight;
            width = hillWidth;
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainHighland(x, y, rtgWorld, river, start, width, height, base - 62f);
        }
    }

    public static class SurfaceBOPTropicalRainforest extends SurfaceBase {

        public SurfaceBOPTropicalRainforest(BiomeConfig config, IBlockState top, IBlockState filler) {

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
