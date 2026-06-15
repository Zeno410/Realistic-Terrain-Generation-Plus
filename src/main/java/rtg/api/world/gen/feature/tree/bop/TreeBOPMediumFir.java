package rtg.api.world.gen.feature.tree.bop;

import java.util.Random;

import biomesoplenty.api.block.BOPBlocks;
import java.util.ArrayList;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.util.BlockUtil;
import rtg.api.world.gen.feature.tree.rtg.RTGTreeBranch;
import rtg.api.world.gen.feature.tree.rtg.SkylightTracker;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;

public class TreeBOPMediumFir extends TreeRTG {

    /**
     * <b>Picea Sitchensis (Sitka Spruce)</b><br><br>
     * <u>Relevant variables:</u><br>
     * logBlock, logMeta, leavesBlock, leavesMeta, trunkSize, crownSize, noLeaves<br><br>
     * <u>DecoTree example:</u><br>
     * DecoTree decoTree = new DecoTree(new TreeRTGPiceaSitchensis());<br>
     * decoTree.setTreeType(DecoTree.TreeType.RTG_TREE);<br>
     * decoTree.setTreeCondition(DecoTree.TreeCondition.NOISE_GREATER_AND_RANDOM_CHANCE);<br>
     * decoTree.setDistribution(new DecoTree.Distribution(100f, 6f, 0.8f));<br>
     * decoTree.setTreeConditionNoise(0f);<br>
     * decoTree.setTreeConditionChance(4);<br>
     * decoTree.setLogBlock(Blocks.LOG);<br>
     * decoTree.logMeta = (byte)1;<br>
     * decoTree.setLeavesBlock(Blocks.LEAVES);<br>
     * decoTree.leavesMeta = (byte)1;<br>
     * decoTree.setMinTrunkSize(4);<br>
     * decoTree.setMaxTrunkSize(9);<br>
     * decoTree.setMinCrownSize(5);<br>
     * decoTree.setMaxCrownSize(14);<br>
     * decoTree.setNoLeaves(false);<br>
     * this.addDeco(decoTree);
     */
	
	protected double fourBranchiness = 0.7;
    public TreeBOPMediumFir() {

        super();

        this.setLogBlock(BOPBlocks.log_0.getStateFromMeta(12));
        this.setLeavesBlock(BOPBlocks.leaves_1.getStateFromMeta(2));
        this.setBranchBlock(BOPBlocks.log_0.getStateFromMeta(15));
        this.trunkSize = 8;
        this.crownSize = 10;
        this.setNoLeaves(false);
    }
	public float estimatedSize() {

    	float branchLength= (crownSize/4) + 1;
    	return branchLength*branchLength/10f;
	}
	
	@Override
    public int furthestLikelyExtension() {
    	float branchLength= (crownSize/4) + 1;
    	float extension = 2f;
    	return (int)(extension + branchLength);
	}
	
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
    	fourBranchiness = 0.5 + 0.4*rand.nextDouble();
        
    	pos = this.dropToGround(world, pos);
    	
    	if (!this.isGroundValid(world, pos)) {
           return false;
        }

    	//if (rand.nextDouble()>.001) throw new RuntimeException();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        SkylightTracker lightTracker = new SkylightTracker(this.furthestLikelyExtension(),pos,world);
        
        int i;
        for (i = 0; i < this.trunkSize; i++) {
            this.placeTrunkBlock(world, new BlockPos(x, y, z), this.generateFlag, lightTracker);
            y++;
        }

        int pX = 0;
        int pZ = 0;
        i = 0;
        int trunkTop = y;
        y += crownSize;
        // leaf on top
        this.placeLeavesBlock(world, new BlockPos(x, y, z), lightTracker);
        y--;
        // then leafburst

        this.placeLeavesBlock(world, new BlockPos(x+1, y, z), lightTracker);
        this.placeLeavesBlock(world, new BlockPos(x-1, y, z), lightTracker);
        this.placeLeavesBlock(world, new BlockPos(x, y, z+1), lightTracker);
        this.placeLeavesBlock(world, new BlockPos(x, y, z-1), lightTracker);
        this.placeLeavesBlock(world, new BlockPos(x, y, z), lightTracker);
        
