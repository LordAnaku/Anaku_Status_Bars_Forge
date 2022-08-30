package io.github.lordanaku.anaku_status_bars.screen.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

import static io.github.lordanaku.anaku_status_bars.utils.Settings.*;

public class ConfigValues {
    public static void buildPosition(ConfigCategory positionCategory, ConfigEntryBuilder builder) {
        StringListListEntry leftOrder = builder.startStrList(Component.translatable("option.anaku_status_bars.left_order"), sideOrderSettings.get("left"))
                .setDefaultValue(LEFT_ORDER_DEFAULT)
                .setSaveConsumer(newValue -> sideOrderSettings.replace("left", new ArrayList<>(newValue)))
                .build();
        positionCategory.addEntry(leftOrder);

        StringListListEntry rightOrder = builder.startStrList(Component.translatable("option.anaku_status_bars.right_order"), sideOrderSettings.get("right"))
                .setDefaultValue(RIGHT_ORDER_DEFAULT)
                .setSaveConsumer(newValue -> sideOrderSettings.replace("right", new ArrayList<>(newValue)))
                .build();
        positionCategory.addEntry(rightOrder);

        IntegerListEntry leftYOffset = builder.startIntField(Component.translatable("option.anaku_status_bars.left_y_offset"), positionSettings.get("left_y_offset"))
                .setDefaultValue(40)
                .setSaveConsumer(newValue -> positionSettings.replace("left_y_offset", newValue))
                .build();
        positionCategory.addEntry(leftYOffset);

        IntegerListEntry rightYOffset = builder.startIntField(Component.translatable("option.anaku_status_bars.right_y_offset"), positionSettings.get("right_y_offset"))
                .setDefaultValue(40)
                .setSaveConsumer(newValue -> positionSettings.replace("right_y_offset", newValue))
                .build();
        positionCategory.addEntry(rightYOffset);

        IntegerListEntry leftXOffset = builder.startIntField(Component.translatable("option.anaku_status_bars.left_x_offset"), positionSettings.get("left_x_offset"))
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> positionSettings.replace("left_x_offset", newValue))
                .build();
        positionCategory.addEntry(leftXOffset);

        IntegerListEntry rightXOffset = builder.startIntField(Component.translatable("option.anaku_status_bars.right_x_offset"), positionSettings.get("right_x_offset"))
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> positionSettings.replace("right_x_offset", newValue))
                .build();
        positionCategory.addEntry(rightXOffset);
    }
}
