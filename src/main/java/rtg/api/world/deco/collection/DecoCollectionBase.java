package rtg.api.world.deco.collection;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.util.math.ChunkPos;
import rtg.api.config.BiomeConfig;
import rtg.api.util.ChunkInfo;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;
import rtg.api.world.deco.AbstractDeco;
import rtg.api.world.deco.DecoBase;


/**
 * @author WhichOnesPink
 */
// TODO: [1.12] The DecoCollection* classes should be removed and replaced by utility methods that return a Collection<DecoBase> that
//              are added the same as in IRealisticBiome#addDeco
// Zeno: Restored. Nobody is ever going to do this.

public class DecoCollectionBase extends AbstractDeco {

    public ArrayList<DecoBase> decos;
    protected BiomeConfig config;

    public DecoCollectionBase(BiomeConfig config) {

        this.config = config;
        this.decos = new ArrayList<DecoBase>();
    }
    
    protected BiomeConfig getConfig() { return config;}

    public DecoCollectionBase addDeco(DecoBase deco) {

        if (!deco.properlyDefined()) {
            throw new RuntimeException();
        }
        this.decos.add(deco);
        return this;
    }

    public DecoCollectionBase addDeco(DecoBase deco, boolean allowed) {

        if (allowed) {
            if (!deco.properlyDefined()) {
                throw new RuntimeException();
            }
            this.decos.add(deco);
        }
        return this;
    }

    public  void generate(final IRealisticBiome biome, final RTGWorld rtgWorld, final Random rand, final ChunkPos chunkPos, final float river, final boolean hasVillage, ChunkInfo chunkInfo) {
    	for (AbstractDeco deco: decos) {
    		deco.generate(biome, rtgWorld, rand, chunkPos, river, hasVillage, chunkInfo);
    	}
    }

}
