package rtg.world.biome;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import rtg.api.RTGAPI;
import rtg.api.util.CircularSearchCreator;
import rtg.api.util.Logger;
import rtg.api.util.storage.SparseList;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;
import rtg.world.gen.ChunkLandscape;

import java.util.*;


public final class BiomeAnalyzer {
    //Default anvil storage uses a single byte for biome data but with JustEnoughIDs, the biome ID field is expanded
    //to an integer.
    private static final int NO_BIOME = -1;
    //biome flag constants
    private static final int RIVER_FLAG = 1;
    private static final int OCEAN_FLAG = 2;
    private static final int SWAMP_FLAG = 4;
    private static final int BEACH_FLAG = 8;
    private static final int LAND_FLAG = 16;
    //biomeID -> bitField for biomes [ RIVER_BIOME | OCEAN_BIOME | SWAMP_BIOME | BEACH_BIOME | LAND_BIOME ]
    private final List<Integer> biomeIDs = new SparseList<>();
    private final List<Integer> preferredBeach = new SparseList<>();
    //hardcode these because they are world-persistent
    private final IRealisticBiome scenicLakeBiome = RTGAPI.getRTGBiome(Biomes.RIVER);
    private final IRealisticBiome scenicFrozenLakeBiome = RTGAPI.getRTGBiome(Biomes.FROZEN_RIVER);
    private SmoothingSearchStatus beachSearch;
    private SmoothingSearchStatus landSearch;
    private SmoothingSearchStatus oceanSearch;

    public BiomeAnalyzer() {
        initBiomes();
        setupBeachesForBiomes();
        setSearches();
    }

