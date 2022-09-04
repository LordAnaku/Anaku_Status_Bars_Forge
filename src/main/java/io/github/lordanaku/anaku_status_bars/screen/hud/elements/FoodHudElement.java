package io.github.lordanaku.anaku_status_bars.screen.hud.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lordanaku.anaku_status_bars.api.RenderHudFunctions;
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

import static io.github.lordanaku.anaku_status_bars.screen.hud.RenderHudHelper.getPosYMod;
import static io.github.lordanaku.anaku_status_bars.utils.Settings.FOOD;

public class FoodHudElement implements IHudElement {
    private boolean renderSide = FOOD.side();
    private final int MAX_PROGRESS = 81;

    @Override
    public void renderBar(PoseStack poseStack) {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        if (player.hasEffect(MobEffects.HUNGER)) {
            RenderHudFunctions.drawStatusEffectBar(poseStack, getSide(), getPosYMod(getSide()), TextureRecords.DEFAULT_BAR,
                    Settings.colorSettings.get(FOOD.name() + "_hunger"));
        } else {
            RenderHudFunctions.drawDefaultBar(poseStack, getSide(), getPosYMod(getSide()), TextureRecords.DEFAULT_BAR);
        }
        RenderHudFunctions.drawProgressBar(poseStack, getSide(), getPosYMod(getSide()), TextureRecords.PROGRESS_BAR, getFoodProgress(),
                Settings.colorSettings.get(FOOD.name()), Settings.alphaSettings.get(FOOD.name()));

        if (Settings.shouldRenderSettings.get(FOOD.name() + "_saturation")) {
            RenderHudFunctions.drawProgressBar(poseStack, getSide(), getPosYMod(getSide()), TextureRecords.PROGRESS_BAR, getSaturationProgress(),
                    Settings.colorSettings.get(FOOD.name() + "_saturation"), Settings.alphaSettings.get(FOOD.name() + "_saturation"));
        }
        if (Settings.shouldRenderSettings.get(FOOD.name() + "_exhaustion")) {
            RenderHudFunctions.drawExhaustBar(poseStack, getSide(), getPosYMod(getSide()), TextureRecords.EXHAUSTION_BAR, getExhaustionProgress(),
                    Settings.alphaSettings.get(FOOD.name() + "_exhaustion"));
        }
    }

    @Override
    public void renderIcon(PoseStack poseStack) {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        RenderHudFunctions.drawIcon(poseStack, getSide(), getPosYMod(getSide()), TextureRecords.FOOD_OUTLINE_ICON, 81);
        if (player.hasEffect(MobEffects.HUNGER)) {
            RenderHudFunctions.drawIcon(poseStack, getSide(), getPosYMod(getSide()), TextureRecords.HUNGER_ICON, 81);
        } else {
            RenderHudFunctions.drawIcon(poseStack, getSide(), getPosYMod(getSide()), TextureRecords.FOOD_ICON, 81);
        }

    }