        boolean oneMoreLeaves = true;// that top log block looked bad
        while (trunkTop +2 <= y) {
        	
            // skip down 2 blocks; tried occasional 3 blocks but it looked bad
        	int drop = 2;
        	
            while(drop >0) {
            	if (oneMoreLeaves) {
                    this.placeLeavesBlock(world, new BlockPos(x, y, z), this.logBlock, this.generateFlag, lightTracker);
                    oneMoreLeaves = false;
            		
            	} else {
                    this.placeLogBlock(world, new BlockPos(x, y, z), this.logBlock, this.generateFlag, lightTracker);
            	}
            	drop --;
            	y --;
            }
            float branchLength = (float)(trunkTop + crownSize - y)*.25f -.5f;// the branch system tends to round up.
            
            if (rand.nextDouble()> fourBranchiness) {
            	this.threeBranches(world, rand, x, y, z, branchLength, lightTracker);
            } else {
            	this.fourBranches(world, rand, x, y, z, branchLength, lightTracker);
            }
        }

        buildLeaves(world, x, y - 1, z + 1, lightTracker);
        buildLeaves(world, x, y - 1, z - 1, lightTracker);
        buildLeaves(world, x + 1, y - 1, z, lightTracker);
        buildLeaves(world, x - 1, y - 1, z, lightTracker);
        buildLeaves(world, x, y, z, lightTracker);

