package rtg.api.world.gen.feature.tree.rtg;

import net.minecraft.util.math.BlockPos;

public interface RTGBranch {
	public BlockPos moved();
	
	public BlockPos movedOrthogonally();
	
	public boolean notDone();

}
