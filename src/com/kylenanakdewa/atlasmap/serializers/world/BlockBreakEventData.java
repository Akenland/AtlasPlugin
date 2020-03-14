package com.kylenanakdewa.atlasmap.serializers.world;

import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Holds data for block break events, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public class BlockBreakEventData extends BlockEventData {

    private String player_name;
    @SuppressWarnings("unused")
    private String player_username;

    public BlockBreakEventData(BlockBreakEvent event) {
        super(event);

        player_name = ChatColor.stripColor(event.getPlayer().getDisplayName());
        player_username = event.getPlayer().getName();

        type = "block_break";
        event_message = player_name + " broke " + material + " at " + world + " " + x + " " + y + " " + z;
    }

}