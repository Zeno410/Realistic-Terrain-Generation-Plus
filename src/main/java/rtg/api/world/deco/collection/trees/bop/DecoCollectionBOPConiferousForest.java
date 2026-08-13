package rtg.api.world.deco.collection.trees.bop;

import rtg.api.config.BiomeConfig;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.bop.VariableBOPFirTree;
import rtg.api.world.deco.DecoTree.TreeCondition;
import rtg.api.world.deco.collection.DecoCollectionBase;

public class DecoCollectionBOPConiferousForest extends DecoCollectionBase {

    private float tallMin = -1f;
    private float tallMax = 3f;
    
    private Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 4.5f); 

	public DecoCollectionBOPConiferousForest(BiomeConfig config) {
		super(config);        
		this.addDeco(firTrees(tallMin, tallMax));
	}
	

    private DecoTree firTrees(float noiseMin, float noiseMax) {

        DecoTree result =  new VariableBOPFirTree()
            .setStrengthFactorForLoops(6f)
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
