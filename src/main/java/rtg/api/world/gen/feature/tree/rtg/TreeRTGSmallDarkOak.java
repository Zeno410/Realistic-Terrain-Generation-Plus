package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import rtg.api.util.ChunkInfo;
import rtg.api.util.Logger;

public class TreeRTGSmallDarkOak extends TreeRTGAbstractDarkOak {

	public TreeRTGSmallDarkOak() {
	}
	@Override
	public float estimatedSize() {
		return 0.8f;
	}
	
	@Override
	public int furthestLikelyExtension() {
		return 10;
	}
	private int maxAllowedObstruction() {return maxAllowedObstruction;}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		ChunkPos chunkPos = new ChunkPos(pos);
		ChunkInfo chunkInfo;
		Generation generation = new Generation(world,rand,pos);
		boolean result =  generation.generate();
		return result;
	}

    class Generation {
		final Random rand;
		final BlockPos start;
	    World world ;// just so I don't have to type it so much
	    SkylightTracker lightTracker;
		
		Generation(World _world , Random _rand, BlockPos _pos) {
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
	
	
	        BlockPos trunkPos ;
	        for (int i = -2; i < trunkSize; i++) {
	        	trunkPos= new BlockPos(x,y+i,z);
	            placeTrunkBlock(world, trunkPos, 3, lightTracker);
	        	trunkPos = new BlockPos(x+1,y+i,z);
	            placeTrunkBlock(world, trunkPos, 3, lightTracker);
	        	trunkPos = new BlockPos(x+1,y+i,z+1);
	            placeTrunkBlock(world, trunkPos, 3, lightTracker);
	        	trunkPos= new BlockPos(x,y+i,z+1);
	            placeTrunkBlock(world, trunkPos, 3, lightTracker);
	        }
	        
	        int branchCount = 3 + rand.nextInt(2);
	        // direction in radians
	        float baseDir = (float)(rand.nextFloat()*Math.PI*2.0f);
	        float increment = (float)(Math.PI*2.0f)/branchCount;
	        float variability = increment/4;
	        BaseSetter baseSetter = new BaseSetter(branchCount,start);
	        float direction = baseDir - variability/2;// all the branches have a random add; this is the pre-subtraction to average zero.
	        for (int branchNumber = 0; branchNumber < branchCount; branchNumber ++) {
	        	makeBranch(direction + rand.nextFloat()*variability,  baseSetter);
	        	direction += increment;
	        }
		    return true;
		}
		
		void makeBranch(float direction,BaseSetter baseSetter) {
			baseSetter.set(direction);
			//if (crownSize >0) throw new RuntimeException();
        	float ascent = crownSize - 1 - rand.nextInt(2);
        	if (ascent < 0) ascent = 0;
        	float spread = 2 + rand.nextInt(3);
        	baseSetter.set(direction);
        	BlockPos splitStart = baseSetter.base; 
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
	                       placeLeavesBlock(world, new BlockPos(x + i, y + 1, z + j), 3, lightTracker);
	                    }
	                }
	            }

	            for (i = -bottomRadius; i <= bottomRadius; i++) {
	                for (j = -bottomRadius; j <= bottomRadius; j++) {
	                    if (Math.abs(i) + Math.abs(j) < bottomRadius+2) {
	                        placeLeavesBlock(world, new BlockPos(x + i, y, z + j),  3, lightTracker);
	                    }
	                }
	            }
	        }

	        placeLogBlock(world, new BlockPos(x, y, z),3,lightTracker);
	    }
	    

	}
}
