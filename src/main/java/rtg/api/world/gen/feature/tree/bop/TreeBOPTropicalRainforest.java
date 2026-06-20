package rtg.api.world.gen.feature.tree.bop;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.world.gen.feature.tree.rtg.RTG3DBranch;
import rtg.api.world.gen.feature.tree.rtg.SkylightTracker;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;

public class TreeBOPTropicalRainforest extends TreeRTG  {

	public TreeBOPTropicalRainforest() {
		super();
	    this.trunkSize = 8;
	    this.crownSize = 6;
        this.branchBlock = BOPTreeMaterials.mahogany.branches;
        this.logBlock = BOPTreeMaterials.mahogany.log;
        this.leavesBlock = BOPTreeMaterials.mahogany.leaves;
	}
	
	public float estimatedSize() {
	
		float branchLength= (crownSize);
		return branchLength*branchLength/10f;
	}
	
	@Override
	public int furthestLikelyExtension() {
		float branchLength= (crownSize*1.5f);
		float extension = 2f;
		return (int)(extension + branchLength);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
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
	    y--;// fix fencepost error
	    BlockPos branchBase = new BlockPos(x,y,z);
	
		double initialDirection = rand.nextDouble()*2.0*Math.PI;
	    int branches = 3;

		double verticalAngle = 1.2 + jigger(rand,0.15);
		RTG3DBranch branch = new RTG3DBranch(
				initialDirection + jigger(rand,.2),
				verticalAngle, 
				this.crownSize, branchBase) ;
		buildUpBranch(world,rand,branch, lightTracker);
		initialDirection += Math.PI*2.0/(double)branches;
		for (int j = 0; j < branches; j++) {
			double horizontalAngle = 0.1 + jigger(rand,0.25);
			branch = new RTG3DBranch(
					initialDirection + jigger(rand,.2),
					horizontalAngle, 
					(float)(this.crownSize)*1.5f, branchBase) ;
			buildSideBranch(world,rand,branch, lightTracker);
			initialDirection += Math.PI*2.0/(double)branches;
		}
	    return true;
	    
	}
	
    protected void buildUpBranch(World world,Random rand, RTG3DBranch branch, SkylightTracker lightTracker) {
    	int startLevel = branch.location().getY();
    	while ((branch.notDone())&&(branch.location().getY()-startLevel < 2)) {
    		this.placeBranchBlock(world, branch.movedOrthogonally(), lightTracker);
    	}
    	if (branch.remainingLength()>2) {
    		double backDirection = branch.horizontalDirection() - Math.PI + jigger(rand,.5); 
    		// back towards the center but highly randomized for variation
    		this.splitUpwards(world, rand, branch.location(), branch.remainingLength(), backDirection, lightTracker);
    	} else {
    		while (branch.remainingLength()>1.5) {
    			this.placeBranchBlock(world, branch.movedOrthogonally(), lightTracker);
    		}
    		this.leafTop(world, rand, branch.location(), lightTracker);
    	}
    	
    }
    
    protected void buildSideBranch(World world,Random rand, RTG3DBranch branch, SkylightTracker lightTracker) {
		float splitpoint = (float) (branch.remainingLength() -3.5 + jigger(rand,0.5));
		RTG3DBranch leafBranch = new RTG3DBranch(branch);
		while (branch.remainingLength() > splitpoint&&branch.remainingLength() > 2){
			this.placeBranchBlock(world, branch.moved(), lightTracker);
		}
		while (leafBranch.remainingLength()>branch.remainingLength()) {
			this.placeLeavesBlock(world, leafBranch.movedOrthogonally(), lightTracker);
		}
		// is enough left to split
		if (branch.remainingLength() > 3 + jigger(rand, 0.5) ) {
			//yes
			{ // I hate crossing up vars in copied code
				double leftDirection = branch.horizontalDirection()-Math.PI/3.0 + jigger(rand,.15);
				double leftVerticalAngle = branch.verticalAngle + jigger(rand,0.1);
				branch = new RTG3DBranch(
						leftDirection,
						leftVerticalAngle, 
						branch.remainingLength()+(float)jigger(rand,2), branch.location()) ;
				this.buildSideBranch(world, rand, branch, lightTracker);
			}
			{
				double rightDirection = branch.horizontalDirection()+Math.PI/3.0 + jigger(rand,.15);
				double rightVerticalAngle = branch.verticalAngle + jigger(rand,0.1);
				branch = new RTG3DBranch(
						rightDirection,
						rightVerticalAngle, 
						branch.remainingLength()+(float)jigger(rand,2), branch.location()) ;
				this.buildSideBranch(world, rand, branch, lightTracker);
			}
		} else {
			//no
			while (branch.remainingLength() > 2){
				this.placeBranchBlock(world, branch.movedOrthogonally(), lightTracker);
			}
			while (leafBranch.remainingLength()>branch.remainingLength()) {
				this.placeLeavesBlock(world, leafBranch.movedOrthogonally(), lightTracker);
			}
			this.branchLeaves(world, rand, branch, lightTracker);
		}
    	
    }
    
