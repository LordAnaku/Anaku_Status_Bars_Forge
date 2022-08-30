package io.github.lordanaku.anaku_status_bars.network;

import io.github.lordanaku.anaku_status_bars.AnakuStatusBarsCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/* * Credit Ryan Liptak and Squeek (AppleSkin) * */
public class SyncHandler
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(AnakuStatusBarsCore.MOD_ID, "sync"))
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void init()
    {
        CHANNEL.registerMessage(1, MessageExhaustionSync.class, MessageExhaustionSync::encode, MessageExhaustionSync::decode, MessageExhaustionSync::handle);
        CHANNEL.registerMessage(2, MessageSaturationSync.class, MessageSaturationSync::encode, MessageSaturationSync::decode, MessageSaturationSync::handle);

        MinecraftForge.EVENT_BUS.register(new SyncHandler());
    }

    /*
     * Sync saturation (vanilla MC only syncs when it hits 0)
     * Sync exhaustion (vanilla MC does not sync it at all)
     */
    private static final Map<UUID, Float> lastSaturationLevels = new HashMap<>();
    private static final Map<UUID, Float> lastExhaustionLevels = new HashMap<>();

    @SubscribeEvent
    public void onLivingTickEvent(LivingEvent.LivingTickEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;

        Float lastSaturationLevel = lastSaturationLevels.get(player.getUUID());
        Float lastExhaustionLevel = lastExhaustionLevels.get(player.getUUID());

        if (lastSaturationLevel == null || lastSaturationLevel != player.getFoodData().getSaturationLevel())
        {
            Object msg = new MessageSaturationSync(player.getFoodData().getSaturationLevel());
            CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            lastSaturationLevels.put(player.getUUID(), player.getFoodData().getSaturationLevel());
        }

        float exhaustionLevel = player.getFoodData().getExhaustionLevel();
        if (lastExhaustionLevel == null || Math.abs(lastExhaustionLevel - exhaustionLevel) >= 0.01f)
        {
            Object msg = new MessageExhaustionSync(exhaustionLevel);
            CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            lastExhaustionLevels.put(player.getUUID(), exhaustionLevel);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer))
            return;

        lastSaturationLevels.remove(event.getEntity().getUUID());
        lastExhaustionLevels.remove(event.getEntity().getUUID());
    }
}

