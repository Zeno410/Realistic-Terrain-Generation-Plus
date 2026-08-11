package rtg.event;

import java.util.Collection;
import java.util.HashSet;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.generation.GeneratorStage;
import biomesoplenty.api.generation.IGenerator;
import net.minecraft.world.biome.Biome;
//import rtg.api.util.Logger;

public class TreeGenerationManager {
	
	private HashSet<Biome> takenOverBiomes = new HashSet<>();
	private HashSet<Biome> suppressedBOPBiomes = new HashSet<>();
	private boolean suppressingBOP = true;
	/* This is separate because BoP has a different system for tree generation so the usual suppression does not work
	 * Suppression is done here because it's convenient to couple it with tree insertion.
	 * The system is that the manager keeps a list of BOP biomes to suppress (setup during init)
	 * and then gets flagged to activate the suppression while in RTG worlds.
	 */
	
	public TreeGenerationManager() {
	}

	public boolean managingBiome(Biome considered) {
		if (takenOverBiomes.contains(considered)) return true;
		if (suppressingBOP&&suppressedBOPBiomes.contains(considered)) return true;
		return false;
	}
	
	public void suppressBOPBiome(Biome toSuppress) {
		suppressedBOPBiomes.add(toSuppress);
		resuppressBOPBiome(toSuppress);
	}
	
	public void resuppressBOPBiome(Biome toSuppress) {
		if (!(suppressedBOPBiomes.contains(toSuppress))) throw new RuntimeException(toSuppress.getBiomeName());
		IExtendedBiome extendedBiome = BOPBiomes.REG_INSTANCE.getExtendedBiome(toSuppress);
		Collection<IGenerator> generators = extendedBiome.getGenerationManager().getGeneratorsForStage(GeneratorStage.TREE);
		//Logger.info("[BOP Tree Generation]");
		for (IGenerator generator: generators) {
			//Logger.info("[BOP Tree Generation] ID: "+ generator.getIdentifier() + " name: " + generator.getName() + " text: " + generator.toString());
			
			extendedBiome.getGenerationManager().removeGenerator(generator.getIdentifier());
		}
		//Logger.info("after removal");
		generators = extendedBiome.getGenerationManager().getGeneratorsForStage(GeneratorStage.TREE);
		for (IGenerator generator: generators) {
			//Logger.info("[BOP Tree Generation] ID: "+ generator.getIdentifier() + " name: " + generator.getName() + " text: " + generator.toString());
			
			
			extendedBiome.getGenerationManager().removeGenerator(generator.getName());
		}
		//Logger.info("after reremoval");
		generators = extendedBiome.getGenerationManager().getGeneratorsForStage(GeneratorStage.TREE);
		for (IGenerator generator: generators) {
			//Logger.info("[BOP Tree Generation] ID: "+ generator.getIdentifier() + " name: " + generator.getName() + " text: " + generator.toString());
			
			
			extendedBiome.getGenerationManager().removeGenerator(generator.getName());
		}
		generators = extendedBiome.getGenerationManager().getGeneratorsForStage(GeneratorStage.POST);
		for (IGenerator generator: generators) {
			//Logger.info("[BOP Post Generation] ID: "+ generator.getIdentifier() + " name: " + generator.getName() + " text: " + generator.toString());
			
			if (generator.getName().equals("sacred_oak_trees")) {
			    extendedBiome.getGenerationManager().removeGenerator(generator.getName());
		    }
		}
	}
	

}
