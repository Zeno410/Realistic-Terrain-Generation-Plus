package rtg.api.world.gen.feature.tree.rtg;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import rtg.api.util.FractionalBlockPos;

public class BetterTreeBranch implements AbstractTreeBranch {
	static int reports = 0;
	final BranchVector direction;
	private final float initialLength;
	private float remainingLength;
	private final double initialHorizontal;
	private final float initialVertical;
	private final int stage;
	final FractionalBlockPos branchLocation;
	
	public BetterTreeBranch (double horizontal, float vertical, float length, int stage, BlockPos branchStart) {
		initialLength = length;
		remainingLength = length;
		initialHorizontal = horizontal;
		initialVertical = vertical;
		this.stage = stage;
		direction = new BranchVector(horizontal,vertical/length);
		branchLocation = new FractionalBlockPos(branchStart);
	}
	
	public BetterTreeBranch (Vec3d vector, int stage, FractionalBlockPos _branchLocation) {
		initialLength = (float)Math.sqrt(vector.x*vector.x+vector.z*vector.z) ;
		remainingLength = initialLength;
		Vec3d standardized = vector.scale(1.0/initialLength);
		if (Math.abs(vector.x)<=  0.0001) {
			// to avoid rounding; horizontal will be pi/2 if x is positive
			 double horizontal = Math.PI/2.0;
			 // if negative make it 3*pi/2
			 if (vector.z < 0) horizontal  += Math.PI;
			 initialHorizontal = horizontal;
		} else {
		     initialHorizontal = Math.atan(vector.z/vector.x);
		}
		initialVertical = (float)vector.y;
		this.stage = stage;
		direction = new BranchVector(standardized);
		branchLocation = _branchLocation;
	}
	
	public BetterTreeBranch (Vec3d vector, int stage, BlockPos branchStart) {
		this(vector,stage,new FractionalBlockPos(branchStart));
	}
	
	public BetterTreeBranch(AbstractTreeBranch copied) {
		initialLength = copied.horizontalExtension();
		remainingLength = initialLength;
		initialHorizontal = copied.horizontalDirection();
		initialVertical= copied.ascent();
		stage = copied.stage();
		direction = new BranchVector(initialHorizontal,initialVertical/initialLength);
		branchLocation = new FractionalBlockPos(copied.location());
	}
	
	public BlockPos moved() {
		direction.move(branchLocation);
		remainingLength -= direction.horizontalLength;
		return branchLocation.location();
	}
	
	public BlockPos movedOrthogonally() {
		//BlockPos oldPos = branchLocation.location();
		/*double oldX = branchLocation.x();
		double oldY = branchLocation.y();
		double oldZ = branchLocation.z();*/
		double multiplier = multiplierToNextBlock() + .0001; // fudge factor for rounding errors
		direction.moveFractionally(branchLocation,multiplier);
		remainingLength -= Math.abs(multiplier)*direction.horizontalLength;
		//if (testing) BetterForestsMod.LOGGER.info("multiplier {} remaining {} location {} {}",multiplier,remainingLength,location(),branchLocation.toString());
		
		//BlockPos newPos = branchLocation.location();
		// debugging code, currently off but not dead issues
		/*int differences = 0;
		if (oldPos.getX() != newPos.getX()) differences ++;
		if (oldPos.getY() != newPos.getY()) differences ++;
		if (oldPos.getZ() != newPos.getZ()) differences ++;
		if (differences != 1&&differences == 1) {

			BetterForestsMod.LOGGER.info("old {},{},{}",oldX, oldY,oldZ);
			BetterForestsMod.LOGGER.info("pos {},{},{}",oldPos.getX(), oldPos.getY(),oldPos.getZ());

			BetterForestsMod.LOGGER.info("vector {},{},{}",direction.dx, direction.dy,direction.dz);
			BetterForestsMod.LOGGER.info("new {},{},{}",branchLocation.x(), branchLocation.y(),branchLocation.z());
			BetterForestsMod.LOGGER.info("pos {},{},{}",newPos.getX(), newPos.getY(),newPos.getZ());
			BetterForestsMod.LOGGER.info("multiplier: {}", multiplier);
			
		}*/
		return branchLocation.location();
	}
	
	private double multiplierToNextBlock() {
		double xMult = multiplierToNextInteger(branchLocation.x(), direction.dx);
		double yMult = multiplierToNextInteger(branchLocation.y(), direction.dy);
		double zMult = multiplierToNextInteger(branchLocation.z(), direction.dz);
	   
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
	
	public boolean notDone() {return remainingLength > 0;}
	
	public double horizontalDirection() {return initialHorizontal;}
	
	public float ascent() {return initialVertical;}
	
	public float horizontalExtension() {return initialLength;}
	
	public int stage() {return stage;}

}
