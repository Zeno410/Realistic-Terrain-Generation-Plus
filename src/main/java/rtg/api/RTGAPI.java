package rtg.api;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Level;
import rtg.RTG;
import rtg.api.util.Logger;
import rtg.api.util.UtilityClass;
import rtg.api.util.storage.SparseList;
import rtg.api.world.biome.IRealisticBiome;
import rtg.world.WorldTypeRTG;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.*;


@UtilityClass
public final class RTGAPI {

    public static final String   RTG_API_ID       = "rtgapi";
    public static final String   VERSION          = "@API_VERSION@";
    public static final String   RTG_MOD_ID       = "rtg";
    public static final String   RTG_WORLDTYPE_ID = "RTG";
    public static final SparseList<Map.Entry<Biome, IRealisticBiome>> RTG_BIOMES = new SparseList<>();
    private static final Set<DimensionType> ALLOWED_DIMENSION_TYPES = new ObjectArraySet<>();
    private static boolean rtgBiomesLocked = false;
    private static Path configPath;
    private static Path biomeConfigPath;
    private static IRealisticBiome patchBiome;
    private static IBlockState
            shadowStoneBlock = null,
            shadowDesertBlock = null;

    private RTGAPI() {

    }

    public static void lockRtgBiomes() {
        rtgBiomesLocked = true;
    }

    public static Path getConfigPath() {
        return configPath;
    }

    public static void setConfigPath(Path path) {
        if (configPath == null) {
            configPath = path;
            biomeConfigPath = path.resolve("biomes");
        }
    }

    public static Path getBiomeConfigPath() {
        return biomeConfigPath;
    }

    public static boolean checkWorldType(WorldType worldType) {
        return WorldTypeRTG.getInstance().equals(worldType);
    }

    public static void addAllowedDimensionType(DimensionType dimType) {
        ALLOWED_DIMENSION_TYPES.add(dimType);
    }

    public static void removeAllowedDimensionType(DimensionType dimType) {
        ALLOWED_DIMENSION_TYPES.remove(dimType);
    }

    public static boolean isAllowedDimensionType(DimensionType dimType) {
        return ALLOWED_DIMENSION_TYPES.contains(dimType) || dimType.getSuffix().equals("_rtg") || dimType.name().startsWith("jed_surface");
    }

    public static boolean isAllowedDimensionType(int dimId) {
        DimensionType type = (DimensionManager.isDimensionRegistered(dimId)) ? DimensionManager.getProviderType(dimId) : null;
        return type != null && isAllowedDimensionType(type);
    }

    public static IRealisticBiome getRTGBiome(@Nonnull Biome biome) {
        final Map.Entry<Biome, IRealisticBiome> entry = RTG_BIOMES.get(Biome.getIdForBiome(biome));
        if (entry != null) {
            return entry.getValue();
        }
        return patchBiome;
    }

    public static IRealisticBiome getRTGBiome(int biomeId) {
        final Map.Entry<Biome, IRealisticBiome> entry = RTG_BIOMES.get(biomeId);
        if (entry != null) {
            return entry.getValue();
        }
        return patchBiome;
    }

    public static void addRTGBiomes(IRealisticBiome... biomes) {
        if (!rtgBiomesLocked) {
            for (final IRealisticBiome biome : biomes) {
                final Biome baseBiome = biome.baseBiome();
                RTG_BIOMES.set(Biome.getIdForBiome(baseBiome), new AbstractMap.SimpleEntry<>(baseBiome, biome));
            }
        }
    }   

    public static void initPatchBiome(Biome biome) {
        IRealisticBiome rtgBiome = getRTGBiome(biome);
        if (rtgBiome == null) {
            Logger.error("Erroneous patch biome set in config: {} (no RTG version), Using default.", biome.getRegistryName());
            rtgBiome = Objects.requireNonNull(getRTGBiome(Biomes.PLAINS), "Cannot find an RTG version of minecraft:plains. This should be impossible.");
        }
        Logger.debug("Setting patch biome to: {}", rtgBiome.baseBiomeResLoc());
        patchBiome = rtgBiome;
    }

    public static void setShadowBlocks(@Nullable IBlockState stone, @Nullable IBlockState desert) {
        if (shadowStoneBlock == null) {
            shadowStoneBlock = stone != null ? stone : Blocks.STONE.getDefaultState();
        }
        if (shadowDesertBlock == null) {
            shadowDesertBlock = desert != null ? desert : Blocks.SAND.getDefaultState();
        }
    }

    public static IBlockState getShadowStoneBlock() {
        return shadowStoneBlock;
    }

    public static IBlockState getShadowDesertBlock() {
        return shadowDesertBlock;
    }

    public static int getMaxBiomeIDs() {
        if (Loader.isModLoaded("jeid")) {
            return 65536;
        } else {
            return 256;
        }
    }

    public static void dumpGenLayerStack(@Nonnull final GenLayer layersIn, final Level level) {
        final Collection<String> initialStack = Lists.newArrayList();
        final Collection<String> riverStack = Lists.newArrayList();
        final Collection<String> biomeStack = Lists.newArrayList();

        int count = 0;
        int biomecount;
        int rivercount;
        GenLayer layer = layersIn;
        initialStack.add(String.format("%s. %s", ++count, layer.getClass().getName()));
        while (layer.parent != null) {
            initialStack.add(String.format("%s. %s", ++count, layer.parent.getClass().getName()));
            layer = layer.parent;
        }
        if (layer instanceof GenLayerRiverMix) {
            biomecount = rivercount = count;
            GenLayer biomeLayer = ((GenLayerRiverMix) layer).biomePatternGeneratorChain;
            while (biomeLayer.parent != null) {
                biomeStack.add(String.format("%s. %s", ++biomecount, biomeLayer.parent.getClass().getName()));
                biomeLayer = biomeLayer.parent;
            }
            GenLayer riverLayer = ((GenLayerRiverMix) layer).riverPatternGeneratorChain;
            while (riverLayer.parent != null) {
                riverStack.add(String.format("%s. %s", ++rivercount, riverLayer.parent.getClass().getName()));
                riverLayer = riverLayer.parent;
            }
        }

        if (biomeStack.isEmpty() || riverStack.isEmpty()) {
            Logger.log(level, "\nGenLayer stack:\n{}", String.join("\n  ", initialStack));
        } else {
            Logger.log(level, "\nInitial GenLayer stack:\n  {}\nBiome GenLayer stack:\n  {}\nRiver GenLayer stack:\n  {}",
                    String.join("\n  ", initialStack), String.join("\n  ", biomeStack), String.join("\n  ", riverStack));
        }
    }
}
