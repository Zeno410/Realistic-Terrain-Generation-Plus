package rtg;

import net.minecraft.client.Minecraft;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import rtg.RTGConfig.RTGGuiConfigFactory;
import rtg.api.RTGAPI;
import rtg.api.util.PlateauUtil;
import rtg.event.EventHandlerCommon;
import rtg.init.BiomeInit;
import rtg.server.RTGCommandTree;
import rtg.compat.ModCompat;
import rtg.world.WorldTypeRTG;

import java.nio.file.Paths;


@SuppressWarnings({"unused", "WeakerAccess"})
@Mod(
        modid = RTG.MOD_ID,
        name = "RTG Unofficial",
        version = RTG.VERSION,
        dependencies = "required-after:forge@[14.23.5.2847,);after:biomesoplenty@[7.0.1.2441,);after:traverse@[1.6.0,2.0.0)",
        guiFactory = RTGGuiConfigFactory.LOCATION,
        acceptableRemoteVersions = "*"
)
public final class RTG {

    public static final String MOD_ID = RTGAPI.RTG_MOD_ID;
    public static final String VERSION = "1.1.0";
    private static final RTG instance = new RTG();

    private static boolean DISABLE_DECORATIONS;
    private static boolean DISABLE_SURFACES;
    @SidedProxy
    private static RTGProxy proxy;

    private RTG() {
    }

    @Mod.InstanceFactory
    public static RTG getInstance() {
        return instance;
    }

    public static RTGProxy getProxy() {
        return proxy;
    }

    public static boolean decorationsDisable() {
        return DISABLE_DECORATIONS;
    }

    public static boolean surfacesDisabled() {
        return DISABLE_SURFACES;
    }

    @Mod.EventHandler
    public void initPre(FMLPreInitializationEvent event) {

        DISABLE_DECORATIONS = System.getProperties().containsKey("rtg.disableDecorations");
        DISABLE_SURFACES = System.getProperties().containsKey("rtg.disableSurfaces");

        RTGAPI.setConfigPath(Paths.get(event.getModConfigurationDirectory().getPath(), RTG.MOD_ID.toUpperCase()));
        RTGConfig.init(event);

        RTGAPI.addAllowedDimensionType(DimensionType.OVERWORLD);

        WorldTypeRTG.init();
        ModCompat.init();

        BiomeInit.preInit();// initialise river and beach biomes
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        EventHandlerCommon.init();// TERRAIN_GEN_BUS, ORE_GEN_BUS
    }

    @Mod.EventHandler
    public void initPost(FMLPostInitializationEvent event) {
        BiomeInit.init();// initialise all biomes supported internally
        ModCompat.doBiomeCheck();
        PlateauUtil.init();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        RTGAPI.lockRtgBiomes();// We don't want the biome map to change after this point, so we lock it.
    }

    @Mod.EventHandler
    public void serverStarting(final FMLServerStartingEvent event) {
        event.registerServerCommand(new RTGCommandTree());
    }

    public interface RTGProxy {
        void displayCustomizeWorldScreen(net.minecraft.client.gui.GuiCreateWorld guiCreateWorld);
    }

    public static final class ClientProxy implements RTGProxy {
        @Override
        public void displayCustomizeWorldScreen(net.minecraft.client.gui.GuiCreateWorld guiCreateWorld) {
            Minecraft.getMinecraft().displayGuiScreen(new rtg.client.GuiCustomizeWorldScreenRTG(guiCreateWorld, guiCreateWorld.chunkProviderSettingsJson));
        }
    }

    public static class ServerProxy implements RTGProxy {
        @Override
        public void displayCustomizeWorldScreen(net.minecraft.client.gui.GuiCreateWorld guiCreateWorld) {
        }
    }
}
