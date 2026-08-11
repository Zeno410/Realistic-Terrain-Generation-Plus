package rtg.world.biome.realistic.biomesoplenty;

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
import rtg.api.world.deco.DecoFallenTree;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTreeSet;
import rtg.api.world.deco.DecoVariableBirch;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.gen.feature.tree.bop.BOPTreeMaterials;
import rtg.api.world.gen.feature.tree.bop.TreeBOPJacaranda;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGQuercusRobur;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGResizable;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.event.EventHandlerCommon;

import static rtg.api.world.deco.DecoFallenTree.LogCondition.RANDOM_CHANCE;


public class RealisticBiomeBOPMysticGrove extends RealisticBiomeBase {

    public RealisticBiomeBOPMysticGrove(final Biome biome) { super(biome); }

    @Override
    public Biome preferredBeach() {
        return baseBiome();
    }

    @Override
    public void initConfig() {
        this.getConfig().addProperty(this.getConfig().ALLOW_LOGS).set(true);
        this.getConfig().addProperty(this.getConfig().FALLEN_LOG_DENSITY_MULTIPLIER);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainBOPMysticGrove();
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceBOPMysticGrove(getConfig(), baseBiome().topBlock, baseBiome().fillerBlock);
    }

    @Override
    public void initDecos() {

    	EventHandlerCommon.treeGenerationManager.suppressBOPBiome(baseBiome());
    	
    	TreeRTG oakTree = new TreeRTGQuercusRobur();
    	oakTree.setMinCrownSize(6);
    	oakTree.setMaxCrownSize(10);
    	oakTree.setMinTrunkSize(6);
    	oakTree.setMaxTrunkSize(8);
    	oakTree.setLeafChoice(BOPTreeMaterials.floweringOak());
    	TreeRTGResizable variableOak = new TreeRTGResizable(oakTree);
    	variableOak.changeAverageHeightSqrt(-0.5f);// apparently the sizes aren't consistent
    	variableOak.changeHeightNoiseVariability(-1);
    	
    	DecoTree oakDeco = new DecoTree(variableOak);
    	
    	DecoVariableBirch mysticDeco = new DecoVariableBirch();
    	mysticDeco.changeAverageHeightSqrt(-1);
    	mysticDeco.changeHeightNoiseVariability(-1);
    	mysticDeco.setMaterials(BOPTreeMaterials.mystic);
    	
    	TreeRTG jacarandaTree = new TreeBOPJacaranda();
    	TreeRTGResizable variableJacaranda = new TreeRTGResizable(jacarandaTree);
    	variableJacaranda.changeAverageHeightSqrt(-0.5f);
    	variableJacaranda.changeHeightNoiseVariability(-1);
    	
    	DecoTree jacarandaDeco = new DecoTree(variableJacaranda);
    	
    	DecoTreeSet treeCombination = new DecoTreeSet();
    	treeCombination.add(oakDeco, 2);
    	treeCombination.add(mysticDeco, 1);
    	treeCombination.add(jacarandaDeco, 1);
    	

		Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 3f, 6.0f); 
		treeCombination.setDistribution(treeFrequencyDistribution);
		
    	DecoCollectionBase rtgTrees = new DecoCollectionBase(getConfig());
    	rtgTrees.addDeco(treeCombination);

    	treeGenerator = rtgTrees;
    	
        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.getDistribution().setNoiseDivisor(80f);
        decoFallenTree.getDistribution().setNoiseFactor(60f);
        decoFallenTree.getDistribution().setNoiseAddend(-15f);
        decoFallenTree.setLogCondition(RANDOM_CHANCE);
        decoFallenTree.setLogConditionChance(12);
        decoFallenTree.setRandomLogBlocks(new IBlockState[]{Blocks.LOG.getDefaultState(), BOPBlocks.log_3.getStateFromMeta(4), BOPBlocks.log_1.getStateFromMeta(5)});
        decoFallenTree.setMinSize(3);
        decoFallenTree.setMaxSize(5);
        this.addDeco(decoFallenTree, this.getConfig().ALLOW_LOGS.get());
    }

    public static class TerrainBOPMysticGrove extends TerrainBase {

        public TerrainBOPMysticGrove() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainPlains(x, y, rtgWorld, river, 160f, 10f, 60f, 200f, 66f);
        }
    }

    public static class SurfaceBOPMysticGrove extends SurfaceBase {

        public SurfaceBOPMysticGrove(BiomeConfig config, IBlockState top, IBlockState filler) {

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
