package rtg.world.biome.realistic.gravityfalls;

import javax.annotation.Nonnull;

import net.minecraft.world.biome.Biome;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.surface.SurfaceBase;
import rtg.api.world.surface.SurfaceGeneric;


public abstract class RealisticBiomeGFBase extends RealisticBiomeBase {

    public RealisticBiomeGFBase(@Nonnull final Biome baseBiome, @Nonnull final RiverType riverType, @Nonnull final BeachType beachType) {

        super(baseBiome, riverType, beachType);
    }

    public RealisticBiomeGFBase(@Nonnull final Biome baseBiome) {
        this(baseBiome, RiverType.NORMAL, BeachType.NORMAL);
    }

    public RealisticBiomeGFBase(@Nonnull final Biome baseBiome, @Nonnull final RiverType riverType) {
        this(baseBiome, riverType, BeachType.NORMAL);
    }

    public RealisticBiomeGFBase(@Nonnull final Biome baseBiome, @Nonnull final BeachType beachType) {
        this(baseBiome, RiverType.NORMAL, beachType);
    }

    @Override
    public SurfaceBase initSurface() {
        return new SurfaceGeneric(getConfig(), this.baseBiome().topBlock, this.baseBiome().fillerBlock);
    }

    @Override
    public void initDecos() {
    }
}
