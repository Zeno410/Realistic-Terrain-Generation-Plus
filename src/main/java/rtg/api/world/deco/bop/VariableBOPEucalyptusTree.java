package rtg.api.world.deco.bop;

import rtg.api.world.deco.DecoVariableTree;
import rtg.api.world.gen.feature.tree.bop.BOPTreeMaterials;
import rtg.api.world.gen.feature.tree.bop.TreeBOPLargeEucalyptus;
import rtg.api.world.gen.feature.tree.bop.TreeBOPMediumEucalyptus;
import rtg.api.world.gen.feature.tree.rtg.TreeMaterials;

public class VariableBOPEucalyptusTree  extends DecoVariableTree {

	
	public VariableBOPEucalyptusTree() {
		 tallTree = new TreeBOPLargeEucalyptus();
	     mediumTree = new TreeBOPMediumEucalyptus();
	     smallTree = new TreeBOPMediumEucalyptus();
	     this.materials = BOPTreeMaterials.eucalyptus;
	     //this.averageHeightSqrt += 1f;
	     this.heightNoiseVariability += 0.2f;
	     // we don't have a vanilla tree, so edit the min heights to prevent
	     this.smallTreeMinimumHeight = 1;
	     this.vanillaTreeMinimumHeight = 10;
	}
	
	// and had to change this too
	public int smallestSaplingHeight() {
		return smallTreeMinimumHeight + 4;
	}
}
