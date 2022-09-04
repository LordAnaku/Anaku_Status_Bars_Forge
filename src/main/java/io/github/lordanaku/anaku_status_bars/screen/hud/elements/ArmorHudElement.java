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
import net.minecraft.world.item.ArmorItem;


import java.util.concurrent.atomic.AtomicInteger;

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
    public void renderText(PoseStack poseStack) {
        RenderHudFunctions.drawText(poseStack, String.valueOf(getArmorDamageValue()), getSide(), shouldRenderIcon(), RenderHudHelper.getPosYMod(getSide()), Settings.textColorSettings.get(ARMOR.name()), 81);
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
    public boolean shouldRenderText() {
        return shouldRender() && Settings.shouldRenderTextSettings.get(ARMOR.name());
    }

    @Override
    public void registerSettings(ConfigCategory mainCategory, ConfigCategory iconCategory, ConfigCategory textCategory, ConfigCategory colorCategory, ConfigCategory textColorSettings, ConfigCategory alphaCategory, ConfigEntryBuilder builder) {
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

        BooleanListEntry enableArmorText = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_armor_text"), Settings.shouldRenderTextSettings.get(ARMOR.name()))
                .setDefaultValue(ARMOR.shouldRenderText())
                .setSaveConsumer(newValue -> Settings.shouldRenderTextSettings.replace(ARMOR.name(), newValue))
                .build();
        textCategory.addEntry(enableArmorText);

        ColorEntry colorArmor = builder.startColorField(Component.translatable("option.anaku_status_bars.color_armor"), Settings.colorSettings.get(ARMOR.name()))
                .setDefaultValue(ARMOR.color())
                .setSaveConsumer(newValue -> Settings.colorSettings.replace(ARMOR.name(), newValue))
                .build();
        colorCategory.addEntry(colorArmor);

        ColorEntry textColorArmor = builder.startColorField(Component.translatable("option.anaku_status_bars.armor_text_color"), Settings.textColorSettings.get(ARMOR.name()))
                .setDefaultValue(ARMOR.color())
                .setSaveConsumer(newValue -> Settings.textColorSettings.replace(ARMOR.name(), newValue))
                .build();
        textColorSettings.addEntry(textColorArmor);

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
        float ratio = getArmorPercentage();
        int maxProgress = 81;
        return (int) Math.min(maxProgress, Math.ceil(ratio * maxProgress));
    }

    private float getArmorPercentage() {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        final float[] damage = {0, 0};
        player.getArmorSlots().forEach(itemStack -> {
            damage[0] += itemStack.getMaxDamage() - itemStack.getDamageValue();
            damage[1] += itemStack.getMaxDamage();
        });
        if (damage[1] == 0) return 0;
        return Math.min(1, Math.max(0, damage[0] / damage[1]));
    }

    private int getArmorDamageValue() {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        AtomicInteger armorDamage = new AtomicInteger();
        player.getArmorSlots().forEach(itemStack -> {
            if (itemStack.getItem() instanceof ArmorItem) {
                armorDamage.addAndGet(itemStack.getMaxDamage() - itemStack.getDamageValue());
            }
        });
        return armorDamage.get();
    }
}
