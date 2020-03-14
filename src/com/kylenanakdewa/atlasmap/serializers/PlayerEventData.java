package com.kylenanakdewa.atlasmap.serializers;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerEvent;

/**
 * Holds data for player events, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public abstract class PlayerEventData extends EventData {

    protected String player_name;
    protected String player_username;

    protected PlayerEventData(PlayerEvent event) {
        player_name = ChatColor.stripColor(event.getPlayer().getDisplayName());
        player_username = event.getPlayer().getName();
    }

}