package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.util.BlockUtil;
import rtg.api.util.Direction;

public abstract class TreeRTGAbstractDarkOak extends TreeRTG {

	public TreeRTGAbstractDarkOak() {

        this.setLogBlock(BlockUtil.getStateLog(EnumType.DARK_OAK));
        this.setLeavesBlock(BlockUtil.getStateLeaf(EnumType.DARK_OAK));
        this.setBranchBlock(Blocks.LOG2.getStateFromMeta(13));
        this.maxAllowedObstruction = 6;
	}

	@Override
	abstract public boolean generate(World worldIn, Random rand, BlockPos position) ;

    class BaseSetter {
    	BlockPos start;
	   	int branchCount;
	   	int currentBranch = -1;
	   	float currentRadians;
	   	BlockPos.MutableBlockPos base;
	   	
	   	BaseSetter(int _branchCount, BlockPos _start) {
	   		branchCount = _branchCount;
	   		start = _start;
	   		base = new BlockPos.MutableBlockPos(start.getX(),start.getY()+trunkSize-1,start.getZ());
	   	}
	   	
	   	void set(float radians) {
   			currentBranch += 1;
   			currentRadians = radians;
   			Direction direction = Direction.nearestDiagonal(radians);
   			if (direction.xOffset>0) {
   				base.add(1, 0, 0);
   			} else  {
   				// no change to base
   			}
   			if (direction.zOffset>0) {
   				base.add(0, 0, 1);
   			} else  {
   				// no change to base
   			}
     	}
   }
}
