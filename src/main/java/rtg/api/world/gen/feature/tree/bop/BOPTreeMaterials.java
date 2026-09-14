package rtg.api.world.gen.feature.tree.bop;

import java.util.Random;
import java.util.function.Function;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import rtg.api.util.BlockUtil;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;

public class BOPTreeMaterials extends TreeMaterials { 

	public BOPTreeMaterials(String name, IBlockState log, IBlockState leaves, IBlockState branches)  {
		super(name,log,leaves,branches);
	}
	
	public BOPTreeMaterials(String name, IBlockState log, IBlockState leaves)  {
		super(name,log,leaves,branches(log));
	}
	
	public static final TreeMaterials willow = new TreeMaterials(
			"Willow",
			BlockBOPLog.paging.getVariantState(BOPWoods.WILLOW),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.WILLOW).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			BlockBOPLog.paging.getVariantState(BOPWoods.WILLOW).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
		

	public static final TreeMaterials yellowAutumn = new TreeMaterials(
			"Snowy Birch",
			Blocks.LOG.getStateFromMeta(2),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.YELLOW_AUTUMN).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			Blocks.LOG.getStateFromMeta(14));
	
	public static final TreeMaterials mahogany = new TreeMaterials(
			"Mahogany",
			BlockBOPLog.paging.getVariantState(BOPWoods.MAHOGANY),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.MAHOGANY).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			BlockBOPLog.paging.getVariantState(BOPWoods.MAHOGANY).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
	
	public static final TreeMaterials magic = new TreeMaterials(
			"Mahogany",
			BlockBOPLog.paging.getVariantState(BOPWoods.MAGIC),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.MAGIC).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			BlockBOPLog.paging.getVariantState(BOPWoods.MAGIC).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE));
	
	public static final TreeMaterials sacredOak = new TreeMaterials(
			"Sacred Oak",
			BlockBOPLog.paging.getVariantState(BOPWoods.SACRED_OAK),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.SACRED_OAK),
			BlockBOPLog.paging.getVariantState(BOPWoods.SACRED_OAK).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE)
	);
	
	public static final TreeMaterials palm = new TreeMaterials(
			"Sacred Oak",
			BlockBOPLog.paging.getVariantState(BOPWoods.PALM),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.PALM).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			BlockBOPLog.paging.getVariantState(BOPWoods.PALM).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE)
			//.withProperty(BlockLeaves.DECAYABLE, false)
	);
	
	public static final TreeMaterials pink_cherry = new TreeMaterials(
			"Pink Cherry",
			BlockBOPLog.paging.getVariantState(BOPWoods.CHERRY),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.PINK_CHERRY).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false))
	);
	
	public static final TreeMaterials jacaranda = new TreeMaterials(
			"Jacaranda",
			BlockBOPLog.paging.getVariantState(BOPWoods.JACARANDA),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.JACARANDA).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			BlockBOPLog.paging.getVariantState(BOPWoods.JACARANDA).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE)
	);
	
	public static final TreeMaterials redwood = new TreeMaterials(
			"Redwood",
			BlockBOPLog.paging.getVariantState(BOPWoods.REDWOOD),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.REDWOOD).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			BlockBOPLog.paging.getVariantState(BOPWoods.REDWOOD).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE)
	);
	
	public static final TreeMaterials fir = new TreeMaterials(
		"Fir",
		BlockBOPLog.paging.getVariantState(BOPWoods.FIR),
		BOPBlocks.leaves_1.getStateFromMeta(2).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
		BOPBlocks.log_0.getStateFromMeta(15));
	
	
	public static final TreeMaterials maple = new TreeMaterials(
			"Maple",
			Blocks.LOG.getStateFromMeta(0),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.MAPLE).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			Blocks.LOG.getStateFromMeta(12)
	);
	
	public static final TreeMaterials mangrove = new TreeMaterials(
			"Mangrove",
			BlockBOPLog.paging.getVariantState(BOPWoods.MANGROVE),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.MANGROVE).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			BlockBOPLog.paging.getVariantState(BOPWoods.MANGROVE).withProperty(BlockLog.LOG_AXIS, EnumAxis.NONE)
	);
	
	public static final TreeMaterials orangeAutumn = new TreeMaterials(
			"Orange Autumn",
			BlockUtil.getStateLog(EnumType.DARK_OAK),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.ORANGE_AUTUMN).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			Blocks.LOG2.getStateFromMeta(13)
	);
	
	public static final TreeMaterials snowyForest = new TreeMaterials(
			"Snowy Forest",
			Blocks.LOG.getDefaultState(),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD),
			Blocks.LOG.getStateFromMeta(12)
	);
	
	public static final TreeMaterials eucalyptus = new TreeMaterials(
			"Eucalyptus",
			BlockBOPLog.paging.getVariantState(BOPWoods.EUCALYPTUS),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.EUCALYPTUS).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false)),
			BOPBlocks.log_3.getStateFromMeta(15)
	);
	
	public static final TreeMaterials white_cherry = new TreeMaterials(
			"Pink Cherry",
			BlockBOPLog.paging.getVariantState(BOPWoods.CHERRY),
			BlockBOPLeaves.paging.getVariantState(BOPTrees.WHITE_CHERRY).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false))
	);
	
			
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
