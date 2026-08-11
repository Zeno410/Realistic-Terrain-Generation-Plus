package rtg.world.biome.realistic.biomesoplenty;

import java.util.ArrayList;
import java.util.Random;

import biomesoplenty.api.block.BOPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.config.BiomeConfig;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.deco.DecoVariableBirch;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.gen.feature.tree.bop.BOPTreeMaterials;
import rtg.api.world.gen.feature.tree.bop.TreeBOPJacaranda;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGCeibaPentandra;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGCeibaRosea;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGQuercusRobur;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGResizable;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.event.EventHandlerCommon;


public class RealisticBiomeBOPMangrove extends RealisticBiomeBase {

    public RealisticBiomeBOPMangrove(final Biome biome) { super(biome); }

    @Override
    public void initDecos() {
    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(baseBiome());
    	
    	TreeRTG petandraTree = new TreeRTGCeibaPentandra();
    	petandraTree.setMaterials(BOPTreeMaterials.mangrove);
    	ArrayList<IBlockState> ground = petandraTree.getValidGroundBlocks();
    	ground.add(BOPBlocks.mud.getDefaultState());
    	petandraTree.setValidGroundBlocks(ground);
    	TreeRTGResizable variablePetandra = new TreeRTGResizable(petandraTree);
    	variablePetandra.changeAverageHeightSqrt(-1f);// 
    	variablePetandra.changeHeightNoiseVariability(-1);
    	
    	DecoTree petandraDeco = new DecoTree(variablePetandra);
    	
    	TreeRTG roseaTree = new TreeRTGCeibaRosea();
    	roseaTree.setMaterials(BOPTreeMaterials.mangrove);
    	roseaTree.setValidGroundBlocks(ground);
    	TreeRTGResizable variableRosea = new TreeRTGResizable(roseaTree);
    	variableRosea.changeAverageHeightSqrt(-1f);
    	variableRosea.changeHeightNoiseVariability(-1);
    	
    	DecoTree roseaDeco = new DecoTree(variableRosea);
    	
    	DecoTreeSet treeCombination = new DecoTreeSet();
    	treeCombination.add(petandraDeco, 10);
    	treeCombination.add(roseaDeco, 5);
    	
		Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 3f, 6.0f); 
		treeCombination.setDistribution(treeFrequencyDistribution);

    	treeGenerator = treeCombination;
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

        return new TerrainBOPMangrove();
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceBOPMangrove(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock);
    }

    public static class TerrainBOPMangrove extends TerrainBase {

        public TerrainBOPMangrove() {
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {
            return terrainBeach(x, y, rtgWorld, river, 60f);
        }
    }

    public static class SurfaceBOPMangrove extends SurfaceBase {

        public SurfaceBOPMangrove(BiomeConfig config, IBlockState top, IBlockState filler) {

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

                    if (cliff && k > 64) {
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
