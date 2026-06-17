package rtg.api.world.gen.feature.tree.rtg;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import rtg.api.util.FractionalBlockPos;
import rtg.api.util.Logger;

public class RTG3DBranch {
		static int reports = 0;
		final BranchVector direction;
		public final float initialLength;
		private float remainingLength;
		public final double horizontalAngle;
		public final double verticalAngle;
		public final int stage;// under consideration for deprecation
		final FractionalBlockPos branchLocation;
		
		public RTG3DBranch (double horizontal, double vertical, float length, int stage, BlockPos branchStart) {
			initialLength = length;
			remainingLength = length;
			horizontalAngle = horizontal;
			verticalAngle = vertical;
			this.stage = stage;
			double dy = Math.sin(verticalAngle);
			double horizontalMove = Math.cos(verticalAngle);
			
			double dx = horizontalMove*Math.cos(horizontalAngle);
			double dz = horizontalMove*Math.sin(horizontalAngle);
			direction = new BranchVector(new Vec3d(dx,dy,dz));
			branchLocation = new FractionalBlockPos(branchStart);
		}

		public RTG3DBranch (double horizontal, double vertical, float length, BlockPos branchStart) {
			this(horizontal,vertical,length,0,branchStart);
		}
		
		public BlockPos moved() {
			direction.move(branchLocation);
			remainingLength -= 1.0; // the d's reach the unit sphere
			return branchLocation.location();
		}
		
		public BlockPos movedOrthogonally() {
			BlockPos oldPos = branchLocation.location();
			double oldX = branchLocation.x;
			double oldY = branchLocation.y;
			double oldZ = branchLocation.z;
			double multiplier = multiplierToNextBlock() + .0001; // fudge factor for rounding errors
			direction.moveFractionally(branchLocation,multiplierToNextBlock());
			remainingLength -= multiplier;
			BlockPos newPos = branchLocation.location();
			int differences = 0;
			if (oldPos.getX() != newPos.getX()) differences ++;
			if (oldPos.getY() != newPos.getY()) differences ++;
			if (oldPos.getZ() != newPos.getZ()) differences ++;
			if (differences != 1&&differences == 1) {// debugging code, currently off but not dead issues

				Logger.info("old {},{},{}",oldX, oldY,oldZ);
				Logger.info("pos {},{},{}",oldPos.getX(), oldPos.getY(),oldPos.getZ());

				Logger.info("vector {},{},{}",direction.dx, direction.dy,direction.dz);
				Logger.info("new {},{},{}",branchLocation.x, branchLocation.y,branchLocation.z);
				Logger.info("pos {},{},{}",newPos.getX(), newPos.getY(),newPos.getZ());
				Logger.info("multiplier: {}", multiplier);
				
			}
			return branchLocation.location();
		}
		
		private double multiplierToNextBlock() {
			double xMult = multiplierToNextInteger(branchLocation.x, direction.dx);
			double yMult = multiplierToNextInteger(branchLocation.y, direction.dy);
			double zMult = multiplierToNextInteger(branchLocation.z, direction.dz);
		   
			double result = Math.min(Math.min(yMult, zMult),xMult);
			if (result <0) throw new RuntimeException();
			return result;
		}
		
		private double multiplierToNextInteger(double number, double change) {
			double toGo;
			if (Math.abs(change) <.00001) return 100000;// something has to be big and this isn't it.
			if (change < 0) {
				toGo = Math.floor(number) - (number) - .000001; //make both toGo and change negative		}
				if (toGo > -.00001) toGo = -1.0;
			} else {
				toGo = Math.ceil(number) - (number) + .000001;
				if (toGo < .00001) toGo = 1.0;
			}
			return toGo/change;
			
		}
		
		public final BlockPos location() {return branchLocation.location();}
		
		public float remainingLength() { return remainingLength;}
		
		public boolean notDone() {return remainingLength > 0;}
		
		public double horizontalDirection() {return horizontalAngle;}

	}
