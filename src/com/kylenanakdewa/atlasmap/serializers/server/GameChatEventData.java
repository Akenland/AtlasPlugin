package com.kylenanakdewa.atlasmap.serializers.server;

import com.kylenanakdewa.atlasmap.serializers.PlayerEventData;

import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Holds data for chat events, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public class GameChatEventData extends PlayerEventData {

    protected String chat_message;

    public GameChatEventData(AsyncPlayerChatEvent event) {
        super(event);
        type = "game_chat";
        event_message = ChatColor
                .stripColor(String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));
        chat_message = ChatColor.stripColor(event.getMessage());
    }

}