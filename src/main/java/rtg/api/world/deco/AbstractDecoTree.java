package rtg.api.world.deco;

import rtg.RTGConfig;
import rtg.api.util.Distribution;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;

public abstract class AbstractDecoTree extends DecoBase {
	
    protected Distribution distribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 4.5f); 
    // Parameter object for noise calculations.

	public AbstractDecoTree() {
	}

    protected int applyConfigMultipliers(final int loopCount, final IRealisticBiome biome) {
        return (int)(loopCount * RTGConfig.treeDensityMultiplier() * biome.getConfig().TREE_DENSITY_MULTIPLIER.get());
    }

    public Distribution getDistribution() {

        return distribution;
    }

    public AbstractDecoTree setDistribution(Distribution distribution) {

        this.distribution = distribution;
        return this;
    }
}
