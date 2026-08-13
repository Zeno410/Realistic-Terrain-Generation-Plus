package rtg.api.world.deco.collection;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.world.gen.feature.WorldGenTrees;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
import rtg.api.world.deco.*;
import rtg.api.world.deco.helper.DecoHelperRandomSplit;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGBetulaPapyrifera;

import static net.minecraft.block.BlockFlower.EnumFlowerType.HOUSTONIA;
import static net.minecraft.block.BlockFlower.EnumFlowerType.WHITE_TULIP;
import static rtg.api.world.deco.DecoFallenTree.LogCondition.RANDOM_CHANCE;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionBirchForest extends DecoCollectionBase {

    protected Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 4.5f); 
    private float tallMin = -1f;
    private float tallMax = 3f;
    
    public DecoCollectionBirchForest(BiomeConfig config) {

        super(config);

        this
            .addDeco(tallVariableTrees(tallMin, tallMax))
            //.addDeco(randomTrees())
            .addDeco(logs(), config.ALLOW_LOGS.get()) // Add some fallen birch trees.
            .addDeco(shrubsOak()) // Oak shrubs to fill in the blanks.
            .addDeco(flowers()) // Only 1-block tall flowers so we can see the trees better.
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

    private DecoFlowersRTG flowers() {
        return new DecoFlowersRTG()
            .addFlowers(HOUSTONIA, WHITE_TULIP)
            .setMaxY(128)
            .setStrengthFactor(12f);
    }
}
