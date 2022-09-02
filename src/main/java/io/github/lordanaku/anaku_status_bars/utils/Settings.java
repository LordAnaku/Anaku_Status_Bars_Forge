package io.github.lordanaku.anaku_status_bars.utils;

import io.github.lordanaku.anaku_status_bars.utils.records.HudElementType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Settings {
    /* * Element Records * */
    public static final HudElementType HEALTH = new HudElementType("health", true, true, true, 0xff1313, 1);
    public static final HudElementType FOOD = new HudElementType("hunger", true, false, true, 0x9d6d43, 1);
    public static final HudElementType ARMOR = new HudElementType("armor", true, true, true, 0xb8b9c4, 1);
    public static final HudElementType AIR = new HudElementType("air", true, false, true, 0x0094ff, 1);
    public static final HudElementType MOUNT_HEALTH = new HudElementType("mount_health", true, false, true, 0xda662c, 1);

    /* * Default Settings * */
    public static boolean shouldUseVanillaTextures = false;
    public static final int LEFT_DEFAULT = 40;
    public static final int RIGHT_DEFAULT = 40;
    public static final ArrayList<String> LEFT_ORDER_DEFAULT = new ArrayList<>(Arrays.asList(HEALTH.name(), ARMOR.name()));
    public static final ArrayList<String> RIGHT_ORDER_DEFAULT = new ArrayList<>(Arrays.asList(FOOD.name(), AIR.name(), MOUNT_HEALTH.name()));
    public static final float ALPHA_DEFAULT = 0.5f;
    public static final int ABSORPTION_COLOR_DEFAULT = 0xd4af37;
    public static final int POISON_COLOR_DEFAULT = 0x8b8712;
    public static final int WITHER_COLOR_DEFAULT = 0x2b2b2b;
    public static final int FROSTBITE_COLOR_DEFAULT = 0x4bbad7;
    public static final int HURT_COLOR_DEFAULT = 0x550000;
    public static final int HUNGER_COLOR_DEFAULT = 0x5f6d43;
    public static final int SATURATION_COLOR_DEFAULT = 0xd42a2a;

    /* * Setting Categories * */
    public static final Map<String, Boolean> shouldRenderSettings = new HashMap<>();
    public static final Map<String, Boolean> sideSettings = new HashMap<>();
    public static final Map<String, Boolean> shouldRenderIconSettings = new HashMap<>();
    public static final Map<String, Integer> colorSettings = new HashMap<>();
    public static final Map<String, Float> alphaSettings = new HashMap<>();
    public static final Map<String, ArrayList<String>> sideOrderSettings = new HashMap<>();
    public static final Map<String, Integer> positionSettings = new HashMap<>();

    /* * Register Settings * */
    public static void registerSettings() {
        /* * Should Render Settings * */
        shouldRenderSettings.put(HEALTH.name(), HEALTH.shouldRender());
        shouldRenderSettings.put(HEALTH.name() + "_absorption", HEALTH.shouldRender());
        shouldRenderSettings.put(FOOD.name(), FOOD.shouldRender());
        shouldRenderSettings.put(FOOD.name() + "_saturation", FOOD.shouldRender());
        shouldRenderSettings.put(FOOD.name() + "_exhaustion", FOOD.shouldRender());
        shouldRenderSettings.put(ARMOR.name(), ARMOR.shouldRender());
        shouldRenderSettings.put(AIR.name(), AIR.shouldRender());
        shouldRenderSettings.put(MOUNT_HEALTH.name(), MOUNT_HEALTH.shouldRender());

        /* * Side Settings * */
        sideSettings.put(HEALTH.name(), HEALTH.side());
        sideSettings.put(FOOD.name(), HEALTH.side());
        sideSettings.put(ARMOR.name(), ARMOR.side());
        sideSettings.put(AIR.name(), AIR.side());
        sideSettings.put(MOUNT_HEALTH.name(), MOUNT_HEALTH.side());

        /* * Should Render Icon Settings * */
        shouldRenderIconSettings.put(HEALTH.name(), HEALTH.shouldRenderIcon());
        shouldRenderIconSettings.put(FOOD.name(), FOOD.shouldRenderIcon());
        shouldRenderIconSettings.put(ARMOR.name(), ARMOR.shouldRenderIcon());
        shouldRenderIconSettings.put(AIR.name(), AIR.shouldRenderIcon());
        shouldRenderIconSettings.put(MOUNT_HEALTH.name(), MOUNT_HEALTH.shouldRenderIcon());

        /* * Color Settings * */
        colorSettings.put(HEALTH.name(), HEALTH.color());
        colorSettings.put(HEALTH.name() + "_absorption", ABSORPTION_COLOR_DEFAULT);
        colorSettings.put(HEALTH.name() + "_poison", POISON_COLOR_DEFAULT);
        colorSettings.put(HEALTH.name() + "_wither", WITHER_COLOR_DEFAULT);
        colorSettings.put(HEALTH.name() + "_frostbite", FROSTBITE_COLOR_DEFAULT);
        colorSettings.put(HEALTH.name() + "_hurt", HURT_COLOR_DEFAULT);
        colorSettings.put(FOOD.name(), FOOD.color());
        colorSettings.put(FOOD.name() + "_saturation", SATURATION_COLOR_DEFAULT);
        colorSettings.put(FOOD.name() + "_hunger", HUNGER_COLOR_DEFAULT);
        colorSettings.put(ARMOR.name(), ARMOR.color());
        colorSettings.put(AIR.name(), AIR.color());
        colorSettings.put(MOUNT_HEALTH.name(), MOUNT_HEALTH.color());

        /* * Alpha Settings * */
        alphaSettings.put(HEALTH.name(), HEALTH.alpha());
        alphaSettings.put(HEALTH.name() + "_absorption", HEALTH.alpha());
        alphaSettings.put(FOOD.name(), FOOD.alpha());
        alphaSettings.put(FOOD.name() + "_saturation", FOOD.alpha());
        alphaSettings.put(FOOD.name() + "_exhaustion", ALPHA_DEFAULT);
        alphaSettings.put(ARMOR.name(), ARMOR.alpha());
        alphaSettings.put(AIR.name(), AIR.alpha());
        alphaSettings.put(MOUNT_HEALTH.name(), MOUNT_HEALTH.alpha());

        // * Side Order Settings * //
        sideOrderSettings.put("left", new ArrayList<>(Arrays.asList(HEALTH.name(), ARMOR.name())));
        sideOrderSettings.put("right", new ArrayList<>(Arrays.asList(FOOD.name(), AIR.name(), MOUNT_HEALTH.name())));

        // * Position Settings * //
        positionSettings.put("left_y_offset", LEFT_DEFAULT);
        positionSettings.put("right_y_offset", RIGHT_DEFAULT);
        positionSettings.put("left_x_offset", 0);
        positionSettings.put("right_x_offset", 0);
    }
}
