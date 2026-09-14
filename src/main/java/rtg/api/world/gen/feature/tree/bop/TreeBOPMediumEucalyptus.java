package rtg.api.world.gen.feature.tree.bop;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.world.gen.feature.tree.rtg.RTG3DBranch;
import rtg.api.world.gen.feature.tree.rtg.SkylightTracker;

public class TreeBOPMediumEucalyptus extends TreeBOPBaseEucalyptus {

	public TreeBOPMediumEucalyptus() {
		super();
        this.trunkSize = 8;
        this.crownSize = 10;
        this.lowestVariableTrunkProportion += .1;
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
	    for (i = 0; i < this.trunkSize; i++) {
	        this.placeTrunkBlock(world, new BlockPos(x, y, z), this.generateFlag, lightTracker);
	        y++;
	    }
	    y--;// fix fencepost error
	    BlockPos branchBase = new BlockPos(x,y,z);
	    int branches = 2 + rand.nextInt(3)/2;// usually 2 with occasionally 3

    	double initialDirection = rand.nextDouble()*2.0*Math.PI;
    	for (int j = 0; j < branches; j++) {
    		double verticalAngle = 1.1 + jigger(rand,0.15);
    		RTG3DBranch branch = new RTG3DBranch(
    				initialDirection + jigger(rand,.2),
    				verticalAngle, 
    				this.crownSize, branchBase) ;
    		buildBranch(world,rand,branch,false, lightTracker);
    		initialDirection += Math.PI*2.0/(double)branches;
    	}
	    return true;
	}

}
