package com.kylenanakdewa.atlasmap.listeners;

import com.kylenanakdewa.atlasmap.serializers.server.GameChatEventData;
import com.kylenanakdewa.atlasmap.serializers.server.JoinGameEventData;
import com.kylenanakdewa.atlasmap.serializers.server.QuitGameEventData;
import com.kylenanakdewa.atlasmap.websocket.AtlasWebSocketClient;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listens for Bukkit game server events that do not relate to the game world,
 * including player joins/quits, and chat.
 *
 * @author Kyle Nanakdewa
 */
public class ServerEventListener extends AtlasListener {

    public ServerEventListener(AtlasWebSocketClient wsClient) {
        super(wsClient);
    }

    /**
     * Sends a chat message.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("atlas.invisible"))
            return;

        sendEventData(new GameChatEventData(event));
    }

    /**
     * Sends a player join.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("atlas.invisible"))
            return;

        sendEventData(new JoinGameEventData(event));
    }

    /**
     * Sends a player quit.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        if (event.getPlayer().hasPermission("atlas.invisible"))
            return;

        sendEventData(new QuitGameEventData(event));
    }

}