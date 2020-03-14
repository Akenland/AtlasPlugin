package com.kylenanakdewa.atlasmap.serializers.world;

import com.kylenanakdewa.atlasmap.serializers.EventData;

import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockEvent;

/**
 * Holds data for block events, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public abstract class BlockEventData extends EventData {

    protected String material;

    protected String world;
    protected int x;
    protected int y;
    protected int z;

    protected byte light_level;

    protected BlockEventData(BlockEvent event) {
        BlockState block = event.getBlock().getState();

        material = block.getType().getKey().getKey();

        world = block.getWorld().getName();
        x = block.getX();
        y = block.getY();
        z = block.getZ();

        light_level = block.getLightLevel();
    }

}