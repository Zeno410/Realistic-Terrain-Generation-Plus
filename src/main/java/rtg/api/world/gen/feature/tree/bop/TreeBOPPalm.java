package rtg.api.world.gen.feature.tree.bop;

import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.world.gen.feature.tree.rtg.SkylightTracker;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;

public class TreeBOPPalm extends TreeRTG {

	public TreeBOPPalm() {
		this.trunkSize = 8;
		this.crownSize = 7;
		this.setMaterials(BOPTreeMaterials.palm);
		this.leavesBlock = leavesBlock.withProperty(BlockLeaves.DECAYABLE, false);
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
		
		placeOrthogonal(world,pos,1,lightTracker);
		placeDiagonal(world,pos,2,lightTracker);
		placeOrthogonal(world,pos.down(),2,lightTracker);
		pos = pos.up();
		this.placeLeavesBlock(world, pos, lightTracker);
		placeDiagonal(world,pos,1,lightTracker);
		placeOrthogonal(world,pos.up(),2,lightTracker);
		return true;
	}
}
