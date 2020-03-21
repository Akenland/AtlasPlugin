package com.kylenanakdewa.atlasmap.serializers.world.worlddata;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Holds data for blocks, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public class BlockData {

    /** The material type (id) of this block. */
    @SuppressWarnings("unused")
    private String material;

    public BlockData(Block block) {
        this(block.getType());
    }

    public BlockData(Material material) {
        this.material = material.getKey().getKey();
    }

}