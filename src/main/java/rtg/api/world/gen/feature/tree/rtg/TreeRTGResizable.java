package rtg.api.world.gen.feature.tree.rtg;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.util.ChunkInfo;
import rtg.api.world.gen.feature.WorldGenShrubRTG;

public class TreeRTGResizable extends TreeRTG {
	
	// TODO; figure out a way to generalize this to deal with variable numbers of trees, and not having a vanilla
	
	protected TreeRTG tree;
			
	protected float averageHeightSqrt = 4.4f; // average tree height square root; trees vary
	protected float heightNoiseVariability = 2f; // maximum change in average height up or down from noise
	                                               // can go up or down so range is twice this number
	protected float localHeightSqrtVariability = 0.25f; // similar but tree to tree;
	
	protected float saplingChance = .1f; // chance a tree will be shorter than expected
	
	protected int minimumSize = 0;
	
	public TreeRTGResizable(TreeRTG _tree) {
		this.tree = _tree;
	}
	
	public void setLeafChoice(Function<Random,IBlockState> newChoice) {
		tree.setLeafChoice(newChoice);
	}
	
	public void  changeAverageHeightSqrt(float change) {
		averageHeightSqrt += change;
	}
	
	public void  changeHeightNoiseVariability(float change) {
		heightNoiseVariability += change;
	}
	
	public void  changeLocalNoiseVariability(float change) {
		localHeightSqrtVariability += change;
	}
	
	public void setMinimumSize(int newMinimum) {this.minimumSize = newMinimum;}
	
	public void setSaplingChance(float newChance) {saplingChance = newChance;}


	public void doVariableGenerate(Random rand, ChunkInfo chunkInfo, BlockPos column, int y, TreeDensityLimiter treesRemaining) { 
		
		float averageHeightIndex = chunkInfo.treeHeightNoiseValue();
		
		float noise = averageHeightIndex;
		// value is -1 to 1, so adjust to be in [2.5,5.5] or whatever, the targeted range of height square *root*
		averageHeightIndex *= heightNoiseVariability;
		averageHeightIndex += averageHeightSqrt;
		// a little tree to tree variability. Math is different from noise math because noises are [-1,1] and randoms [0,1]
		float actualHeightIndex = averageHeightIndex + rand.nextFloat()*localHeightSqrtVariability*2f - localHeightSqrtVariability;
		// shrink down if high
		if (y>70) {
			actualHeightIndex -= ((float)(y-70))/20f;
			// no negatives!
			if (averageHeightIndex <0) {
				treesRemaining.occupy(0.3f);// use up a little to avoid infinite loops
				return;  //too small, no tree generated.
			}
		}
		// square for actual height
		int actualHeight = (int)(actualHeightIndex*actualHeightIndex);
		// occasional smaller saplings
		if (rand.nextFloat()<saplingChance&&actualHeight>minimumSize) {
			actualHeight = minimumSize + rand.nextInt(actualHeight-minimumSize);
		} 
		if (actualHeight >minimumSize) {
		     generateTree(chunkInfo.world(), rand, column.up(y), actualHeight,treesRemaining);
		} else {
			if (treesRemaining.allowed(0.7f, rand))  {
			    new WorldGenShrubRTG(actualHeight,tree.getLogBlock(),tree.getLeavesBlock(),false).generate(chunkInfo.world(), rand, column.up(y));
			}
		}
		return;
	}
	
	public void doGenerate(World world, Random rand, BlockPos pos, int actualHeight, TreeDensityLimiter treesRemaining) {
		throw new RuntimeException();//called without the size check
    }
    
	private void generateTree(World world, Random random, BlockPos pos,int actualHeight, TreeDensityLimiter treesRemaining) {
		tree.setTreeSize(actualHeight, random);
		tree.setNoLeaves(false);
        if (treesRemaining.allowed(tree.estimatedSize(), random)) {
        	tree.generate(world, random, pos);
        }
	}
    @Override
    protected boolean canGrowInto(Block block) {return tree.canGrowInto(block);}

    public IBlockState getTrunkLog(IBlockState defaultLog) {return tree.getTrunkLog(defaultLog);}

    public IBlockState getLogBlock() {return tree.logBlock; }

    public TreeRTG setLogBlock(IBlockState logBlock) {return tree.setLogBlock(logBlock);}
    
    public IBlockState getBranchBlock() {return tree.getBranchBlock();}

    public TreeRTG setBranchBlock(IBlockState branchBlock) {return tree.setBranchBlock(branchBlock);}

    public IBlockState getLeavesBlock() {return tree.leavesBlock;}

    public TreeRTG setLeavesBlock(IBlockState leavesBlock) {return tree.setLeavesBlock(leavesBlock);}
    
    public TreeRTG setMaterials(TreeMaterials materials) {
    	return tree.setMaterials(materials);
    }

    public int getTrunkSize() {return tree.getTrunkSize(); }

    public TreeRTG setTrunkSize(int trunkSize) {return tree.setTrunkSize(trunkSize);}

    public int getCrownSize() { return tree.crownSize; }

    public TreeRTG setCrownSize(int crownSize) { return tree.setCrownSize(crownSize);}

    public boolean getNoLeaves() {return tree.noLeaves;}

    public TreeRTG setNoLeaves(boolean noLeaves) {return tree.setNoLeaves(noLeaves);}

    public int getGenerateFlag() {return tree.generateFlag;}

    public TreeRTG setGenerateFlag(int generateFlag) {return tree.setGenerateFlag(generateFlag);}

    public int getMinTrunkSize() {return tree.minTrunkSize;}

    public TreeRTG setMinTrunkSize(int minTrunkSize) {return tree.setMinTrunkSize(minTrunkSize);}

    public int getMaxTrunkSize() {return tree.maxTrunkSize;}

    public TreeRTG setMaxTrunkSize(int maxTrunkSize) {return tree.setMaxTrunkSize(maxTrunkSize);}

    public int getMinCrownSize() {return tree.minCrownSize;}

    public TreeRTG setMinCrownSize(int minCrownSize) {return tree.setMinCrownSize(minCrownSize);}

    public int getMaxCrownSize() { return tree.maxCrownSize;}

    public TreeRTG setMaxCrownSize(int maxCrownSize) { return tree.setMaxCrownSize(maxCrownSize);}
    
    public float getLowestVariableTrunkProportion() { return tree.lowestVariableTrunkProportion;}
    
    public TreeRTG setLowestVariableTrunkProportion (float lowest) {return tree.setLowestVariableTrunkProportion(lowest);}
    
    public float getTrunkProportionVariability() {return tree.trunkProportionVariability;}
    
    public TreeRTG setTrunkProportionVariability (float variability) {return tree.setTrunkProportionVariability(variability);}
    
    public int getTrunkReserve() {return tree.trunkReserve;}

    public ArrayList<IBlockState> getValidGroundBlocks() {return tree.validGroundBlocks;}

    public TreeRTG setValidGroundBlocks(ArrayList<IBlockState> validGroundBlocks) {return tree.setValidGroundBlocks(validGroundBlocks);}
    
    public int getMaxAllowedObstruction() { return tree.maxAllowedObstruction;}
    
    public void setMaxAllowedObstruction(int newObstruction) {tree.maxAllowedObstruction = newObstruction;}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		throw new RuntimeException();//called without the size check
	}
	
}