    @Override
    public void renderText(PoseStack poseStack) {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        if (Settings.shouldRenderSettings.get(FOOD.name() + "_saturation") && player.getFoodData().getSaturationLevel() > 0) {
            RenderHudFunctions.drawText(poseStack, String.valueOf(Math.round(player.getFoodData().getFoodLevel() + player.getFoodData().getSaturationLevel())), getSide(), shouldRenderIcon(), getPosYMod(getSide()),
                    Settings.textColorSettings.get(FOOD.name() + "_saturation"), 81);
        } else {
            RenderHudFunctions.drawText(poseStack, String.valueOf(player.getFoodData().getFoodLevel()), getSide(), shouldRenderIcon(), getPosYMod(getSide()), Settings.textColorSettings.get(FOOD.name()), 81);
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
        return Settings.shouldRenderSettings.get(FOOD.name());
    }

    @Override
    public boolean shouldRenderIcon() {
        return shouldRender() && Settings.shouldRenderIconSettings.get(FOOD.name());
    }

    @Override
    public boolean shouldRenderText() {
        return shouldRender() && Settings.shouldRenderTextSettings.get(FOOD.name());
    }

    @Override
    public void registerSettings(ConfigCategory mainCategory, ConfigCategory iconCategory, ConfigCategory textCategory, ConfigCategory colorCategory, ConfigCategory textColorSettings, ConfigCategory alphaCategory, ConfigEntryBuilder builder) {
        /* * Main Food Bar * */
        BooleanListEntry enableFoodBar = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_food_bar"), Settings.shouldRenderSettings.get(FOOD.name()))
                .setDefaultValue(FOOD.shouldRender())
                .setSaveConsumer(newValue -> Settings.shouldRenderSettings.replace(FOOD.name(), newValue))
                .build();
        mainCategory.addEntry(enableFoodBar);

        BooleanListEntry enableFoodIcon = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_food_icon"), Settings.shouldRenderIconSettings.get(FOOD.name()))
                .setDefaultValue(FOOD.shouldRenderIcon())
                .setSaveConsumer(newValue -> Settings.shouldRenderIconSettings.replace(FOOD.name(), newValue))
                .build();
        iconCategory.addEntry(enableFoodIcon);

        BooleanListEntry enableFoodText = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_food_text"), Settings.shouldRenderTextSettings.get(FOOD.name()))
                .setDefaultValue(FOOD.shouldRenderText())
                .setSaveConsumer(newValue -> Settings.shouldRenderTextSettings.replace(FOOD.name(), newValue))
                .build();
        textCategory.addEntry(enableFoodText);

        ColorEntry foodColor = builder.startColorField(Component.translatable("option.anaku_status_bars.food_color"), Settings.colorSettings.get(FOOD.name()))
                .setDefaultValue(FOOD.color())
                .setSaveConsumer(newValue -> Settings.colorSettings.replace(FOOD.name(), newValue))
                .build();
        colorCategory.addEntry(foodColor);

        ColorEntry foodTextColor = builder.startColorField(Component.translatable("option.anaku_status_bars.food_text_color"), Settings.textColorSettings.get(FOOD.name()))
                .setDefaultValue(FOOD.color())
                .setSaveConsumer(newValue -> Settings.textColorSettings.replace(FOOD.name(), newValue))
                .build();
        textColorSettings.addEntry(foodTextColor);

        FloatListEntry foodAlpha = builder.startFloatField(Component.translatable("option.anaku_status_bars.food_alpha"), Settings.alphaSettings.get(FOOD.name()))
                .setDefaultValue(FOOD.alpha())
                .setMin(0.0f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("option.anakus_status_bars.alpha_tooltip"))
                .setSaveConsumer(newValue -> Settings.alphaSettings.replace(FOOD.name(), newValue))
                .build();
        alphaCategory.addEntry(foodAlpha);

        /* * Saturation Bar * */
        BooleanListEntry enableSaturationBar = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_saturation_bar"), Settings.shouldRenderSettings.get(FOOD.name() + "_saturation"))
                .setDefaultValue(FOOD.shouldRender())
                .setSaveConsumer(newValue -> Settings.shouldRenderSettings.replace(FOOD.name() + "_saturation", newValue))
                .build();
        mainCategory.addEntry(enableSaturationBar);

        ColorEntry saturationColor = builder.startColorField(Component.translatable("option.anaku_status_bars.saturation_color"), Settings.colorSettings.get(FOOD.name() + "_saturation"))
                .setDefaultValue(Settings.SATURATION_COLOR_DEFAULT)
                .setSaveConsumer(newValue -> Settings.colorSettings.replace(FOOD.name() + "_saturation", newValue))
                .build();
        colorCategory.addEntry(saturationColor);

        ColorEntry saturationTextColor = builder.startColorField(Component.translatable("option.anaku_status_bars.saturation_text_color"), Settings.textColorSettings.get(FOOD.name() + "_saturation"))
                .setDefaultValue(Settings.SATURATION_COLOR_DEFAULT)
                .setSaveConsumer(newValue -> Settings.textColorSettings.replace(FOOD.name() + "_saturation", newValue))
                .build();
        textColorSettings.addEntry(saturationTextColor);

        FloatListEntry saturationAlpha = builder.startFloatField(Component.translatable("option.anaku_status_bars.saturation_alpha"), Settings.alphaSettings.get(FOOD.name() + "_saturation"))
                .setDefaultValue(FOOD.alpha())
                .setMin(0.0f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("option.anakus_status_bars.alpha_tooltip"))
                .setSaveConsumer(newValue -> Settings.alphaSettings.replace(FOOD.name() + "_saturation", newValue))
                .build();
        alphaCategory.addEntry(saturationAlpha);

        /* * Exhaustion Bar * */
        BooleanListEntry enableExhaustionBar = builder.startBooleanToggle(Component.translatable("option.anaku_status_bars.enable_exhaustion_bar"), Settings.shouldRenderSettings.get(FOOD.name() + "_exhaustion"))
                .setDefaultValue(FOOD.shouldRender())
                .setSaveConsumer(newValue -> Settings.shouldRenderSettings.replace(FOOD.name() + "_exhaustion", newValue))
                .build();
        mainCategory.addEntry(enableExhaustionBar);

        FloatListEntry exhaustionAlpha = builder.startFloatField(Component.translatable("option.anaku_status_bars.exhaustion_alpha"), Settings.alphaSettings.get(FOOD.name() + "_exhaustion"))
                .setDefaultValue(Settings.ALPHA_DEFAULT)
                .setMin(0.0f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("option.anakus_status_bars.alpha_tooltip"))
                .setSaveConsumer(newValue -> Settings.alphaSettings.replace(FOOD.name() + "_exhaustion", newValue))
                .build();
        alphaCategory.addEntry(exhaustionAlpha);

    }

    @Override
    public String name() {
        return FOOD.name();
    }

    private int getFoodProgress() {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        float foodLevel = player.getFoodData().getFoodLevel();
        float maxFoodLevel = 20f;
        float ratio = Math.min(1, Math.max(0, foodLevel / maxFoodLevel));
        return (int) Math.min(MAX_PROGRESS, Math.ceil(MAX_PROGRESS * ratio));
    }

    private int getSaturationProgress() {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        float saturation = player.getFoodData().getSaturationLevel();
        float maxSaturation = 20f;
        float ratio = Math.min(1, Math.max(0, saturation / maxSaturation));
        return (int) Math.min(MAX_PROGRESS, Math.ceil(MAX_PROGRESS * ratio));
    }

    private int getExhaustionProgress() {
        assert Minecraft.getInstance().player != null; Player player = Minecraft.getInstance().player;
        float exhaustion = player.getFoodData().getExhaustionLevel();
        float maxExhaustion = 4f;
        float ratio = Math.min(1, Math.max(0, exhaustion / maxExhaustion));
        return (int) Math.min(MAX_PROGRESS, Math.ceil(MAX_PROGRESS * ratio));
    }
}
