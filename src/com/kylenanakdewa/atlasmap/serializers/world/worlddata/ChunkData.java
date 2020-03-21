package com.kylenanakdewa.atlasmap.serializers.world.worlddata;

import java.util.HashMap;
import java.util.Map;

import com.kylenanakdewa.atlasmap.serializers.Data;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;

/**
 * Holds data for chunks, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public class ChunkData extends Data {

    /** The world that this chunk is in. */
    @SuppressWarnings("unused")
    private String world;

    /** The chunk x-coordinate of this chunk. Not a block coordinate. */
    @SuppressWarnings("unused")
    private int chunkX;
    /** The chunk z-coordinate of this chunk. Not a block coordinate. */
    @SuppressWarnings("unused")
    private int chunkZ;

    /** The blocks in this chunk. */
    private Map<String, BlockData> blocks;

    public ChunkData(Chunk chunk) {
        type = "set_chunk";

        world = chunk.getWorld().getName();
        chunkX = chunk.getX();
        chunkZ = chunk.getZ();

        blocks = new HashMap<String, BlockData>();

        ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                for (int y = 0; y < 256; y++) {

                    Material blockMaterial = chunkSnapshot.getBlockType(localX, y, localZ);

                    if (isMaterialAllowed(blockMaterial)) {
                        BlockData blockData = new BlockData(blockMaterial);

                        blocks.put(localX + "," + y + "," + localZ, blockData);
                    }

                }
            }
        }
    }

    /**
     * Checks if a material is allowed on the Atlas map.
     */
    private static boolean isMaterialAllowed(Material material) {
        // Check if block
        if (!material.isBlock() || !material.isSolid())
            return false;

        return true;
    }

}