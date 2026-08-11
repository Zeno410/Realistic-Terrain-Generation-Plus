package rtg.api.world.gen.feature.tree.rtg;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import rtg.api.util.FractionalBlockPos;
import rtg.api.util.Logger;
import rtg.api.util.Valued;

public class RTG3DBranch implements RTGBranch {
		static int reports = 0;
		final BranchVector direction;
		public final float initialLength;
		private float remainingLength;
		public final double horizontalAngle;
		public final double verticalAngle;
		public final int stage;// under consideration for deprecation
		final FractionalBlockPos branchLocation;
		private final Vec3d originalLocation;
		//private ArrayList<Valued<Vec3d>> stageLocations = new ArrayList<>();
		
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
			originalLocation = branchLocation.vector();
		}

		public RTG3DBranch (double horizontal, double vertical, float length, BlockPos branchStart) {
			this(horizontal,vertical,length,0,branchStart);
		}
		
		public RTG3DBranch(RTG3DBranch copied) {
			this.direction = new BranchVector(new Vec3d(copied.direction.dx,copied.direction.dy,copied.direction.dz));
			initialLength = copied.initialLength;
			remainingLength = copied.remainingLength;
			horizontalAngle = copied.horizontalAngle;
			verticalAngle = copied.verticalAngle;
			stage = copied.stage;
			branchLocation = new FractionalBlockPos(copied.branchLocation);
			//this(copied.horizontalAngle,copied.verticalAngle,copied.remainingLength,copied.stage,copied.location());
			originalLocation = copied.originalLocation;
		}
		
		public RTG3DBranch (Vec3d vector, float length, BlockPos branchStart) {
			vector = vector.normalize();
			initialLength = length;
			remainingLength = length;
			if (vector.x <.001) {
				  if (vector.z> 0) {horizontalAngle = Math.PI/2.0;} else {horizontalAngle = -Math.PI/2.0;}
				}
			
			else {horizontalAngle = Math.atan(vector.z/vector.x);}
			double dHorizontal = Math.sqrt(vector.x*vector.x + vector.z*vector.z);
			
			if (dHorizontal <.001) {
				if (vector.y> 0) {verticalAngle = Math.PI/2.0;} else {verticalAngle = -Math.PI/2.0;}
				}
			else {verticalAngle = Math.atan(vector.y/dHorizontal);}
			
			this.stage = 0;
			direction = new BranchVector(vector);
			branchLocation = new FractionalBlockPos(branchStart);
			originalLocation = branchLocation.vector();
		}
		
		public BlockPos moved() {
			direction.move(branchLocation);
			remainingLength -= 1.0; // the d's reach the unit sphere
			return branchLocation.location();
		}
		
		public BlockPos movedOrthogonally() {
			BlockPos oldPos = branchLocation.location();
			//double oldX = branchLocation.x;
			//double oldY = branchLocation.y;
			//double oldZ = branchLocation.z;
			double multiplier = multiplierToNextBlock() + .0001; // fudge factor for rounding errors
			direction.moveFractionally(branchLocation,multiplierToNextBlock());
			remainingLength -= direction.length* multiplier;
			BlockPos newPos = branchLocation.location();
			int differences = 0;
			if (oldPos.getX() != newPos.getX()) differences ++;
			if (oldPos.getY() != newPos.getY()) differences ++;
			if (oldPos.getZ() != newPos.getZ()) differences ++;
			/*if (differences != 1) {// debugging code, currently off

				Logger.info("old {},{},{}",oldX, oldY,oldZ);
				Logger.info("pos {},{},{}",oldPos.getX(), oldPos.getY(),oldPos.getZ());

				Logger.info("vector {},{},{}",direction.dx, direction.dy,direction.dz);
				Logger.info("new {},{},{}",branchLocation.x, branchLocation.y,branchLocation.z);
				Logger.info("pos {},{},{}",newPos.getX(), newPos.getY(),newPos.getZ());
				Logger.info("multiplier: {}", multiplier);
				
			}
			*/
			/*this.stageLocations.add(new Valued<>(branchLocation.vector(),this.remainingLength));
			if (!notDone()&&branchLocation.vector().distanceTo(originalLocation)>(this.initialLength+0.5)) {
				Logger.info("");
				Logger.info("initial "+ this.originalLocation.toString() + " " + initialLength);
				for (Valued<Vec3d> stageInfo: stageLocations) {
					double distance = stageInfo.item.distanceTo(originalLocation);
					Logger.info(" {} effectiveLength {}",stageInfo.item.toString(),(distance + stageInfo.value));
				}
			}*/
			return branchLocation.location();
		}
		
		private double multiplierToNextBlock() {
			double xMult = multiplierToNextInteger(branchLocation.x, direction.dx);
			double yMult = multiplierToNextInteger(branchLocation.y, direction.dy);
			double zMult = multiplierToNextInteger(branchLocation.z, direction.dz);
		   
			double result = Math.min(Math.min(yMult, zMult),xMult);
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
		
		public Vec3d direction() {
			return new Vec3d(direction.dx,direction.dy,direction.dz);
		}

	}
