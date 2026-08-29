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
import rtg.api.world.deco.DecoVariableDarkOak;
import rtg.api.world.deco.helper.DecoHelperThisOrThat;

public class DecoCollectionRoofedForest extends DecoCollectionBase {
	

	public DecoCollectionRoofedForest(BiomeConfig config) {
		super(config);
		initDecos();
	}
    
	public void initDecos() {

        DecoMushrooms decoMushrooms = new DecoMushrooms();
        decoMushrooms.setChance(4);
        decoMushrooms.setMaxY(90);
        decoMushrooms.setRandomType(DecoMushrooms.RandomType.ALWAYS_GENERATE);
        this.addDeco(decoMushrooms);

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

    }
}
