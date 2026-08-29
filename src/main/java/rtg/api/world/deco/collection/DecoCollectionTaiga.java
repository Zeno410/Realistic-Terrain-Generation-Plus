package rtg.api.world.deco.collection;

import net.minecraft.init.Blocks;
import rtg.api.config.BiomeConfig;
import rtg.api.world.deco.*;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionTaiga extends DecoCollectionBase {
	
    		
    private float tallMin = -1f;
    private float tallMax = 3f;

    public DecoCollectionTaiga(BiomeConfig config, float grassStrengthFactor) {

        super(config);

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
}
