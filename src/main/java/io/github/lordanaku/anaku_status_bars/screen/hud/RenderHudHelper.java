package io.github.lordanaku.anaku_status_bars.screen.hud;

import io.github.lordanaku.anaku_status_bars.utils.Settings;
import io.github.lordanaku.anaku_status_bars.utils.interfaces.IHudElement;

import java.util.*;

public class RenderHudHelper {
    private static final Map<String, IHudElement> registry = new HashMap<>();
    private static final List<IHudElement> hudElementRegistry = new ArrayList<>();
    private static int leftPosYIncrement;
    private static int rightPosYIncrement;
    private static int posXLeft;
    private static int posXRight;
    private static int posY;

    public static void setupHudElements() {
        hudElementRegistry.clear();
        for (String iHudElement : Settings.sideOrderSettings.get("left")) {
            hudElementRegistry.add(registry.get(iHudElement).setRenderSide(true));
        }
        for (String iHudElement : Settings.sideOrderSettings.get("right")) {
            hudElementRegistry.add(registry.get(iHudElement).setRenderSide(false));
        }
    }

    public static void registerHudElements(IHudElement... iHudElements) {
        Arrays.stream(iHudElements).forEach(hudElement -> registry.put(hudElement.name(), hudElement));
    }

    public static void setOrderDefaults(String id, boolean side) {
        if (side) {
            Settings.LEFT_ORDER_DEFAULT.add(id);
        } else {
            Settings.RIGHT_ORDER_DEFAULT.add(id);
        }
    }

    public static void resetIncrements() {
        leftPosYIncrement = -Settings.positionSettings.get("left_y_offset");
        rightPosYIncrement = -Settings.positionSettings.get("right_y_offset");
    }

    public static void setPosY (int posY) {
        RenderHudHelper.posY = posY;
    }

    public static void setPosYMod(boolean side, int amount) {
        if(side) {
            leftPosYIncrement += amount;
        } else {
            rightPosYIncrement += amount;
        }
    }

    public static void setPosX(boolean side, int amount) {
        if(side) {
            posXLeft = amount;
        } else {
            posXRight = amount;
        }
    }

    public static int getPosY() {
        return posY;
    }

    public static int getPosYMod(boolean side) {
        if(side) {
            return leftPosYIncrement;
        } else {
            return rightPosYIncrement;
        }
    }

    public static int getPosX(boolean side) {
        if(side) {
            return posXLeft;
        } else {
            return posXRight;
        }
    }
    public static ArrayList<IHudElement> getHudElementRegistry() {
        return (ArrayList<IHudElement>) hudElementRegistry;
    }
}
