package com.kylenanakdewa.atlasmap.listeners;

import com.kylenanakdewa.atlasmap.serializers.EventData;
import com.kylenanakdewa.atlasmap.websocket.AtlasWebSocketClient;

import org.bukkit.event.Listener;

/**
 * Base listener class for Bukkit game server events.
 *
 * @author Kyle Nanakdewa
 */
public abstract class AtlasListener implements Listener {

    /** The websocket client. */
    private final AtlasWebSocketClient wsClient;

    protected AtlasListener(AtlasWebSocketClient wsClient) {
        this.wsClient = wsClient;
    }

    /**
     * Sends event data to the Atlas server.
     */
    protected void sendEventData(EventData eventData) {
        wsClient.sendMessage(eventData.toJson());
    }

}