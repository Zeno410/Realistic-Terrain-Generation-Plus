package rtg.api.world.deco.helper;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.util.math.ChunkPos;
import rtg.api.util.ChunkInfo;
import rtg.api.util.Valued;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;
import rtg.api.world.deco.AbstractDeco;

public class DecoHelperWeighted extends AbstractDeco {
	
	ArrayList<Valued<AbstractDeco>> weightedDecos = new ArrayList<>();

	public DecoHelperWeighted() {
	}

	public void add(AbstractDeco added, double weight) {
		if (weight < 0) throw new RuntimeException();
		weightedDecos.add(new Valued<>(added,weight));
	}
	
	@Override
	public void generate(IRealisticBiome biome, RTGWorld rtgWorld, Random rand, ChunkPos chunkPos, float river,
			boolean hasVillage, ChunkInfo chunkInfo) {
		choose(rand).generate(biome, rtgWorld, rand, chunkPos, river, hasVillage, chunkInfo);
	}
	
	private AbstractDeco choose(Random rand) {
		double totalWeight = totalWeight();
		if (totalWeight <=0) throw new RuntimeException();
		double goal = totalWeight*rand.nextDouble();
		for (Valued<AbstractDeco> value: weightedDecos) {
			goal -= value.value;
			if (goal <= 0) return value.item;
		}
		return null;
	}

	private double totalWeight() {
		double result= 0;
		for (Valued<AbstractDeco> value: weightedDecos) {
			result += value.value;
		}
		return result;
	}
}
