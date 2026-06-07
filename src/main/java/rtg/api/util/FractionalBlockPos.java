package rtg.api.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FractionalBlockPos {
	public double x;
	public double y;
	public double z;
	
	public FractionalBlockPos(BlockPos start) {
		x = (double)start.getX() + 0.5;
		y = (double)start.getY() + 0.5;
		z = (double)start.getZ() + 0.5;
	}
	
	public double x() {return x;}
	public double y() {return y;}
	public double z() {return z;}
	
	public FractionalBlockPos(FractionalBlockPos copied) {
		this.x = copied.x;
		this.y = copied.y;
		this.z = copied.z;
	}
	
	public BlockPos location() {
		return new BlockPos((int)Math.floor(x),(int)y,(int)Math.floor(z));
	}
	
	public void moveX(double moved) {x += moved;}
	public void moveY(double moved) {y += moved;}
	public void moveZ(double moved) {z += moved;}

	public Vec3d vector() {return new Vec3d(x,y,z);}
	
	public String toString() {return "{"+x+ ","+y+","+z+"}";}
}
