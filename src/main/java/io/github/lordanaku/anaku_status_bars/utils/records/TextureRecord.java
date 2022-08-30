package io.github.lordanaku.anaku_status_bars.utils.records;

import net.minecraft.resources.ResourceLocation;

/**
 * Create a Texture Record.
 * @param texture ResourceLocation path to the texture.
 * @param startX The x position of the texture wanted.
 * @param startY The y position of the texture wanted.
 * @param width The width of the texture wanted.
 * @param height The height of the texture wanted.
 * @param maxWidth The width of the texture file.
 * @param maxHeight The height of the texture file.
 */
public record TextureRecord(ResourceLocation texture, int startX, int startY, int width, int height, int maxWidth, int maxHeight) {
}
