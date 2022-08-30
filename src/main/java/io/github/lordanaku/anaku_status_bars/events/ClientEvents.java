package io.github.lordanaku.anaku_status_bars.events;

import io.github.lordanaku.anaku_status_bars.AnakuStatusBarsCore;
import io.github.lordanaku.anaku_status_bars.screen.gui.Config;
import io.github.lordanaku.anaku_status_bars.screen.hud.RenderHudElements;
import io.github.lordanaku.anaku_status_bars.utils.KeyBinding;
import io.github.lordanaku.anaku_status_bars.utils.Settings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = AnakuStatusBarsCore.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void renderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
            if (!Settings.shouldUseVanillaTextures){
                ArrayList<NamedGuiOverlay> overlays = new ArrayList<>(Arrays.asList(
                        VanillaGuiOverlay.PLAYER_HEALTH.type(),
                        VanillaGuiOverlay.ARMOR_LEVEL.type(),
                        VanillaGuiOverlay.FOOD_LEVEL.type(),
                        VanillaGuiOverlay.AIR_LEVEL.type(),
                        VanillaGuiOverlay.MOUNT_HEALTH.type()));

                if (overlays.contains(event.getOverlay())) {
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.KEY_BINDING_MENU_KEY.consumeClick()) {
                Minecraft.getInstance().setScreen(Config.CreateConfigScreen(Minecraft.getInstance().screen));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = AnakuStatusBarsCore.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("render_hud_elements", RenderHudElements.RENDER_HUD_ELEMENTS);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.KEY_BINDING_MENU_KEY);
        }
    }
}
