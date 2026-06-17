package rtg.api.world.gen.feature.tree.bop;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.world.gen.feature.tree.rtg.RTG3DBranch;
import rtg.api.world.gen.feature.tree.rtg.RTGTreeBranch;
import rtg.api.world.gen.feature.tree.rtg.SkylightTracker;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;

public abstract class TreeBOPBaseEucalyptus extends TreeRTG {

		protected double fourBranchiness = 0.7;
		protected double shortenFrequency = .1;
		protected double shortenDistance = 3.0;
		
	    public TreeBOPBaseEucalyptus() {

	        super();

	        this.setLogBlock(BOPTreeMaterials.Picker.eucalyptus.log);
	        this.setLeavesBlock(BOPTreeMaterials.Picker.eucalyptus.leaves);
	        this.setBranchBlock(BOPTreeMaterials.Picker.eucalyptus.branches);
	    }
		public float estimatedSize() {

	    	float branchLength= (crownSize/4) + 2;
	    	return branchLength*branchLength/10f;
		}
		
		@Override
	    public int furthestLikelyExtension() {
	    	float branchLength= (crownSize/4) + 2;
	    	float extension = 2f;
	    	return (int)(extension + branchLength);
		}
		
		public double shorten(Random rand) {
			if (rand.nextDouble()<this.shortenFrequency) {
				return this.shortenDistance + jigger(rand, 1.0);
			}
			return 0;
		}
	    public void leafFlourish(World world, BlockPos pos, SkylightTracker lightTracker) {
	    	// places leaves around a specified block;
	    	this.placeLeavesBlock(world, pos.up(), lightTracker);
	    	this.placeLeavesBlock(world, pos.east(), lightTracker);
	    	this.placeLeavesBlock(world, pos.north(), lightTracker);
	    	this.placeLeavesBlock(world, pos.west(), lightTracker);
	    	this.placeLeavesBlock(world, pos.south(), lightTracker);
	    }
	    
	    public void leafSpray(World world, Random rand, BlockPos pos, SkylightTracker lightTracker) {
	    	// puts up a couple of leaf branches with flourishes; intended for the tops
	    	int branches = 3 - rand.nextInt(4)/3; // usually 3 with occasional 2
	    	double initialDirection = rand.nextDouble()*2.0*Math.PI;
	    	for (int i = 0; i < branches; i++) {
	    		RTGTreeBranch branch = new RTGTreeBranch(
	    				initialDirection + jigger(rand,.2),
	    				(float)(0.5 + jigger(rand,.5)), 
	    				2, pos) ;
	    		leafSprayBranch(world,rand,branch,lightTracker);
	    		initialDirection += Math.PI*2.0/(double)branches;
	    	}
	    }
	    
	    protected void leafSprayBranch(World world, Random rand, RTGTreeBranch branch, SkylightTracker lightTracker) {
	    	int placed = 0;
	    	while ((placed<3 && branch.notDone())) {
	    		if (!this.placeLeavesBlock(world, branch.movedOrthogonally(), lightTracker)) return;
	    		placed ++;
	    	}
	    	this.leafFlourish(world, branch.location(), lightTracker);
	    }
	    
	    protected void buildBranch(World world, Random rand, RTG3DBranch branch, boolean beefy, SkylightTracker lightTracker) {
	    	if (branch.stage >10) throw new RuntimeException();// infinite loop defense;
	    	// decide whether to  split
	    	if (3.0 + rand.nextDouble()*2.0< branch.remainingLength())  {
	    		// split
	    		double targetLength = branch.remainingLength() - (Math.min(6.0,branch.remainingLength()/(2.0)) + jigger(rand,.5));
	    		while (branch.remainingLength() > targetLength) {
	    			if (beefy) {
		    			if (!this.placeBranchBlock(world, branch.movedOrthogonally(), lightTracker)) return;// abort if blocked
	    			} else {
		    			if (!this.placeBranchBlock(world, branch.moved(), lightTracker)) return;// abort if blocked
	    			}
	    		}
	    		double leftDirection = branch.horizontalDirection()-1.4+jigger(rand,.2);
	    		RTG3DBranch leftBranch = new RTG3DBranch(
	    				leftDirection,
	    				branch.verticalAngle+jigger(rand,.05),
	    				(float)(branch.remainingLength()+jigger(rand,.4) - shorten(rand)),
	    				branch.stage + 1,
	    				branch.location());
	    		double rightDirection = branch.horizontalDirection()+1.4+jigger(rand,.2);
	    		RTG3DBranch rightBranch = new RTG3DBranch(
	    				rightDirection,
	    				branch.verticalAngle+jigger(rand,.05),
	    				(float)(branch.remainingLength()+jigger(rand,.4) - shorten(rand)),
	    				branch.stage + 1,
	    				branch.location());
	    		if (rand.nextFloat()> 0.5f) {
	    			// randomizing order in case
	    			buildBranch(world,rand,leftBranch,beefy, lightTracker);
	    			buildBranch(world,rand,rightBranch,beefy,lightTracker);
	    		} else {
	    			buildBranch(world,rand,rightBranch,beefy,lightTracker);
	    			buildBranch(world,rand,leftBranch,beefy, lightTracker);
	    		}
	    	} else {
	    		// continue to end
	    		while (branch.remainingLength() > 3.0) {
	    			if (beefy) {
		    			if (!this.placeBranchBlock(world, branch.movedOrthogonally(), lightTracker)) return;// abort if blocked
	    			} else {
		    			if (!this.placeBranchBlock(world, branch.moved(), lightTracker)) return;// abort if blocked
	    			}
	    		}
	    		leafSpray(world,rand,branch.location(),lightTracker);
	    	}
	    }
}
