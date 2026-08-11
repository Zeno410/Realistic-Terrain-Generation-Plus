package rtg.api.util;

import java.util.Random;

import net.minecraft.util.math.Vec3d;

public class NormalBasis {
	
	Vec3d main;
	Vec3d secondary;
	
	public NormalBasis(Vec3d _main,Vec3d _secondary) {
		main = _main;
		secondary = _secondary;
		normalize();
	}

	private void normalize() {
		main = main.normalize();
		double dotProduct = main.dotProduct(secondary);
		Vec3d projection = main.scale(dotProduct);
		secondary = secondary.subtract(projection);
		secondary = secondary.normalize();
	}
	
	public static NormalBasis orthogonalTo(Vec3d initial, Random rand) {
		initial = initial.normalize();
		Vec3d first = new Vec3d(1,0,0);
		int i = 0;
		do {
			if (i++>100) throw new RuntimeException(initial.toString() + " " + first.toString());
			first = randomUnitVector(rand);
		} while (Math.abs (first.dotProduct(initial))<.001);
		first = perpendicular(initial,first);
		Vec3d second = new Vec3d(1,0,0);
		do {
			if (i++>100) throw new RuntimeException(second.toString() + " " + first.toString());
			second = randomUnitVector(rand);
			second = perpendicular(initial,second);
		} while (Math.abs(second.dotProduct(first))<.001);
		return new NormalBasis(first,second);
	}
	
	public static Vec3d randomUnitVector(Random rand) {
		Vec3d result = new Vec3d(0.5-rand.nextDouble(),0.5-rand.nextDouble(),0.5-rand.nextDouble());
		return result.normalize();
	}
	
	public static Vec3d perpendicular(Vec3d primary, Vec3d secondary) {
		primary = primary.normalize();
		double dotProduct = primary.dotProduct(secondary);
		Vec3d projection = primary.scale(dotProduct);
		secondary = secondary.subtract(projection);
		if (primary.dotProduct(secondary)>.001) throw new RuntimeException(); 
		return secondary.normalize();
	}
	
	public Vec3d atAngle(double angle) {
		return main.scale(Math.cos(angle)).add(secondary.scale(Math.sin(angle)));
	}
}
