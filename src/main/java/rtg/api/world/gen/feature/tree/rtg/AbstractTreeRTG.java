package rtg.api.world.gen.feature.tree.rtg;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public abstract class AbstractTreeRTG extends WorldGenAbstractTree{

	public AbstractTreeRTG(boolean notify) {

        super(notify);
	}
	
	public AbstractTreeRTG() {

        super(false);
	}

}
