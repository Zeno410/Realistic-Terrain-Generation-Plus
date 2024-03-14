package rtg.init;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import rtg.RTGConfig;
import rtg.api.RTGAPI;
import rtg.api.util.UtilityClass;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.compat.ModCompat.Mods;
import rtg.world.biome.realistic.abyssalcraft.RealisticBiomeACCoraliumInfestedSwamp;
import rtg.world.biome.realistic.abyssalcraft.RealisticBiomeACDarklands;
import rtg.world.biome.realistic.abyssalcraft.RealisticBiomeACDarklandsForest;
import rtg.world.biome.realistic.abyssalcraft.RealisticBiomeACDarklandsHills;
import rtg.world.biome.realistic.abyssalcraft.RealisticBiomeACDarklandsMountains;
import rtg.world.biome.realistic.abyssalcraft.RealisticBiomeACDarklandsPlains;
import rtg.world.biome.realistic.auxiliarybiomes.RealisticBiomeAUXForestedIsland;
import rtg.world.biome.realistic.auxiliarybiomes.RealisticBiomeAUXIceWasteland;
import rtg.world.biome.realistic.auxiliarybiomes.RealisticBiomeAUXMarsh;
import rtg.world.biome.realistic.auxiliarybiomes.RealisticBiomeAUXWasteland;
import rtg.world.biome.realistic.auxiliarybiomes.RealisticBiomeAUXWhiteForest;
import rtg.world.biome.realistic.betteragriculture.RealisticBiomeBAFarmlandBiome;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPAlps;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPAlpsFoothills;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPBambooForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPBayou;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPBog;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPBorealForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPBrushland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPChaparral;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPCherryBlossomGrove;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPColdDesert;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPConiferousForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPCoralReef;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPCrag;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPDeadForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPDeadSwamp;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPEucalyptusForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPFen;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPFlowerField;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPFlowerIsland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPGlacier;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPGrassland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPGravelBeach;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPGrove;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPHighland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPKelpForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPLandOfLakes;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPLavenderFields;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPLushDesert;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPLushSwamp;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPMangrove;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPMapleWoods;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPMarsh;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPMeadow;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPMoor;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPMountainFoothills;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPMountainPeaks;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPMysticGrove;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPOasis;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPOminousWoods;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPOrchard;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPOriginBeach;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPOriginIsland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPOutback;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPOvergrownCliffs;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPPasture;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPPrairie;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPQuagmire;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPRainforest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPRedwoodForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPRedwoodForestEdge;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPSacredSprings;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPSeasonalForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPShield;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPShrubland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPSnowyConiferousForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPSnowyForest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPSnowyTundra;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPSteppe;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPTemperateRainforest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPTropicalIsland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPTropicalRainforest;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPTundra;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPVolcanicIsland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPWasteland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPWetland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPWhiteBeach;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPWoodland;
import rtg.world.biome.realistic.biomesoplenty.RealisticBiomeBOPXericShrubland;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAlliumFields;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAlps;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAmaranthFields;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAncientForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGAspenForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBambooForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBaobabSavanna;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBayou;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBlueTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBluffMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBog;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGBorealForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGCanyons;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGChaparralLowlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGCherryGrove;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGCikaForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGConiferousForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGCyprusSwampland;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGDeadSea;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGDeciduousForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGDoverMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGDunes;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGEbonyWoods;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGEnchantedForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGEucalyptusTropics;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGEvergreenTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGFrostyForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGFungalJungle;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGiantBlueSpruceTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGiantSeasonalSpruceTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGlaciers;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGlowshroomBayou;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGrasslandPlateau;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGreatLakes;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGGreatOakLowlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGJacarandaForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGLushDesert;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGMangroveMarshes;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGMapleForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGMeadow;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGOrchard;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGOutback;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGOutlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGPaperBirchForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGPineLowlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGPineMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGPraire;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGPrairie;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGQuagmire;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGRedDesert;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGRedOakForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGRedOutlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGRedwoodTropics;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSavannaCanopy;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSeasonalBirchForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSeasonalDeciduous;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSeasonalForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSeasonalTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGShrublands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSkyrisHighlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSnowyConiferousForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSnowyEvergeenTaiga;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSnowyPineMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGSonoranDesert;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGStellataPasture;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGStoneBrushlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGTropicalIslands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGTropicalMountains;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGTropicalRainforest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGTundra;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGWeepingWitchForest;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGWhisperingWoods;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGWoodlands;
import rtg.world.biome.realistic.biomesyougo.RealisticBiomeBYGZelkovaForest;
import rtg.world.biome.realistic.bionisation.RealisticBiomeBIOInfectedForest;
import rtg.world.biome.realistic.buildcraft.RealisticBiomeBCDesertOilField;
import rtg.world.biome.realistic.buildcraft.RealisticBiomeBCOceanOilField;
import rtg.world.biome.realistic.candyworld.RealisticBiomeCWChocolateForest;
import rtg.world.biome.realistic.candyworld.RealisticBiomeCWCottonCandyPlains;
import rtg.world.biome.realistic.candyworld.RealisticBiomeCWGummySwamp;
import rtg.world.biome.realistic.defiledlands.RealisticBiomeDLDesertDefiled;
import rtg.world.biome.realistic.defiledlands.RealisticBiomeDLForestTenebra;
import rtg.world.biome.realistic.defiledlands.RealisticBiomeDLForestVilespine;
import rtg.world.biome.realistic.defiledlands.RealisticBiomeDLHillsDefiled;
import rtg.world.biome.realistic.defiledlands.RealisticBiomeDLIcePlainsDefiled;
import rtg.world.biome.realistic.defiledlands.RealisticBiomeDLPlainsDefiled;
import rtg.world.biome.realistic.defiledlands.RealisticBiomeDLSwampDefiled;
import rtg.world.biome.realistic.douglasforest.RealisticBiomeDFDouglasForest;
import rtg.world.biome.realistic.douglasforest.RealisticBiomeDFMapleForest;
import rtg.world.biome.realistic.environs.RealisticBiomeENVDeadOakForest;
import rtg.world.biome.realistic.environs.RealisticBiomeENVDeadOakForestHills;
import rtg.world.biome.realistic.environs.RealisticBiomeENVEmeraldCliffs;
import rtg.world.biome.realistic.environs.RealisticBiomeENVEndForest;
import rtg.world.biome.realistic.environs.RealisticBiomeENVEndShrubland;
import rtg.world.biome.realistic.environs.RealisticBiomeENVExtremeJungle;
import rtg.world.biome.realistic.environs.RealisticBiomeENVIcyHills;
import rtg.world.biome.realistic.environs.RealisticBiomeENVMoor;
import rtg.world.biome.realistic.environs.RealisticBiomeENVPineTaiga;
import rtg.world.biome.realistic.environs.RealisticBiomeENVPineTaigaHills;
import rtg.world.biome.realistic.environs.RealisticBiomeENVSilkglades;
import rtg.world.biome.realistic.environs.RealisticBiomeENVSparseEndForest;
import rtg.world.biome.realistic.environs.RealisticBiomeENVStoneFlats;
import rtg.world.biome.realistic.environs.RealisticBiomeENVTallOakForest;
import rtg.world.biome.realistic.environs.RealisticBiomeENVTallOakForestHills;
import rtg.world.biome.realistic.environs.RealisticBiomeENVTallOakWetland;
import rtg.world.biome.realistic.environs.RealisticBiomeENVWastelandEroded;
import rtg.world.biome.realistic.environs.RealisticBiomeENVWastelandSpikes;
import rtg.world.biome.realistic.explorercraft.RealisticBiomeECBambooForest;
import rtg.world.biome.realistic.explorercraft.RealisticBiomeECForestedMountain;
import rtg.world.biome.realistic.explorercraft.RealisticBiomeECSnowdonia;
import rtg.world.biome.realistic.floricraft.RealisticBiomeFLORIRoseLand;
import rtg.world.biome.realistic.floricraft.RealisticBiomeFLORITulipLand;
import rtg.world.biome.realistic.fyrecraft.RealisticBiomeFYRECrimsonGrove;
import rtg.world.biome.realistic.fyrecraft.RealisticBiomeFYREMegaMountains;
import rtg.world.biome.realistic.fyrecraft.RealisticBiomeFYREMushroomGrove;
import rtg.world.biome.realistic.fyrecraft.RealisticBiomeFYRERedDesert;
import rtg.world.biome.realistic.fyrecraft.RealisticBiomeFYRERedDesertHills;
import rtg.world.biome.realistic.fyrecraft.RealisticBiomeFYRERockyWasteland;
import rtg.world.biome.realistic.fyrecraft.RealisticBiomeFYRETropicalLakes;
import rtg.world.biome.realistic.fyrecraft.RealisticBiomeFYREVolcanicWasteland;
import rtg.world.biome.realistic.gravityfalls.RealisticBiomeGFGravityFalls;
import rtg.world.biome.realistic.gravityfalls.RealisticBiomeGFNightmareRealm;
import rtg.world.biome.realistic.mistbiomes.RealisticBiomeMBColdMistTaiga;
import rtg.world.biome.realistic.mistbiomes.RealisticBiomeMBMistDesert;
import rtg.world.biome.realistic.mistbiomes.RealisticBiomeMBMistForest;
import rtg.world.biome.realistic.mistbiomes.RealisticBiomeMBMistMushroomIsland;
import rtg.world.biome.realistic.mistbiomes.RealisticBiomeMBMistPlains;
import rtg.world.biome.realistic.mistbiomes.RealisticBiomeMBMistSwamp;
import rtg.world.biome.realistic.mistbiomes.RealisticBiomeMBMistTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTAegeanArchipelago;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTAliumMeadow;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTAlps;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTAutumnForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTAutumnForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTAutumnTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTAutumnTaigaHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTBirchHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTBlackBeach;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTBlossomForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTBlossomForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTBlueTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTBlueTaigaHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTBrownBeach;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTClayland;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTCliffs;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdBirchForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdBirchForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdBlueTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdBlueTaigaHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdDesert;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdFlowerForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdFlowerForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdMegaBlueTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdMegaBlueTaigaHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdMegaSpruceTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdMegaSpruceTaigaHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdMegaTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdMegaTaigaHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdRoofedForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTColdRoofedForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTEstuary;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTFen;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTFungalJungle;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTFungalJungleHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTGlacier;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTGrassHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTGreenMixedForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTGreenMixedForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTGreenSwamp;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTHeath;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTHighland;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTHotspring;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTIcyTundra;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTIronBeach;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTLake;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTLowland;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTLushDesert;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMapleForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMapleForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMeadow;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMegaAutumnTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMegaAutumnTaigaHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMegaBlueTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMegaBlueTaigaHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMegaMapleForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMegaMapleForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMixedForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMixedForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMonsoonForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMoorland;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTMountains;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTOakForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTOakForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTOlivineBeach;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTOrangeBeach;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTPineland;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTPinkBeach;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTPoppyMeadow;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTPurpleBeach;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTRedSandDune;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTRockland;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTRockyTaiga;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTRoyalForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTRoyalForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTSahel;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTSandDune;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTSandyMountains;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTScrubland;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTShrubland;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTSnowdune;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTStoneFields;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTStoneMountains;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTTallOakForest;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTTallOakForestHills;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTTropicalDesert;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTTundra;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTWhiteBeach;
import rtg.world.biome.realistic.novamterram.RealisticBiomeNTWhiteOrchard;
import rtg.world.biome.realistic.odioita.RealisticBiomeODIOOrangeBlancoaForest;
import rtg.world.biome.realistic.odioita.RealisticBiomeODIOPinkBlancoaForest;
import rtg.world.biome.realistic.odioita.RealisticBiomeODIORedBlancoaForest;
import rtg.world.biome.realistic.odioita.RealisticBiomeODIOWhiteBlancoaForest;
import rtg.world.biome.realistic.plants.RealisticBiomePLANTSCrystalForest;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJAspenGrove;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJBaobabFields;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJBlossomingFields;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJBorealForest;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJOvergrownSpires;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJPraire;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJRedwoodPeaks;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJRedwoods;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJSnowyBorealForest;
import rtg.world.biome.realistic.projectvibrantjourneys.RealisticBiomePVJWillowSwamp;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWBambooMarsh;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWBirchAutumnForest;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWBlueOakForest;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWBombonaBeach;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWBurOakForest;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWEmperorRidge;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWFlatlandThicket;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWSilverBirchHills;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWSpinyForest;
import rtg.world.biome.realistic.realworld.RealisticBiomeRWSpruceMountains;
import rtg.world.biome.realistic.redwoods.RealisticBiomeREDAlpine;
import rtg.world.biome.realistic.redwoods.RealisticBiomeREDLushRedwoodForest;
import rtg.world.biome.realistic.redwoods.RealisticBiomeREDRedwoodForest;
import rtg.world.biome.realistic.redwoods.RealisticBiomeREDSnowyRainforest;
import rtg.world.biome.realistic.redwoods.RealisticBiomeREDTemperateRainforest;
import rtg.world.biome.realistic.rockhounding.RealisticBiomeRHWhiteSands;
import rtg.world.biome.realistic.spookybiomes.RealisticBiomeSBGhostlyForest;
import rtg.world.biome.realistic.spookybiomes.RealisticBiomeSBWitchwoodForest;
import rtg.world.biome.realistic.sugiforest.RealisticBiomeSFSugiForest;
import rtg.world.biome.realistic.terscraft.RealisticBiomeTERSBiomeDemonite;
import rtg.world.biome.realistic.thaumcraft.RealisticBiomeTCEerie;
import rtg.world.biome.realistic.thaumcraft.RealisticBiomeTCMagicalForest;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVAridHighland;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVAutumnalWoodedHills;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVAutumnalWoods;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVBadlands;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVBirchForestedHills;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVCliffs;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVDesertShrubland;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVForestedHills;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVGlacier;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVGlacierSpikes;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVGreenSwamp;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVLushHills;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVMeadow;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVMiniJungle;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVMountainousDesert;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVRedDesert;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVRockyPlains;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVRockyPlateau;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVSnowyConiferousForest;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVTemperateRainforest;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVThicket;
import rtg.world.biome.realistic.traverse.RealisticBiomeTRAVWoodlands;
import rtg.world.biome.realistic.vampirism.RealisticBiomeVAMPVampireForest;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaBeach;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaBirchForest;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaBirchForestHills;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaBirchForestHillsM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaBirchForestM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaColdBeach;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaColdTaiga;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaColdTaigaHills;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaColdTaigaM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaDeepOcean;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaDesert;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaDesertHills;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaDesertM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaExtremeHills;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaExtremeHillsEdge;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaExtremeHillsM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaExtremeHillsPlus;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaExtremeHillsPlusM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaFlowerForest;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaForest;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaForestHills;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaFrozenOcean;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaFrozenRiver;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaIceMountains;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaIcePlains;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaIcePlainsSpikes;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaJungle;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaJungleEdge;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaJungleEdgeM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaJungleHills;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaJungleM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMegaSpruceTaiga;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMegaTaiga;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMegaTaigaHills;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMesa;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMesaBryce;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMesaPlateau;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMesaPlateauF;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMesaPlateauFM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMesaPlateauM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMushroomIsland;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaMushroomIslandShore;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaOcean;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaPlains;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaRedwoodTaigaHills;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaRiver;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaRoofedForest;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaRoofedForestM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaSavanna;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaSavannaM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaSavannaPlateau;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaSavannaPlateauM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaStoneBeach;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaSunflowerPlains;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaSwampland;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaSwamplandM;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaTaiga;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaTaigaHills;
import rtg.world.biome.realistic.vanilla.RealisticBiomeVanillaTaigaM;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEArchipelago;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEAustralianOutback;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEBarelands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEBluff;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEBrushlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEBrushlandsHills;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEBush;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEBushlandHills;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEDenseBrushlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEExtremeSouthernAlps;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEForestedCanyonPillars;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEForestedCanyonPlateau;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEForestedCanyonPlateauM;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEGhostForest;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEGrassyFen;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEGrassyMarshland;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEHighInhabitedWoodlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEHighWoodlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEHighlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEHotBrushlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOELowSpruceWoodlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOELowTallWoodlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOELowWoodlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOELowlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOELowlandsChapparal;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEMire;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEMoorlands;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOENorthernAlps;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOENorthernSnowAlps;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOENorthernSnowSubalpineAlps;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOENorthernSubalpineAlps;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEOrchidFields;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOERedSandDunes;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOESandDunes;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOESandDunesM;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOESandDunesOasis;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOESnowRocks;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOESnowRocksMountains;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOESnowRocksPlateau;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOESouthernAlps;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOESouthernAlpsSubalpine;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEStonyReef;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOETropicalJungle;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEWastelandFlats;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEWastelandFlatsOasis;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEWastelandFlatsSlopes;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEWastelandFlatsTrees;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEWhiteOaks;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEWhiteOaksHills;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEWhiteWoods;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEWhiteWoodsHills;
import rtg.world.biome.realistic.zoesteria.RealisticBiomeZOEWoodlandsHills;


