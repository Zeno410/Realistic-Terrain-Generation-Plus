package rtg.api.world.deco.collection;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenTrees;
import rtg.api.config.BiomeConfig;
import rtg.api.world.deco.DecoBase;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoTree.TreeCondition;
import rtg.api.world.deco.helper.DecoHelperRandomSplit;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGPiceaSitchensis;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGUmbrella;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionSmallPineTreesForest extends DecoCollectionBase {

    public DecoCollectionSmallPineTreesForest(BiomeConfig config) {

        super(config);

        TreeRTG sitchensisTree = new TreeRTGPiceaSitchensis();
        sitchensisTree.setLogBlock(Blocks.LOG.getDefaultState());
        sitchensisTree.setLeavesBlock(Blocks.LEAVES.getDefaultState());
        sitchensisTree.setMinTrunkSize(4);
        sitchensisTree.setMaxTrunkSize(10);
        sitchensisTree.setMinCrownSize(6);
        sitchensisTree.setMaxCrownSize(14);

        DecoTree oakPine = new DecoTree(sitchensisTree);
        oakPine.setStrengthFactorForLoops(3f);
        oakPine.setTreeCondition(TreeCondition.RANDOM_CHANCE);
        oakPine.setTreeConditionChance(4);
        oakPine.setMaxY(110);

        DecoTree vanillaTrees = new DecoTree(new TreeRTGUmbrella());
        vanillaTrees.setStrengthFactorForLoops(3f);
        vanillaTrees.setTreeCondition(TreeCondition.RANDOM_CHANCE);
        vanillaTrees.setTreeConditionChance(4);
        vanillaTrees.setMaxY(110);

        DecoHelperRandomSplit decoHelperRandomSplit = new DecoHelperRandomSplit();
        decoHelperRandomSplit.decos = new DecoBase[]{oakPine, vanillaTrees};
        decoHelperRandomSplit.chances = new int[]{8, 4};
        this.addDeco(decoHelperRandomSplit);
    }
}
