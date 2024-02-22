package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


/**
 * Acacia Abyssinica (Flat-top Acacia)
 */
public class TreeRTGAcaciaAbyssinica extends TreeRTG {

    /**
     * <b>Acacia Abyssinica (Flat-top Acacia)</b><br><br>
     * <u>Relevant variables:</u><br>
     * logBlock, logMeta, leavesBlock, leavesMeta, trunkSize, <s>crownSize</s>, noLeaves<br><br>
     * <u>DecoTree example:</u><br>
     * DecoTree decoTree = new DecoTree(new TreeRTGAcaciaAbyssinica());<br>
     * decoTree.setTreeType(DecoTree.TreeType.RTG_TREE);<br>
     * decoTree.setTreeCondition(DecoTree.TreeCondition.NOISE_GREATER_AND_RANDOM_CHANCE);<br>
     * decoTree.setDistribution(new DecoTree.Distribution(100f, 6f, 0.8f));<br>
     * decoTree.setTreeConditionNoise(0f);<br>
     * decoTree.setTreeConditionChance(4);<br>
     * decoTree.setLogBlock(Blocks.LOG2);<br>
     * decoTree.logMeta = (byte)0;<br>
     * decoTree.setLeavesBlock(Blocks.LEAVES2);<br>
     * decoTree.leavesMeta = (byte)0;<br>
     * decoTree.setMinTrunkSize(6);<br>
     * decoTree.setMaxTrunkSize(16);<br>
     * decoTree.setNoLeaves(false);<br>
     * this.addDeco(decoTree);
     */
    public TreeRTGAcaciaAbyssinica() {

        super();

        this.setLogBlock(Blocks.LOG2.getDefaultState());
        this.setLeavesBlock(Blocks.LEAVES2.getDefaultState());
        this.trunkSize = 12;
        this.lowestVariableTrunkProportion = 0.5f;
        this.trunkProportionVariability = 0.2f;
        this.maxAllowedObstruction = 4;
        
    }

    @Override
    public int furthestLikelyExtension() {
    	return crownSize+2;
    }
    
    @Override
    public float estimatedSize() {
    	return (float)(furthestLikelyExtension()*furthestLikelyExtension())/16f;
    }   
    
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {

        if (!this.isGroundValid(world, pos)) {
            return false;
        }
        

        SkylightTracker lightTracker = new SkylightTracker(this.furthestLikelyExtension(),pos,world);

        final int x = pos.getX();
        int y = pos.getY();
        final int z = pos.getZ();


        for (int i = 0; i < trunkSize+crownSize; i++) {
            this.placeTrunkBlock(world, new BlockPos(x, y + i, z), this.generateFlag, lightTracker);
        }
        genLeaves(world, rand, x, y + trunkSize+crownSize, z,lightTracker);

        int sh, eh, dir;
        float xd, yd, c;

        dir = rand.nextInt(360);
        int highestBranch = trunkSize;// not currently used
        int branchCount = crownSize/2 + rand.nextInt(crownSize-crownSize/2);
        for (int a = branchCount; a > -1; a--) {
            sh = trunkSize + rand.nextInt(crownSize);
            highestBranch = Math.max(highestBranch, sh);
            dir += 100 + rand.nextInt(72);// roughly golden mean with lots of variability
            xd = (float) Math.cos(dir * Math.PI / 180f) * 2f;
            yd = (float) Math.sin(dir * Math.PI / 180f) * 2f;
            c = 1;

            while (sh < trunkSize+crownSize) {
                this.placeLogBlock(world, new BlockPos(x + (int) (xd * c), y + sh, z + (int) (yd * c)), this.logBlock, this.generateFlag, lightTracker);
                sh++;
                c += 0.5f;
            }
            genLeaves(world, rand, x + (int) (xd * c), y + sh, z + (int) (yd * c),lightTracker);
        }

        return true;
    }

    public void genLeaves(World world, Random rand, int x, int y, int z, SkylightTracker lightTracker) {

        if (!this.noLeaves) {

            int i;
            int j;
            for (i = -2; i <= 2; i++) {
                for (j = -2; j <= 2; j++) {
                    if (Math.abs(i) + Math.abs(j) < 4) {
                        this.placeLeavesBlock(world, new BlockPos(x + i, y + 1, z + j), this.leavesBlock, this.generateFlag, lightTracker);
                    }
                }
            }

            for (i = -3; i <= 3; i++) {
                for (j = -3; j <= 3; j++) {
                    if (Math.abs(i) + Math.abs(j) < 5) {
                        this.placeLeavesBlock(world, new BlockPos(x + i, y, z + j), this.leavesBlock, this.generateFlag, lightTracker);
                    }
                }
            }
        }

        this.placeLogBlock(world, new BlockPos(x, y, z), this.logBlock, this.generateFlag, lightTracker);
    }
}
