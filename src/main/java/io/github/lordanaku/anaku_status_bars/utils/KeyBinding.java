package io.github.lordanaku.anaku_status_bars.utils;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_BINDING_CATEGORY = "key.categories.anaku_status_bars";
    public static final String KEY_BINDING_MENU = "key.anaku_status_bars.menu";

    public static final KeyMapping KEY_BINDING_MENU_KEY = new KeyMapping(KEY_BINDING_MENU, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_BACKSLASH, KEY_BINDING_CATEGORY);
}
