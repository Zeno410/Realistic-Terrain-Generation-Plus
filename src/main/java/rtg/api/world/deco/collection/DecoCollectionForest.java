package rtg.api.world.deco.collection;

import rtg.api.config.BiomeConfig;
import rtg.api.world.deco.DecoFlowersRTG;

import static net.minecraft.block.BlockFlower.EnumFlowerType.ALLIUM;
import static net.minecraft.block.BlockFlower.EnumFlowerType.BLUE_ORCHID;
import static net.minecraft.block.BlockFlower.EnumFlowerType.DANDELION;
import static net.minecraft.block.BlockFlower.EnumFlowerType.HOUSTONIA;
import static net.minecraft.block.BlockFlower.EnumFlowerType.ORANGE_TULIP;
import static net.minecraft.block.BlockFlower.EnumFlowerType.OXEYE_DAISY;
import static net.minecraft.block.BlockFlower.EnumFlowerType.PINK_TULIP;
import static net.minecraft.block.BlockFlower.EnumFlowerType.POPPY;
import static net.minecraft.block.BlockFlower.EnumFlowerType.RED_TULIP;
import static net.minecraft.block.BlockFlower.EnumFlowerType.WHITE_TULIP;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionForest extends DecoCollectionBase {


    public DecoCollectionForest(BiomeConfig config) {

        super(config);

        this
            .addDeco(flowers()) // Only 1-block tall flowers so we can see the trees better.
        ;
       
    }

    private DecoFlowersRTG flowers() {
        return new DecoFlowersRTG()
            .addFlowers(POPPY, BLUE_ORCHID, ALLIUM, HOUSTONIA, RED_TULIP, ORANGE_TULIP, WHITE_TULIP, PINK_TULIP, OXEYE_DAISY, DANDELION)
            .setMaxY(128)
            .setStrengthFactor(6f);
    }        
    

}
