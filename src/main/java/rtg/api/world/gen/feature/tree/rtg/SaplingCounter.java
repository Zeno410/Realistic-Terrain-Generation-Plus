package rtg.api.world.gen.feature.tree.rtg;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.util.Direction;

public class SaplingCounter {
	
	private HashMap<IBlockState,Integer> saplingCounts = new HashMap<>();
	private ArrayList<IBlockState> saplings = new ArrayList<>();
	private int totalCount;
	private TreeMaterials mostCommon ;
	private TreeMaterials secondMostCommon;
	
	private int mostCommonCount;
	private int secondMostCommonCount;
	
	
	public SaplingCounter(World world, BlockPos pos) {

		IBlockState centerSapling = world.getBlockState(pos);
		if (RTGSaplingManager.isSapling(centerSapling)) {
			totalCount += 1;
		} else {
			return;// not a sapling we 
		}
    	for (Direction direction: Direction.list()) {
    		BlockPos adjacent = direction.moved(pos);
    		IBlockState adjacentState = world.getBlockState(adjacent);
    		if (RTGSaplingManager.isSapling(adjacentState)) {
    			totalCount ++;
    			if (saplingCounts.containsKey(adjacentState)) {
    				saplingCounts.put(adjacentState, saplingCounts.get(adjacentState) + 1);
    			} else {
    				saplings.add(adjacentState);
    				saplingCounts.put(adjacentState, 1);
    			}
    		}
    	}
    	// determine most common adjacent saplings;
    	saplingCounts.keySet().forEach((sapling) -> {
    		TreeMaterials materials = RTGSaplingManager.materialsFor(sapling);
    		if (materials == null) return;// nothing to use for this sapling
    		int count = saplingCounts.get(sapling);
    		if (count > mostCommonCount) {
    			secondMostCommon = mostCommon;
    			secondMostCommonCount = mostCommonCount;
    			mostCommon = materials;
    			mostCommonCount = count;
    		} else {
    			if (count > secondMostCommonCount) {
        			secondMostCommon = materials;
        			secondMostCommonCount = count;
    			}
    		}
    	});
    	
	}
	
	public int count() {return totalCount;}
	
	public IBlockState log() {
		if (mostCommon != null) return mostCommon.log;
		return TreeMaterials.Picker.oak.log;
	}
	
	
	public IBlockState branches() {
		if (mostCommon != null) return mostCommon.branches;
		return TreeMaterials.Picker.oak.branches;
	}
	
	public IBlockState leaves() {
		if (secondMostCommon != null) return secondMostCommon.leaves;
		if (mostCommon != null) return mostCommon.leaves;
		return TreeMaterials.Picker.oak.leaves;
	}
	
}