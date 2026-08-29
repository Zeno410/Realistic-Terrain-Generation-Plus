package rtg.api.world.deco;

import java.util.Random;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import rtg.api.util.ChunkInfo;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGMediumDarkOak;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGMonstrous;
import rtg.api.world.gen.feature.tree.rtg.TreeRTGSmallDarkOak;

public class DecoVariableDarkOak extends DecoVariableTree {

	public DecoVariableDarkOak() {
		tallTree = new TreeRTGMonstrous();
		mediumTree = new TreeRTGMediumDarkOak();
		smallTree = new TreeRTGSmallDarkOak();
		this.materials = this.materialsPicker.darkOak;
		this.averageHeightSqrt -= .7f;
		this.tallTreeMinimumHeight = 15;
		this.mediumTreeMinimumHeight = 10;
		this.smallTreeMinimumHeight = 5;
		
		
		/*	protected int tallTreeMinimumHeight = 21; // shortest allowed tall tree
	protected int tallTreeMinimumVariability = 9; // this less 1 (Random.nextInt()) added to minimum for largest allowed medium tree
	protected int mediumTreeMinimumHeight = 12; // etc.
	protected int mediumTreeMinimumVariability = 5;
	protected int smallTreeMinimumHeight = 7;
	protected int smallTreeMinimumVariability = 3;
	protected int vanillaTreeMinimumHeight = 2;
	protected int vanillaTreeMinimumVariability = 2;
			
	protected float averageHeightSqrt = 4.4f; // average tree height square root; trees vary
	*/
	}
	
	protected WorldGenAbstractTree vanillaTree() {
		return new WorldGenCanopyTree(false);
	}
	
}