@UtilityClass
public final class BiomeInit {

    private BiomeInit() {

    }

    public static void init() {

        init_minecraft();
        if (Mods.abyssalcraft.isLoaded()) { init_abyssalcraft(); }
        if (Mods.auxbiomes.isLoaded()) { init_auxiliarybiomes(); }
        if (Mods.betteragriculture.isLoaded()) { init_betteragriculture(); }
        if (Mods.biomesoplenty.isLoaded()) { init_biomesoplenty(); }
        if (Mods.byg.isLoaded()) { init_biomesyougo(); }
        if (Mods.bionisation3.isLoaded()) { init_bionisation(); }
        if (Mods.buildcraftenergy.isLoaded()) { init_buildcraft(); }
        if (Mods.candymod.isLoaded()) { init_candymod(); }
        if (Mods.defiledlands.isLoaded()) { init_defiledlands(); }
        if (Mods.douglas_forest.isLoaded()) { init_douglasforest(); }
        if (Mods.environs.isLoaded()) { init_environs(); }
        if (Mods.explorercraft.isLoaded()) { init_explorercraft(); }
        if (Mods.floricraft.isLoaded()) { init_floricraft(); }
        if (Mods.fyrecraft.isLoaded()) { init_fyrecraft(); }
        if (Mods.gravityfalls.isLoaded()) { init_gravityfalls(); }
        if (Mods.mistbiomes.isLoaded()) { init_mistbiomes(); }
        //if (Mods.nt.isLoaded()) {
        //    init_novamterram();
        //}
        if (Mods.odioitamod.isLoaded()) { init_odioitamod(); }
        if (Mods.plants2.isLoaded()) { init_plants(); }
        if (Mods.pvj.isLoaded()) { init_pvj(); }
        if (Mods.realworld.isLoaded()) { init_realworld(); }
        if (Mods.redwoods.isLoaded()) { init_redwoods(); }
        if (Mods.rockhounding_surface.isLoaded()) { init_rockhounding(); }
        if (Mods.spookybiomes.isLoaded()) { init_spookybiomes(); }
        if (Mods.sugiforest.isLoaded()) { init_sugiforest(); }
        if (Mods.terscraft.isLoaded()) { init_terscraft(); }
        if (Mods.thaumcraft.isLoaded()) { init_thaumcraft(); }
        if (Mods.traverse.isLoaded()) { init_traverse(); }
        if (Mods.valoegheses_be.isLoaded()) { init_zoesteria(); }
        if (Mods.vampirism.isLoaded()) { init_vampirism(); }

        // This must be done after all biomes have been initialised so that they are all available.
        RTGAPI.initPatchBiome(RTGConfig.patchBiome());
    }

