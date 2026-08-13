package rtg.api.util;

import java.util.ArrayList;
import java.util.Random;


public class WeightedChooser<T> {

	ArrayList<Valued<T>> weighted = new ArrayList<>();
	
	public WeightedChooser() {
	}

	public void add(T added, double weight) {
		if (weight < 0 ) throw new RuntimeException(" attempt to add object with negative weight");
		weighted.add(new Valued<>(added,weight));
	}
	
	public void add(Valued<T> added) {weighted.add(added);}
	
	public void add(T added) {weighted.add(new Valued<>(added,1));}
	
	public T choice(Random rand) {
		double totalWeights = totalWeights();
		if (totalWeights <= 0) throw new RuntimeException("Choice from itemlist with 0 weights");
		double target = totalWeights * rand.nextDouble();
		for (Valued<T> valued: weighted) {
			target -= valued.value;
			if (target <=0 ) {
				return valued.item;
			}
		}
		throw new RuntimeException("Unexplained WeightedChooser error");
		// really should not happen; would be something like simultaneous access
	}
	
	private double totalWeights() {
		double result = 0.0;
		for (Valued<T> valued: weighted) {
			result += valued.value;
		}
		return result;
	}
}
