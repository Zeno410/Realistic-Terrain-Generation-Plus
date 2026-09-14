package rtg.api.world.gen.feature.tree.bop;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rtg.api.util.NormalBasis;
import rtg.api.world.gen.feature.tree.rtg.RTG3DBranch;
import rtg.api.world.gen.feature.tree.rtg.SkylightTracker;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;

public class TreeBOPJacaranda  extends TreeRTG  {
	
	private double forwardMultiplier = Math.sqrt(1-.5);
	private double perpendicularMultiplier = Math.sqrt(1 - forwardMultiplier*forwardMultiplier);
	private double outishForwardMultiplier = Math.sqrt(1-.05);
	private double outishPerpendicularMultiplier = Math.sqrt(1 - outishForwardMultiplier*outishForwardMultiplier);
	
	private int level;

	public TreeBOPJacaranda() {
    super();
    this.trunkSize = 6;
    this.crownSize = 8;
    this.branchBlock = BOPTreeMaterials.jacaranda.branches;
    this.logBlock = BOPTreeMaterials.jacaranda.log;
    this.leavesBlock = BOPTreeMaterials.jacaranda.leaves;
	}
	
	public float estimatedSize() {
	
		float branchLength= (crownSize);
		return branchLength*branchLength/10f;
	}
	
	@Override
	public int furthestLikelyExtension() {
		float branchLength= (crownSize*1.2f);
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
	
	    SkylightTracker tracker = new SkylightTracker(this.furthestLikelyExtension(),pos,world);
	    
	    int i;
	    for (i = 0; i < this.trunkSize; i++) {
	        this.placeTrunkBlock(world, new BlockPos(x, y, z), this.generateFlag, tracker);
	        y++;
	    }
	    y--;// fix fencepost error
	    BlockPos branchBase = new BlockPos(x,y,z);
	
	    level = 0;
	    Vec3d up = new Vec3d(0,1,0);
	    build(world,rand,up,branchBase,branchBase,this.crownSize -1 ,tracker);// the flourish system increases the expected size

	    return true;
	    
	}
	
	protected void build (World world, Random rand, Vec3d direction, BlockPos start, BlockPos crownBase, float length, SkylightTracker tracker) {
		if (level++>9) {
			level--;
			return;
		}
		Vec3d fromCrownBase = new Vec3d(start.getX()-crownBase.getX(),start.getY()-crownBase.getY(),start.getZ()-crownBase.getZ());
		if (fromCrownBase.lengthSquared() >.01) fromCrownBase = fromCrownBase.normalize();
		if (length <= 3) {
			this.leafFlourish(world, rand, direction, start, length, tracker);
		} else {
			NormalBasis perpendicular = NormalBasis.orthogonalTo(direction, rand);
			boolean outbranched = false;
			for (double perpendicularAngle = 0; perpendicularAngle < 7; perpendicularAngle += Math.PI*2.0/3.0) {
				Vec3d perpendicularDirection = perpendicular.atAngle(perpendicularAngle);
				Vec3d branchDirection;
				if (outbranched) {
				    branchDirection = direction.scale(this.forwardMultiplier).add(perpendicularDirection.scale(this.perpendicularMultiplier));
				} else {
				    branchDirection = direction.scale(this.outishForwardMultiplier)
				    		.add(perpendicularDirection.scale(this.outishPerpendicularMultiplier));
				    outbranched = true;
				}

				if (fromCrownBase.dotProduct(branchDirection.normalize())<-0.2) continue;// no branches heading backwards;
				
				RTG3DBranch branch = new RTG3DBranch(
						branchDirection,
						length+(float)jigger(rand,.2), start) ;
				this.buildBranch(world, rand, branch, crownBase, tracker);
			}
		}
		level--;
	}
	
	protected void buildBranch(World world , Random rand, RTG3DBranch branch, BlockPos crownBase,  SkylightTracker tracker) {
		float targetLength = branch.remainingLength() - 2.3f + (float)jigger(rand,.7);
		if (level == 1) targetLength += 1;
		boolean blocked = false;
		RTG3DBranch leafBranch = new RTG3DBranch(branch);
		int i = 0;
		while (branch.remainingLength() > targetLength&!blocked) {
			if (level > 1 ) {
				if (!this.placeBranchBlock(world, branch.location(), tracker)) {blocked = true;}// stop if blocked
				branch.moved();
			} else {
				if (!this.placeBranchBlock(world, branch.location(), tracker)) {blocked = true;}// stop if blocked
				branch.movedOrthogonally();
			}
			if (!blocked&& level>1) {
				BlockPos location = branch.location();
				placeLeavesBlock(world, location.up(), tracker);
				placeLeavesBlock(world, location.east(), tracker);
				placeLeavesBlock(world, location.north(), tracker);
				placeLeavesBlock(world, location.west(), tracker); 
				placeLeavesBlock(world, location.south(), tracker);
				placeLeavesBlock(world, location.down(), tracker);
			}
		}

		/*i = 0;
		while (leafBranch.remainingLength() > branch.remainingLength()&&leafBranch.notDone()) {
			this.placeLeavesBlock(world, leafBranch.movedOrthogonally(), tracker);
			if (i++>15) throw new RuntimeException("" + leafBranch.remainingLength() + " " + branch.remainingLength() + " "+blocked);
		}*/
		if (!blocked) {
		   this.build(world, rand, branch.direction(), branch.location(), crownBase, branch.remainingLength(), tracker);
		}
	}  
	
	protected void leafFlourish(World world, Random rand, Vec3d direction, BlockPos location, float length, SkylightTracker lightTracker) {
		if (!this.placeBranchBlock(world,location, lightTracker)) {return;}// stop if blocked
		placeLeavesBlock(world, location.up(), lightTracker);
		placeLeavesBlock(world, location.east(), lightTracker);
		placeLeavesBlock(world, location.north(), lightTracker);
		placeLeavesBlock(world, location.west(), lightTracker); 
		placeLeavesBlock(world, location.south(), lightTracker);
		placeLeavesBlock(world, location.down(), lightTracker);
		

		NormalBasis perpendicular = NormalBasis.orthogonalTo(direction, rand);
		for (double perpendicularAngle = 0; perpendicularAngle < 7; perpendicularAngle += Math.PI*2.0/4.0) {
			Vec3d perpendicularDirection = perpendicular.atAngle(perpendicularAngle);
			Vec3d branchDirection = direction.scale(this.forwardMultiplier).add(perpendicularDirection.scale(this.perpendicularMultiplier));

			RTG3DBranch leafBranch = new RTG3DBranch(
					branchDirection,
					length+(float)jigger(rand,.2), location) ;

			this.leafLine(world, rand, leafBranch, 1, lightTracker);
			
		}
	}
	
	
}
