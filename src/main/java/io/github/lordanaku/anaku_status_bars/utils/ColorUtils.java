package io.github.lordanaku.anaku_status_bars.utils;

public class ColorUtils {
    private final int color;

    private ColorUtils(int c) {
        this.color = c;
    }

    public static ColorUtils fromHex(int color) {
        return new ColorUtils(0xFF000000 | color);
    }

    public float getRedF() { return (color >> 16 & 0xFF)/255f; }

    public float getGreenF() { return (color >> 8 & 0xFF)/255f;}

    public float getBlueF() { return (color & 0xFF)/255f; }

    @SuppressWarnings("unused")
    public float getAlphaF() { return (color >> 24 & 0xFF)/255f; }
}
