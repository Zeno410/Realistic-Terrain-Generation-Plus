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
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.deco.DecoShrub;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.gen.feature.tree.bop.BOPTreeMaterials;
import rtg.api.world.gen.feature.tree.bop.TreeBOPTropicalRainforest;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGBetulaPopulifolia;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGQuercusRobur;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.event.EventHandlerCommon;


public class RealisticBiomeBOPSacredSprings extends RealisticBiomeBase {

    public RealisticBiomeBOPSacredSprings(final Biome biome) { super(biome); }

    @Override
    public void initDecos() {
    	
    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(baseBiome());
    	TreeRTGQuercusRobur sacredTree = new TreeRTGQuercusRobur();
    	sacredTree.setMinCrownSize(16);
    	sacredTree.setMaxCrownSize(20);
    	sacredTree.setMinTrunkSize(8);
    	sacredTree.setMaxTrunkSize(12);
    	sacredTree.setTrunkWidth(2);
    	sacredTree.setMaterials(BOPTreeMaterials.sacredOak);
    	DecoTree sacredDeco = new DecoTree(sacredTree);
    	
    	TreeRTG oakTree = new TreeRTGQuercusRobur();
    	oakTree.setMinCrownSize(6);
    	oakTree.setMaxCrownSize(10);
    	oakTree.setMinTrunkSize(6);
    	oakTree.setMaxTrunkSize(8);
    	oakTree.setLeafChoice(BOPTreeMaterials.floweringOak());
    	
    	DecoTree oakDeco = new DecoTree(oakTree);


    	DecoTreeSet treeCombination = new DecoTreeSet();
    	treeCombination.add(sacredDeco,1);
    	treeCombination.add(oakDeco,3);
		Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 4.5f); 
		treeCombination.setDistribution(treeFrequencyDistribution);
   
    	DecoShrub shrubs = new DecoShrub();
    	shrubs.setLeafChoice(BOPTreeMaterials.floweringOak());
    	shrubs.setLoops(8);
    	
    	DecoCollectionBase rtgTrees = new DecoCollectionBase(getConfig());
    	rtgTrees.addDeco(treeCombination);
    	rtgTrees.addDeco(shrubs);

    	treeGenerator = rtgTrees;
    }

    @Override
    public Biome preferredBeach() {
        return baseBiome();
    }

    @Override
    public void initConfig() {
        this.getConfig().SURFACE_WATER_LAKE_MULT.set(0.5f);
        this.getConfig().ALLOW_RIVERS.set(false);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPSacredSprings(150f, 30f, 68f);
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceBOPSacredSprings(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock);
    }

    public static class TerrainBOPSacredSprings extends TerrainBase {

        private float width;
        private float strength;
        private float lakeDepth;
        private float lakeWidth;
        private float terrainHeight;

        public TerrainBOPSacredSprings(float mountainWidth, float mountainStrength, float height) {

            width = mountainWidth;
            strength = mountainStrength;
            terrainHeight = height;
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainLonelyMountain(x, y, rtgWorld, river, strength, width, terrainHeight);

        }
    }

    public static class SurfaceBOPSacredSprings extends SurfaceBase {

        public SurfaceBOPSacredSprings(BiomeConfig config, IBlockState top, IBlockState filler) {

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
