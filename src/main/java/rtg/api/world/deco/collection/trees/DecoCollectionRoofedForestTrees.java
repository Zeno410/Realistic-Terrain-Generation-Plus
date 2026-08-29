package rtg.api.world.deco.collection.trees;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
import rtg.api.world.deco.DecoBoulder;
import rtg.api.world.deco.DecoCobwebs;
import rtg.api.world.deco.DecoDeadBush;
import rtg.api.world.deco.DecoFallenTree;
import rtg.api.world.deco.DecoMushrooms;
import rtg.api.world.deco.DecoShrub;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoWorldGen;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.deco.DecoTree.TreeCondition;
import rtg.api.world.deco.DecoVariableDarkOak;
import rtg.api.world.deco.helper.DecoHelperThisOrThat;

public class DecoCollectionRoofedForestTrees extends DecoCollectionBase {
	
    protected Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 3.5f, 7.0f); 
    private float tallMin = -1f;
    private float tallMax = 3f;
    
	public DecoCollectionRoofedForestTrees(BiomeConfig config) {
		super(config);
		initDecos();
	}
	
    private DecoTree darkOakTrees(float noiseMin, float noiseMax) {

        DecoTree result =  new DecoVariableDarkOak()
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
    
	public void initDecos() {       
		DecoWorldGen decoBigShroom = new DecoWorldGen(new WorldGenBigMushroom(), DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM);
	    decoBigShroom.setMinY(63);
	    decoBigShroom.setMaxY(100);
	    decoBigShroom.setChance(8);
	    decoBigShroom.setLoops(4);
	    this.addDeco(decoBigShroom);

		addDeco(darkOakTrees(tallMin, tallMax));
		
        DecoFallenTree decoFallenTree = new DecoFallenTree();
        decoFallenTree.getDistribution().setNoiseDivisor(80f);
        decoFallenTree.getDistribution().setNoiseFactor(60f);
        decoFallenTree.getDistribution().setNoiseAddend(-15f);
        decoFallenTree.setLogConditionChance(16);
        decoFallenTree.setLogBlock(BlockUtil.getStateLog(EnumType.DARK_OAK));
        decoFallenTree.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.DARK_OAK));
        decoFallenTree.setMinSize(4);
        decoFallenTree.setMaxSize(9);
        this.addDeco(decoFallenTree, this.config.ALLOW_LOGS.get());

        DecoShrub darkOakShrub = new DecoShrub();
        darkOakShrub.setLogBlock(BlockUtil.getStateLog(EnumType.DARK_OAK));
        darkOakShrub.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.DARK_OAK));
        darkOakShrub.setMaxY(100);
        darkOakShrub.setLoopMultiplier(3f);

        DecoShrub oakShrub = new DecoShrub();
        oakShrub.setLogBlock(Blocks.LOG.getDefaultState());
        oakShrub.setLeavesBlock(Blocks.LEAVES.getDefaultState());
        oakShrub.setMaxY(100);
        oakShrub.setLoopMultiplier(3f);

        this.addDeco(new DecoHelperThisOrThat(4, DecoHelperThisOrThat.ChanceType.NOT_EQUALS_ZERO, darkOakShrub, oakShrub));


    }
}