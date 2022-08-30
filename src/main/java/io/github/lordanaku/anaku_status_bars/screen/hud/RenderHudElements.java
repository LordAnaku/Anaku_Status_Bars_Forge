package io.github.lordanaku.anaku_status_bars.screen.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lordanaku.anaku_status_bars.utils.Settings;
import io.github.lordanaku.anaku_status_bars.utils.interfaces.IHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@SuppressWarnings("unused")
public class RenderHudElements extends ForgeGui {
    private static int yModIncrement = 10;
    private static int posXLeftOffset = 91;
    private static int posXRightOffset = 10;

    public static final IGuiOverlay RENDER_HUD_ELEMENTS = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;

        RenderHudHelper.setPosX(true, (x - posXLeftOffset) + Settings.positionSettings.get("left_x_offset"));
        RenderHudHelper.setPosX(false, (x + posXRightOffset) + Settings.positionSettings.get("right_x_offset"));
        RenderHudHelper.setPosY(screenHeight);

        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderHudHelper.resetIncrements();

        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        if (!player.isSpectator()){
            if (!player.isCreative()) {
                for (IHudElement hudElement : RenderHudHelper.getHudElementRegistry()) {
                    if (hudElement.shouldRender()) {
                        hudElement.renderBar(poseStack);
                        if (hudElement.shouldRenderIcon()) {
                            hudElement.renderIcon(poseStack);
                        }
                        RenderHudHelper.setPosYMod(hudElement.getSide(), -yModIncrement);
                    }
                }
            }
        }
    });

    public RenderHudElements(Minecraft mc) {
        super(mc);
    }

    /**
     * This method is used to set the amount the elements will offset upwards for every element rendered.
     * @param yModIncrement The amount to increment the yMod by.
     */
    public static void setYModIncrement(int yModIncrement) {
        RenderHudElements.yModIncrement = yModIncrement;
    }

    /**
     * This method is used to set the amount the elements will offset to the left.
     */
    public static void setPosXLeftOffset(int posXLeftOffset) {
        RenderHudElements.posXLeftOffset = posXLeftOffset;
    }

    /**
     * This method is used to set the amount the elements will offset to the right.
     */
    public static void setPosXRightOffset(int posXRightOffset) {
        RenderHudElements.posXRightOffset = posXRightOffset;
    }
}
