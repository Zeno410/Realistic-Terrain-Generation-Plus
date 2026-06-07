package rtg.api.world.gen.feature.tree.rtg;


import java.util.Random;
import java.util.function.Function;

import net.minecraft.util.math.Vec3d;


// A shell for various bend techniques
// the basic idea is that it take an input vector and outputs a midpoint displacement
public abstract class Bend implements Function<Vec3d,Vec3d>{

	public Bend() {
		// TODO Auto-generated constructor stub
	}

	public static Bend fromMidline ( final double minFromMidline, final double maxFromMidline, final double maxAlongCenterLine, Random random) {
		// the goal is a vector with projection on to the target up to maxAlongCenterLine proportion of base vector length
		// and perpendicular projection between min and max
		return new Bend() {

			@Override
			public Vec3d apply(Vec3d base) {
				Vec3d projection = null;
				Vec3d perpendicular = null;
				double lengths = 0;
				while (lengths <.001) {
					// algorithm: generate random vector
					// obtain projection and perpendicular
					// repeat until both are reasonably long to avoid rounding errors
					Vec3d randomSpherical = new Vec3d(random.nextDouble()*2.0 - 1.0,random.nextDouble()*2.0 - 1.0,random.nextDouble()*2.0 - 1.0);
					double projectionLength = randomSpherical.dotProduct(base);
					projection = base.scale(projectionLength/base.length());
					perpendicular = randomSpherical.subtract(projection);
					lengths = Math.max(projection.length(),perpendicular.length());
				}
				double projectionLength = random.nextDouble()*maxAlongCenterLine;
				projection = projection.scale(projectionLength/projection.length());
				double perpendicularLength = random.nextDouble()*(maxFromMidline - minFromMidline)+ minFromMidline;
				perpendicular = perpendicular.scale(perpendicularLength/perpendicular.length());
				return projection.add(perpendicular);
				
			}
			
		};
	}
	
	public static Bend upwards(double lengthProportion, Random random) {
		return new Bend() {
			@Override
			public Vec3d apply(Vec3d base) {
				double maxShift = base.length()*lengthProportion;
				double dx = nextSpherical(random)*lengthProportion;
				double dy = random.nextDouble()*lengthProportion;// must be positive;
				double dz = nextSpherical(random)*lengthProportion;
				return new Vec3d(dx,dy,dz);
			}
		};
	}
	
	private static final double nextSpherical(Random random) {
		// random either positive or negative
		return random.nextDouble()*2.0-1.0;
	}
}

