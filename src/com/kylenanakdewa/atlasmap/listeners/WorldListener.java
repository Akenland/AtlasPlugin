package com.kylenanakdewa.atlasmap.listeners;

import com.kylenanakdewa.atlasmap.serializers.world.BlockBreakEventData;
import com.kylenanakdewa.atlasmap.serializers.world.BlockPlaceEventData;
import com.kylenanakdewa.atlasmap.websocket.AtlasWebSocketClient;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Listens for Bukkit game server events that relate to the game world,
 * including blocked placed or broken.
 *
 * @author Kyle Nanakdewa
 */
public class WorldListener extends AtlasListener {

    public WorldListener(AtlasWebSocketClient wsClient) {
        super(wsClient);
    }

    /**
     * Sends block place.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled() || !event.canBuild())
            return;

        sendData(new BlockPlaceEventData(event));
    }

    /**
     * Sends block break.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        sendData(new BlockBreakEventData(event));
    }

}