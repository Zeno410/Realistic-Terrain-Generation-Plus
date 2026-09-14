package rtg.compat;

import java.util.ArrayList;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import biomesoplenty.common.block.BlockBOPSapling;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import rtg.api.util.BlockUtil;
import rtg.api.world.deco.DecoTree;
import rtg.api.world.deco.DecoVariableBirch;
import rtg.api.world.deco.DecoVariableOak;
import rtg.api.world.deco.DecoVariableTree;
import rtg.api.world.deco.bop.VariableBOPEucalyptusTree;
import rtg.api.world.gen.feature.tree.bop.BOPTreeMaterials;
import rtg.api.world.gen.feature.tree.bop.TreeBOPJacaranda;
import rtg.api.world.gen.feature.tree.bop.TreeBOPMediumFir;
import rtg.api.world.gen.feature.tree.bop.TreeBOPPalm;
import rtg.api.world.gen.feature.tree.bop.TreeBOPTropicalRainforest;
import rtg.api.world.gen.feature.tree.rtg.RTGSaplingAction;
import rtg.api.world.gen.feature.tree.rtg.RTGSaplingManager;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;
import rtg.api.world.gen.feature.tree.rtg.TreeRTG;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGCeibaPentandra;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGQuercusFalcata;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGQuercusNigra;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGQuercusRobur;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGResizable;

public class RegisterBoP {

	public static void saplings() {
		// register saplings with RTGSaplingManager so users can grow the tree and/or use the tree leaves and log in other trees
		
		register(BOPTrees.YELLOW_AUTUMN, BOPTreeMaterials.yellowAutumn, new DecoVariableBirch());
		
		register(BOPTrees.ORANGE_AUTUMN, BOPTreeMaterials.orangeAutumn, new DecoVariableOak());
		
		// nonstandard wood
		RTGSaplingManager.setSapling(
				BlockBOPSapling.paging.getVariantState(BOPTrees.BAMBOO),
				new BOPTreeMaterials("Bamboo",
						BOPBlocks.bamboo.getDefaultState(),
						BlockBOPLeaves.paging.getVariantState(BOPTrees.BAMBOO).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false))));

		register(BOPTrees.MAGIC, BOPTreeMaterials.magic, new DecoVariableOak());
		
		setSapling("umbran", BOPTrees.UMBRAN, BOPWoods.UMBRAN);
		
		RTGSaplingManager.setSapling(
				BlockBOPSapling.paging.getVariantState(BOPTrees.DEAD),
				new BOPTreeMaterials("dead",
						BlockUtil.getStateLog(BlockPlanks.EnumType.OAK),
						BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD)));

		register(BOPTrees.FIR, BOPTreeMaterials.fir, new DecoTree(new TreeBOPMediumFir()));
		
		RTGSaplingManager.setSapling(
				BlockBOPSapling.paging.getVariantState(BOPTrees.ORIGIN),
				new BOPTreeMaterials("origin",
						BlockUtil.getStateLog(BlockPlanks.EnumType.OAK),
						BlockBOPLeaves.paging.getVariantState(BOPTrees.ORIGIN).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false))));

		register(BOPTrees.PINK_CHERRY, BOPTreeMaterials.pink_cherry, new DecoTree(new TreeRTGQuercusFalcata()));
		
		register(BOPTrees.WHITE_CHERRY, BOPTreeMaterials.white_cherry, new DecoTree(new TreeRTGQuercusFalcata()));
		
		register(BOPTrees.MAPLE, BOPTreeMaterials.maple, new DecoTree(new TreeRTGQuercusNigra()));
		
		setSapling("Hellbark",BOPTrees.HELLBARK, BOPWoods.HELLBARK);
		
    	DecoVariableTree orchardTree = new DecoVariableOak();
    	// no Nigra
    	orchardTree.setTallTree(new TreeRTGQuercusFalcata());
    	// give it flowering leaves
    	orchardTree.setLeafChoice(BOPTreeMaterials.floweringOak());
    	
		register(BOPTrees.FLOWERING, TreeMaterials.Picker.oak, orchardTree);

		register(BOPTrees.JACARANDA, BOPTreeMaterials.jacaranda, new DecoTree(new TreeBOPJacaranda()));

		register(BOPTrees.SACRED_OAK, BOPTreeMaterials.sacredOak, new DecoTree(new TreeRTGQuercusRobur()));
		
		// Mangrove will use the currently unavailable Pentandra
	   	TreeRTG petandraTree = new TreeRTGCeibaPentandra();
    	petandraTree.setMaterials(BOPTreeMaterials.mangrove);
    	ArrayList<IBlockState> ground = petandraTree.getValidGroundBlocks();
    	ground.add(BOPBlocks.mud.getDefaultState());
    	petandraTree.setValidGroundBlocks(ground);

		register(BOPTrees.MANGROVE, BOPTreeMaterials.mangrove, new DecoTree(petandraTree));

    	TreeRTG palmTree = new TreeBOPPalm();

		register(BOPTrees.PALM, BOPTreeMaterials.palm, new DecoTree(palmTree));
		
		setSapling("Redwood", BOPTrees.REDWOOD, BOPWoods.REDWOOD);
		
		setSapling("Willow", BOPTrees.WILLOW, BOPWoods.WILLOW);
		
		setSapling("Pine", BOPTrees.PINE, BOPWoods.PINE);

    	TreeRTG mahoganyTree = new TreeBOPTropicalRainforest();
    	mahoganyTree.setMinCrownSize(6);
    	mahoganyTree.setMaxCrownSize(8);
    	mahoganyTree.setMinTrunkSize(12);
    	mahoganyTree.setMaxTrunkSize(14);
    	
    	mahoganyTree.setLowestVariableTrunkProportion(0.5f);
    	
		register(BOPTrees.MAHOGANY, BOPTreeMaterials.mahogany, new DecoTree(mahoganyTree));
		
		setSapling("Ebony", BOPTrees.EBONY, BOPWoods.EBONY);

		register(BOPTrees.EUCALYPTUS, BOPTreeMaterials.eucalyptus, new VariableBOPEucalyptusTree());
	}
	
	private static void setSapling(String name, BOPTrees variant, BOPWoods wood) {
		
		RTGSaplingManager.setSapling(
				BlockBOPSapling.paging.getVariantState(variant),
				new BOPTreeMaterials(name,
						BlockBOPLog.paging.getVariantState(wood),
						BlockBOPLeaves.paging.getVariantState(variant).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false))));
		
	}
	
	private static void register(BOPTrees variant,TreeMaterials materials, DecoTree tree) {

		RTGSaplingManager.register(
				BlockBOPSapling.paging.getVariantState(variant),
				materials,  
				new RTGSaplingAction(tree));
	}

	
}