    protected void splitUpwards(World world, Random rand, BlockPos branchBase, float branchLength, double initialDirection, SkylightTracker lightTracker) {

	    int branches = 2 + (1+rand.nextInt(2))/2;// usually 3 with occasionally 2
		double verticalAngle = 1.2 + jigger(rand,0.15);
		RTG3DBranch branch = new RTG3DBranch(
				initialDirection + jigger(rand,.2),
				verticalAngle, 
				branchLength, branchBase) ;
		buildUpBranch(world,rand,branch, lightTracker);
		initialDirection += Math.PI*2.0/(double)branches;
		for (int j = 0; j < branches; j++) {
			double horizontalTilt = 0.1 + jigger(rand,0.25);
			branch = new RTG3DBranch(
					initialDirection + jigger(rand,.2),
					horizontalTilt, 
					branchLength*1.5f+1, branchBase) ;
			buildSideBranch(world,rand,branch, lightTracker);
			initialDirection += Math.PI*2.0/(double)branches;
		}
    }
    
    protected void leafTop(World world, Random rand, BlockPos top, SkylightTracker lightTracker) {
    	int x = top.getX();
    	int y = top.getY();
    	int z = top.getZ();
    	for (int dx = -3; dx <4 ; dx ++){
    		for (int dz = -3; dz <4 ; dz ++) {
    			if (Math.abs(dx) + Math.abs(dz)>= 5) continue;
    			this.placeLeavesBlock(world, new BlockPos(x+dx,y,z+dz), lightTracker);
    		}
    	}
    	y++;
    	for (int dx = -1; dx <2 ; dx ++){
    		for (int dz = -1; dz <2 ; dz ++) {
    			this.placeLeavesBlock(world, new BlockPos(x+dx,y,z+dz), lightTracker);
    		}
    	}
    	
    }    
    
    protected void branchLeaves(World world, Random rand, RTG3DBranch branch, SkylightTracker lightTracker) {
    	{
			double leftDirection = branch.horizontalDirection()-Math.PI/3.0 + jigger(rand,.15);
			double leftVerticalAngle = branch.verticalAngle + jigger(rand,0.1);
			RTG3DBranch leafBranch = new RTG3DBranch(
					leftDirection,
					leftVerticalAngle, 
					branch.remainingLength()+(float)jigger(rand,.2), branch.location()) ;
			this.leafTop(world, rand, branch.location(), lightTracker);
			//leafFlourish(world, rand, leafBranch, lightTracker);
		}
		{
			double rightDirection = branch.horizontalDirection()+Math.PI/3.0 + jigger(rand,.15);
			double rightVerticalAngle = branch.verticalAngle + jigger(rand,0.1);
			RTG3DBranch leafBranch = new RTG3DBranch(
					rightDirection,
					rightVerticalAngle, 
					branch.remainingLength()+(float)jigger(rand,.2), branch.location()) ;
			this.leafTop(world, rand, branch.location(), lightTracker);
			//this.leafFlourish(world, rand, leafBranch, lightTracker);
		}
    }
    
    protected void leafFlourish(World world, Random rand, RTG3DBranch branch, SkylightTracker lightTracker) {
    	while (branch.remainingLength()>1) {
    		if (!placeLeavesBlock(world, branch.movedOrthogonally(), lightTracker)) {}//return;// quit if blocked
    		placeLeavesBlock(world, branch.location().up(), lightTracker);
    		placeLeavesBlock(world, branch.location().east(), lightTracker);
    		placeLeavesBlock(world, branch.location().north(), lightTracker);
    		placeLeavesBlock(world, branch.location().west(), lightTracker);
    		placeLeavesBlock(world, branch.location().south(), lightTracker);
    		// not down, trying for flattish bottom.
    	}
    	while (branch.notDone()) {
    		if (!this.placeLeavesBlock(world, branch.movedOrthogonally(), lightTracker)) {}//return;// quit if blocked
    	}
    }
}