    public int[] xyinverted() {
        int[] result = new int[256];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                result[i * 16 + j] = j * 16 + i;
            }
        }

        for (int i = 0; i < 256; i++) {
            if (result[result[i]] != i) {
                throw new RuntimeException(i + " " + result[i] + " " + result[result[i]]);
            }
        }

        return result;
    }

    private void initBiomes() {

        Logger.rtgDebug("Initialising biomes.");

        for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {

            int id = Biome.getIdForBiome(biome);
            Integer biomeFlags = biomeIDs.get(id);
            biomeFlags = (biomeFlags == null ? 0 : biomeFlags);

            if (BiomeDictionary.hasType(biome, Type.RIVER)) {
                biomeFlags |= RIVER_FLAG;
                Logger.rtgDebug("Assigning " + biome.getRegistryName() + " to river flag.");
            } else if (BiomeDictionary.hasType(biome, Type.OCEAN)) {
                biomeFlags |= OCEAN_FLAG;
                Logger.rtgDebug("Assigning " + biome.getRegistryName() + " to ocean flag.");
            } else if (BiomeDictionary.hasType(biome, Type.SWAMP)) {
                biomeFlags |= SWAMP_FLAG;
                Logger.rtgDebug("Assigning " + biome.getRegistryName() + " to swamp flag.");
            } else if (BiomeDictionary.hasType(biome, Type.BEACH)) {
                biomeFlags |= BEACH_FLAG;
                Logger.rtgDebug("Assigning " + biome.getRegistryName() + " to beach flag.");
            } else {
                biomeFlags |= LAND_FLAG;
                Logger.rtgDebug("Assigning " + biome.getRegistryName() + " to land flag.");
            }

            biomeIDs.set(id, biomeFlags);
        }
    }

    private void setupBeachesForBiomes() {
        for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
            if (biome != null) {
                final int id = Biome.getIdForBiome(biome);
                final Map.Entry<Biome, IRealisticBiome> realisticBiome = RTGAPI.RTG_BIOMES.get(id);
                if (realisticBiome != null) {
                    preferredBeach.set(id, realisticBiome.getValue().getBeachBiome().baseBiomeId());
                }
            }
        }
    }

    public void newRepair(final Biome[] genLayerBiomes, final int[] biomeNeighborhood, final ChunkLandscape landscape) {

        final IRealisticBiome [] jitteredBiomes = landscape.biome;
        final float[] noise = landscape.noise;
        final float[] riverStrength = landscape.river;

        IRealisticBiome realisticBiome;
        int realisticBiomeId;

        // currently, just stuffs the genLayer into the jitter;
        for (int i = 0; i < genLayerBiomes.length; i++) {

            realisticBiome = RTGAPI.getRTGBiome(genLayerBiomes[i]);
            realisticBiomeId = realisticBiome.baseBiomeId();

            boolean canBeRiver = riverStrength[i] > 0.7;

            if (noise[i] > 61.5) {
                // replace
                jitteredBiomes[i] = realisticBiome;
            } else {
                // check for river
                final int biomeFlags = biomeIDs.get(realisticBiomeId);
                if (canBeRiver && (biomeFlags & OCEAN_FLAG) == 0 && (biomeFlags & SWAMP_FLAG) == 0) {
                    // make river
                    jitteredBiomes[i] = realisticBiome.getRiverBiome();
                } else {
                    // replace
                    jitteredBiomes[i] =  realisticBiome;
                }
            }
        }

        // put beaches on shores
        beachSearch.setNotHunted();
        beachSearch.setAbsent();
        float beachTop = 64.5f;
        for (int i = 0; i < genLayerBiomes.length; i++) {
            if (beachSearch.isAbsent()) {
                break; //no point
            }
            float beachBottom = 61.5f;
            if (noise[i] < beachBottom || noise[i] > riverAdjusted(beachTop, riverStrength[i])) {
                continue;// this block isn't beach level
            }
            int biomeID = Biome.getIdForBiome(jitteredBiomes[i].baseBiome());
            if ((biomeIDs.get(biomeID) & SWAMP_FLAG) != 0) {
                continue;// swamps are acceptable at beach level
            }
            if (beachSearch.isNotHunted()) {
                beachSearch.hunt(biomeNeighborhood);
                landSearch.hunt(biomeNeighborhood);
            }
            int foundBiome = beachSearch.biomeIDs.get(i);
            if (foundBiome != NO_BIOME) {
                int nearestLandBiome = landSearch.biomeIDs.get(i);
                if (nearestLandBiome > -1) {
                    foundBiome = preferredBeach.get(nearestLandBiome);
                }
                jitteredBiomes[i] = RTGAPI.getRTGBiome(foundBiome);
            }
        }

        // put land higher up;
        landSearch.setAbsent();
        landSearch.setNotHunted();
        for (int i = 0; i < genLayerBiomes.length; i++) {
            if (landSearch.isAbsent() && beachSearch.isAbsent()) {
                break; //no point
            }
            // skip if this block isn't above beach level, adjusted for river effect to prevent abrupt beach stops
            if (noise[i] < riverAdjusted(beachTop, riverStrength[i])) {
                continue;
            }
            int biomeID = Biome.getIdForBiome(jitteredBiomes[i].baseBiome());
            final int biomeFlags = biomeIDs.get(biomeID);
            // already land
            if ((biomeFlags & LAND_FLAG) != 0) {
                continue;
            }
            // swamps are acceptable above water
            if ((biomeFlags & SWAMP_FLAG) != 0) {
                continue;
            }
            if (landSearch.isNotHunted()) {
                landSearch.hunt(biomeNeighborhood);
            }
            int foundBiome = landSearch.biomeIDs.get(i);

            if (foundBiome == NO_BIOME) {
                // no land found; try for a beach
                if (beachSearch.isNotHunted()) {
                    beachSearch.hunt(biomeNeighborhood);
                }
                foundBiome = beachSearch.biomeIDs.get(i);
            }

            if (foundBiome != NO_BIOME) {
                jitteredBiomes[i] = RTGAPI.getRTGBiome(foundBiome);
            }
        }

        // put ocean below sea level
        oceanSearch.setAbsent();
        oceanSearch.setNotHunted();
        for (int i = 0; i < genLayerBiomes.length; i++) {
            if (oceanSearch.isAbsent()) {
                break; //no point
            }
            float oceanTop = 61.5f;
            if (noise[i] > oceanTop) {
                continue;// too height
            }
            int biomeID = Biome.getIdForBiome(jitteredBiomes[i].baseBiome());
            final int biomeFlags = biomeIDs.get(biomeID);
            if ((biomeFlags & OCEAN_FLAG) != 0) {
                continue;// obviously ocean is OK
            }
            if ((biomeFlags & SWAMP_FLAG) != 0) {
                continue;// swamps are acceptable
            }
            if ((biomeFlags & RIVER_FLAG) != 0) {
                continue;// rivers stay rivers
            }
            if (oceanSearch.isNotHunted()) {
                oceanSearch.hunt(biomeNeighborhood);
            }
            int foundBiome = oceanSearch.biomeIDs.get(i);

            if (foundBiome != NO_BIOME) {
                jitteredBiomes[i] = RTGAPI.getRTGBiome(foundBiome);
            }
        }
        // convert remainder below sea level to lake biome
        for (int i = 0; i < genLayerBiomes.length; i++) {
            int biomeID = Biome.getIdForBiome(jitteredBiomes[i].baseBiome());
            final int biomeFlags = biomeIDs.get(biomeID);
            if (noise[i] <= 61.5 && (biomeFlags & RIVER_FLAG) == 0) {
                // check for river
                if ((biomeFlags & OCEAN_FLAG) == 0 && (biomeFlags & SWAMP_FLAG) == 0 && (biomeFlags & BEACH_FLAG) == 0) {
                    int riverReplacementID = jitteredBiomes[i].getRiverBiome().baseBiomeId(); // make river
                    if (riverReplacementID == Biome.getIdForBiome(Biomes.FROZEN_RIVER)) {
                        jitteredBiomes[i] = scenicFrozenLakeBiome;
                    } else {
                        jitteredBiomes[i] = scenicLakeBiome;
                    }
                }
            }
        }
    }

    private List<Boolean> filterForFlag(final int flag) {
        final List<Boolean> result = new SparseList<>();
        for (Integer biomeId : biomeIDs) {
            if (biomeId != null) {
                result.set(biomeId, (biomeId & flag) != 0);
            }
        }
        return result;
    }

    private void setSearches() {
        beachSearch = new SmoothingSearchStatus(filterForFlag(BEACH_FLAG));
        landSearch = new SmoothingSearchStatus(filterForFlag(LAND_FLAG));
        oceanSearch = new SmoothingSearchStatus(filterForFlag(OCEAN_FLAG));
    }

    private float riverAdjusted(float top, float river) {
        if (river >= 1) {
            return top;
        }
        float erodedRiver = river / RTGWorld.ACTUAL_RIVER_PROPORTION;
        if (erodedRiver <= 1f) {
            top = top * (1 - erodedRiver) + 62f * erodedRiver;
        }
        top = top * (1 - river) + 62f * river;
        return top;
    }

    private static final class SmoothingSearchStatus {

        private final int upperLeftFinding = 0;
        private final int upperRightFinding = 3;
        private final int lowerLeftFinding = 1;
        private final int lowerRightFinding = 4;
        private final int[] quadrantBiome = new int[4];
        private final float[] quadrantBiomeWeighting = new float[4];
        private final List<Boolean> desired;
        private final int[] findings = new int[3 * 3];
        // weightings are part of a system to generate some variability in repaired chunks weighting is
        // based on how long the search went on (so quasi-pseudo-random, based on direction plus distance)
        private final float[] weightings = new float[3 * 3];
        public List<Integer> biomeIDs = new SparseList<>();
        private boolean absent = false;
        private boolean notHunted;
        private int arraySize;
        private int[] pattern;
        private int biomeCount;

        private SmoothingSearchStatus(final List<Boolean> desired) {
            this.desired = desired;
        }

        private int size() {
            return 3;
        }

        private void hunt(int[] biomeNeighborhood) {
            // 0,0 in the chunk is 9,9 int the array ; 8,8 is 10,10 and is treated as the center
            clear();
            int oldArraySize = arraySize;
            arraySize = (int) Math.sqrt(biomeNeighborhood.length);
            if (arraySize * arraySize != biomeNeighborhood.length) {
                throw new RuntimeException("non-square array");
            }

            if (arraySize != oldArraySize) {
                pattern = new CircularSearchCreator().pattern(arraySize / 2f - 1, arraySize);
            }

            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    search(xOffset, zOffset, biomeNeighborhood);
                }
            }
            // calling a routine because it gets too indented otherwise
            smoothBiomes();
        }

        private void search(int xOffset, int zOffset, int[] biomeNeighborhood) {
            int offset = xOffset * arraySize + zOffset;
            int location = (xOffset + 1) * size() + zOffset + 1;
            // set to failed search, which sticks if nothing is found
            findings[location] = NO_BIOME;
            weightings[location] = 2f;
            for (int i = 0; i < pattern.length; i++) {
                int biome = biomeNeighborhood[pattern[i] + offset];
                Boolean d = desired.get(biome);
                d = (d != null && d);
                if (d && desired.get(biome)) {
                    findings[location] = biome;
                    weightings[location] = (float) Math.sqrt(pattern.length) - (float) Math.sqrt(i) + 2f;
                    break;
                }
            }
        }

        private void smoothBiomes() {
            // more sophisticated version offsets into findings and biomes upper left
            smoothQuadrant(biomeIndex(0, 0), upperLeftFinding);
            smoothQuadrant(biomeIndex(8, 0), upperRightFinding);
            smoothQuadrant(biomeIndex(0, 8), lowerLeftFinding);
            smoothQuadrant(biomeIndex(8, 8), lowerRightFinding);
        }

        private void smoothQuadrant(int biomesOffset, int findingsOffset) {
            int upperLeft = findings[upperLeftFinding + findingsOffset];
            int upperRight = findings[upperRightFinding + findingsOffset];
            int lowerLeft = findings[lowerLeftFinding + findingsOffset];
            int lowerRight = findings[lowerRightFinding + findingsOffset];
            // check for uniformity
            if ((upperLeft == upperRight) && (upperLeft == lowerLeft) && (upperLeft == lowerRight)) {
                // everything's the same; uniform fill;
                for (int x = 0; x < 8; x++) {
                    for (int z = 0; z < 8; z++) {
                        biomeIDs.set(biomeIndex(x, z) + biomesOffset, upperLeft);
                    }
                }
                return;
            }
            // not all the same; we have to work;
            biomeCount = 0;
            addBiome(upperLeft);
            addBiome(upperRight);
            addBiome(lowerLeft);
            addBiome(lowerRight);
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    addBiome(lowerRight);
                    for (int i = 0; i < 4; i++) {
                        quadrantBiomeWeighting[i] = 0;
                    }
                    // weighting strategy: weights go down as you move away from the corner.
                    // they go to 0 on the far edges so only the points on the edge have effects there
                    // for continuity with the next quadrant
                    addWeight(upperLeft, weightings[upperLeftFinding + findingsOffset] * (7 - x) * (7 - z));
                    addWeight(upperRight, weightings[upperRightFinding + findingsOffset] * x * (7 - z));
                    addWeight(lowerLeft, weightings[lowerLeftFinding + findingsOffset] * (7 - x) * z);
                    addWeight(lowerRight, weightings[lowerRightFinding + findingsOffset] * x * z);
                    biomeIDs.set(biomeIndex(x, z) + biomesOffset, preferredBiome());
                }
            }
        }

        private void addBiome(int biome) {
            for (int i = 0; i < biomeCount; i++) {
                if (biome == quadrantBiome[i]) {
                    return;
                }
            }
            // not there, add
            quadrantBiome[biomeCount++] = biome;
        }

        private void addWeight(int biome, float weight) {
            for (int i = 0; i < biomeCount; i++) {
                if (biome == quadrantBiome[i]) {
                    quadrantBiomeWeighting[i] += weight;
                    return;
                }
            }
        }

        private int preferredBiome() {
            float bestWeight = 0;
            int result = -2;
            for (int i = 0; i < biomeCount; i++) {
                if (quadrantBiomeWeighting[i] > bestWeight) {
                    bestWeight = quadrantBiomeWeighting[i];
                    result = quadrantBiome[i];
                }
            }
            return result;
        }

        private int biomeIndex(int x, int z) {
            return x * 16 + z;
        }

        private void clear() {
            Arrays.fill(findings, -1);
        }

        private boolean isAbsent() {
            return absent;
        }

        private void setAbsent() {
            this.absent = false;
        }

        private boolean isNotHunted() {
            return notHunted;
        }

        private void setNotHunted() {
            this.notHunted = true;
        }
    }
}