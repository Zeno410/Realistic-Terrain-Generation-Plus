package rtg.api.world.deco.collection.trees;

import net.minecraft.block.BlockPlanks.EnumType;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
import rtg.api.world.deco.*;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;

import static rtg.api.world.deco.DecoFallenTree.LogCondition.RANDOM_CHANCE;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionBirchForestTrees extends DecoCollectionBase {

    protected Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 4.5f); 
    private float tallMin = -1f;
    private float tallMax = 3f;
    
    public DecoCollectionBirchForestTrees(BiomeConfig config) {

        super(config);

        this
            .addDeco(tallVariableTrees(tallMin, tallMax))
            //.addDeco(randomTrees())
            .addDeco(logs(), config.ALLOW_LOGS.get()) // Add some fallen birch trees.
            .addDeco(shrubsOak()) // Oak shrubs to fill in the blanks.
        ;
    }

    protected DecoTree tallVariableTrees(float noiseMin, float noiseMax) {
    	
        DecoTree result =  new DecoVariableMaterialTree(TreeMaterials.inBirchForest)
                .setStrengthFactorForLoops(6f)
                .setTreeCondition(DecoTree.TreeCondition.ALWAYS_GENERATE)
                .setTreeConditionNoise(noiseMin)
                .setTreeConditionNoise2(noiseMax)
                .setTreeConditionChance(1)
                .setStrengthNoiseFactorForLoops(true);
        
        result.setDistribution(treeFrequencyDistribution);
        
        return result;
    }

    private DecoFallenTree logs() {
        return new DecoFallenTree()
            .setLogCondition(RANDOM_CHANCE)
            .setLogConditionChance(8)
            .setLogBlock(BlockUtil.getStateLog(EnumType.BIRCH))
            .setLeavesBlock(BlockUtil.getStateLeaf(EnumType.BIRCH))
            .setMinSize(3)
            .setMaxSize(6);
    }

    private DecoShrub shrubsOak() {
        return new DecoShrub()
            .setMaxY(120)
            .setLoopMultiplier(1.5f);
    }

}
