package com.kylenanakdewa.atlasmap.websocket;

import java.io.IOException;
import java.net.URI;

import com.kylenanakdewa.atlasmap.AtlasPlugin;
import com.neovisionaries.ws.client.WebSocketException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
     * Sends a chat message.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("atlas.invisible"))
            return;

        String chatString = ChatColor
                .stripColor(String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));

        String jsonString = "{\"type\": \"game_chat\", \"message\": \"" + chatString + "\"}";

        sendMessage(jsonString);
    }

    /**
     * Sends a player join.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("atlas.invisible"))
            return;

        String joinString = ChatColor.stripColor(event.getJoinMessage());

        String jsonString = "{\"type\": \"game_chat\", \"message\": \"" + joinString + "\"}";

        sendMessage(jsonString);
    }

    /**
     * Sends a player quit.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        if (event.getPlayer().hasPermission("atlas.invisible"))
            return;

        String quitString = ChatColor.stripColor(event.getQuitMessage());

        String jsonString = "{\"type\": \"game_chat\", \"message\": \"" + quitString + "\"}";

        sendMessage(jsonString);
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
     * Sends block place.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled() || !event.canBuild())
            return;

        sendBlockChange(event.getBlock(), BlockChangeType.PLACE);
    }

    /**
     * Sends block break.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        sendBlockChange(event.getBlock(), BlockChangeType.BREAK);
    }

    /**
     * Sends a block change.
     */
    public void sendBlockChange(Block block, BlockChangeType type) {
        // Check that block is visible to skylight
        if (block.getLightLevel() == 0 /* block.getLightFromSky() < 4 */)
            return;

        String typeString = "block_" + type.name().toLowerCase();

        String material = block.getType().getKey().getKey();
        String world = block.getLocation().getWorld().getName();
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();

        String jsonString = "{\"type\": \"" + typeString + "\", \"material\": \"" + material + "\", \"world\": \""
                + world + "\", \"x\": " + x + ", \"y\": " + y + ", \"z\": " + z + "}";

        sendMessage(jsonString);
    }

    /** Whether a block was placed or broken. */
    private enum BlockChangeType {
        PLACE, BREAK;
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

                for (; y > 0 /*
                              * && chunkSnapshot.getBlockEmittedLight(x, y, z) +
                              * chunkSnapshot.getBlockSkyLight(x, y, z) > 0
                              */; y--) {
                    Material material = chunkSnapshot.getBlockType(x, y, z);
                    if (material.isBlock() && !material.isAir()) {
                        String materialString = material.getKey().getKey();
                        String world = chunkSnapshot.getWorldName();

                        String jsonString = "{\"type\": \"block_place\", \"material\": \"" + materialString
                                + "\", \"world\": \"" + world + "\", \"x\": " + x + ", \"y\": " + y + ", \"z\": " + z
                                + "}";

                        sendMessage(jsonString);
                    }
                }
            }
        }
    }
}