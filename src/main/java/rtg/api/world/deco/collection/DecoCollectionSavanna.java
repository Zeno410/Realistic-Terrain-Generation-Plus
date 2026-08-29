package rtg.api.world.deco.collection;

import net.minecraft.init.Blocks;
import rtg.api.config.BiomeConfig;
import rtg.api.world.deco.*;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionSavanna extends DecoCollectionBase {

    public DecoCollectionSavanna(BiomeConfig config) {

        super(config);

        DecoBoulder decoBoulder = new DecoBoulder();
        decoBoulder.setBoulderBlock(Blocks.COBBLESTONE.getDefaultState());
        decoBoulder.setChance(32);
        decoBoulder.setMaxY(95);
        this.addDeco(decoBoulder);

        DecoDoubleGrass decoDoubleGrass = new DecoDoubleGrass();
        decoDoubleGrass.setMaxY(128);
        decoDoubleGrass.setStrengthFactor(3f);
        this.addDeco(decoDoubleGrass);
    }
}
