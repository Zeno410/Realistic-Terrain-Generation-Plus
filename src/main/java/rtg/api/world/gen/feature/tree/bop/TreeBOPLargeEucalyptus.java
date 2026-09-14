package rtg.api.world.gen.feature.tree.bop;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.world.gen.feature.tree.rtg.RTG3DBranch;
import rtg.api.world.gen.feature.tree.rtg.SkylightTracker;

public class TreeBOPLargeEucalyptus extends TreeBOPBaseEucalyptus {

	public TreeBOPLargeEucalyptus() {
		super();
        this.trunkSize = 12;
        this.crownSize = 15;
        this.lowestVariableTrunkProportion += .25;
        this.trunkProportionVariability = .15f;
	}
	
	public float estimatedSize() {

    	float branchLength= (crownSize/3.0f) + 1f;
    	return branchLength*branchLength/10f;
	}
	
	@Override
    public int furthestLikelyExtension() {
    	float branchLength= (crownSize/3.0f) + 1f;
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
        for (i = 0; i < trunkSize/2; i++) {
            if (!this.placeTrunkBlock(world, new BlockPos(x, y+i, z), this.generateFlag, lightTracker)) return false;
            // abort if lighting issues
            this.placeTrunkBlock(world, new BlockPos(x, y+i, z-1), this.generateFlag, lightTracker);
            this.placeTrunkBlock(world, new BlockPos(x+1, y+i, z), this.generateFlag, lightTracker);
            this.placeTrunkBlock(world, new BlockPos(x, y+i, z+1), this.generateFlag, lightTracker);
            this.placeTrunkBlock(world, new BlockPos(x-1, y+i, z), this.generateFlag, lightTracker);
            // no aborts for failed trunk extensions;
        }

        // rest of the trunk is 2 wide
	    BlockPos logBase = new BlockPos(x,y,z);
		double stepDirection = rand.nextDouble()*2.0*Math.PI;
		RTG3DBranch logOut = new RTG3DBranch(
				stepDirection,
				0, 
				this.crownSize, logBase) ;
		BlockPos outBase = logOut.movedOrthogonally();
		
        for (i = trunkSize/2; i < trunkSize; i++) {
            this.placeTrunkBlock(world, new BlockPos(x, y+i, z), this.generateFlag, lightTracker); 
            this.placeTrunkBlock(world, new BlockPos(outBase.getX(), y+i, outBase.getZ()), this.generateFlag, lightTracker);
            // no aborts for failed trunk extensions;
        }
		
	    y += trunkSize -1 ;
	    
	    BlockPos crownBase = new BlockPos(x,y,z);
	    int branches = 2 + rand.nextInt(3)/2;// usually 2 with occasionally 3

    	double initialDirection = rand.nextDouble()*2.0*Math.PI;
    	for (int j = 0; j < branches; j++) {
    		double verticalAngle = 1.1 + jigger(rand,0.15);
    		double branchDirection = initialDirection + jigger(rand,.2);
    		// this was built on a cross shaped trunk, so each branch gets stepped out one block
    		// currently paused
    		RTG3DBranch stepOut = new RTG3DBranch(
    				branchDirection,
    				0, 
    				this.crownSize, crownBase) ;
    		
    		BlockPos branchBase = stepOut.movedOrthogonally();
    		// if not in the direction of the trunk extension use the crownbase;
    		if (branchBase.getX()!=outBase.getX()||branchBase.getZ()!=outBase.getZ()) branchBase = crownBase;
    		
    		RTG3DBranch branch = new RTG3DBranch(
    				branchDirection,
    				verticalAngle, 
    				this.crownSize, branchBase);//stepOut.movedOrthogonally()) ;
    		
    		buildBranch(world,rand,branch,false, lightTracker);
    		initialDirection += Math.PI*2.0/(double)branches;
    	}
	    return true;
	}

}