        lightTracker.checkLighting(world);
        return true;
    }
    

    private void threeBranches(World world, Random rand, int x, int y, int z, float length, SkylightTracker lightTracker) {
    	double initialDirection = rand.nextDouble()*2.0*Math.PI;
    	BlockPos start = new BlockPos(x,y,z);
    	float branchLength = length -.2f + rand.nextFloat()*0.4f;
    	branchOfSet(world,rand,start,initialDirection, branchLength,3,lightTracker);
    	initialDirection += Math.PI*2.0/3.0 + 0.1 + rand.nextDouble()*0.2;
    	branchLength = length -.2f + rand.nextFloat()*0.4f;
    	branchOfSet(world,rand,start,initialDirection, branchLength,3,lightTracker);
    	initialDirection += Math.PI*2.0/3.0 + 0.1 + rand.nextDouble()*0.2;
    	branchLength = length -.2f + rand.nextFloat()*0.4f;
    	branchOfSet(world,rand,start,initialDirection, branchLength,3,lightTracker);
    }
    
    private void branchOfSet(World world, Random rand, BlockPos start, double direction, float length, int setSize, SkylightTracker lightTracker) {
    	ArrayList<BlockPos> locations = new ArrayList<>();
    	ArrayList<Float> remainingLengths = new ArrayList<>();
    	RTGTreeBranch branch = new RTGTreeBranch(direction,0f, length, 0,start); 
    	
    	double leftDirection = direction - Math.PI/(double)(setSize);
    	double rightDirection = direction + Math.PI/(double)(setSize);
    	
    	while (branch.notDone()) {
    		locations.add(branch.movedOrthogonally());
    		remainingLengths.add(branch.remainingLength());
    	}
    	int leafDistanceUsed = 0;
    	// place blocks up to last six
    	int i = 0;
    	for( i = 0; i < locations.size() -6;i ++)  {
    		 if (!this.placeLogBlock(world, locations.get(i), lightTracker))  return; // abort branch if placement fails
    	    	RTGTreeBranch leftLeaves = new RTGTreeBranch(
    	    			leftDirection,
    	    			0f, 
    	    			leavesToPlace(remainingLengths.get(i),rand,1), 
    	    			0,
    	    			locations.get(i)); 
    	    	this.leafLine(world, rand, leftLeaves, leafDistanceUsed, lightTracker);
    	    	RTGTreeBranch rightLeaves = new RTGTreeBranch(
    	    			rightDirection,
    	    			0f, 
    	    			leavesToPlace(remainingLengths.get(i),rand,1), 
    	    			0,
    	    			locations.get(i)); 
    	    	this.leafLine(world, rand, rightLeaves, leafDistanceUsed, lightTracker);
    	}
    	while ( i< locations.size()) {
    		leafDistanceUsed++;
   		 if (!this.placeLeavesBlock(world, locations.get(i), lightTracker))  return; // abort branch if placement fails
	    	RTGTreeBranch leftLeaves = new RTGTreeBranch(
	    			leftDirection,
	    			0f, 
	    			leavesToPlace(remainingLengths.get(i),rand,1), 
	    			0,
	    			locations.get(i)); 
	    	this.leafLine(world, rand, leftLeaves, leafDistanceUsed, lightTracker);
	    	RTGTreeBranch rightLeaves = new RTGTreeBranch(
	    			rightDirection,
	    			0f, 
	    			leavesToPlace(remainingLengths.get(i),rand,1), 
	    			0,
	    			locations.get(i)); 
	    	this.leafLine(world, rand, rightLeaves, leafDistanceUsed, lightTracker);
    		
    		i++;
    	}
    }
    
    private float leavesToPlace(float remainingLength, Random rand,float variability) {
    	float leafLength = remainingLength - variability + rand.nextFloat()*variability;
    	return Math.min(leafLength, 6f);
    }
    
    private void fourBranches(World world, Random rand, int x, int y, int z, float length, SkylightTracker lightTracker) {
    	double initialDirection = rand.nextDouble()*2.0*Math.PI;
    	BlockPos start = new BlockPos(x,y,z);
    	float branchLength = length -.2f + rand.nextFloat()*0.4f;
    	branchOfSet(world,rand,start,initialDirection, branchLength,4,lightTracker);
    	initialDirection += Math.PI/2.0 + 0.1 + rand.nextDouble()*0.2;
    	branchLength = length -.2f + rand.nextFloat()*0.4f;
    	branchOfSet(world,rand,start,initialDirection, branchLength,4,lightTracker);
    	initialDirection += Math.PI/2.0 + 0.1 + rand.nextDouble()*0.2;
    	branchLength = length -.2f + rand.nextFloat()*0.4f;
    	branchOfSet(world,rand,start,initialDirection, branchLength, 4,lightTracker);
    	initialDirection += Math.PI/2.0 + 0.1 + rand.nextDouble()*0.2;
    	branchLength = length -.2f + rand.nextFloat()*0.4f;
    	branchOfSet(world,rand,start,initialDirection, branchLength,4,lightTracker);
    }


    
    public void buildBranch(World world, Random rand, int x, int y, int z, int dX, int dZ, int logLength, int leaveSize, SkylightTracker lightTracker) {

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = 0; k < 1; k++) {
                	int ragged = 0;
                	if (rand.nextInt(4)==0) ragged = 1;
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) + ragged < leaveSize + 1) {
                        buildLeaves(world, x + i + (dX * logLength), y + k, z + j + (dZ * logLength),lightTracker);
                    }
                }
            }
        }

        // one on top
        
        buildLeaves(world, x + (dX * logLength), y + 1, z + (dZ * logLength),lightTracker);
        
        for (int m = 1; m <= logLength; m++) {
            this.placeLogBlock(world, new BlockPos(x + (dX * m), y, z + (dZ * m)), this.branchBlock, generateFlag, lightTracker);
            if (m>1) {
            	// extra leaves
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        for (int k = 0; k < 1; k++) {
                        	int ragged = 0;
                        	if (rand.nextInt(4)==0) ragged = 1;
                            if (Math.abs(i) + Math.abs(j) + Math.abs(k) + ragged < leaveSize + 1) {
                                buildLeaves(world, x + i + (dX * m), y + k, z + j + (dZ * m), lightTracker);
                            }
                        }
                    }
                }
            }
        }
    }

    public void buildLeaves(World world, int x, int y, int z, SkylightTracker lightTracker) {

        if (!this.noLeaves) {

            this.placeLeavesBlock(world, new BlockPos(x, y, z), this.leavesBlock, this.generateFlag, lightTracker);
        }
    }
}
