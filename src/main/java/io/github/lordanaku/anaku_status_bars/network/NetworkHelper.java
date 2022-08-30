package io.github.lordanaku.anaku_status_bars.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

/* * Credit Ryan Liptak and Squeek (AppleSkin) * */
public class NetworkHelper
{
	public static Player getSidedPlayer(NetworkEvent.Context ctx)
	{
		return ctx.getDirection() == NetworkDirection.PLAY_TO_SERVER ? ctx.getSender() : Minecraft.getInstance().player;
	}
}