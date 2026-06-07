package rtg.api.world.gen.feature.tree.rtg;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BentBranch implements AbstractTreeBranch {
		
		private BetterTreeBranch first;
		private BetterTreeBranch second;
	    private final Vec3d target;// stored to generate second branch;
		private boolean firstDone = false;
		private final int storedStage;
		private final double totalHorizontal;
		private final float totalAscent;
		private final float totalLength;

		public BentBranch(double horizontal, float vertical, float length, int stage, BlockPos branchStart, Bend bend) {
			BranchVector direction = new BranchVector(horizontal,vertical/length);
			double dx = Math.cos(horizontal)*length;
			double dz = Math.sin(horizontal)*length;
			Vec3d branchMovement = new Vec3d(dx,vertical,dz);
			Vec3d start = new Vec3d(branchStart);
			target = start.add(branchMovement);
			
			Vec3d midpointDisplacement = bend.apply(branchMovement);
			Vec3d firstVector = branchMovement.scale(1.0/2.0).add(midpointDisplacement);
			first = new BetterTreeBranch(firstVector, stage,branchStart);
			storedStage = stage;
			totalHorizontal = horizontal;
			totalAscent = vertical;
			totalLength = length;
			//BetterForestsMod.LOGGER.info("horizontal {} vertical {} length {} first {} second {}", horizontal,vertical,length,firstVector.toString(),secondVector.toString());
		}

		@Override
		public BlockPos moved() {
			if (firstDone) return second.moved();
			BlockPos result = first.moved();
			if (!first.notDone()) {changeToSecond();}
			return result;
		}

		@Override
		public BlockPos movedOrthogonally() {
			if (firstDone) {
				BlockPos result = second.movedOrthogonally();
				//BetterForestsMod.LOGGER.info("on second " + result.toString());
				return result;
			}
			BlockPos result = first.movedOrthogonally();
			//BetterForestsMod.LOGGER.info(result.toString());
			if (!first.notDone()) {changeToSecond();}
			return result;
		}

		@Override
		public BlockPos location() {
			if (firstDone) return second.location();
			return first.location();
		}

		@Override
		public boolean notDone() {
			if (firstDone) return second.notDone();
			return true;
		}

		@Override
		public double horizontalDirection() {
			return totalHorizontal;
		}

		@Override
		public float ascent() {
			return totalAscent;
		}

		@Override
		public float horizontalExtension() {
			return totalLength;
		}

		@Override
		public int stage() {
			return storedStage;
		}
		
		private void changeToSecond() {
			firstDone = true;
			
			second = new BetterTreeBranch(target.subtract(first.branchLocation.vector()),storedStage,first.branchLocation);
		}

}
