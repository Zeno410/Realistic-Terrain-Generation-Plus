package rtg.api.world.gen.feature.tree.bop;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import rtg.api.util.BlockUtil;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;

public class BOPTreeMaterials extends TreeMaterials { 

	public BOPTreeMaterials(String name, IBlockState log, IBlockState leaves, IBlockState branches)  {
		super(name,log,leaves,branches);
	}
	
	
	public static class Picker extends TreeMaterials.Picker {		
		public static final TreeMaterials fir = new TreeMaterials(
			"Fir",
			BOPBlocks.log_0.getStateFromMeta(12),
			BOPBlocks.leaves_1.getStateFromMeta(2),
			BOPBlocks.log_0.getStateFromMeta(15));
		
		public static final TreeMaterials yellowAutumn = new TreeMaterials(
				"Yellow Autumn",
				Blocks.LOG.getStateFromMeta(2),
				BlockBOPLeaves.paging.getVariantState(BOPTrees.YELLOW_AUTUMN),
				Blocks.LOG.getStateFromMeta(14)
		);
		
		public static final TreeMaterials maple = new TreeMaterials(
				"Maple",
				Blocks.LOG.getStateFromMeta(0),
				BlockBOPLeaves.paging.getVariantState(BOPTrees.MAPLE),
				Blocks.LOG.getStateFromMeta(12)
		);
		
		public static final TreeMaterials orangeAutumn = new TreeMaterials(
				"Orange Autumn",
				BlockUtil.getStateLog(EnumType.DARK_OAK),
				BlockBOPLeaves.paging.getVariantState(BOPTrees.ORANGE_AUTUMN),
				Blocks.LOG2.getStateFromMeta(13)
		);
		
	}

}
