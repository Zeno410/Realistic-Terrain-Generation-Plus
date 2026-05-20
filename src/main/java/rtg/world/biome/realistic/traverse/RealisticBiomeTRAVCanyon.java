package rtg.world.biome.realistic.traverse;

import net.minecraft.world.biome.Biome;
import rtg.api.util.PlateauUtil;
import rtg.api.util.noise.ISimplexData2D;
import rtg.api.util.noise.SimplexData2D;
import rtg.api.world.RTGWorld;
import rtg.api.world.terrain.TerrainBase;
import rtg.api.world.terrain.heighteffect.BumpyHillsEffect;
import rtg.api.world.terrain.heighteffect.JitterEffect;
import rtg.api.world.terrain.heighteffect.VoronoiPlateauEffect;

public class RealisticBiomeTRAVCanyon extends RealisticBiomeTRAVBase {


	public RealisticBiomeTRAVCanyon(Biome baseBiome) {
		super(baseBiome);
	}

	@Override
	public TerrainBase initTerrain() {
		return new TerrainRTGCanyon(68);
	}
    public static class TerrainRTGCanyon extends TerrainBase {
    	// based on the Mesa code but with varying step height

        private static final float stepStart = 0.15f;
        private static final float stepFinish = 0.4f;
        final VoronoiPlateauEffect plateau;
        final int groundNoise;
        private float jitterWavelength = 30;
        private float jitterAmplitude = 10;
        private float bumpinessMultiplier = 0.1f;
        private float bumpinessWavelength = 10f;
        private float variabilityMultiplier = 0.15f;
        private float variabilityWavelength = 30f;
        private float variabilityMultiplier2 = 0.075f;
        private float variabilityWavelength2 = 15f;
        private TerrainBase stepSize = new HighlandStep();

        public TerrainRTGCanyon(float base) {
            plateau = new VoronoiPlateauEffect();
            plateau.pointWavelength = 200;
            this.base = base;
            groundNoise = 10;
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int passedX, int passedY, float border, float river) {
            ISimplexData2D jitterData = SimplexData2D.newDisk();
            rtgWorld.simplexInstance(1).multiEval2D(passedX / jitterWavelength, passedY / jitterWavelength, jitterData);
            float x = (float) (passedX + jitterData.getDeltaX() * jitterAmplitude);
            float y = (float) (passedY + jitterData.getDeltaY() * jitterAmplitude);
            float bordercap = (bordercap = border * 3.5f - 2.5f) > 1 ? 1.0f : bordercap;
            float rivercap = (rivercap = 3f * river) > 1 ? 1.0f : rivercap;
            float bumpiness = rtgWorld.simplexInstance(2).noise2f(x / bumpinessWavelength, y / bumpinessWavelength) * bumpinessMultiplier;
            float variability = rtgWorld.simplexInstance(3).noise2f(x / variabilityWavelength, y / variabilityWavelength) * variabilityMultiplier;
            float variability2 = rtgWorld.simplexInstance(3).noise2f(x / variabilityWavelength2, y / variabilityWavelength2) * variabilityMultiplier2;
            // no holes
            variability += variability2;
            variability= Math.max(variability, 0f);
            float baseSimplex = plateau.added(rtgWorld, x, y);
            baseSimplex += variability ;
            float simplex = baseSimplex * bordercap * rivercap + bumpiness;
            
            
            // calculate step height
            // based on the Highlands code but much more variability
            float stepHeight = stepSize.generateNoise(rtgWorld, passedX, passedY, border, river);
            
            float added = PlateauUtil.stepIncrease(simplex, stepStart, stepFinish, stepHeight) / border;
            float result = riverized(base + TerrainBase.groundNoise(x, y, groundNoise, rtgWorld), river) + added;
            return result;//64f + 100f*simplex;//result;
        }

    }
    
    public static class HighlandStep extends TerrainBase {

        private float baseHeight = 5f;
        private BumpyHillsEffect onTop = new BumpyHillsEffect();
        private JitterEffect bigJitter;
        private JitterEffect withJitter;

        public HighlandStep() {

            onTop.hillHeight = 60;
            onTop.hillWavelength = 80;
            onTop.hillsEverywhere = false;
            onTop.spikeHeight = 40;
            onTop.spikeWavelength = 15;

            withJitter = new JitterEffect();
            withJitter.amplitude = 3;
            withJitter.wavelength = 5;
            withJitter.jittered = onTop;
            
            bigJitter = new JitterEffect();
            bigJitter.amplitude = 10;
            bigJitter.wavelength = 20;
            bigJitter.jittered = withJitter;
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return baseHeight + bigJitter.added(rtgWorld, x, y) + groundNoise(x, y, 6, rtgWorld);
        }
    }
}
