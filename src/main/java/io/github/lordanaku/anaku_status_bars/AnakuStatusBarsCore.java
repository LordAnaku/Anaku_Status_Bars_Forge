package io.github.lordanaku.anaku_status_bars;

import com.mojang.logging.LogUtils;
import io.github.lordanaku.anaku_status_bars.network.SyncHandler;
import io.github.lordanaku.anaku_status_bars.screen.gui.ConfigFileHandler;
import io.github.lordanaku.anaku_status_bars.screen.gui.ConfigModMenu;
import io.github.lordanaku.anaku_status_bars.screen.hud.RenderHudHelper;
import io.github.lordanaku.anaku_status_bars.screen.hud.elements.*;
import io.github.lordanaku.anaku_status_bars.utils.Settings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(AnakuStatusBarsCore.MOD_ID)
public class AnakuStatusBarsCore {

    public static final String MOD_ID = "anaku_status_bars";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AnakuStatusBarsCore() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        SyncHandler.init();
        /* * register Settings * */
        Settings.registerSettings();
        /* * register Config * */
        ConfigModMenu.registerModsPage();
        /* * Read Settings * */
        ConfigFileHandler.readFromConfig();
        /* * Register Hud Elements * */
        RenderHudHelper.registerHudElements(new HealthHudElement(), new FoodHudElement(), new ArmorHudElement(), new AirHudElement(), new MountHealthHudElement());
        RenderHudHelper.setupHudElements();
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
