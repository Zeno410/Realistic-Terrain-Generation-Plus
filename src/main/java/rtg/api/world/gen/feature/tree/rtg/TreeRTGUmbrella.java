package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// this class creates the standard vanilla 'umbrella' tree, but using the light tracekr
public class TreeRTGUmbrella extends TreeRTG {

	public TreeRTGUmbrella() {
		
	}
	public float estimatedSize() {
		
		float branchLength= 3;
		return branchLength*branchLength/10f;
	}
	
	@Override
	public int furthestLikelyExtension() {
		return 5;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {

		pos = this.dropToGround(world, pos);
	
		if (!this.isGroundValid(world, pos)) {
	       return false;
	    }
		
	    SkylightTracker lightTracker = new SkylightTracker(this.furthestLikelyExtension(),pos,world);
		
		if (!this.placeTrunkBlock(world, pos, lightTracker)) return false;
		
		for (int i = 1; i < trunkSize +  crownSize; i ++) {
			pos = pos.up();
			this.placeTrunkBlock(world, pos, lightTracker);
		}
	
		this.placeLeavesBlock(world, pos.up(), lightTracker);
		placeOrthogonal(world,pos.up(),1,lightTracker);
		for (int dx = -1; dx <2; dx ++ )
			for (int dz =- 1; dz <2; dz ++) {
				this.placeLeavesBlock(world, new BlockPos(pos.getX()+dz, pos.getY(), pos.getZ()+dz), lightTracker);
			}
		pos = pos.down();
		for (int dx = -2; dx <3; dx ++ )
			for (int dz =- 2; dz <3; dz ++) {
				if (dx*dx+dz*dz==8&&rand.nextInt(2)==0) continue;
				this.placeLeavesBlock(world, new BlockPos(pos.getX()+dz, pos.getY(), pos.getZ()+dz), lightTracker);
			}
		pos = pos.down();
		for (int dx = -2; dx <3; dx ++ )
			for (int dz =- 2; dz <3; dz ++) {
				if (dx*dx+dz*dz==8&&rand.nextInt(2)==0) continue;
				this.placeLeavesBlock(world, new BlockPos(pos.getX()+dz, pos.getY(), pos.getZ()+dz), lightTracker);
			}
		return true;
	}
}
