package com.kylenanakdewa.atlasmap.listeners;

import com.kylenanakdewa.atlasmap.serializers.world.worlddata.ChunkData;
import com.kylenanakdewa.atlasmap.websocket.AtlasWebSocketClient;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Listens for Bukkit game server events for player movement.
 *
 * @author Kyle Nanakdewa
 */
public class PlayerMovementListener extends AtlasListener {

    public PlayerMovementListener(AtlasWebSocketClient wsClient) {
        super(wsClient);
    }

    /**
     * Sends block place.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled() || event.getPlayer().hasPermission("atlas.invisible"))
            return;

        // Sneaking, invisibility check, minimum movement threshold
        if (event.getPlayer().isSneaking() || event.getPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)
                || event.getFrom().distanceSquared(event.getTo()) <= 0.02)
            return;

        // If player moved into new chunk, send blocks
        if (!event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            sendData(new ChunkData(event.getTo().getChunk()));
        }
    }

}