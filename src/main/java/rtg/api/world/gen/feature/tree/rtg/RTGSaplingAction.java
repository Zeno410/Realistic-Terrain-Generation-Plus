package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoVariableTree;

public class RTGSaplingAction {

	protected final DecoTree tree;
	public RTGSaplingAction(DecoTree _tree) {
		tree = _tree;
	}
	
	public boolean generate(World world, Random rand, BlockPos pos, int actualHeight, IBlockState log, IBlockState leaves) {
		IBlockState branches = log;
		try {
			branches = log.withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE);
		} catch (Exception e) {
		}
		TreeMaterials materials = new TreeMaterials("",log,leaves,branches);
		tree.setMaterials(materials);
		if (tree instanceof DecoVariableTree) {
			return ((DecoVariableTree)tree).doGenerate(world, rand, pos, actualHeight, new TreeDensityLimiter(1000));
		} else return tree.doGenerate(world, rand, pos, actualHeight);
	}

}
