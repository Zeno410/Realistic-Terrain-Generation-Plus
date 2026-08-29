package rtg.api.world.deco.collection;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.world.deco.DecoFallenTree;
import rtg.api.world.deco.DecoShrub;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.helper.DecoHelper5050;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGPinusNigra;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionExtremeHills extends DecoCollectionBase {

    public DecoCollectionExtremeHills(BiomeConfig config) {

        super(config);

        this.addDeco(nigraDecos(85, 12, 18, 10, 14)); // Small- to medium-size trees.    
        this.addDeco(logDecos(), config.ALLOW_LOGS.get()) // Logs.
        .addDeco(shrubDecos()); // Shrubs.
        //.addDeco(flowers()) // Flowers.
        //.addDeco(mushrooms()) // Mushrooms.
        //.addDeco(pumpkins()); // Pumpkins.
	}
	
	private DecoFallenTree logs(IBlockState log, IBlockState leaves) {
	
	    DecoFallenTree decoFallenTree = new DecoFallenTree();
	    decoFallenTree.getDistribution().setNoiseDivisor(100f);
	    decoFallenTree.getDistribution().setNoiseFactor(6f);
	    decoFallenTree.getDistribution().setNoiseAddend(0.8f);
	    decoFallenTree.setLogConditionChance(16);
	    decoFallenTree.setLogBlock(log);
	    decoFallenTree.setLeavesBlock(leaves);
	    decoFallenTree.setMinSize(4);
	    decoFallenTree.setMaxSize(7);
	    decoFallenTree.setMaxY(75);
	
	    return decoFallenTree;
	}
	
	private DecoHelper5050 logDecos() {
	    return new DecoHelper5050(
	        logs(Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState()),
	        logs(BlockUtil.getStateLog(EnumType.SPRUCE), BlockUtil.getStateLeaf(EnumType.SPRUCE))
	    );
	}
	
	private DecoShrub shrubs(IBlockState log, IBlockState leaves) {
	    DecoShrub decoShrub = new DecoShrub();
	    decoShrub.setLogBlock(log);
	    decoShrub.setLeavesBlock(leaves);
	    decoShrub.setLoopMultiplier(4f);
	    decoShrub.setChance(2);
	    decoShrub.setMaxY(110);
	    return decoShrub;
	}
	
	private DecoHelper5050 shrubDecos() {
	    return new DecoHelper5050(
	        shrubs(Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState()),
	        shrubs(BlockUtil.getStateLog(EnumType.SPRUCE), BlockUtil.getStateLeaf(EnumType.SPRUCE))
	    );
	}

    protected DecoTree nigraTrees(IBlockState log, IBlockState leaves, int maxY, int minTrunkSize, int maxTrunkSize, int minCrownSize, int maxCrownSize) {

        TreeRTG nigraTree = new TreeRTGPinusNigra();
        nigraTree.setLogBlock(log);
        nigraTree.setLeavesBlock(leaves);
        nigraTree.setMinTrunkSize(minTrunkSize);
        nigraTree.setMaxTrunkSize(maxTrunkSize);
        nigraTree.setMinCrownSize(minCrownSize);
        nigraTree.setMaxCrownSize(maxCrownSize);

        DecoTree nigraDeco = new DecoTree(nigraTree);
        nigraDeco.setStrengthFactorForLoops(3f);
        nigraDeco.setStrengthNoiseFactorXForLoops(true);
        nigraDeco.getDistribution().setNoiseDivisor(100f);
        nigraDeco.getDistribution().setNoiseFactor(6f);
        nigraDeco.getDistribution().setNoiseAddend(0.8f);
        nigraDeco.setTreeCondition(DecoTree.TreeCondition.RANDOM_CHANCE);
        nigraDeco.setTreeConditionChance(20);
        nigraDeco.setMaxY(maxY);

        return nigraDeco;
    }

    protected DecoHelper5050 nigraDecos(int maxY, int minTrunkSize, int maxTrunkSize, int minCrownSize, int maxCrownSize) {
        return new DecoHelper5050(
            nigraTrees(Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState(), maxY, minTrunkSize, maxTrunkSize, minCrownSize, maxCrownSize),
            nigraTrees(BlockUtil.getStateLog(EnumType.SPRUCE), BlockUtil.getStateLeaf(EnumType.SPRUCE), maxY, minTrunkSize, maxTrunkSize, minCrownSize, maxCrownSize)
        );
    }
}