    public static void preInit() {
        RTGAPI.addRTGBiomes(
                RealisticBiomeBase.RiverType.NORMAL.setRTGBiome(new RealisticBiomeVanillaRiver()),
                RealisticBiomeBase.RiverType.FROZEN.setRTGBiome(new RealisticBiomeVanillaFrozenRiver()),
                RealisticBiomeBase.BeachType.NORMAL.setRTGBiome(new RealisticBiomeVanillaBeach()),
                RealisticBiomeBase.BeachType.STONE.setRTGBiome(new RealisticBiomeVanillaStoneBeach()),
                RealisticBiomeBase.BeachType.COLD.setRTGBiome(new RealisticBiomeVanillaColdBeach())
        );
    }

    private static void init_minecraft() {
        // vanilla rivers and beaches are initialised to enum fields during #preInit
        RTGAPI.addRTGBiomes(
                new RealisticBiomeVanillaBirchForest(),
                new RealisticBiomeVanillaBirchForestHills(),
                new RealisticBiomeVanillaBirchForestHillsM(),
                new RealisticBiomeVanillaBirchForestM(),
                new RealisticBiomeVanillaColdTaiga(),
                new RealisticBiomeVanillaColdTaigaHills(),
                new RealisticBiomeVanillaColdTaigaM(),
                new RealisticBiomeVanillaDeepOcean(),
                new RealisticBiomeVanillaDesert(),
                new RealisticBiomeVanillaDesertHills(),
                new RealisticBiomeVanillaDesertM(),
                new RealisticBiomeVanillaExtremeHills(),
                new RealisticBiomeVanillaExtremeHillsEdge(),
                new RealisticBiomeVanillaExtremeHillsM(),
                new RealisticBiomeVanillaExtremeHillsPlus(),
                new RealisticBiomeVanillaExtremeHillsPlusM(),
                new RealisticBiomeVanillaFlowerForest(),
                new RealisticBiomeVanillaForest(),
                new RealisticBiomeVanillaForestHills(),
                new RealisticBiomeVanillaFrozenOcean(),
                new RealisticBiomeVanillaIceMountains(),
                new RealisticBiomeVanillaIcePlains(),
                new RealisticBiomeVanillaIcePlainsSpikes(),
                new RealisticBiomeVanillaJungle(),
                new RealisticBiomeVanillaJungleEdge(),
                new RealisticBiomeVanillaJungleEdgeM(),
                new RealisticBiomeVanillaJungleHills(),
                new RealisticBiomeVanillaJungleM(),
                new RealisticBiomeVanillaMegaSpruceTaiga(),
                new RealisticBiomeVanillaMegaTaiga(),
                new RealisticBiomeVanillaMegaTaigaHills(),
                new RealisticBiomeVanillaMesa(),
                new RealisticBiomeVanillaMesaBryce(),
                new RealisticBiomeVanillaMesaPlateau(),
                new RealisticBiomeVanillaMesaPlateauF(),
                new RealisticBiomeVanillaMesaPlateauFM(),
                new RealisticBiomeVanillaMesaPlateauM(),
                new RealisticBiomeVanillaMushroomIsland(),
                new RealisticBiomeVanillaMushroomIslandShore(),
                new RealisticBiomeVanillaOcean(),
                new RealisticBiomeVanillaPlains(),
                new RealisticBiomeVanillaRedwoodTaigaHills(),
                new RealisticBiomeVanillaRoofedForest(),
                new RealisticBiomeVanillaRoofedForestM(),
                new RealisticBiomeVanillaSavanna(),
                new RealisticBiomeVanillaSavannaM(),
                new RealisticBiomeVanillaSavannaPlateau(),
                new RealisticBiomeVanillaSavannaPlateauM(),
                new RealisticBiomeVanillaSunflowerPlains(),
                new RealisticBiomeVanillaSwampland(),
                new RealisticBiomeVanillaSwamplandM(),
                new RealisticBiomeVanillaTaiga(),
                new RealisticBiomeVanillaTaigaHills(),
                new RealisticBiomeVanillaTaigaM()
        );
    }

