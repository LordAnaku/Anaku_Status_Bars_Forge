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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import static io.github.lordanaku.anaku_status_bars.utils.Settings.HEALTH;

public class HealthHudElement implements IHudElement {
    private boolean renderSide = HEALTH.side();

    @Override
    public void renderBar(PoseStack poseStack) {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        if (player.hasEffect(MobEffects.POISON)) {
            RenderHudFunctions.drawStatusEffectBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.DEFAULT_BAR,
                    Settings.colorSettings.get(HEALTH.name() + "_poison"));
        } else if (player.hasEffect(MobEffects.WITHER)) {
            RenderHudFunctions.drawStatusEffectBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.DEFAULT_BAR,
                    Settings.colorSettings.get(HEALTH.name() + "_wither"));
        } else if (player.isFreezing()){
            RenderHudFunctions.drawStatusEffectBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.DEFAULT_BAR,
                    Settings.colorSettings.get(HEALTH.name() + "_frostbite"));
        } else {
            RenderHudFunctions.drawDefaultBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.DEFAULT_BAR);
        }
        RenderHudFunctions.drawProgressBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.PROGRESS_BAR, getHealthProgress(),
                Settings.colorSettings.get(HEALTH.name()), Settings.alphaSettings.get(HEALTH.name()));

        if(Settings.shouldRenderSettings.get(HEALTH.name() + "_absorption")) {
            RenderHudFunctions.drawProgressBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.PROGRESS_BAR, getAbsorptionProgress(),
                    Settings.colorSettings.get(HEALTH.name() + "_absorption"), Settings.alphaSettings.get(HEALTH.name() + "_absorption"));
        }
    }

    @Override
    public void renderIcon(PoseStack poseStack) {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.HEART_OUTLINE_ICON, 81);
        if (player.hasEffect(MobEffects.POISON)) {
            RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.HEART_POISON_ICON, 81);
        } else if (player.hasEffect(MobEffects.WITHER)) {
            RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.HEART_WITHER_ICON, 81);
        } else if (player.isFreezing()) {
            RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.HEART_FROSTBITE_ICON, 81);
        } else if (player.hasEffect(MobEffects.ABSORPTION)) {
            RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.HEART_ABSORPTION_ICON, 81);
        } else {
            RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.HEART_ICON, 81);
        }
    }

    @Override
    public void renderText(PoseStack poseStack) {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        if (player.hasEffect(MobEffects.ABSORPTION) && player.getAbsorptionAmount() > 0 && Settings.shouldRenderSettings.get(HEALTH.name() + "_absorption")) {
            RenderHudFunctions.drawText(poseStack, String.valueOf(Math.round(player.getHealth() + player.getAbsorptionAmount())), getSide(), shouldRenderIcon(), RenderHudHelper.getPosYMod(getSide()),
                    Settings.textColorSettings.get(HEALTH.name() + "_absorption"), 81);
        } else {
            RenderHudFunctions.drawText(poseStack, String.valueOf(Math.round(player.getHealth())), getSide(), shouldRenderIcon(), RenderHudHelper.getPosYMod(getSide()), Settings.textColorSettings.get(HEALTH.name()), 81);
        }
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
        return Settings.shouldRenderSettings.get(HEALTH.name());
    }

    @Override
    public boolean shouldRenderIcon() {
        return shouldRender() && Settings.shouldRenderIconSettings.get(HEALTH.name());
    }

    @Override
    public boolean shouldRenderText() {
        return shouldRender() && Settings.shouldRenderTextSettings.get(HEALTH.name());
    }

    @Override
    public void registerSettings(ConfigCategory mainCategory, ConfigCategory iconCategory, ConfigCategory textCategory, ConfigCategory colorCategory, ConfigCategory textColorSettings, ConfigCategory alphaCategory, ConfigEntryBuilder builder) {
        BooleanListEntry enableHealthBar = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_health_bar"), Settings.shouldRenderSettings.get(HEALTH.name()))
                .setDefaultValue(HEALTH.shouldRender())
                .setSaveConsumer(newValue -> Settings.shouldRenderSettings.replace(HEALTH.name(), newValue))
                .build();
        mainCategory.addEntry(enableHealthBar);

        BooleanListEntry enableHealthIcon = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_health_icon"), Settings.shouldRenderIconSettings.get(HEALTH.name()))
                .setDefaultValue(HEALTH.shouldRenderIcon())
                .setSaveConsumer(newValue -> Settings.shouldRenderIconSettings.replace(HEALTH.name(), newValue))
                .build();
        iconCategory.addEntry(enableHealthIcon);

        BooleanListEntry enableHealthText = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_health_text"), Settings.shouldRenderTextSettings.get(HEALTH.name()))
                .setDefaultValue(HEALTH.shouldRenderText())
                .setSaveConsumer(newValue -> Settings.shouldRenderTextSettings.replace(HEALTH.name(), newValue))
                .build();
        textCategory.addEntry(enableHealthText);

        ColorEntry healthBarColor = builder.startColorField(Component.translatable("option.anaku_status_bars.health_bar_color"), Settings.colorSettings.get(HEALTH.name()))
                .setDefaultValue(HEALTH.color())
                .setSaveConsumer(newValue -> Settings.colorSettings.replace(HEALTH.name(), newValue))
                .build();
        colorCategory.addEntry(healthBarColor);

        ColorEntry healthTextColor = builder.startColorField(Component.translatable("option.anaku_status_bars.health_text_color"), Settings.textColorSettings.get(HEALTH.name()))
                .setDefaultValue(HEALTH.color())
                .setSaveConsumer(newValue -> Settings.textColorSettings.replace(HEALTH.name(), newValue))
                .build();
        textColorSettings.addEntry(healthTextColor);

        FloatListEntry healthBarAlpha = builder.startFloatField(Component.translatable("option.anaku_status_bars.health_bar_alpha"), Settings.alphaSettings.get(HEALTH.name()))
                .setDefaultValue(HEALTH.alpha())
                .setMin(0.0f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("option.anakus_status_bars.alpha_tooltip"))
                .setSaveConsumer(newValue -> Settings.alphaSettings.replace(HEALTH.name(), newValue))
                .build();
        alphaCategory.addEntry(healthBarAlpha);

        BooleanListEntry enableHealthAbsorption = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_health_absorption"), Settings.shouldRenderSettings.get(HEALTH.name() + "_absorption"))
                .setDefaultValue(HEALTH.shouldRender())
                .setSaveConsumer(newValue -> Settings.shouldRenderSettings.replace(HEALTH.name() + "_absorption", newValue))
                .build();
        mainCategory.addEntry(enableHealthAbsorption);

        ColorEntry healthAbsorptionBarColor = builder.startColorField(Component.translatable("option.anaku_status_bars.health_absorption_bar_color"), Settings.colorSettings.get(HEALTH.name() + "_absorption"))
                .setDefaultValue(Settings.ABSORPTION_COLOR_DEFAULT)
                .setSaveConsumer(newValue -> Settings.colorSettings.replace(HEALTH.name() + "_absorption", newValue))
                .build();
        colorCategory.addEntry(healthAbsorptionBarColor);

        ColorEntry healthAbsorptionTextColor = builder.startColorField(Component.translatable("option.anaku_status_bars.health_absorption_text_color"), Settings.textColorSettings.get(HEALTH.name() + "_absorption"))
                .setDefaultValue(Settings.ABSORPTION_COLOR_DEFAULT)
                .setSaveConsumer(newValue -> Settings.textColorSettings.replace(HEALTH.name() + "_absorption", newValue))
                .build();
        textColorSettings.addEntry(healthAbsorptionTextColor);

        FloatListEntry healthAbsorptionBarAlpha = builder.startFloatField(Component.translatable("option.anaku_status_bars.health_absorption_bar_alpha"), Settings.alphaSettings.get(HEALTH.name() + "_absorption"))
                .setDefaultValue(HEALTH.alpha())
                .setMin(0.0f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("option.anakus_status_bars.alpha_tooltip"))
                .setSaveConsumer(newValue -> Settings.alphaSettings.replace(HEALTH.name() + "_absorption", newValue))
                .build();
        alphaCategory.addEntry(healthAbsorptionBarAlpha);
    }

    @Override
    public String name() {
        return HEALTH.name();
    }

    private int getHealthProgress() {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        int maxProgress = 81;
        float progress = Math.min(1, Math.max(0, health / maxHealth));
        return (int) Math.min(maxProgress, Math.ceil(progress * maxProgress));
    }

    private int getAbsorptionProgress() {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        float absorption = player.getAbsorptionAmount();
        float maxAbsorption = player.getMaxHealth();
        float ratio = Math.min(1, Math.max(0, absorption / maxAbsorption));
        int maxProgress = 81;
        return (int) Math.min(maxProgress, Math.ceil(ratio * maxProgress));
    }
}
