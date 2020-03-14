package com.kylenanakdewa.atlasmap.serializers.server;

import com.kylenanakdewa.atlasmap.serializers.PlayerEventData;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Holds data for player quit events, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public class QuitGameEventData extends PlayerEventData {

    public QuitGameEventData(PlayerQuitEvent event) {
        super(event);
        type = "quit_game";
        event_message = ChatColor.stripColor(event.getQuitMessage());
    }

}