    private static void init_abyssalcraft() {

        Biome biome;
        if ((biome = getBiome(Mods.abyssalcraft.getResourceLocation("coralium_infested_swamp"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeACCoraliumInfestedSwamp(biome));
        }
        if ((biome = getBiome(Mods.abyssalcraft.getResourceLocation("darklands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeACDarklands(biome));
        }
        if ((biome = getBiome(Mods.abyssalcraft.getResourceLocation("darklands_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeACDarklandsForest(biome));
        }
        if ((biome = getBiome(Mods.abyssalcraft.getResourceLocation("darklands_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeACDarklandsHills(biome));
        }
        if ((biome = getBiome(Mods.abyssalcraft.getResourceLocation("darklands_mountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeACDarklandsMountains(biome));
        }
        if ((biome = getBiome(Mods.abyssalcraft.getResourceLocation("darklands_plains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeACDarklandsPlains(biome));
        }
    }

    private static void init_auxiliarybiomes() {

        Biome biome;
        if ((biome = getBiome(Mods.auxbiomes.getResourceLocation("marsh"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeAUXMarsh(biome));
        }
        if ((biome = getBiome(Mods.auxbiomes.getResourceLocation("wasteland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeAUXWasteland(biome));
        }
        if ((biome = getBiome(Mods.auxbiomes.getResourceLocation("ice_wasteland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeAUXIceWasteland(biome));
        }
        if ((biome = getBiome(Mods.auxbiomes.getResourceLocation("forested_island"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeAUXForestedIsland(biome));
        }
        if ((biome = getBiome(Mods.auxbiomes.getResourceLocation("white_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeAUXWhiteForest(biome));
        }
    }

    private static void init_betteragriculture() {

        final Biome biome;
        if ((biome = getBiome(Mods.betteragriculture.getResourceLocation("farmlandbiome"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBAFarmlandBiome(biome));
        }
    }

    private static void init_biomesoplenty() {

        Biome biome;
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("alps"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPAlps(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("alps_foothills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPAlpsFoothills(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("bamboo_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPBambooForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("bayou"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPBayou(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("bog"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPBog(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("boreal_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPBorealForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("brushland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPBrushland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("chaparral"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPChaparral(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("cherry_blossom_grove"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPCherryBlossomGrove(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("cold_desert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPColdDesert(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("coniferous_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPConiferousForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("coral_reef"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPCoralReef(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("crag"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPCrag(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("dead_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPDeadForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("dead_swamp"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPDeadSwamp(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("eucalyptus_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPEucalyptusForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("fen"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPFen(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("flower_field"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPFlowerField(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("flower_island"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPFlowerIsland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("glacier"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPGlacier(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("grassland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPGrassland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("gravel_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPGravelBeach(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("grove"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPGrove(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("highland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPHighland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("kelp_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPKelpForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("land_of_lakes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPLandOfLakes(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("lavender_fields"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPLavenderFields(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("lush_desert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPLushDesert(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("lush_swamp"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPLushSwamp(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("mangrove"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPMangrove(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("maple_woods"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPMapleWoods(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("marsh"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPMarsh(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("meadow"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPMeadow(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("moor"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPMoor(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("mountain"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPMountainPeaks(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("mountain_foothills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPMountainFoothills(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("mystic_grove"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPMysticGrove(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("oasis"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPOasis(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("ominous_woods"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPOminousWoods(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("orchard"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPOrchard(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("origin_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPOriginBeach(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("origin_island"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPOriginIsland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("outback"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPOutback(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("overgrown_cliffs"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPOvergrownCliffs(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("pasture"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPPasture(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("prairie"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPPrairie(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("quagmire"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPQuagmire(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("rainforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPRainforest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("redwood_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPRedwoodForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("redwood_forest_edge"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPRedwoodForestEdge(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("sacred_springs"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPSacredSprings(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("seasonal_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPSeasonalForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("shield"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPShield(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("shrubland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPShrubland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("snowy_coniferous_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPSnowyConiferousForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("snowy_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPSnowyForest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("snowy_tundra"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPSnowyTundra(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("steppe"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPSteppe(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("temperate_rainforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPTemperateRainforest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("tropical_island"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPTropicalIsland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("tropical_rainforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPTropicalRainforest(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("tundra"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPTundra(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("volcanic_island"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPVolcanicIsland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("wasteland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPWasteland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("wetland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPWetland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("white_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPWhiteBeach(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("woodland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPWoodland(biome));
        }
        if ((biome = getBiome(Mods.biomesoplenty.getResourceLocation("xeric_shrubland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBOPXericShrubland(biome));
        }
    }

    private static void init_biomesyougo() {

        Biome biome;
        if ((biome = getBiome(Mods.byg.getResourceLocation("balliumfields"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGAlliumFields(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("balps"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGAlps(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bamaranth_fields"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGAmaranthFields(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bancientforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGAncientForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("baspenforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGAspenForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bbambooforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGBambooForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bbaobabsavanna"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGBaobabSavanna(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bbayou"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGBayou(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bbluetaiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGBlueTaiga(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bbluff_mountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGBluffMountains(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bbog"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGBog(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bborealforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGBorealForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bcanyons"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGCanyons(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bchaparrallowlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGChaparralLowlands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bcherrygrove"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGCherryGrove(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bcikaforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGCikaForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bconiferousforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGConiferousForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bcypress_swamplands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGCyprusSwampland(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bdeadsea"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGDeadSea(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bdeciduousforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGDeciduousForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bdovermoutains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGDoverMountains(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bdunes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGDunes(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bebonywoods"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGEbonyWoods(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("benchantedforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGEnchantedForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("beucalyptustropics"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGEucalyptusTropics(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bevergreentaiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGEvergreenTaiga(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bfrostyforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGFrostyForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bfungaljungle"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGFungalJungle(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bgiant_blue_spruce_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGGiantBlueSpruceTaiga(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bgiant_seasonal_spruce_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGGiantSeasonalSpruceTaiga(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bglaciers"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGGlaciers(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bglowshroombayou"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGGlowshroomBayou(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bgrasslandplateau"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGGrasslandPlateau(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bgreatlakes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGGreatLakes(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bgreatoaklowlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGGreatOakLowlands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bjacarandaforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGJacarandaForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("blushdesert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGLushDesert(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bmangrovemarshes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGMangroveMarshes(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bmapleforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGMapleForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bmeadow"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGMeadow(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("borchard"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGOrchard(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("boutback"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGOutback(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("boutlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGOutlands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bpaperbirchforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGPaperBirchForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bpine_lowlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGPineLowlands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bpinemountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGPineMountains(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bpraire"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGPraire(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bprairie"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGPrairie(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bquagmire"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGQuagmire(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("breddesert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGRedDesert(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bredoakforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGRedOakForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bred_outlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGRedOutlands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bredwoodtropics"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGRedwoodTropics(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bsavannacanopy"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSavannaCanopy(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bseasonalbirchforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSeasonalBirchForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bseasonaldeciduous"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSeasonalDeciduous(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bseasonalforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSeasonalForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bseasonaltaiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSeasonalTaiga(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bshrublands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGShrublands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bskyrishighlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSkyrisHighlands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bsnowyconiferousforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSnowyConiferousForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bsnowyevergeentaiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSnowyEvergeenTaiga(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bsnowypinemountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSnowyPineMountains(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bsonorandesert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGSonoranDesert(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bstellatapasture"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGStellataPasture(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bstone_brushlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGStoneBrushlands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("btropical_islands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGTropicalIslands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("btropicalmountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGTropicalMountains(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("btropicalrainforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGTropicalRainforest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("btundra"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGTundra(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bweepingwitchforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGWeepingWitchForest(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bwhisperingwoods"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGWhisperingWoods(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bwoodlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGWoodlands(biome));
        }
        if ((biome = getBiome(Mods.byg.getResourceLocation("bzelkovaforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBYGZelkovaForest(biome));
        }
    }

    private static void init_bionisation() {

        final Biome biome;
        if ((biome = getBiome(Mods.bionisation3.getResourceLocation("infected_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBIOInfectedForest(biome));
        }
    }

    private static void init_buildcraft() {

        Biome biome;
        if ((biome = getBiome(Mods.buildcraftenergy.getResourceLocation("oil_desert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBCDesertOilField(biome));
        }
        if ((biome = getBiome(Mods.buildcraftenergy.getResourceLocation("oil_ocean"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeBCOceanOilField(biome));
        }
    }

    private static void init_candymod() {

        Biome biome;
        if ((biome = getBiome(Mods.candymod.getResourceLocation("biome_cotton_candy"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeCWCottonCandyPlains(biome));
        }
        if ((biome = getBiome(Mods.candymod.getResourceLocation("biome_chocolate_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeCWChocolateForest(biome));
        }
        if ((biome = getBiome(Mods.candymod.getResourceLocation("biome_gummy_swamp"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeCWGummySwamp(biome));
        }
    }

    private static void init_defiledlands() {

        Biome biome;
        if ((biome = getBiome(Mods.defiledlands.getResourceLocation("desert_defiled"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeDLDesertDefiled(biome));
        }
        if ((biome = getBiome(Mods.defiledlands.getResourceLocation("plains_defiled"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeDLPlainsDefiled(biome));
        }
        if ((biome = getBiome(Mods.defiledlands.getResourceLocation("forest_tenebra"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeDLForestTenebra(biome));
        }
        if ((biome = getBiome(Mods.defiledlands.getResourceLocation("forest_vilespine"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeDLForestVilespine(biome));
        }
        if ((biome = getBiome(Mods.defiledlands.getResourceLocation("hills_defiled"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeDLHillsDefiled(biome));
        }
        if ((biome = getBiome(Mods.defiledlands.getResourceLocation("swamp_defiled"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeDLSwampDefiled(biome));
        }
        if ((biome = getBiome(Mods.defiledlands.getResourceLocation("ice_plains_defiled"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeDLIcePlainsDefiled(biome));
        }
    }

    private static void init_douglasforest() {

        Biome biome;
        if ((biome = getBiome(Mods.douglas_forest.getResourceLocation("douglas_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeDFDouglasForest(biome));
        }
        if ((biome = getBiome(Mods.douglas_forest.getResourceLocation("maple_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeDFMapleForest(biome));
        }
    }

    private static void init_environs() {

        Biome biome;
        if ((biome = getBiome(Mods.environs.getResourceLocation("extreme_jungle"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVExtremeJungle(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("moor"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVMoor(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("emerald_cliffs"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVEmeraldCliffs(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("pine_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVPineTaiga(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("pine_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVPineTaigaHills(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("silkglades"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVSilkglades(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("stone_flats"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVStoneFlats(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("end_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVEndForest(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("sparse_end_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVSparseEndForest(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("end_shrubland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVEndShrubland(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("tall_oak_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVTallOakForest(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("tall_oak_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVTallOakForestHills(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("tall_oak_wetland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVTallOakWetland(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("wasteland_spikes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVWastelandSpikes(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("wasteland_eroded"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVWastelandEroded(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("dead_oak_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVDeadOakForest(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("dead_oak_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVDeadOakForestHills(biome));
        }
        if ((biome = getBiome(Mods.environs.getResourceLocation("icy_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeENVIcyHills(biome));
        }
    }

    private static void init_explorercraft() {

        Biome biome;
        if ((biome = getBiome(Mods.explorercraft.getResourceLocation("bamboo_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeECBambooForest(biome));
        }
        if ((biome = getBiome(Mods.explorercraft.getResourceLocation("forested_mountain"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeECForestedMountain(biome));
        }
        if ((biome = getBiome(Mods.explorercraft.getResourceLocation("snowdonia"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeECSnowdonia(biome));
        }
    }

    private static void init_floricraft() {

        Biome biome;
        if ((biome = getBiome(Mods.floricraft.getResourceLocation("tulip land"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFLORITulipLand(biome));
        }
        if ((biome = getBiome(Mods.floricraft.getResourceLocation("rose land"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFLORIRoseLand(biome));
        }
    }

    private static void init_fyrecraft() {

        Biome biome;
        if ((biome = getBiome(Mods.fyrecraft.getResourceLocation("rocky wasteland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFYRERockyWasteland(biome));
        }
        if ((biome = getBiome(Mods.fyrecraft.getResourceLocation("mushroom grove"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFYREMushroomGrove(biome));
        }
        if ((biome = getBiome(Mods.fyrecraft.getResourceLocation("tropical lakes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFYRETropicalLakes(biome));
        }
        if ((biome = getBiome(Mods.fyrecraft.getResourceLocation("mega mountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFYREMegaMountains(biome));
        }
        if ((biome = getBiome(Mods.fyrecraft.getResourceLocation("red desert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFYRERedDesert(biome));
        }
        if ((biome = getBiome(Mods.fyrecraft.getResourceLocation("red desert hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFYRERedDesertHills(biome));
        }
        if ((biome = getBiome(Mods.fyrecraft.getResourceLocation("volcanic wasteland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFYREVolcanicWasteland(biome));
        }
        if ((biome = getBiome(Mods.fyrecraft.getResourceLocation("crimson grove"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeFYRECrimsonGrove(biome));
        }
    }

    private static void init_gravityfalls() {

        Biome biome;
        if ((biome = getBiome(Mods.gravityfalls.getResourceLocation("gravityfalls"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeGFGravityFalls(biome));
        }
        if ((biome = getBiome(Mods.gravityfalls.getResourceLocation("nightmarerealm"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeGFNightmareRealm(biome));
        }
    }

    private static void init_mistbiomes() {

        Biome biome;
        if ((biome = getBiome(Mods.mistbiomes.getResourceLocation("mistforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeMBMistForest(biome));
        }
        if ((biome = getBiome(Mods.mistbiomes.getResourceLocation("mistplains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeMBMistPlains(biome));
        }
        if ((biome = getBiome(Mods.mistbiomes.getResourceLocation("misttaiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeMBMistTaiga(biome));
        }
        if ((biome = getBiome(Mods.mistbiomes.getResourceLocation("mistdesert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeMBMistDesert(biome));
        }
        if ((biome = getBiome(Mods.mistbiomes.getResourceLocation("coldmisttaiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeMBColdMistTaiga(biome));
        }
        if ((biome = getBiome(Mods.mistbiomes.getResourceLocation("mistswamp"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeMBMistSwamp(biome));
        }
        if ((biome = getBiome(Mods.mistbiomes.getResourceLocation("mistymushroomisland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeMBMistMushroomIsland(biome));
        }
    }

    @SuppressWarnings("unused")
    private static void init_novamterram() {

        Biome biome;

        /*
        NT beaches generate as normal biomes in RTG worlds for some reason, so instead of just letting the biome patcher
        replace them with Plains, let's remove them from the Biome Manager completely.
         */

        final List<Biome> ntbeaches = Stream.of(
            Mods.nt.getResourceLocation("black_beach"),
            Mods.nt.getResourceLocation("brown_beach"),
            Mods.nt.getResourceLocation("iron_beach"),
            Mods.nt.getResourceLocation("olivine_beach"),
            Mods.nt.getResourceLocation("orange_beach"),
            Mods.nt.getResourceLocation("pink_beach"),
            Mods.nt.getResourceLocation("purple_beach"),
            Mods.nt.getResourceLocation("white_beach")
        )
            .map(ForgeRegistries.BIOMES::getValue)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        Arrays.stream(BiomeManager.BiomeType.values()).forEach(type ->
            Objects.requireNonNull(BiomeManager.getBiomes(type)).stream()
                .filter(biomeEntry -> ntbeaches.contains(biomeEntry.biome))
                .forEach(biomeEntry -> BiomeManager.removeBiome(type, biomeEntry)));

        if ((biome = getBiome(Mods.nt.getResourceLocation("aegean_archipelago"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTAegeanArchipelago(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("alium_meadow"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTAliumMeadow(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("alps"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTAlps(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("autumn_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTAutumnForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("autumn_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTAutumnForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("autumn_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTAutumnTaiga(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("autumn_taiga_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTAutumnTaigaHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("birch_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTBirchHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("black_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTBlackBeach(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("blossom_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTBlossomForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("blossom_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTBlossomForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("blue_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTBlueTaiga(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("blue_taiga_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTBlueTaigaHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("brown_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTBrownBeach(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("clayland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTClayland(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cliffs"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTCliffs(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_birch_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdBirchForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_birch_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdBirchForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_blue_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdBlueTaiga(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_blue_taiga_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdBlueTaigaHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_desert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdDesert(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_flower_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdFlowerForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_flower_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdFlowerForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_mega_blue_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdMegaBlueTaiga(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_mega_blue_taiga_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdMegaBlueTaigaHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_mega_spruce_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdMegaSpruceTaiga(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_mega_spruce_taiga_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdMegaSpruceTaigaHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_mega_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdMegaTaiga(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_mega_taiga_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdMegaTaigaHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_roofed_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdRoofedForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("cold_roofed_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTColdRoofedForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("estuary"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTEstuary(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("fen"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTFen(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("fungal_jungle"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTFungalJungle(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("fungal_jungle_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTFungalJungleHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("glacier"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTGlacier(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("grass_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTGrassHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("green_mixed_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTGreenMixedForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("green_mixed_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTGreenMixedForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("green_swamp"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTGreenSwamp(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("heath"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTHeath(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("highland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTHighland(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("hotspring"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTHotspring(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("icy_tundra"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTIcyTundra(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("iron_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTIronBeach(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("lake"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTLake(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("lowland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTLowland(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("lush_desert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTLushDesert(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("maple_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMapleForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("maple_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMapleForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("meadow"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMeadow(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("mega_autumn_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMegaAutumnTaiga(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("mega_autumn_taiga_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMegaAutumnTaigaHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("mega_blue_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMegaBlueTaiga(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("mega_blue_taiga_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMegaBlueTaigaHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("mega_maple_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMegaMapleForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("mega_maple_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMegaMapleForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("mixed_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMixedForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("mixed_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMixedForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("monsoon_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMonsoonForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("moorland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMoorland(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("mountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTMountains(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("oak_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTOakForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("oak_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTOakForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("olivine_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTOlivineBeach(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("orange_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTOrangeBeach(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("pineland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTPineland(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("pink_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTPinkBeach(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("poppy_meadow"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTPoppyMeadow(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("purple_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTPurpleBeach(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("red_sand_dune"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTRedSandDune(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("rockland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTRockland(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("rocky_taiga"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTRockyTaiga(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("royal_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTRoyalForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("royal_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTRoyalForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("sahel"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTSahel(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("sand_dune"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTSandDune(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("sandy_mountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTSandyMountains(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("scrubland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTScrubland(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("shrubland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTShrubland(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("snowdune"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTSnowdune(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("stone_fields"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTStoneFields(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("stone_mountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTStoneMountains(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("tall_oak_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTTallOakForest(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("tall_oak_forest_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTTallOakForestHills(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("tropical_desert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTTropicalDesert(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("tundra"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTTundra(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("white_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTWhiteBeach(biome));
        }
        if ((biome = getBiome(Mods.nt.getResourceLocation("white_orchard"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeNTWhiteOrchard(biome));
        }
    }

    private static void init_odioitamod() {

        Biome biome;
        if ((biome = getBiome(Mods.odioitamod.getResourceLocation("whiteblancoaforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeODIOWhiteBlancoaForest(biome));
        }
        if ((biome = getBiome(Mods.odioitamod.getResourceLocation("orangeblancoaforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeODIOOrangeBlancoaForest(biome));
        }
        if ((biome = getBiome(Mods.odioitamod.getResourceLocation("pinkblancoaforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeODIOPinkBlancoaForest(biome));
        }
        if ((biome = getBiome(Mods.odioitamod.getResourceLocation("redblancoaforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeODIORedBlancoaForest(biome));
        }
    }

    private static void init_plants() {

        final Biome biome;
        if ((biome = getBiome(Mods.plants2.getResourceLocation("crystal_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePLANTSCrystalForest(biome));
        }
    }

    private static void init_pvj() {

        Biome biome;
        if ((biome = getBiome(Mods.pvj.getResourceLocation("prairie"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJPraire(biome));
        }
        if ((biome = getBiome(Mods.pvj.getResourceLocation("redwoods"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJRedwoods(biome));
        }
        if ((biome = getBiome(Mods.pvj.getResourceLocation("redwood_peaks"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJRedwoodPeaks(biome));
        }
        if ((biome = getBiome(Mods.pvj.getResourceLocation("willow_swamp"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJWillowSwamp(biome));
        }
        if ((biome = getBiome(Mods.pvj.getResourceLocation("boreal_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJBorealForest(biome));
        }
        if ((biome = getBiome(Mods.pvj.getResourceLocation("snowy_boreal_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJSnowyBorealForest(biome));
        }
        if ((biome = getBiome(Mods.pvj.getResourceLocation("aspen_grove"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJAspenGrove(biome));
        }
        if ((biome = getBiome(Mods.pvj.getResourceLocation("overgrown_spires"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJOvergrownSpires(biome));
        }
        if ((biome = getBiome(Mods.pvj.getResourceLocation("blossoming_fields"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJBlossomingFields(biome));
        }
        if ((biome = getBiome(Mods.pvj.getResourceLocation("baobab_fields"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomePVJBaobabFields(biome));
        }
    }

    private static void init_realworld() {

        Biome biome;
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_bamboo_marsh"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWBambooMarsh(biome));
        }
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_birch_autumn_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWBirchAutumnForest(biome));
        }
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_blue_oak_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWBlueOakForest(biome));
        }
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_bombona_beach"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWBombonaBeach(biome));
        }
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_bur_oak_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWBurOakForest(biome));
        }
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_flatland_thicket"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWFlatlandThicket(biome));
        }
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_emperor_ridge"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWEmperorRidge(biome));
        }
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_silver_birch_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWSilverBirchHills(biome));
        }
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_spiny_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWSpinyForest(biome));
        }
        if ((biome = getBiome(Mods.realworld.getResourceLocation("rw_spruce_mountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRWSpruceMountains(biome));
        }
    }

    private static void init_redwoods() {

        Biome biome;
        if ((biome = getBiome(Mods.redwoods.getResourceLocation("redwood_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeREDRedwoodForest(biome));
        }
        if ((biome = getBiome(Mods.redwoods.getResourceLocation("lush_redwood_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeREDLushRedwoodForest(biome));
        }
        if ((biome = getBiome(Mods.redwoods.getResourceLocation("temperate_rainforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeREDTemperateRainforest(biome));
        }
        if ((biome = getBiome(Mods.redwoods.getResourceLocation("snowy_rainforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeREDSnowyRainforest(biome));
        }
        if ((biome = getBiome(Mods.redwoods.getResourceLocation("alpine"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeREDAlpine(biome));
        }
    }

    private static void init_rockhounding() {

        Biome biome;
        if ((biome = getBiome(Mods.rockhounding_surface.getResourceLocation("white_sands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeRHWhiteSands(biome));
        }
    }

    private static void init_spookybiomes() {

        Biome biome;
        if ((biome = getBiome(Mods.spookybiomes.getResourceLocation("witchwood_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeSBWitchwoodForest(biome));
        }
        if ((biome = getBiome(Mods.spookybiomes.getResourceLocation("ghostly_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeSBGhostlyForest(biome));
        }
    }

    private static void init_sugiforest() {

        final Biome biome;
        if ((biome = getBiome(Mods.sugiforest.getResourceLocation("sugi_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeSFSugiForest(biome));
        }
    }

    private static void init_terscraft() {

        final Biome biome;
        if ((biome = getBiome(Mods.terscraft.getResourceLocation("biomedemonite"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTERSBiomeDemonite(biome));
        }
    }

    private static void init_thaumcraft() {

        Biome biome;
        if ((biome = getBiome(Mods.thaumcraft.getResourceLocation("magical_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTCMagicalForest(biome));
        }
        if ((biome = getBiome(Mods.thaumcraft.getResourceLocation("eerie"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTCEerie(biome));
        }
    }

    private static void init_traverse() {

        Biome biome;
        if ((biome = getBiome(Mods.traverse.getResourceLocation("autumnal_woods"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVAutumnalWoods(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("woodlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVWoodlands(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("mini_jungle"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVMiniJungle(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("meadow"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVMeadow(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("green_swamp"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVGreenSwamp(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("red_desert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVRedDesert(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("temperate_rainforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVTemperateRainforest(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("badlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVBadlands(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("mountainous_desert"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVMountainousDesert(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("rocky_plateau"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVRockyPlateau(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("forested_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVForestedHills(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("birch_forested_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVBirchForestedHills(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("autumnal_wooded_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVAutumnalWoodedHills(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("cliffs"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVCliffs(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("glacier"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVGlacier(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("glacier_spikes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVGlacierSpikes(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("snowy_coniferous_forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVSnowyConiferousForest(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("lush_hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVLushHills(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("desert_shrubland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVDesertShrubland(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("thicket"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVThicket(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("arid_highland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVAridHighland(biome));
        }
        if ((biome = getBiome(Mods.traverse.getResourceLocation("rocky_plains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeTRAVRockyPlains(biome));
        }
    }

    private static void init_vampirism() {

        final Biome biome;
        if ((biome = getBiome(Mods.vampirism.getResourceLocation("vampireforest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeVAMPVampireForest(biome));
        }
    }

    private static void init_zoesteria() {

        Biome biome;
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("archipelago"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEArchipelago(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("australian outback"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEAustralianOutback(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("barelands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEBarelands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("bluff"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEBluff(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("brushlands hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEBrushlandsHills(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("brushlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEBrushlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("bush"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEBush(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("bushland hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEBushlandHills(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("dense brushlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEDenseBrushlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("extreme southern alps"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEExtremeSouthernAlps(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("forested canyon pillars"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEForestedCanyonPillars(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("forested canyon plateau m"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEForestedCanyonPlateauM(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("forested canyon plateau"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEForestedCanyonPlateau(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("ghost forest"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEGhostForest(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("grassy fen"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEGrassyFen(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("grassy marshland"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEGrassyMarshland(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("high inhabited woodlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEHighInhabitedWoodlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("high woodlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEHighWoodlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("highlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEHighlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("hot brushlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEHotBrushlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("low spruce woodlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOELowSpruceWoodlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("low tall woodlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOELowTallWoodlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("low woodlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOELowWoodlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("lowlands chapparal"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOELowlandsChapparal(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("lowlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOELowlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("mire"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEMire(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("moorlands"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEMoorlands(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("northern alps"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOENorthernAlps(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("northern snow alps"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOENorthernSnowAlps(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("northern snow subalpine alps"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOENorthernSnowSubalpineAlps(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("northern subalpine alps"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOENorthernSubalpineAlps(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("orchid fields"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEOrchidFields(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("red sand dunes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOERedSandDunes(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("sand dunes m"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOESandDunesM(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("sand dunes oasis"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOESandDunesOasis(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("sand dunes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOESandDunes(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("snow rocks mountains"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOESnowRocksMountains(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("snow rocks plateau"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOESnowRocksPlateau(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("snow rocks"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOESnowRocks(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("southern alps subalpine"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOESouthernAlpsSubalpine(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("southern alps"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOESouthernAlps(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("stony reef"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEStonyReef(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("tropical jungle"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOETropicalJungle(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("wasteland flats oasis"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEWastelandFlatsOasis(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("wasteland flats slopes"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEWastelandFlatsSlopes(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("wasteland flats trees"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEWastelandFlatsTrees(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("wasteland flats"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEWastelandFlats(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("white oaks hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEWhiteOaksHills(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("white oaks"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEWhiteOaks(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("white woods hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEWhiteWoodsHills(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("white woods"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEWhiteWoods(biome));
        }
        if ((biome = getBiome(Mods.valoegheses_be.getResourceLocation("woodlands hills"))) != null) {
            RTGAPI.addRTGBiomes(new RealisticBiomeZOEWoodlandsHills(biome));
        }
    }

    @Nullable
    private static Biome getBiome(final ResourceLocation resloc)
    {
        return ForgeRegistries.BIOMES.getValue(resloc);
    }
}
