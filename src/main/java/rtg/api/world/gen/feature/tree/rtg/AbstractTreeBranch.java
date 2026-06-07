package rtg.api.world.gen.feature.tree.rtg;

import net.minecraft.util.math.BlockPos;

public interface AbstractTreeBranch {
	
	BlockPos moved();
	BlockPos movedOrthogonally();
	BlockPos location();
	boolean notDone();
	double horizontalDirection();
	float ascent();
	float horizontalExtension();
	int stage();
}