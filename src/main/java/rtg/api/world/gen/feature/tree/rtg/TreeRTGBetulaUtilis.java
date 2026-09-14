package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.util.Logger;


/**
 * Betula Utilis (Himalayan Birch)
 */
public class TreeRTGBetulaUtilis extends TreeRTG {

    /**
     * <b>Cupressus Sempervirens (Italian Cypress)</b><br><br>
     * <u>Relevant variables:</u><br>
     * logBlock, logMeta, leavesBlock, leavesMeta, trunkSize, crownSize, noLeaves<br><br>
     * <u>DecoTree example:</u><br>
     * DecoTree decoTree = new DecoTree(new TreeRTGCupressusSempervirens());<br>
     * decoTree.setTreeType(DecoTree.TreeType.RTG_TREE);<br>
     * decoTree.setTreeCondition(DecoTree.TreeCondition.NOISE_GREATER_AND_RANDOM_CHANCE);<br>
     * decoTree.setDistribution(new DecoTree.Distribution(100f, 6f, 0.8f));<br>
     * decoTree.setTreeConditionNoise(0f);<br>
     * decoTree.setTreeConditionChance(4);<br>
     * decoTree.setLogBlock(Blocks.LOG);<br>
     * decoTree.logMeta = (byte)1;<br>
     * decoTree.setLeavesBlock(Blocks.LEAVES);<br>
     * decoTree.leavesMeta = (byte)1;<br>
     * decoTree.setMinTrunkSize(3);<br>
     * decoTree.setMaxTrunkSize(6);<br>
     * decoTree.setMinCrownSize(5);<br>
     * decoTree.setMaxCrownSize(10);<br>
     * decoTree.setNoLeaves(false);<br>
     * this.addDeco(decoTree);
     */
    public TreeRTGBetulaUtilis() {

        super();
    }
    
	public float estimatedSize() {

    	float branchLength= 3;
    	return branchLength*branchLength/16f;
	}
	
	@Override
    public int furthestLikelyExtension() {

    	int branchLength= 3;
    	return branchLength;
	}
	
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {

        if (!this.isGroundValid(world, pos)) {
            return false;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        SkylightTracker lightTracker = new SkylightTracker(this.furthestLikelyExtension(),pos,world);
        
        int i, j, k;
        for (i = 0; i < this.trunkSize; i++) {
            this.placeTrunkBlock(world, new BlockPos(x, y, z),  this.generateFlag,lightTracker);
            y++;
        }

        int small = (int) Math.ceil((double) (this.crownSize / 4));
        int large = crownSize - small;

        for (i = 0; i < 2; i++) {
            if (!this.noLeaves) {

                for (j = -1; j <= 1; j++) {
                    for (k = -1; k <= 1; k++) {
                        if (Math.abs(j) + Math.abs(k) < 2) {
                            this.placeLeavesBlock(world, new BlockPos(x + j, y, z + k), this.leavesBlock, this.generateFlag,lightTracker);
                        }
                    }
                }

            }

            this.placeLogBlock(world, new BlockPos(x, y, z), this.logBlock, this.generateFlag,lightTracker);
            y++;
        }
        
        for (i = 0; i < small-3; i++) {
            if (!this.noLeaves) {

                for (j = -1; j <= 1; j++) {
                    for (k = -1; k <= 1; k++) {
                        if (Math.abs(j) + Math.abs(k) < 2 || (rand.nextInt(small) < i)) {
                            this.placeLeavesBlock(world, new BlockPos(x + j, y, z + k), this.leavesBlock, this.generateFlag,lightTracker);
                        }
                    }
                }

            }

            this.placeLogBlock(world, new BlockPos(x, y, z), this.logBlock, this.generateFlag,lightTracker);
            y++;
        }
        
        for (i = 0; i < large; i++) {
            if (!this.noLeaves) {
            	
            	int cap =  Math.max (i,large - 3*(large-i));

                for (j = -2; j <= 2; j++) {
                    for (k = -2; k <= 2; k++) {
                        if (Math.abs(j) + Math.abs(k) != 4 && ((j > -2 && k > -2 && j < 2 && k < 2) || rand.nextInt(large) < cap)) {
                            this.placeLeavesBlock(world, new BlockPos(x + j, y, z + k), this.leavesBlock, this.generateFlag,lightTracker);
                        }
                    }
                }
            }
            this.placeLogBlock(world, new BlockPos(x, y, z), this.logBlock, this.generateFlag,lightTracker);
            y++;
        }

        for (i = 0; i < 1; i++) {
            if (!this.noLeaves) {

                for (j = -1; j <= 1; j++) {
                    for (k = -1; k <= 1; k++) {
                        if (Math.abs(j) + Math.abs(k) < 2 || (rand.nextInt(4) != 0)) {
                            this.placeLeavesBlock(world, new BlockPos(x + j, y, z + k), this.leavesBlock, this.generateFlag,lightTracker);
                        }
                    }
                }
            }

            this.placeLeavesBlock(world, new BlockPos(x, y, z), this.leavesBlock, this.generateFlag,lightTracker);
            y++;
        }
        
        this.placeLeavesBlock(world, new BlockPos(x, y, z), this.leavesBlock, this.generateFlag,lightTracker);

        if (!this.noLeaves) {
            if (rand.nextInt(2)!= 0) this.placeLeavesBlock(world, new BlockPos(x + 1, y, z), this.leavesBlock, this.generateFlag,lightTracker);
            if (rand.nextInt(2)!= 0) this.placeLeavesBlock(world, new BlockPos(x - 1, y, z), this.leavesBlock, this.generateFlag,lightTracker);
            if (rand.nextInt(2)!= 0) this.placeLeavesBlock(world, new BlockPos(x, y, z + 1), this.leavesBlock, this.generateFlag,lightTracker);
            if (rand.nextInt(2)!= 0) this.placeLeavesBlock(world, new BlockPos(x, y, z - 1), this.leavesBlock, this.generateFlag,lightTracker);
        }

        lightTracker.checkLighting(world);
        return true;
    }
}
