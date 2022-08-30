package io.github.lordanaku.anaku_status_bars.api;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lordanaku.anaku_status_bars.screen.hud.RenderHudHelper;
import io.github.lordanaku.anaku_status_bars.utils.ColorUtils;
import io.github.lordanaku.anaku_status_bars.utils.TextureRecords;
import io.github.lordanaku.anaku_status_bars.utils.records.TextureRecord;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class RenderHudFunctions {


    /**
     * Draws the defalut background bar for the HUD.
     * @param side - true if the bar is on the left side of the screen, false if on the right side.
     * @param posYMod - the amount you want to add to the base -40 y position.
     * @param textureRecord - the texture record for the bar.
     */
    public static void drawDefaultBar(PoseStack poseStack, boolean side, int posYMod, TextureRecord textureRecord) {
        RenderSystem.setShaderTexture(0, textureRecord.texture());
        int finalSide = (side) ? RenderHudHelper.getPosX(true) : RenderHudHelper.getPosX(false);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GuiComponent.blit(poseStack, finalSide, RenderHudHelper.getPosY() + posYMod,
                textureRecord.startX(), textureRecord.startY(),
                textureRecord.width(), textureRecord.height(),
                textureRecord.maxWidth(), textureRecord.maxHeight());
    }

    /**
     * Draws an overlay on the bar for an alternative way of showing info.
     * @param side - true if the bar is on the left side of the screen, false if on the right side.
     * @param posYMod - the amount you want to add to the base -40 y position.
     * @param textureRecord - the texture record for the bar.
     * @param progress - the progress of the bar.
     * @param alpha - the color of the bar. (Hex Value)
     */
    public static void drawExhaustBar(PoseStack poseStack, boolean side, int posYMod, TextureRecord textureRecord, int progress, float alpha) {
        RenderSystem.setShaderTexture(0, textureRecord.texture());
        RenderSystem.setShaderColor(1, 1, 1, alpha);
        drawProgress(poseStack, side, posYMod, textureRecord, progress);
    }

    /**
     * Draws the progress bar for the HUD.
     * @param side - true if the bar is on the left side of the screen, false if on the right side.
     * @param posYMod - the amount you want to add to the base -40 y position.
     * @param textureRecord - the texture record for the bar.
     * @param progress - the progress of the bar.
     * @param color - the color of the bar. (Hex Value)
     * @param alpha - the alpha of the bar.
     */
    public static void drawProgressBar(PoseStack poseStack, boolean side, int posYMod, TextureRecord textureRecord, int progress, int color, float alpha) {
        RenderSystem.setShaderTexture(0, textureRecord.texture());
        RenderSystem.setShaderColor(ColorUtils.fromHex(color).getRedF(), ColorUtils.fromHex(color).getGreenF(), ColorUtils.fromHex(color).getBlueF(), alpha);
        drawProgress(poseStack, side, posYMod, textureRecord, progress);
    }

    /**
     * Draws Colorized bar for when status effect is applied.
     * @param side - true if the bar is on the left side of the screen, false if on the right side.
     * @param posYMod - the amount you want to add to the base -40 y position.
     * @param textureRecord - the texture record for the bar.
     * @param color - the color of the bar. (Hex Value)
     */
    public static void drawStatusEffectBar(PoseStack poseStack, boolean side, int posYMod, TextureRecord textureRecord, int color) {
        RenderSystem.setShaderTexture(0, textureRecord.texture());
        RenderSystem.setShaderColor(ColorUtils.fromHex(color).getRedF(), ColorUtils.fromHex(color).getGreenF(), ColorUtils.fromHex(color).getBlueF(), 1);
        int finalSide = (side) ? RenderHudHelper.getPosX(true) : RenderHudHelper.getPosX(false);
        GuiComponent.blit(poseStack, finalSide, RenderHudHelper.getPosY() + posYMod,
                textureRecord.startX(), textureRecord.startY(),
                textureRecord.width(), textureRecord.height(),
                textureRecord.maxWidth(), textureRecord.maxHeight());
    }

    /**
     * Draws the icon for the bar.
     * @param side - true if the bar is on the left side of the screen, false if on the right side.
     * @param posYMod - the amount you want to add to the base -40 y position.
     * @param textureRecord - the texture record for the bar.
     * @param barWidth - the width of the bar so method can determine offset.
     */
    public static void drawIcon(PoseStack poseStack, boolean side, int posYMod, TextureRecord textureRecord, int barWidth) {
        RenderSystem.setShaderTexture(0, textureRecord.texture());
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (side) {
            GuiComponent.blit(poseStack,
                    RenderHudHelper.getPosX(true) - (textureRecord.width() + 1), RenderHudHelper.getPosY() + posYMod,
                    textureRecord.startX(), textureRecord.startY(),
                    textureRecord.width(), textureRecord.height(),
                    textureRecord.maxWidth(), textureRecord.maxHeight());
        } else {
            GuiComponent.blit(poseStack,
                    RenderHudHelper.getPosX(false) + (barWidth + 1), RenderHudHelper.getPosY() + posYMod,
                    textureRecord.startX(), textureRecord.startY(),
                    textureRecord.width(), textureRecord.height(),
                    textureRecord.maxWidth(), textureRecord.maxHeight());
        }
    }

    /**
     * Sets defalut texture for the bars. (only call if you wish to set your own texture for vanilla hud elements)
     * @param texture - New texture for the bars.
     */
    @SuppressWarnings("unused")
    public static void setDefaultTexture(ResourceLocation texture) {
        TextureRecords.statusBarTextures = texture;
    }

    private static void drawProgress(PoseStack poseStack, boolean side, int posYMod, TextureRecord textureRecord, int progress) {
        if (side) {
            GuiComponent.blit(poseStack,
                    RenderHudHelper.getPosX(true), RenderHudHelper.getPosY() + posYMod,
                    textureRecord.startX(), textureRecord.startY(),
                    progress, textureRecord.height(),
                    textureRecord.maxWidth(), textureRecord.maxHeight());
        } else {
            GuiComponent.blit(poseStack,
                    RenderHudHelper.getPosX(false) + (textureRecord.width() - progress), RenderHudHelper.getPosY() + posYMod,
                    textureRecord.startX() + (textureRecord.width() - progress), textureRecord.startY(),
                    textureRecord.width(), textureRecord.height(),
                    textureRecord.maxWidth(), textureRecord.maxHeight());
        }
    }

}