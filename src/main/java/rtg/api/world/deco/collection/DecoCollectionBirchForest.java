package rtg.api.world.deco.collection;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.world.gen.feature.WorldGenTrees;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
import rtg.api.world.deco.*;
import rtg.api.world.deco.helper.DecoHelperRandomSplit;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGBetulaPapyrifera;

import static net.minecraft.block.BlockFlower.EnumFlowerType.HOUSTONIA;
import static net.minecraft.block.BlockFlower.EnumFlowerType.WHITE_TULIP;
import static rtg.api.world.deco.DecoFallenTree.LogCondition.RANDOM_CHANCE;


/**
 * @author WhichOnesPink
 */
public class DecoCollectionBirchForest extends DecoCollectionBase {
    
    public DecoCollectionBirchForest(BiomeConfig config) {

        super(config);

        this.addDeco(flowers()) // Only 1-block tall flowers so we can see the trees better.
        ;
    }

    private DecoFlowersRTG flowers() {
        return new DecoFlowersRTG()
            .addFlowers(HOUSTONIA, WHITE_TULIP)
            .setMaxY(128)
            .setStrengthFactor(12f);
    }
}
