package com.kylenanakdewa.atlasmap.serializers.server;

import com.kylenanakdewa.atlasmap.serializers.PlayerEventData;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Holds data for player join events, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public class JoinGameEventData extends PlayerEventData {

    protected boolean first_join;

    public JoinGameEventData(PlayerJoinEvent event) {
        super(event);
        type = "join_game";
        event_message = ChatColor.stripColor(event.getJoinMessage());

        first_join = !event.getPlayer().hasPlayedBefore();
    }

}