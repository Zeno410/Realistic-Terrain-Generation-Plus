package rtg.api.world.deco.collection;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
import rtg.api.world.deco.*;
import rtg.api.world.deco.DecoTree.TreeCondition;
import rtg.api.world.deco.helper.DecoHelperThisOrThat;
import rtg.api.world.deco.helper.DecoHelperThisOrThat.ChanceType;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGCupressusSempervirens;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGPiceaSitchensis;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionTaiga extends DecoCollectionBase {
	
    private Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 4.5f);
    private final DecoVariableTaigaTree variableTrees;
    		
    private float tallMin = -1f;
    private float tallMax = 3f;

    public DecoCollectionTaiga(BiomeConfig config, float grassStrengthFactor) {

        super(config);
        variableTrees = initVariableTrees(tallMin, tallMax);
        
        this.addDeco(variableTrees);

        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.getDistribution().setNoiseDivisor(100f);
        decoFallenTree.getDistribution().setNoiseFactor(6f);
        decoFallenTree.getDistribution().setNoiseAddend(0.8f);
        decoFallenTree.setLogConditionChance(32);
        decoFallenTree.setLogBlock(BlockUtil.getStateLog(EnumType.SPRUCE));
        decoFallenTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.SPRUCE));
        decoFallenTree.setMinSize(3);
        decoFallenTree.setMaxSize(6);
        this.addDeco(decoFallenTree, config.ALLOW_LOGS.get());

        DecoShrub decoShrubSpruce = new DecoShrub();
        decoShrubSpruce.setLogBlock(BlockUtil.getStateLog(EnumType.SPRUCE));
        decoShrubSpruce.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.SPRUCE));
        decoShrubSpruce.setMaxY(100);
        decoShrubSpruce.setLoopMultiplier(6f);
        decoShrubSpruce.setChance(6);
        this.addDeco(decoShrubSpruce);

        DecoBoulder decoBoulder = new DecoBoulder();
        decoBoulder.setBoulderBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState());
        decoBoulder.setChance(20);
        decoBoulder.setMaxY(95);
        decoBoulder.setStrengthFactor(2f);
        this.addDeco(decoBoulder);

        DecoPumpkin decoPumpkin = new DecoPumpkin();
        decoPumpkin.setMaxY(90);
        decoPumpkin.setRandomFloat(32f);
        //this.addDeco(decoPumpkin);

        DecoMushrooms decoMushrooms = new DecoMushrooms();
        decoMushrooms.setMaxY(90);
        decoMushrooms.setRandomFloat(24f);
        //this.addDeco(decoMushrooms);
    }
    
    public void changeAvgHeightSqrt(float change) {
    	variableTrees.changeAvgHeightSqrt(change);
    }
    
    public void changeHeightVariability(float change) {
    	variableTrees.changeHeightVariability(change);
    }
    
    private DecoVariableTaigaTree initVariableTrees(float noiseMin, float noiseMax) {
    	
    	DecoVariableTaigaTree result = new DecoVariableTaigaTree();
        
            result.setStrengthFactorForLoops(6f)
            .setTreeCondition(TreeCondition.ALWAYS_GENERATE)
            .setTreeConditionNoise(noiseMin)
            .setTreeConditionNoise2(noiseMax)
            .setTreeConditionChance(1)
            .setMaxY(120)
            .setStrengthNoiseFactorForLoops(true)
            .setStrengthNoiseFactorXForLoops(false)// just in case
            ;
            result.setDistribution(treeFrequencyDistribution);
            return result;
    }
}
