package rtg.api.world.deco.collection.trees.bop;

import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLog;
import net.minecraft.init.Blocks;
import rtg.api.config.BiomeConfig;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
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

public class DecoCollectionBOPSeasonalForest extends DecoCollectionBase {

	public DecoCollectionBOPSeasonalForest(BiomeConfig config) {
		super(config);     
		Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 4.5f); 
		DecoTreeSet treeChooser = new DecoTreeSet();
		treeChooser.setDistribution(treeFrequencyDistribution);
		treeChooser.add(evergreenTree());
		treeChooser.add(yellowAutumnTree(), 4);
		treeChooser.add(mapleAutumnTree(), 4);
		treeChooser.add(orangeAutumnTree(), 5);
		treeChooser.add(dyingTree());
		this.addDeco(treeChooser);
	}

	private DecoTree evergreenTree() {
		DecoTree result = new DecoVariableSpruce();
		return result;
	}
	
	private DecoTree yellowAutumnTree() {
		DecoVariableTree result = new DecoVariableBirch();
		result.setMaterials(BOPTreeMaterials.yellowAutumn);
		return result;
	}
	
	private DecoTree mapleAutumnTree() {
		DecoVariableTree result = new DecoVariableOak();
		result.setMaterials(BOPTreeMaterials.maple);
		return result;
	}
	
	private DecoTree orangeAutumnTree() {
		DecoVariableTree result = new DecoVariableOak();
		result.setMaterials(BOPTreeMaterials.orangeAutumn);
		return result;
	}	
	
	private DecoTree dyingTree() {
        TreeRTG ponderosaTree = new TreeRTGPinusPonderosa();
        ponderosaTree.setLogBlock(BlockBOPLog.paging.getVariantState(BOPWoods.DEAD));
        ponderosaTree.setLeavesBlock(Blocks.LEAVES.getDefaultState());
        ponderosaTree.setMinTrunkSize(3);
        ponderosaTree.setMaxTrunkSize(6);
        ponderosaTree.setMinCrownSize(6);
        ponderosaTree.setMaxCrownSize(14);
        ponderosaTree.setNoLeaves(true);

        DecoTree deadPineTree = new DecoTree(ponderosaTree);
        deadPineTree.setLogBlock(BlockBOPLog.paging.getVariantState(BOPWoods.DEAD));
        deadPineTree.setTreeType(DecoTree.TreeType.RTG_TREE);
        deadPineTree.setTreeCondition(DecoTree.TreeCondition.RANDOM_CHANCE);
        deadPineTree.setTreeConditionChance(18);
        deadPineTree.setMaxY(90);
        return deadPineTree;
	}
}
