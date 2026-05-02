package rtg.world.biome.realistic.novamterram;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;

import rtg.api.util.BlockUtil;
import rtg.api.world.RTGWorld;
import rtg.api.world.terrain.TerrainBase;
import rtg.api.world.terrain.heighteffect.GroundEffect;
import rtg.api.world.terrain.heighteffect.HeightEffect;
import rtg.api.world.terrain.heighteffect.JitterEffect;
import rtg.api.world.terrain.heighteffect.RaiseEffect;
import rtg.api.world.terrain.heighteffect.SpikeEverywhereEffect;
import rtg.api.world.terrain.heighteffect.VoronoiBorderEffect;


public class RealisticBiomeNTAegeanArchipelago extends RealisticBiomeNTBaseForest {

    public RealisticBiomeNTAegeanArchipelago(Biome biome) {

        super(biome);
    }

    @Override
    public void initConfig() {

        super.initConfig();
        this.getConfig().addProperty(this.getConfig().ALLOW_LOGS).set(true);
        this.getConfig().addProperty(this.getConfig().FALLEN_LOG_DENSITY_MULTIPLIER);
    }

    @Override
    public void initDecos() {

        super.initDecos();
        fallenTrees(
            new IBlockState[]{
                BlockUtil.getStateLog(BlockPlanks.EnumType.OAK),
                BlockUtil.getStateLog(BlockPlanks.EnumType.OAK)
            },
            new int[]{2, 2}
        );
    }
    
    @Override
    public TerrainBase initTerrain() {
        return new RidgedArchpelago(50f, 52f, 100f,200f);
        //return new TerrainVanillaExtremeHills(10f, 120f, 10f, 200f);
    }
    
    public static class RidgedArchpelago extends TerrainBase {

        private final HeightEffect heightIncrease;
        private final HeightEffect multiplier;
        private final HeightEffect groundEffect;
        private float height;
        private float width;
        private float ridgeWidth = 300f;
        private float valleyFloor = -0.2f;

        public RidgedArchpelago(float landHeight, float baseHeight, float hillWidth, float _ridgeWidth) {
            height = landHeight;
            base = baseHeight;
            width = hillWidth;
            ridgeWidth = _ridgeWidth;

            SpikeEverywhereEffect baseHills = new SpikeEverywhereEffect();
            baseHills.spiked = new RaiseEffect(height * 2f / 3f);
            baseHills.wavelength = width;
            baseHills.minimumSimplex = -0.2f;
            baseHills.octave = 3;
            baseHills.power = 1.9f;

            SpikeEverywhereEffect additionalHeightSpikes = new SpikeEverywhereEffect();
            additionalHeightSpikes.spiked = new RaiseEffect(height / 3f);
            additionalHeightSpikes.wavelength = width / 3f;
            additionalHeightSpikes.minimumSimplex = -0.2f;
            additionalHeightSpikes.octave = 4;
            additionalHeightSpikes.power = 1.9f;

            JitterEffect additionalHeight = new JitterEffect();
            additionalHeight.amplitude = additionalHeightSpikes.wavelength / 3;
            additionalHeight.wavelength = additionalHeightSpikes.wavelength / 2;
            additionalHeight.jittered = additionalHeightSpikes;

            SpikeEverywhereEffect rougheningSpikes = new SpikeEverywhereEffect();
            rougheningSpikes.spiked = new RaiseEffect(height / 8f);
            rougheningSpikes.wavelength = width / 10f;
            rougheningSpikes.minimumSimplex = -0.2f;
            rougheningSpikes.octave = 5;
            rougheningSpikes.power = 1.9f;

            JitterEffect roughening = new JitterEffect();
            roughening.amplitude = rougheningSpikes.wavelength / 3;
            roughening.wavelength = rougheningSpikes.wavelength / 2;
            roughening.jittered = rougheningSpikes;

            JitterEffect hillJitter = new JitterEffect();
            hillJitter.amplitude = 15f;
            hillJitter.wavelength = 50f;
            hillJitter.jittered = baseHills.plus(additionalHeight).plus(roughening);
            heightIncrease = hillJitter;


            VoronoiBorderEffect ridging = new VoronoiBorderEffect();
            ridging.pointWavelength = ridgeWidth;
            ridging.floor = valleyFloor;
            ridging.minimumDivisor = .2f;

            JitterEffect ridgeJitter = new JitterEffect();
            ridgeJitter.amplitude = 20f;
            ridgeJitter.wavelength = 50f;
            ridgeJitter.jittered = ridging;

            JitterEffect ridgeJitterrette = new JitterEffect();
            ridgeJitterrette.amplitude = 7f;
            ridgeJitterrette.wavelength = 20f;
            ridgeJitterrette.jittered = ridgeJitter;
            multiplier = ridgeJitterrette;

            groundEffect = new GroundEffect(6);
        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {
            // ground effect is increased by the multiplier
            float groundEffectLevel = groundEffect.added(rtgWorld, (float) x, (float) y);
            float ridging = multiplier.added(rtgWorld, (float) x, (float) y);
            ridging = TerrainBase.bayesianAdjustment(ridging, 2);
            float result = base + ridging * (groundEffectLevel + heightIncrease.added(rtgWorld, (float) x, (float) y))
                + groundEffectLevel;
            return TerrainBase.mountainCap(result);
        }
    }
}
