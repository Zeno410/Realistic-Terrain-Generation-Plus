package rtg.api.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class TreeWorldGen extends TreeRTG {


    protected WorldGenerator worldGen;
    
	public TreeWorldGen(WorldGenerator _worldGen) {
		worldGen = _worldGen;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		return worldGen.generate(worldIn, rand, position);
	}

}
