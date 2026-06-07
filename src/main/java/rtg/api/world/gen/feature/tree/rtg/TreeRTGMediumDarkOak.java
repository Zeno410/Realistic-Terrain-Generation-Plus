package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TreeRTGMediumDarkOak extends TreeRTGAbstractDarkOak {

	public TreeRTGMediumDarkOak() {
		lowestVariableTrunkProportion = 0.25f; 
	}

	@Override
	public float estimatedSize() {
		return 1.4f;
	}
	
	@Override
	public int furthestLikelyExtension() {
		return 14;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		Generation generation = new Generation(world,rand,pos);
		boolean result =  generation.generate();
		return result;
	}

	private class Generation {
		final Random rand;
		final BlockPos start;
	    World world ;
	    SkylightTracker lightTracker;
		
		Generation(World _world, Random _rand, BlockPos _pos) {
			rand = _rand;
			start = _pos;
			world = _world;
		}

		boolean generate() {        
			if (!isGroundValid(world, start)) {
               return false;
	        }
	       lightTracker = new SkylightTracker(furthestLikelyExtension(),start,world,maxAllowedObstruction);
	
	        final int x = start.getX();
	        int y = start.getY();
	        final int z = start.getZ();
	
	
	        BlockPos trunkPos;
	        for (int i = -2; i < trunkSize; i++) {
	        	trunkPos = new BlockPos(x,y+i,z);
	            placeTrunkBlock(world, trunkPos, lightTracker);
	        	trunkPos = new BlockPos(x+1,y+i,z);
	            placeTrunkBlock(world, trunkPos, lightTracker);
	        	trunkPos = new BlockPos(x+1,y+i,z+1);
	            placeTrunkBlock(world, trunkPos, lightTracker);
	        	trunkPos = new BlockPos(x,y+i,z+1);
	            placeTrunkBlock(world, trunkPos, lightTracker);
	        }
	        
	        int branchCount = 3 + rand.nextInt(2);
	        // direction in radians
	        float baseDir = (float)(rand.nextFloat()*Math.PI*2.0f);
	        float increment = (float)(Math.PI*2.0f)/branchCount;
	        float variability = increment/4;
	        BaseSetter baseSetter = new BaseSetter(branchCount,start);
	        float direction = baseDir - variability/2;// all the branches have a random add; this is the pre-subtraction to average zero.
	        for (int branchNumber = 0; branchNumber < branchCount; branchNumber ++) {
	        	makeReachingBranch(direction + rand.nextFloat()*variability,  baseSetter);
	        	direction += increment;
	        }
	        
		    return true;
		}
		
		void makeReachingBranch(float direction, BaseSetter baseSetter) {
			baseSetter.set(direction);
        	BlockPos splitStart = baseSetter.base;
			//if (crownSize >0) throw new RuntimeException();
        	int ascent = crownSize - 1 + rand.nextInt(2);
        	if (ascent < 0) ascent = 0;
        	float spread = 3 + rand.nextInt(3);
        	int reachAscent = ascent/2;
        	float verticalShift = reachAscent;//((float)reachAscent)/((float)spread);
        	AbstractTreeBranch branch = new BentBranch(direction,verticalShift,spread,1,splitStart,Bend.fromMidline(0.2, 0.4, 0.2, rand));
        	//AbstractTreeBranch branch = new BetterTreeBranch(direction,verticalShift,spread,1,splitStart);
			while (branch.notDone() ) {
				lightTracker.testPlace(world, branch.movedOrthogonally(), branchBlock, generateFlag);
			}	        int branchCount = 2 + rand.nextInt(2);
	        
	        // direction in radians
	        float baseDir = (float)(rand.nextFloat()*Math.PI*2.0f);
	        float increment = (float)(Math.PI*2.0f)/branchCount;
	        float variability = increment/4;
	        float branchDirection = baseDir - variability/2;// all the branches have a random add; this is the pre-subtraction to average zero.
	        final int remainingAscent = ascent + splitStart.getY()- branch.location().getY();

	        for (int branchNumber = 0; branchNumber < branchCount; branchNumber ++) {
	        	makeTerminalBranch(branchDirection + rand.nextFloat()*variability,branch.location(), remainingAscent - 1 + rand.nextInt(2)+ rand.nextInt(2));
	        	branchDirection += increment;
	        }
		}
		
		void makeTerminalBranch(float direction,BlockPos splitStart, int remainingAscent) {
        	float ascent = remainingAscent;
        	if (ascent < 0) ascent = 0;
        	float spread = 2 + rand.nextInt(3);
        	AbstractTreeBranch branch = new BentBranch(direction,ascent,spread,1,splitStart,Bend.fromMidline(0.2, 0.4, 0.2, rand));
			while (branch.notDone() ) {
				lightTracker.testPlace(world, branch.movedOrthogonally(), branchBlock, generateFlag);
			}
			genLeaves(branch.location(),true);
		}
		
		void genLeaves(BlockPos pos, boolean bigger) {
			genLeaves(pos.getX(),pos.getY(),pos.getZ(),bigger);
		}
		
	    void genLeaves(int x, int y, int z, boolean bigger) {

	        if (!noLeaves) {
	        	
	        	int topRadius = 1;
	        	int bottomRadius = 2;
	        	
	        	if (bigger) {
	        		topRadius ++;
	        		bottomRadius ++;
	        	}
	        	
	        	
	            int i;
	            int j;
	            for (i = -topRadius; i <= topRadius; i++) {
	                for (j = -topRadius; j <= topRadius; j++) {
	                    if (Math.abs(i) + Math.abs(j) < topRadius+2) {
	                       placeLeavesBlock(world, new BlockPos(x + i, y + 1, z + j), lightTracker);
	                    }
	                }
	            }

	            for (i = -bottomRadius; i <= bottomRadius; i++) {
	                for (j = -bottomRadius; j <= bottomRadius; j++) {
	                    if (Math.abs(i) + Math.abs(j) < bottomRadius+2) {
	                        placeLeavesBlock(world, new BlockPos(x + i, y, z + j),  lightTracker);
	                    }
	                }
	            }
	        }

	        placeLogBlock(world, new BlockPos(x, y, z),lightTracker);
	    }
	    

	}


}
