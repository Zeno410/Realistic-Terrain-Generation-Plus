package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import rtg.api.util.Logger;
import rtg.RTGConfig;


/**
 * Salix Myrtilloides (Swamp Willow)
 */
public class TreeRTGSalixMyrtilloides extends TreeRTG {

    protected IBlockState trunkLog;

    /**
     * <b>Salix Myrtilloides (Swamp Willow)</b><br>
     * <i>Only spawns near water</i><br><br>
     * <u>Relevant variables:</u><br>
     * logBlock, logMeta, leavesBlock, leavesMeta, <s>trunkSize</s>, <s>crownSize</s>, noLeaves<br><br>
     * <u>DecoTree example:</u><br>
     * DecoTree decoTree = new DecoTree(new TreeRTGSalixMyrtilloides());<br>
     * decoTree.setTreeType(DecoTree.TreeType.RTG_TREE);<br>
     * decoTree.setTreeCondition(DecoTree.TreeCondition.NOISE_GREATER_AND_RANDOM_CHANCE);<br>
     * decoTree.setDistribution(new DecoTree.Distribution(100f, 6f, 0.8f));<br>
     * decoTree.setTreeConditionNoise(0f);<br>
     * decoTree.setTreeConditionChance(4);<br>
     * decoTree.setLogBlock(Blocks.LOG);<br>
     * decoTree.logMeta = (byte)0;<br>
     * decoTree.setLeavesBlock(Blocks.LEAVES);<br>
     * decoTree.leavesMeta = (byte)0;<br>
     * decoTree.setNoLeaves(false);<br>
     * this.addDeco(decoTree);
     */
    public TreeRTGSalixMyrtilloides() {

        super();

        this.setLogBlock(Blocks.LOG.getDefaultState()).setLeavesBlock(Blocks.LEAVES.getDefaultState());
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {

        if (!this.isGroundValid(world, pos, RTGConfig.treesCanGenerateOnSand())) {
            return false;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        this.trunkLog = this.getTrunkLog(this.logBlock);
        
        SkylightTracker lightTracker = new SkylightTracker(this.furthestLikelyExtension(),pos,world);

        int height = 13;
        int leaveheight = 5;
        int branches = 6;
        int branchLenght = 6;

        for (int i = 0; i < height; i++) {
            this.placeTrunkBlock(world, new BlockPos(x, y + i, z), this.generateFlag, lightTracker);
        }
        createLeavesAroundBranch(world, rand, x, y + height, z, 3, 2, lightTracker);
        createTrunk(world, rand, x, y, z, lightTracker);

        int dir = rand.nextInt((int) (360f / branches));
        int bl;
        float xd, yd, hd, c;
        boolean m;
        for (int b = 0; b < branches; b++) {
            c = 0;
            hd = height - rand.nextFloat() * leaveheight - 2f;
            dir += (int) (360f / branches);
            xd = (float) Math.cos(dir * Math.PI / 180f);
            yd = (float) Math.sin(dir * Math.PI / 180f);
            m = false;

            while (c < branchLenght) {
                if (c > branchLenght / 2 && !m) {
                    m = true;
                    createLeavesAroundBranch(world, rand, x + (int) (c * xd), y + (int) hd, z + (int) (c * yd), 2, 1, lightTracker);
                }
                c++;
                hd += 0.5f;

                this.placeLogBlock(world,
                    new BlockPos(x + (int) (c * xd), y + (int) hd, z + (int) (c * yd)), this.trunkLog, this.generateFlag, lightTracker
                );
            }
            createLeavesAroundBranch(world, rand, x + (int) (c * xd), y + (int) hd, z + (int) (c * yd), 2, 1, lightTracker);
        }

        return true;
    }

    private void createLeavesAroundBranch(World world, Random rand, int x, int y, int z, int s, int c,SkylightTracker lightTracker) {

        int l;
        int t = (int) Math.pow(s, 2);
        for (int i = -s; i <= s; i++) {
            for (int j = -s; j <= s; j++) {
                for (int k = -s; k <= s; k++) {
                    l = i * i + j * j + k * k;
                    if (l <= t) {
                        if ((l < t - c || rand.nextBoolean())) {
                            if (!this.noLeaves) {

                                this.placeLeavesBlock(world, new BlockPos(x + i, y + j, z + k), this.leavesBlock, this.generateFlag, lightTracker);
                                if (j < -(s - 2) && rand.nextInt(3) != 0) {
                                    createVine(world, rand, x + i, y + j, z + k, lightTracker);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createVine(World world, Random rand, int x, int y, int z, SkylightTracker lightTracker) {

        int r = rand.nextInt(3) + 5;
        for (int i = -1; i > -r; i--) {
            this.placeLeavesBlock(world, new BlockPos(x, y + i, z), this.leavesBlock, this.generateFlag, lightTracker);
        }
    }

    private void createTrunk(World world, Random rand, int x, int y, int z, SkylightTracker lightTracker) {

        int[] pos = new int[]{0, 0, 1, 0, 0, 1, -1, 0, 0, -1};
        int sh;

        for (int t = 0; t < 5; t++) {
            sh = rand.nextInt(3) + y;
            while (sh > y - 3) {
                this.placeLogBlock(world, new BlockPos(x + pos[t * 2], sh, z + pos[t * 2 + 1]), this.trunkLog, this.generateFlag, lightTracker);
                sh--;
            }
        }
    }

    /*@Override
    protected boolean isGroundValid(World world, BlockPos trunkPos, boolean sandAllowed) {

        int x = trunkPos.getX();
        int y = trunkPos.getY();
        int z = trunkPos.getZ();
        IBlockState cb;
        BlockPos posTemp;
        boolean earth = false;
        boolean water = false;

        Logger.info("acceptable");
        for (IBlockState example: validGroundBlocks) {
            Logger.info("{}",example.toString());
        }
        Logger.info("end");
        for (int c1 = -2; c1 <= 2; c1++) {
            for (int c3 = -2; c3 <= 2; c3++) {
                for (int c2 = -1; c2 <= 1; c2++) {
                    posTemp = new BlockPos(x + c1, y + c2, z + c3);
                    cb = world.getBlockState(posTemp);
                    if (cb == Blocks.WATER.getDefaultState()) {
                        water = true;
                    } else {
	                    for (IBlockState example: validGroundBlocks) {
	                        boolean match = example.equals(cb);
	                        if (match) earth = true;
	                        Logger.info("{} {} {}",example.toString(), cb.toString(), match);
	                    }
                    }
                }
            }
        }

        if (!(earth && water)) {
            return false;
        }

        return true;
    }*/
}
