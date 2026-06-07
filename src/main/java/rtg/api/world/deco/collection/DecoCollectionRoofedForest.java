package rtg.api.world.deco.collection;

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
import rtg.api.world.deco.DecoTree.TreeCondition;
import rtg.api.world.deco.DecoTree.TreeType;
import rtg.api.world.deco.DecoVariableDarkOak;
import rtg.api.world.deco.helper.DecoHelperThisOrThat;

public class DecoCollectionRoofedForest extends DecoCollectionBase {
	
    protected Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 4.5f); 
    private float tallMin = -1f;
    private float tallMax = 3f;
    
	public DecoCollectionRoofedForest(BiomeConfig config) {
		super(config);
		addDeco(darkOakTrees(tallMin, tallMax));
		initDecos();
	}
	
    private DecoTree darkOakTrees(float noiseMin, float noiseMax) {

        return new DecoVariableDarkOak()
            .setStrengthFactorForLoops(6f)
            .setTreeType(TreeType.RTG_TREE)
            .setDistribution(treeFrequencyDistribution)
            .setTreeCondition(TreeCondition.ALWAYS_GENERATE)
            .setTreeConditionNoise(noiseMin)
            .setTreeConditionNoise2(noiseMax)
            .setTreeConditionChance(1)
            .setMaxY(120)
            .setStrengthNoiseFactorForLoops(true)
            .setStrengthNoiseFactorXForLoops(false)// just in case
            ;
    }
    
	public void initDecos() {

        DecoMushrooms decoMushrooms = new DecoMushrooms();
        decoMushrooms.setChance(4);
        decoMushrooms.setMaxY(90);
        decoMushrooms.setRandomType(DecoMushrooms.RandomType.ALWAYS_GENERATE);
        this.addDeco(decoMushrooms);

        
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
        darkOakShrub.setLoopMultiplier(6f);

        DecoShrub oakShrub = new DecoShrub();
        oakShrub.setLogBlock(Blocks.LOG.getDefaultState());
        oakShrub.setLeavesBlock(Blocks.LEAVES.getDefaultState());
        oakShrub.setMaxY(100);
        oakShrub.setLoopMultiplier(6f);

        this.addDeco(new DecoHelperThisOrThat(4, DecoHelperThisOrThat.ChanceType.NOT_EQUALS_ZERO, darkOakShrub, oakShrub));

        DecoBoulder decoBoulder = new DecoBoulder();
        decoBoulder.setBoulderBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState());
        decoBoulder.setChance(16);
        decoBoulder.setMaxY(80);
        decoBoulder.setStrengthFactor(2f);
        this.addDeco(decoBoulder);

        DecoCobwebs decoCobwebs = new DecoCobwebs();
        decoCobwebs.setChance(1);
        decoCobwebs.setMinY(63);
        decoCobwebs.setMaxY(76);
        decoCobwebs.setStrengthFactor(24f);
        decoCobwebs.setAdjacentBlock(BlockUtil.getStateLog(EnumType.DARK_OAK));
        decoCobwebs.setMinAdjacents(2);
        this.addDeco(decoCobwebs, this.getConfig().ALLOW_COBWEBS.get());

        DecoDeadBush decoDeadBush = new DecoDeadBush();
        decoDeadBush.setMaxY(100);
        decoDeadBush.setChance(2);
        decoDeadBush.setStrengthFactor(2f);
        this.addDeco(decoDeadBush);

        DecoWorldGen decoBigShroom = new DecoWorldGen(new WorldGenBigMushroom(), DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM);
        decoBigShroom.setMinY(63);
        decoBigShroom.setMaxY(100);
        decoBigShroom.setChance(8);
        this.addDeco(decoBigShroom);
    }
}
