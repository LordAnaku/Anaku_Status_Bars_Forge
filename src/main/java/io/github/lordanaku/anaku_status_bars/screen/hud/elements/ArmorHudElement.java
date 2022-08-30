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

import static io.github.lordanaku.anaku_status_bars.utils.Settings.ARMOR;

public class ArmorHudElement implements IHudElement {
    private boolean renderSide = ARMOR.side();

    @Override
    public void renderBar(PoseStack poseStack) {
        RenderHudFunctions.drawDefaultBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.DEFAULT_BAR);
        RenderHudFunctions.drawProgressBar(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.PROGRESS_BAR, getArmorProgress(),
                Settings.colorSettings.get(ARMOR.name()), Settings.alphaSettings.get(ARMOR.name()));
    }

    @Override
    public void renderIcon(PoseStack poseStack) {
        RenderHudFunctions.drawIcon(poseStack, getSide(), RenderHudHelper.getPosYMod(getSide()), TextureRecords.ARMOR_ICON, 81);
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
        if (!Settings.shouldRenderSettings.get(ARMOR.name())) return false;
        assert Minecraft.getInstance().player != null;
        return Minecraft.getInstance().player.getArmorValue() > 0;
    }

    @Override
    public boolean shouldRenderIcon() {
        return shouldRender() && Settings.shouldRenderIconSettings.get(ARMOR.name());
    }

    @Override
    public void registerSettings(ConfigCategory mainCategory, ConfigCategory iconCategory, ConfigCategory colorCategory, ConfigCategory alphaCategory, ConfigEntryBuilder builder) {
        BooleanListEntry enableArmorBar = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_armor_bar"), Settings.shouldRenderSettings.get(ARMOR.name()))
                .setDefaultValue(ARMOR.shouldRender())
                .setSaveConsumer(newValue -> Settings.shouldRenderSettings.replace(ARMOR.name(), newValue))
                .build();
        mainCategory.addEntry(enableArmorBar);

        BooleanListEntry enableArmorIcon = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_armor_icon"), Settings.shouldRenderIconSettings.get(ARMOR.name()))
                .setDefaultValue(ARMOR.shouldRenderIcon())
                .setSaveConsumer(newValue -> Settings.shouldRenderIconSettings.replace(ARMOR.name(), newValue))
                .build();
        iconCategory.addEntry(enableArmorIcon);

        ColorEntry colorArmor = builder.startColorField(Component.translatable("option.anaku_status_bars.color_armor"), Settings.colorSettings.get(ARMOR.name()))
                .setDefaultValue(ARMOR.color())
                .setSaveConsumer(newValue -> Settings.colorSettings.replace(ARMOR.name(), newValue))
                .build();
        colorCategory.addEntry(colorArmor);

        FloatListEntry alphaArmor = builder.startFloatField(Component.translatable("option.anaku_status_bars.alpha_armor"), Settings.alphaSettings.get(ARMOR.name()))
                .setDefaultValue(ARMOR.alpha())
                .setMin(0.0f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("option.anakus_status_bars.alpha_tooltip"))
                .setSaveConsumer(newValue -> Settings.alphaSettings.replace(ARMOR.name(), newValue))
                .build();
        alphaCategory.addEntry(alphaArmor);
    }

    @Override
    public String name() {
        return ARMOR.name();
    }

    private int getArmorProgress() {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        float armor = player.getArmorValue();
        float armorMax = 20f;
        float ratio = Math.min(1, Math.max(0, armor / armorMax));
        int maxProgress = 81;
        return (int) Math.min(maxProgress, Math.ceil(ratio * maxProgress));
    }
}
