package rtg.api.world.deco;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.fml.common.eventhandler.Event;
import rtg.RTGConfig;
import rtg.api.event.DecorateBiomeEventRTG;
import rtg.api.util.BlockUtil;
import rtg.api.util.ChunkInfo;
import rtg.api.util.Distribution;
import rtg.api.util.Valued;
import rtg.api.util.WeightedChooser;
import rtg.api.util.BlockUtil.MatchType;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;
import rtg.api.world.deco.DecoTree.TreeType;
import rtg.api.world.gen.feature.tree.rtg.TreeDensityLimiter;

public class DecoTreeSet extends DecoTree {

    private Distribution treeFrequencyDistribution = new Distribution(RTGWorld.getTreeFrequencyNoiseDivisor(), 2.5f, 4.5f); 
    
	public DecoTreeSet() {
		super();
		setStrengthNoiseFactorForLoops(true);
		setDistribution(treeFrequencyDistribution);
	}
	
	WeightedChooser<DecoTree> trees = new WeightedChooser<>();
	
    public void add(DecoTree tree) {trees.add(tree);}
    
    public void add(Valued<DecoTree> tree) {trees.add(tree);}
    
    public void add(DecoTree tree, double weight) {trees.add(tree,weight);}    
    
    @Override
    @Deprecated
    public boolean properlyDefined() {

        return true;
    }
	
    @Override
    public void generate(final IRealisticBiome biome, final RTGWorld rtgWorld, final Random rand, final ChunkPos chunkPos, final float river, final boolean hasVillage, ChunkInfo chunkInfo) {
    	// duped from DecoVariableTree. Should probably be moved to DecoTree but I don't have the nerve

        final BlockPos offsetPos = getOffsetPos(chunkPos);
        /*
         * Determine how many trees we're going to try to generate (loopCount).
         * The actual number of trees that end up being generated could be *less* than this value,
         * depending on environmental conditions.
         */
        // TODO: [1.12] What is the point of deriving a noise value from static BlockPos within a chunk (population origin) and then applying
        //              it to a feature taking place at some other arbitrary place in the chunk. This seems nonsensical and makes needless
        //              calls to the noise generator. This should be replaced by a random amount.
        // Zeno: These are slowly changing values and essentially the same within a chunk, but must differ substantially between chunks for variability
        float noise = distribution.getValue(offsetPos, rtgWorld.treeDistributionNoise());
        int loopCount = (this.strengthFactorForLoops > 0f) ? (int) this.strengthFactorForLoops : this.loops;
        loopCount = (this.strengthNoiseFactorForLoops) ? (int) noise : loopCount;
        loopCount = (this.strengthNoiseFactorXForLoops) ? (int) (noise * this.strengthFactorForLoops) : loopCount;

        if (loopCount < 1) {
            return;
        }

        // Now let's check the configs to see if we should increase/decrease this value.
        loopCount = this.applyConfigMultipliers(loopCount, biome);

        if (loopCount < 1) {
            return;
        }

        /*
         * Since RTG posts a TREE event for each batch of trees it tries to generate (instead of one event per chunk),
         * we post this custom event so that we can pass the number of trees RTG expects to generate in each batch.
         *
         * This provides more contextual information to mods like Recurrent Complex, which can use the info to better
         * determine how to handle each batch of trees.
         *
         * Because the custom event extends DecorateBiomeEvent.Decorate, it still works with mods that don't need
         * the additional context.
         */
        //TODO [1.12] Trees should just generate how they do in the vanilla BiomeDecorator::genDecorations and use the Forge event.
        DecorateBiomeEventRTG.DecorateRTG event = new DecorateBiomeEventRTG.DecorateRTG(rtgWorld.world(), rand, offsetPos, Decorate.EventType.TREE, loopCount);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);

        if (event.getResult() != Event.Result.DENY) {

            loopCount = event.getModifiedAmount();
            if (loopCount < 1) { return; }

            // TODO: [1.12] This should be done in #setLeavesBlock.
            DecoBase.tweakTreeLeaves(this, false, true);

            TreeDensityLimiter treesRemaining = new TreeDensityLimiter(loopCount);
            while (treesRemaining.notDone()) {
                final BlockPos pos = offsetPos.add(rand.nextInt(16), 0, rand.nextInt(16));
                int y = rtgWorld.world().getHeight(pos).getY();
                if (y <= this.maxY && y >= this.minY && isValidTreeCondition(noise, rand)) {

                    // If we're in a village, check to make sure the tree has extra room to grow to avoid corrupting the village.
                    if (hasVillage) {
                        if (BlockUtil.checkVerticalBlocks(MatchType.ALL, rtgWorld.world(), pos, -1, Blocks.FARMLAND) ||
                            !BlockUtil.checkAreaBlocks(MatchType.ALL_IGNORE_REPLACEABLE, rtgWorld.world(), pos, 2)) {
                            return;
                        }
                    }
                    // get a suitable tree Type
                    doVariableGenerate(rand,  chunkInfo, pos,y, treesRemaining);
                } else {
                	treesRemaining.allowed(1f, rand);
                }

            }
        }
        else if (RTGConfig.enableDebugging()) {
        }
    }
	public void doVariableGenerate(Random rand, ChunkInfo chunkInfo, BlockPos column, int y, TreeDensityLimiter treesRemaining) { 
		// pick one of the trees and have it do the generate
		trees.choice(rand).doVariableGenerate(rand, chunkInfo, column, y, treesRemaining);
		return;
	}
}
