package rtg.api.world.gen.feature.tree.bop;

import java.util.Random;
import java.util.function.Function;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import rtg.api.util.BlockUtil;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;

public class BOPTreeMaterials extends TreeMaterials { 

	public BOPTreeMaterials(String name, IBlockState log, IBlockState leaves, IBlockState branches)  {
		super(name,log,leaves,branches);
	}
	
	public static final TreeMaterials willow = new TreeMaterials(
			"Willow",
			BlockBOPLog.paging.getVariantState(BOPWoods.WILLOW),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.WILLOW),
			BlockBOPLog.paging.getVariantState(BOPWoods.WILLOW).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
		

	public static final TreeMaterials snowyBirch = new TreeMaterials(
			"Snowy Birch",
			Blocks.LOG.getStateFromMeta(2),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.YELLOW_AUTUMN),
			Blocks.LOG.getStateFromMeta(14));
	
	public static final TreeMaterials mahogany = new TreeMaterials(
			"Mahogany",
			BlockBOPLog.paging.getVariantState(BOPWoods.MAHOGANY),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.MAHOGANY),
			BlockBOPLog.paging.getVariantState(BOPWoods.MAHOGANY).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
	
	public static final TreeMaterials sacredOak = new TreeMaterials(
			"Eucalyptus",
			BlockBOPLog.paging.getVariantState(BOPWoods.SACRED_OAK),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.SACRED_OAK),
			BlockBOPLog.paging.getVariantState(BOPWoods.SACRED_OAK).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE)
	);
	
	public static class Picker extends TreeMaterials.Picker {		
		public static final TreeMaterials fir = new TreeMaterials(
			"Fir",
			BlockBOPLog.paging.getVariantState(BOPWoods.FIR),
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
		

		public static final TreeMaterials eucalyptus = new TreeMaterials(
				"Eucalyptus",
				BlockBOPLog.paging.getVariantState(BOPWoods.EUCALYPTUS),
				BlockBOPLeaves.paging.getVariantState(BOPTrees.EUCALYPTUS),
				BOPBlocks.log_3.getStateFromMeta(15)
		);
		
		public static final TreeMaterials snowyForest = new TreeMaterials(
				"Snowy Forest",
				Blocks.LOG.getDefaultState(),
				BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD),
				Blocks.LOG.getStateFromMeta(13)
		);
		
	}
	
	public static Function<Random,IBlockState> floweringOak() {
		return new Function<Random,IBlockState>() {

			@Override
			public IBlockState apply(Random t) {
				if (t.nextInt(5) == 0) return BlockBOPLeaves.paging.getVariantState(BOPTrees.FLOWERING);
				return BlockUtil.getStateLeaf(EnumType.OAK);
			}
			
		};
	}

}
