package rtg.world.gen;

import java.util.List;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;


/**
 * @author Zeno410
 */
public class MesaBiomeCombiner {

    public final int mesa = Biome.getIdForBiome(Biomes.MESA);
    public final int mesaBryce = Biome.getIdForBiome(Biomes.MUTATED_MESA);
    public final int mesaPlateau = Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK);
    public final int mesaPlateauF = Biome.getIdForBiome(Biomes.MESA_ROCK);
    public final int mesaPlateauM = Biome.getIdForBiome(Biomes.MUTATED_MESA_CLEAR_ROCK);
    public final int mesaPlateauFM = Biome.getIdForBiome(Biomes.MUTATED_MESA_ROCK);

    public void adjust(final List<Float> result) {
        float mesaBorder = result.get(mesa);
        float bryceBorder = result.get(mesaBryce);
        float plateauBorder = result.get(mesaPlateau) + result.get(mesaPlateauM);
        float plateauFBorder = result.get(mesaPlateauF) + result.get(mesaPlateauFM);
        //result.set(mesa, 0f);
        result.set(mesaPlateauM, 0f);
        result.set(mesaPlateauFM, 0f);

        if (bryceBorder > plateauBorder&& bryceBorder > plateauFBorder) {
        	result.set(mesaPlateau,0f);
        	result.set(mesaPlateauF,0f);
        	result.set(mesaBryce,  plateauBorder + plateauFBorder+ bryceBorder);
        }
        else if (plateauBorder > plateauFBorder) {
        	result.set(mesaPlateau,plateauBorder + plateauFBorder+ bryceBorder);// ;
            result.set(mesaPlateauF,0f);
        	result.set(mesaBryce,  0f);
        }
        else {
        	result.set(mesaPlateau,0f);
        	result.set(mesaPlateauF,plateauBorder + plateauFBorder+ bryceBorder);// ;
        	result.set(mesaBryce,  0f);
        }
    }
}
