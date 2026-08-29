package rtg.api.world.biome;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.event.terraingen.DeferredBiomeDecorator;
import rtg.RTGConfig;
import rtg.api.config.BiomeConfig;
import rtg.api.util.BlockUtil;
import rtg.api.util.ChunkInfo;
import rtg.api.util.Logger;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.RealisticBiomeBase.BeachType;
import rtg.api.world.biome.RealisticBiomeBase.RiverType;
import rtg.api.world.deco.AbstractDeco;
import rtg.api.world.deco.DecoBase;
import rtg.api.world.deco.collection.DecoCollectionBase;
import rtg.api.world.deco.collection.DecoCollectionDesertRiver;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.terrain.TerrainBase;
import rtg.event.EventHandlerCommon;
import rtg.world.biome.TreelessDecorator;

import java.util.Collection;
import java.util.Random;


public interface IRealisticBiome {

    Biome baseBiome();

    ResourceLocation baseBiomeResLoc();

    int baseBiomeId();

    RiverType getRiverType();

    BeachType getBeachType();

    IRealisticBiome getRiverBiome();

    IRealisticBiome getBeachBiome();

    Biome preferredBeach();

    BiomeConfig getConfig();

    TerrainBase terrain();

    SurfaceBase surface();

    void rReplace(final ChunkPrimer primer, final BlockPos blockPos, final int x, final int y, final int depth, final RTGWorld rtgWorld, final float[] noise, final float river, final Biome[] base);

    void rReplace(ChunkPrimer primer, int i, int j, int x, int y, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base);

    float rNoise(RTGWorld rtgWorld, int x, int y, float border, float river);

    double waterLakeMult();

    double lavaLakeMult();

    float lakePressure(RTGWorld rtgWorld, int x, int y, float border, float lakeInterval, float largeBendSize, float mediumBendSize, float smallBendSize);

    void initDecos();
    
    public boolean allowVanillaTrees();

    Collection<DecoBase> getDecos();
    
    public AbstractDeco getTreeDecos();
    
    public BiomeDecorator getDecorator();

    /**
     * Adds a deco object to the list of biome decos.
     * The 'allowed' parameter allows us to pass biome config booleans dynamically when configuring the decos in the biome.
     */
    default void addDeco(DecoBase deco, boolean allowed) {

        if (allowed) {

            Collection<DecoBase> decos = this.getDecos();

            if (!deco.properlyDefined()) {
                throw new RuntimeException(deco.toString());
            }

            decos.add(deco);
        }
    }

    // Use this method to override a base biome's decorations.
    default void overrideDecorations() {}

    /**
     * Convenience method for addDeco() where 'allowed' is assumed to be true.
     */
    default void addDeco(DecoBase deco) {
        if (!deco.properlyDefined()) {
            throw new RuntimeException(deco.toString());
        }
        this.addDeco(deco, true);
    }

    // TODO: [1.12] The DecoCollection* classes should be removed and replaced by utility methods that return Collection<DecoBase>s that
    //              are added the same as in #addDeco
    @Deprecated
    default void addDecoCollection(DecoCollectionBase decoCollection) {

        // Don't add the desert river deco collection if the user has disabled it.
        if (decoCollection instanceof DecoCollectionDesertRiver) {
            if (!RTGConfig.lushRiverbanksInDesert()) {
                return;
            }
        }

        // Add this collection's decos to master deco list.
        if (decoCollection.decos.size() > 0) {
            for (int i = 0; i < decoCollection.decos.size(); i++) {
                this.addDeco(decoCollection.decos.get(i));
            }
        }

    }

    default void useTreeManager() {
    	EventHandlerCommon.treeGenerationManager.manageBiome(baseBiome());
    }
    
    default void rDecorate(final RTGWorld rtgWorld, final Random rand, final ChunkPos chunkPos, final float river, final boolean hasVillage,
    		IRealisticBiome treeBiome) {
    	ChunkInfo info = new ChunkInfo(chunkPos,rtgWorld);
    	for (DecoBase deco: this.getDecos() ) {
    		if (deco.preGenerate(river)) {
    			try {
					deco.generate(this, rtgWorld, rand, chunkPos, river, hasVillage, info);
				} catch (Exception e) {
					throw new RuntimeException(" crash generated in "+treeBiome.baseBiome().biomeName);
				}
    		}
    	}
        BlockPos basePos =  new BlockPos(chunkPos.x * 16, 0, chunkPos.z * 16);
    	if (this.allowVanillaTrees()) {
            this.baseBiome().decorate(rtgWorld.world(), rand, basePos);
    	} else {
    		// have to shut off tree decorations but not the rest
    		
    		BiomeDecorator  biomeDecorator = this.baseBiome().decorator;
    		/*biomeDecorator.extraTreeChance = 0f; 
    		biomeDecorator.treesPerChunk = 0;
    		biomeDecorator.bigMushroomsPerChunk =0;*/
    		// Biomes o Plenty has a different biome system shut off a different way; managed in TreeGenerator
    		
    		// insert RTG Trees if appropriate
    		if (EventHandlerCommon.treeGenerationManager.managingBiome(this.baseBiome())) {
    			treeBiome.getTreeDecos().generate(this, rtgWorld, rand, chunkPos, river, hasVillage, info);
    		}
    		
    		biomeDecorator.chunkPos = basePos;
    		if (overridesHardcoded()) {
    			// some biomes do decorations we don't want and can't stop in the generate() call; vanilla Flower and Roofed Forests
                baseBiome().decorator.decorate(rtgWorld.world(), rand, baseBiome(),new BlockPos(chunkPos.x * 16, 0, chunkPos.z * 16));
                //new LimitedBiomeDecorator(baseBiome().decorator).decorate(rtgWorld.world(), rand, baseBiome(),new BlockPos(chunkPos.x * 16, 0, chunkPos.z * 16));
    		} else {
                this.baseBiome().decorate(rtgWorld.world(), rand, new BlockPos(chunkPos.x * 16, 0, chunkPos.z * 16));
    		}
    		
    	}
    }

    /**
     * Some biomes have hard-coded decorations.
     * If true, RTG will call the biome decorator's decorate() method instead of the biome's decorate() method.
     */
    default boolean overridesHardcoded() { return false; }

    TerrainBase initTerrain();

    SurfaceBase initSurface();

    void initConfig();
    
    
}
