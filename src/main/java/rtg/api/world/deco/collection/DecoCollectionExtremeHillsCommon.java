package rtg.api.world.deco.collection;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.world.deco.*;
import rtg.api.world.deco.helper.DecoHelper5050;

import static net.minecraft.block.BlockFlower.EnumFlowerType.*;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionExtremeHillsCommon extends DecoCollectionBase {

    public DecoCollectionExtremeHillsCommon(BiomeConfig config) {

        super(config);
        this.addDeco(boulders()); // Boulders.
    }

    private DecoBoulder boulders() {
        DecoBoulder decoBoulder = new DecoBoulder();
        decoBoulder.setBoulderBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState());
        decoBoulder.setChance(12);
        decoBoulder.setMaxY(90);
        decoBoulder.setStrengthFactor(2f);
        return decoBoulder;
    }

    private DecoFlowersRTG flowers() {
        DecoFlowersRTG decoFlowersRTG = new DecoFlowersRTG();
// TODO: [1.12] Original flowers list had dupes.. may need to increase flower generation here.
        decoFlowersRTG.addFlowers(ALLIUM, HOUSTONIA, DANDELION);
        decoFlowersRTG.setMaxY(80);
        decoFlowersRTG.setLoops(3);
        return decoFlowersRTG;
    }

    private DecoMushrooms mushrooms() {
        DecoMushrooms decoMushrooms = new DecoMushrooms();
        decoMushrooms.setMaxY(70);
        decoMushrooms.setRandomFloat(3f);
        return decoMushrooms;
    }

    private DecoPumpkin pumpkins() {
        DecoPumpkin decoPumpkin = new DecoPumpkin();
        decoPumpkin.setMaxY(90);
        decoPumpkin.setRandomFloat(30f);
        return decoPumpkin;
    }
}
