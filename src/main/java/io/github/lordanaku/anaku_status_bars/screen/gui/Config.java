package io.github.lordanaku.anaku_status_bars.screen.gui;

import io.github.lordanaku.anaku_status_bars.AnakuStatusBarsCore;
import io.github.lordanaku.anaku_status_bars.screen.hud.RenderHudHelper;
import io.github.lordanaku.anaku_status_bars.utils.Settings;
import io.github.lordanaku.anaku_status_bars.utils.interfaces.IHudElement;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class Config{

    public static Screen CreateConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("title.anaku_status_bars.config"));

        builder.setSavingRunnable(ConfigFileHandler::writeToConfig);

        ConfigCategory mainCategory = builder.getOrCreateCategory(Component.translatable("category.anaku_status_bars.general"));
        ConfigCategory iconCategory = builder.getOrCreateCategory(Component.translatable("category.anaku_status_bars.icon"));
        ConfigCategory colorCategory = builder.getOrCreateCategory(Component.translatable("category.anaku_status_bars.color"));
        ConfigCategory alphaCategory = builder.getOrCreateCategory(Component.translatable("category.anaku_status_bars.alpha"));
        ConfigCategory positionCategory = builder.getOrCreateCategory(Component.translatable("category.anaku_status_bars.position"));

        BooleanListEntry vanillaTextures = builder.entryBuilder().startBooleanToggle(Component.translatable("option.anaku_status_bars.vanilla_textures"), Settings.shouldUseVanillaTextures)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> Settings.shouldUseVanillaTextures = newValue)
                .build();
        mainCategory.addEntry(vanillaTextures);

        for (IHudElement hudElement : RenderHudHelper.getHudElementRegistry()) {
            hudElement.registerSettings(mainCategory, iconCategory, colorCategory, alphaCategory, builder.entryBuilder());
            AnakuStatusBarsCore.LOGGER.info("Registered settings for " + hudElement.name());
        }

        ConfigValues.buildPosition(positionCategory, builder.entryBuilder());

        return builder.build();
    }
}
