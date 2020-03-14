package com.kylenanakdewa.atlasmap.serializers.world;

import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Holds data for block place events, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public class BlockPlaceEventData extends BlockEventData {

    private String player_name;
    @SuppressWarnings("unused")
    private String player_username;

    public BlockPlaceEventData(BlockPlaceEvent event) {
        super(event);

        player_name = ChatColor.stripColor(event.getPlayer().getDisplayName());
        player_username = event.getPlayer().getName();

        type = "block_place";
        event_message = player_name + " placed " + material + " at " + world + " " + x + " " + y + " " + z;
    }

}