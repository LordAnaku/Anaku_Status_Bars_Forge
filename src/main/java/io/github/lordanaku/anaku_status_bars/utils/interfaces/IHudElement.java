package io.github.lordanaku.anaku_status_bars.utils.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

public interface IHudElement {
    void renderBar(PoseStack poseStack);
    void renderIcon(PoseStack poseStack);
    void renderText(PoseStack poseStack);
    boolean getSide();
    IHudElement setRenderSide(boolean side);
    boolean shouldRender();
    boolean shouldRenderIcon();
    boolean shouldRenderText();
    void registerSettings(ConfigCategory mainCategory, ConfigCategory iconCategory, ConfigCategory textCategory, ConfigCategory colorCategory, ConfigCategory textColorSettings, ConfigCategory alphaCategory, ConfigEntryBuilder builder);
    String name();
}
