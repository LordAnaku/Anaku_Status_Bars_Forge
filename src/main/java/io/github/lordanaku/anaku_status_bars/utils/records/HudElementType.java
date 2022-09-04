package io.github.lordanaku.anaku_status_bars.utils.records;

/**
 * Create a Hud Element Type.
 * (default values)
 * @param name The name of the Hud Element Type.
 * @param shouldRender Whether the Hud Element should be rendered.
 * @param side What side the Hud Element should be rendered on. (True = Left)
 * @param shouldRenderIcon Whether the Hud Element should render an icon.
 * @param color The color of the Hud Element.
 * @param alpha The alpha of the Hud Element.
 */
public record HudElementType(String name, boolean shouldRender, boolean side, boolean shouldRenderIcon, boolean shouldRenderText, int color, float alpha) {

}
