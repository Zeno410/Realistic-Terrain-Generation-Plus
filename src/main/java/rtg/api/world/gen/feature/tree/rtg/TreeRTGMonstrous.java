package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TreeRTGMonstrous extends TreeRTGAbstractDarkOak {

	public TreeRTGMonstrous() {
		lowestVariableTrunkProportion = 0.25f; 
	}

	@Override
	public float estimatedSize() {
		return 1.8f;
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
	    World world ;// just so I don't have to type it so much
	    SkylightTracker lightTracker;
		
		Generation(World _world, Random _rand, BlockPos _pos) {
			rand = _rand;
			world = _world;
			start = dropToGround(world,_pos);
		}

		int maxTorsoLean() {
			return trunkSize/3;
		}
		float averageLegSpread() {
			return (float)trunkSize/3f;
		}
		
		boolean generate() {  
			
			// first generate the "legs" and the "torso", starting from the bottom to avoid floating blocks
			// this is complex since the "chest" needs to be above the passed location.
			// so the torso and legs are back-calculated and generated from the bottom up
			
			int hipHeight = trunkSize/2;
			int torsoHeight = trunkSize - hipHeight;
		    lightTracker = new SkylightTracker(furthestLikelyExtension(),start,world,maxAllowedObstruction);
			
			// start with the "chest", the junction of trunk and branches, above the target block
			BlockPos chestPos = start.add(0, trunkSize+1, 0);
			
			// place the "hip", junction of trunk and roots

	        float toHipDir = (float)(rand.nextFloat()*Math.PI*2.0f);
	        float toHipShift = rand.nextFloat() * maxTorsoLean();
	        
	        float length = toHipShift;
	        
	        BetterTreeBranch reverseTorso = new BetterTreeBranch(toHipDir,-(float)torsoHeight,length,0,chestPos);
	        
	        // rather than backcalculate the messy branch algo, just run it in reverse
	        
	        while (reverseTorso.notDone()) {reverseTorso.movedOrthogonally();}
	        
	        BlockPos hipPos = reverseTorso.location();
	        
	        if (hipPos.getY()<64) return false; // don't generate in deep water
	        
	        int legCount = 2 + rand.nextInt(2);
	        boolean successfulLeg = false;
	        
	        // direction in radians
	        float baseLegDir = (float)(rand.nextFloat()*Math.PI*2.0f);
	        float legIncrement = (float)(Math.PI*2.0f)/legCount;
	        float legVariability = legIncrement/4;
	        float legDirection = baseLegDir - legVariability/2;// all the branches have a random add; this is the pre-subtraction to average zero.
	        
	        for (int branchNumber = 0; branchNumber < legCount; branchNumber ++) {
	        	successfulLeg |= makeLeg(legDirection + rand.nextFloat()*legVariability,hipPos);
	        	legDirection += legIncrement;
	        }
	        
	        // if the tree doesn't have a leg to stand on, fail generation
	        if (!successfulLeg) return false;
	        
	        // now the torso
	        
	        AbstractTreeBranch torso = new BetterTreeBranch(toHipDir + Math.PI,(float)torsoHeight,length,0,hipPos);
	        while(torso.notDone()) {
	        	// if the torso is interrupted, fail generation
	        	if (!placeLogBlock(world,torso.movedOrthogonally(),lightTracker)) return false;
	        }
	
	        int branchCount = 2 + rand.nextInt(2);
	        
	        // direction in radians
	        float baseDir = (float)(rand.nextFloat()*Math.PI*2.0f);
	        float increment = (float)(Math.PI*2.0f)/branchCount;
	        float variability = increment/4;
	        float direction = baseDir - variability/2;// all the branches have a random add; this is the pre-subtraction to average zero.
	        for (int branchNumber = 0; branchNumber < branchCount; branchNumber ++) {
	        	makeReachingBranch(direction + rand.nextFloat()*variability,chestPos);
	        	direction += increment;
	        }
	        
		    return true;
		}
		

		boolean makeLeg(float direction, BlockPos hipPos) {
			float actuallegSpread = averageLegSpread();
			int dx = (int)Math.round(Math.cos(direction)*actuallegSpread);
			int dz = (int)Math.round(Math.sin(direction)*actuallegSpread);
			BlockPos footColumn = new BlockPos(hipPos.getX() - dx, hipPos.getY(),hipPos.getZ()-dz);
			
			// move up or down to just above ground
			if (aboveGround(footColumn)) {
				// scan down until just above ground
				BlockPos below = footColumn.add(0,-1,0);
				while (aboveGround(below)&&footColumn.getY()>60) {
					footColumn =below;
					below = footColumn.add(0,-1,0);;
				}
			} else {
				//scan up until just above ground
				BlockPos above = footColumn.add(0,1,0);
				while (!aboveGround(footColumn)&&footColumn.getY()<200) {
					footColumn =above;
					above = footColumn.add(0,1,0);
				}
			}
			// phew! now footcolumn is where we want to start the root.
			float ascent = hipPos.getY() - footColumn.getY();
			float length = actuallegSpread ;
			AbstractTreeBranch root = new BentBranch(direction,ascent,length,0,footColumn,Bend.upwards(0.4, rand));

			//AbstractTreeBranch root = new BetterTreeBranch(direction,ascent,length,0,footColumn);
			placeTrunkBlock(world, root.location(), lightTracker);
			while (root.notDone()) {
				placeLogBlock(world,root.movedOrthogonally(),lightTracker);
			}
			return true;
		}
		
		boolean aboveGround(BlockPos tested) {
			IBlockState state = world.getBlockState(tested);
			if (state.getLightOpacity()<10) return true;
	    	if (state.getBlock().equals(Blocks.LOG)) return true;
	    	if (state.getBlock().equals(Blocks.LOG2)) return true;
			return false;
		}
		
		void makeReachingBranch(float direction, BlockPos splitStart) {
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
		
		
		void makeTerminalBranch(float direction, BlockPos splitStart, int ascent) {
        	if (ascent < 0) ascent = 0;
        	float spread = 2 + rand.nextInt(3);
        	float verticalShift = ascent;
        	AbstractTreeBranch branch = new BentBranch(direction,verticalShift,spread,1,splitStart,Bend.fromMidline(0.2, 0.4, 0.2, rand));
        	//AbstractTreeBranch branch = new BetterTreeBranch(direction,verticalShift,spread,1,splitStart);
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
