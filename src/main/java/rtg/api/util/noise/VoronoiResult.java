package rtg.api.util.noise;

import java.awt.geom.Point2D;


/**
 * @author Zeno410
 */
public class VoronoiResult {

    private double shortestDistanceSq = 32000000.0;
    private double nextDistanceSq = 32000000.0;
    private double thirdDistanceSq = 32000000.0;
    private double fourthDistanceSq = 32000000.0;
    private double closestX = 32000000.0;
    private double closestZ = 32000000.0;
    private double nextClosestX = 32000000.0;
    private double nextClosestZ = 32000000.0;
    private double thirdClosestX = 32000000.0;
    private double thirdClosestZ = 32000000.0;
    private double fourthClosestX = 32000000.0;
    private double fourthClosestZ = 32000000.0;

    public final double getShortestDistanceSq() {
        return this.shortestDistanceSq;
    }

    public final double getNextDistanceSq() {
        return this.nextDistanceSq;
    }    
    
    public final double getThirdDistanceSq() {
        return this.thirdDistanceSq;
    }
    
    public final double getFourthDistanceSq() {
        return this.thirdDistanceSq;
    }

    // returns 0 in the middle of a cell and 1 on the border;
    public final double borderValue() {
        return shortestDistanceSq / nextDistanceSq;
    }

    // returns 1 in the middle of a cell and 0 on the border;
    public final double interiorValue() {
        return (nextDistanceSq - shortestDistanceSq) / nextDistanceSq;
    }
    
    public final double borderDistance() {
    	return Math.min(Math.min(nextBorderDistance(), thirdBorderDistance()),fourthBorderDistance());
    }
    
    private final double nextBorderDistance() {
    	return nextBorderDistance (basinToNextDistanceSquare(),shortestDistanceSq,nextDistanceSq);
    }
    
    private final double thirdBorderDistance() {
    	double basinCenterDistanceSquare = (closestX - thirdClosestX)*(closestX - thirdClosestX);
    	basinCenterDistanceSquare += (closestZ - thirdClosestZ)*(closestZ - thirdClosestZ);
    	return nextBorderDistance (basinCenterDistanceSquare,shortestDistanceSq,thirdDistanceSq);
    }
    
    private final double fourthBorderDistance() {
    	double basinCenterDistanceSquare = (closestX - fourthClosestX)*(closestX - fourthClosestX);
    	basinCenterDistanceSquare += (closestZ - fourthClosestZ)*(closestZ - fourthClosestZ);
    	return nextBorderDistance (basinCenterDistanceSquare,shortestDistanceSq,fourthDistanceSq);
    }
    
    private final static double nextBorderDistance(double basinDistanceSq, double shortestDistanceSq, double nextDistanceSq) {
    	// construct a triangle: side half the interbasin distance
    	double downleg = Math.sqrt(basinDistanceSq)/2.0;
    	double closestCosine = closestCosine(basinDistanceSq,shortestDistanceSq,nextDistanceSq);
    	double closestSine = Math.sqrt(1.0 - closestCosine*closestCosine);
    	if (closestSine < .001) {
    		// basically colinear; just do that;
    		double shortest = Math.sqrt(shortestDistanceSq);
    		double nextShortest = Math.sqrt(nextDistanceSq);
    		return (nextShortest - shortest)/2.0;
    	}
    	double hypoteneuse = downleg/closestCosine;
		double shortest = Math.sqrt(shortestDistanceSq);
    	double portion = 1.0 - shortest/hypoteneuse;
    	double distance = portion*downleg;
    	if (Double.isNaN(distance)) throw new RuntimeException();
    	return distance;
    }
    
    private double basinToNextDistanceSquare() {
    	double basinCenterDistanceSquare = (closestX - nextClosestX)*(closestX - nextClosestX);
    	basinCenterDistanceSquare += (closestZ - nextClosestZ)*(closestZ - nextClosestZ);
    	return basinCenterDistanceSquare;
    }

    private static double closestCosine(double basinCenterDistanceSquare,double shortestDistanceSq, double nextDistanceSq) {
    	//double basinCenterDistanceSquare= basinDistanceSquare();
    	
    	if (shortestDistanceSq > nextDistanceSq) throw new RuntimeException();
    	double result =  (shortestDistanceSq - nextDistanceSq + basinCenterDistanceSquare);
    	result = result / (2.0*Math.sqrt(basinCenterDistanceSquare*shortestDistanceSq));
    	

    	return result;
    }
    
    // returns a point on the vector from closestX, closestZ to the passed point but at the specified length
    public final Point2D.Float toLength(Point2D.Float toMap, float radius) {
        double distance = toMap.distance(this.closestX, this.closestZ);
        double xDist = toMap.getX() - this.closestX;
        double zDist = toMap.getY() - this.closestZ;
        xDist *= radius / distance;
        zDist *= radius / distance;
        return new Point2D.Float((float) (this.closestX + xDist), (float) (this.closestZ + zDist));
    }
    
    void evaluate(Point2D.Double[] points, double x, double z) {
        for (Point2D.Double point : points) {
            double distance = point.distanceSq(x, z);
            if (distance < this.shortestDistanceSq) {
            	
            	this.fourthDistanceSq = this.thirdDistanceSq;
            	this.thirdDistanceSq = this.nextDistanceSq;
                this.nextDistanceSq = this.shortestDistanceSq;
                this.shortestDistanceSq = distance;

                this.fourthClosestX = thirdClosestX;
                this.fourthClosestZ = thirdClosestZ;
                this.thirdClosestX = nextClosestX;
                this.thirdClosestZ = nextClosestZ;
                this.nextClosestX = closestX;
                this.nextClosestZ = closestZ;
                this.closestX = point.getX();
                this.closestZ = point.getY();
            }
            else if ((distance < this.nextDistanceSq)) {
            	// check for repeat point
            	if (this.closestX == point.getX() && this.closestZ == point.getY())  {
            		// ignore
	            } else {
	            	this.fourthDistanceSq = this.thirdDistanceSq;
	                this.fourthClosestX = thirdClosestX;
	                this.fourthClosestZ = thirdClosestZ;
	            	this.thirdDistanceSq = nextDistanceSq;
	            	this.thirdClosestX = nextClosestX;
	            	this.thirdClosestZ = nextClosestZ;
	                this.nextDistanceSq = distance;
	                this.nextClosestX = point.getX();
	                this.nextClosestZ = point.getY();
            	}
            } 
            else if (distance < thirdDistanceSq) {
            	// check for repeat point
            	if (this.nextClosestX == point.getX() && this.nextClosestZ == point.getY())  {
            		//ignore. Note can't be same as closest because that would have tripped replacing next;
            	} else {
                	this.fourthDistanceSq = this.thirdDistanceSq;
                    this.fourthClosestX = thirdClosestX;
                    this.fourthClosestZ = thirdClosestZ;
            		this.thirdDistanceSq = distance;
	                this.thirdClosestX = point.getX();
	                this.thirdClosestZ = point.getY();
            	}
            } else if ((distance < thirdDistanceSq)) {
            	// check for repeat point
            	if (this.thirdClosestX == point.getX() && this.thirdClosestZ == point.getY())  {
            		//ignore. Note can't be same as closest because that would have tripped replacing next;
            	} else {
            		this.fourthDistanceSq = distance;
	                this.fourthClosestX = point.getX();
	                this.fourthClosestZ = point.getY();
            	}
            	
            }
            if (shortestDistanceSq> nextDistanceSq) {
            	System.out.println("closest " + shortestDistanceSq);
            	System.out.println("next " + nextDistanceSq);
            	throw new RuntimeException();
            }
        }
    }
}
