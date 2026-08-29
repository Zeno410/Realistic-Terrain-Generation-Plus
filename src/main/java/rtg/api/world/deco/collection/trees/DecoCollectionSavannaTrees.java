package rtg.api.world.deco.collection.trees;

import net.minecraft.init.Blocks;
import rtg.api.config.BiomeConfig;
import rtg.api.world.deco.*;
import rtg.api.world.deco.collection.DecoCollectionBase;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionSavannaTrees extends DecoCollectionBase {

    public DecoCollectionSavannaTrees(BiomeConfig config) {

        super(config);

        DecoShrub acaciaShrub = new DecoShrub();
        acaciaShrub.setLogBlock(Blocks.LOG2.getDefaultState());
        acaciaShrub.setLeavesBlock(Blocks.LEAVES2.getDefaultState());
        acaciaShrub.setMaxY(160);
        acaciaShrub.setLoopMultiplier(2f);
        acaciaShrub.setChance(12);
        this.addDeco(acaciaShrub);

        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.setLoops(1);
        decoFallenTree.getDistribution().setNoiseDivisor(100f);
        decoFallenTree.getDistribution().setNoiseFactor(6f);
        decoFallenTree.getDistribution().setNoiseAddend(0.8f);
        decoFallenTree.setLogConditionChance(36);
        decoFallenTree.setLogBlock(Blocks.LOG2.getDefaultState());
        decoFallenTree.setLeavesBlock(Blocks.LEAVES2.getDefaultState());
        decoFallenTree.setMinSize(3);
        decoFallenTree.setMaxSize(6);
        this.addDeco(decoFallenTree, config.ALLOW_LOGS.get());

        this.addDeco(new DecoVariableAcacia());
    }
}