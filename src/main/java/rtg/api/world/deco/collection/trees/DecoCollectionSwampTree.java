package rtg.api.world.deco.collection.trees;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.world.deco.DecoFallenTree;
import rtg.api.world.deco.DecoShrub;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGPinusPonderosa;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGSalixMyrtilloides;

public class DecoCollectionSwampTree extends DecoCollectionBase {

	public DecoCollectionSwampTree(BiomeConfig config) {
		super(config);
		TreeRTG myrtilloidesTree = new TreeRTGSalixMyrtilloides();
        myrtilloidesTree.setLogBlock(Blocks.LOG.getDefaultState());
        myrtilloidesTree.setLeavesBlock(Blocks.LEAVES.getDefaultState());

        DecoTree decoTrees = new DecoTree(myrtilloidesTree);
        decoTrees.setStrengthNoiseFactorXForLoops(true);
        decoTrees.setStrengthFactorForLoops(1f);
        decoTrees.getDistribution().setNoiseDivisor(80f);
        decoTrees.getDistribution().setNoiseFactor(60f);
        decoTrees.getDistribution().setNoiseAddend(-15f);
        decoTrees.setTreeCondition(DecoTree.TreeCondition.RANDOM_CHANCE);
        decoTrees.setTreeConditionChance(12);
        decoTrees.setMaxY(70);
        this.addDeco(decoTrees);

        TreeRTG ponderosaTree = new TreeRTGPinusPonderosa();
        ponderosaTree.setLogBlock(Blocks.LOG.getDefaultState());
        ponderosaTree.setLeavesBlock(Blocks.LEAVES.getDefaultState());
        ponderosaTree.setMinTrunkSize(3);
        ponderosaTree.setMaxTrunkSize(6);
        ponderosaTree.setMinCrownSize(6);
        ponderosaTree.setMaxCrownSize(14);
        ponderosaTree.setNoLeaves(true);

        DecoTree deadPineTree = new DecoTree(ponderosaTree);
        deadPineTree.setTreeCondition(DecoTree.TreeCondition.RANDOM_CHANCE);
        deadPineTree.setTreeConditionChance(18);
        deadPineTree.setMaxY(90);
        this.addDeco(deadPineTree);

        DecoShrub decoShrub = new DecoShrub();
        decoShrub.setMaxY(100);
        decoShrub.setLoopMultiplier(3f);
        this.addDeco(decoShrub);

        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.getDistribution().setNoiseDivisor(80f);
        decoFallenTree.getDistribution().setNoiseFactor(60f);
        decoFallenTree.getDistribution().setNoiseAddend(-15f);
        decoFallenTree.setLogConditionChance(6);
        decoFallenTree.setLogBlock(BlockUtil.getStateLog(EnumType.DARK_OAK));
        decoFallenTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.DARK_OAK));
        decoFallenTree.setMinSize(3);
        decoFallenTree.setMaxSize(6);
        this.addDeco(decoFallenTree, this.getConfig().ALLOW_LOGS.get());

	}

}
