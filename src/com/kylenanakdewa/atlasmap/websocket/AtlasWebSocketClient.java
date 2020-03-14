package com.kylenanakdewa.atlasmap.websocket;

import java.io.IOException;
import java.net.URI;

import com.kylenanakdewa.atlasmap.AtlasPlugin;
import com.neovisionaries.ws.client.WebSocketException;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Websocket client for the Atlas map.
 *
 * @author Kyle Nanakdewa
 */
public class AtlasWebSocketClient extends PluginWebSocketClient implements Listener {

    /**
     * Creates a new websocket client, and opens the connection to the server.
     *
     * @param endpoint the websocket server to connect to
     * @param plugin   the plugin that owns this websocket client
     */
    public AtlasWebSocketClient(URI endpoint, AtlasPlugin plugin) throws IOException, WebSocketException {
        super(endpoint, plugin);
    }

    @Override
    public void onMessage(String message) {
        super.onMessage(message);

    }

    /**
     * Sends player movement.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        if (event.isCancelled() || event.getPlayer().hasPermission("atlas.invisible"))
            return;

        // Sneaking, invisibility check, minimum movement threshold
        if (event.getPlayer().isSneaking() || event.getPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)
                || event.getFrom().distanceSquared(event.getTo()) <= 0.02)
            return;

        // If player moved into new chunk, send blocks
        if (!event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            sendChunkBlocks(event.getTo().getChunk());
        }
    }

    /**
     * Sends an entire chunk of block changes.
     */
    public void sendChunkBlocks(Chunk chunk) {
        if (!chunk.isLoaded() /* || chunk.getInhabitedTime() < 200 */)
            return;

        Bukkit.broadcast("Sent chunk " + chunk.getX() + " " + chunk.getZ(), "core.admin");

        ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int y = chunkSnapshot.getHighestBlockYAt(x, z);

                for (; y > 0; y--) {
                    Material material = chunkSnapshot.getBlockType(x, y, z);
                    if (material.isBlock() && !material.isAir()) {

                        // make sure there is air on at least one face
                        if (chunkSnapshot.getBlockType(x, Math.max(y + 1, 255), z).isAir()
                                || chunkSnapshot.getBlockType(Math.min(x - 1, 0), y, z).isAir()
                                || chunkSnapshot.getBlockType(Math.max(x + 1, 15), y, z).isAir()
                                || chunkSnapshot.getBlockType(x, y, Math.min(z - 1, 0)).isAir()
                                || chunkSnapshot.getBlockType(x, y, Math.max(z + 1, 15)).isAir()) {

                            String materialString = material.getKey().getKey();
                            String world = chunkSnapshot.getWorldName();
                            int worldX = chunkSnapshot.getX() * 16 + x;
                            int worldZ = chunkSnapshot.getZ() * 16 + z;

                            String jsonString = "{\"type\": \"block_place\", \"material\": \"" + materialString
                                    + "\", \"world\": \"" + world + "\", \"x\": " + worldX + ", \"y\": " + y
                                    + ", \"z\": " + worldZ + "}";

                            sendMessage(jsonString);
                        }
                    }
                }
            }
        }
    }
}