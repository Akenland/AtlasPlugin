package com.kylenanakdewa.atlasmap.websocket;

import java.io.IOException;
import java.net.URI;

import com.neovisionaries.ws.client.WebSocketException;

import org.bukkit.plugin.Plugin;

/**
 * A simple websocket client for Bukkit plugins.
 *
 * @author Kyle Nanakdewa
 */
public class PluginWebSocketClient extends WebSocketClient {

    /**
     * Creates a new websocket client, and opens the connection to the server.
     *
     * @param endpoint the websocket server to connect to
     * @param plugin   the plugin that owns this websocket client
     */
    public PluginWebSocketClient(URI endpoint, Plugin plugin) throws IOException, WebSocketException {
        super(endpoint, plugin.getLogger());
    }

}