package io.github.lordanaku.anaku_status_bars.screen.hud.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lordanaku.anaku_status_bars.api.RenderHudFunctions;
import io.github.lordanaku.anaku_status_bars.screen.hud.RenderHudHelper;
import io.github.lordanaku.anaku_status_bars.utils.Settings;
import io.github.lordanaku.anaku_status_bars.utils.TextureRecords;
import io.github.lordanaku.anaku_status_bars.utils.interfaces.IHudElement;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static io.github.lordanaku.anaku_status_bars.utils.Settings.AIR;

public class AirHudElement implements IHudElement {
    private boolean renderSide = AIR.side();

    @Override
    public void renderBar(PoseStack poseStack) {
        if (getAirProgress() <= 0) {
            RenderHudFunctions.drawStatusEffectBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.DEFAULT_BAR,
                    Settings.colorSettings.get(Settings.HEALTH.name() + "_hurt"));
        } else {
            RenderHudFunctions.drawDefaultBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.DEFAULT_BAR);
        }
        RenderHudFunctions.drawProgressBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.PROGRESS_BAR, getAirProgress(),
                Settings.colorSettings.get(AIR.name()), Settings.alphaSettings.get(AIR.name()));
    }

    @Override
    public void renderIcon(PoseStack poseStack) {
        if (getAirProgress() <= 0) {
            RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.BUBBLE_BURST_ICON, 81);
        } else {
            RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.BUBBLE_ICON, 81);
        }
    }

    @Override
    public void renderText(PoseStack poseStack) {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        RenderHudFunctions.drawText(poseStack, String.valueOf(Math.round(player.getAirSupply())), getSide(), shouldRenderIcon(), RenderHudHelper.getPosYMod(getSide()), Settings.textColorSettings.get(AIR.name()), 81);

    }

    @Override
    public boolean getSide() {
        return this.renderSide;
    }

    @Override
    public IHudElement setRenderSide(boolean side) {
        this.renderSide = side;
        return this;
    }

    @Override
    public boolean shouldRender() {
        if (!Settings.shouldRenderSettings.get(AIR.name())) return false;
        assert Minecraft.getInstance().player != null;
        return Minecraft.getInstance().player.getAirSupply() < 300;
    }

    @Override
    public boolean shouldRenderIcon() {
        return shouldRender() && Settings.shouldRenderIconSettings.get(AIR.name());
    }

    @Override
    public boolean shouldRenderText() {
        return shouldRender() && Settings.shouldRenderTextSettings.get(AIR.name());
    }

    @Override
    public void registerSettings(ConfigCategory mainCategory, ConfigCategory iconCategory, ConfigCategory textCategory, ConfigCategory colorCategory, ConfigCategory textColorSettings, ConfigCategory alphaCategory, ConfigEntryBuilder builder) {
        BooleanListEntry enableAirBar = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_air_bar"), Settings.shouldRenderSettings.get(AIR.name()))
                .setDefaultValue(AIR.shouldRender())
                .setSaveConsumer(value -> Settings.shouldRenderSettings.replace(AIR.name(), value))
                .build();
        mainCategory.addEntry(enableAirBar);

        BooleanListEntry enableAirIcon = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_air_icon"), Settings.shouldRenderIconSettings.get(AIR.name()))
                .setDefaultValue(AIR.shouldRenderIcon())
                .setSaveConsumer(value -> Settings.shouldRenderIconSettings.replace(AIR.name(), value))
                .build();
        iconCategory.addEntry(enableAirIcon);

        BooleanListEntry enableAirText = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_air_text"), Settings.shouldRenderTextSettings.get(AIR.name()))
                .setDefaultValue(AIR.shouldRenderText())
                .setSaveConsumer(value -> Settings.shouldRenderTextSettings.replace(AIR.name(), value))
                .build();
        textCategory.addEntry(enableAirText);

        ColorEntry airColor = builder.startColorField(Component.translatable("option.anaku_status_bars.air_color"), Settings.colorSettings.get(AIR.name()))
                .setDefaultValue(AIR.color())
                .setSaveConsumer(value -> Settings.colorSettings.replace(AIR.name(), value))
                .build();
        colorCategory.addEntry(airColor);

        ColorEntry airTextColor = builder.startColorField(Component.translatable("option.anaku_status_bars.air_text_color"), Settings.textColorSettings.get(AIR.name()))
                .setDefaultValue(AIR.color())
                .setSaveConsumer(value -> Settings.textColorSettings.replace(AIR.name(), value))
                .build();
        textColorSettings.addEntry(airTextColor);

        FloatListEntry airAlpha = builder.startFloatField(Component.translatable("option.anaku_status_bars.air_alpha"), Settings.alphaSettings.get(AIR.name()))
                .setDefaultValue(AIR.alpha())
                .setMin(0.0f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("option.anakus_status_bars.alpha_tooltip"))
                .setSaveConsumer(value -> Settings.alphaSettings.replace(AIR.name(), value))
                .build();
        alphaCategory.addEntry(airAlpha);
    }

    @Override
    public String name() {
        return AIR.name();
    }

    private int getAirProgress() {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        float air = player.getAirSupply();
        float airMax = 300f;
        float ratio = Math.min(1, Math.max(0, air / airMax));
        int maxProgress = 81;
        return (int) Math.min(maxProgress, Math.ceil(ratio * maxProgress));
    }
}
