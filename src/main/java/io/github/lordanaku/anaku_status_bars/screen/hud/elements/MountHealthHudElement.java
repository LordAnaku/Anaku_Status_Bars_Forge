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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import static io.github.lordanaku.anaku_status_bars.utils.Settings.MOUNT_HEALTH;

public class MountHealthHudElement implements IHudElement {
    private boolean renderSide = MOUNT_HEALTH.side();

    @Override
    public void renderBar(PoseStack poseStack) {
        RenderHudFunctions.drawDefaultBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.DEFAULT_BAR);
        RenderHudFunctions.drawProgressBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.PROGRESS_BAR, getMountHealthProgress(),
                Settings.colorSettings.get(MOUNT_HEALTH.name()), Settings.alphaSettings.get(MOUNT_HEALTH.name()));
    }

    @Override
    public void renderIcon(PoseStack poseStack) {
        RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.HEART_OUTLINE_ICON, 81);
        RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.HEART_MOUNT_ICON, 81);
    }

    @Override
    public void renderText(PoseStack poseStack) {
        LivingEntity mountEntity = getRiddenEntity(); assert mountEntity != null;
        RenderHudFunctions.drawText(poseStack, String.valueOf(Math.round(mountEntity.getHealth())), getSide(), shouldRenderIcon(), RenderHudHelper.getPosYMod(getSide()), Settings.textColorSettings.get(MOUNT_HEALTH.name()), 81);
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
        if (!Settings.shouldRenderSettings.get(MOUNT_HEALTH.name())) return false;
        assert Minecraft.getInstance().player != null;
        return getRiddenEntity() != null;
    }

    @Override
    public boolean shouldRenderIcon() {
        return shouldRender() && Settings.shouldRenderIconSettings.get(MOUNT_HEALTH.name());
    }

    @Override
    public boolean shouldRenderText() {
        return shouldRender() && Settings.shouldRenderTextSettings.get(MOUNT_HEALTH.name());
    }

    @Override
    public void registerSettings(ConfigCategory mainCategory, ConfigCategory iconCategory, ConfigCategory textCategory, ConfigCategory colorCategory, ConfigCategory textColorSettings, ConfigCategory alphaCategory, ConfigEntryBuilder builder) {
        BooleanListEntry enableMountHealthBar = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_mount_health_bar"), Settings.shouldRenderSettings.get(MOUNT_HEALTH.name()))
                .setDefaultValue(MOUNT_HEALTH.shouldRender())
                .setSaveConsumer(value -> Settings.shouldRenderSettings.replace(MOUNT_HEALTH.name(), value))
                .build();
        mainCategory.addEntry(enableMountHealthBar);

        BooleanListEntry enableMountHealthIcon = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_mount_health_icon"), Settings.shouldRenderIconSettings.get(MOUNT_HEALTH.name()))
                .setDefaultValue(MOUNT_HEALTH.shouldRenderIcon())
                .setSaveConsumer(value -> Settings.shouldRenderIconSettings.replace(MOUNT_HEALTH.name(), value))
                .build();
        iconCategory.addEntry(enableMountHealthIcon);

        BooleanListEntry enableMountHealthText = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_mount_health_text"), Settings.shouldRenderTextSettings.get(MOUNT_HEALTH.name()))
                .setDefaultValue(MOUNT_HEALTH.shouldRenderText())
                .setSaveConsumer(value -> Settings.shouldRenderTextSettings.replace(MOUNT_HEALTH.name(), value))
                .build();
        textCategory.addEntry(enableMountHealthText);

        ColorEntry mountHealthColor = builder.startColorField(Component.translatable("option.anaku_status_bars.mount_health_color"), Settings.colorSettings.get(MOUNT_HEALTH.name()))
                .setDefaultValue(MOUNT_HEALTH.color())
                .setSaveConsumer(value -> Settings.colorSettings.replace(MOUNT_HEALTH.name(), value))
                .build();
        colorCategory.addEntry(mountHealthColor);

        ColorEntry mountHealthTextColor = builder.startColorField(Component.translatable("option.anaku_status_bars.mount_health_text_color"), Settings.textColorSettings.get(MOUNT_HEALTH.name()))
                .setDefaultValue(MOUNT_HEALTH.color())
                .setSaveConsumer(value -> Settings.textColorSettings.replace(MOUNT_HEALTH.name(), value))
                .build();
        textColorSettings.addEntry(mountHealthTextColor);

        FloatListEntry mountHealthAlpha = builder.startFloatField(Component.translatable("option.anaku_status_bars.mount_health_alpha"), Settings.alphaSettings.get(MOUNT_HEALTH.name()))
                .setDefaultValue(MOUNT_HEALTH.alpha())
                .setMin(0.0f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("option.anakus_status_bars.alpha_tooltip"))
                .setSaveConsumer(value -> Settings.alphaSettings.replace(MOUNT_HEALTH.name(), value))
                .build();
        alphaCategory.addEntry(mountHealthAlpha);
    }

    @Override
    public String name() {
        return MOUNT_HEALTH.name();
    }

    private static LivingEntity getRiddenEntity() {
        Player playerEntity = Minecraft.getInstance().player;
        assert playerEntity != null; Entity mountEntity = playerEntity.getVehicle();
        if (mountEntity == null) return null;
        if (mountEntity instanceof LivingEntity) {
            return (LivingEntity) mountEntity;
        }
        return null;
    }

    private static int getMountHealthProgress() {
        LivingEntity mountEntity = getRiddenEntity(); assert mountEntity != null;
        float mHealth  = mountEntity.getHealth();
        float mHealthMax = mountEntity.getMaxHealth();
        float ratio = Math.min(1, Math.max(0, mHealth / mHealthMax));
        int maxProgress = 81;
        return (int) Math.min(maxProgress, Math.ceil(ratio * maxProgress));
    }
}
