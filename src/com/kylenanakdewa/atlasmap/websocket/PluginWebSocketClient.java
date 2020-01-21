package com.kylenanakdewa.atlasmap.websocket;

import java.io.IOException;
import java.net.URI;
import javax.websocket.DeploymentException;

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
     * @throws DeploymentException if the annotated endpoint instance is not valid
     * @throws IOException         if there was a network or protocol problem that
     *                             prevented the client endpoint being connected to
     *                             its server
     */
    public PluginWebSocketClient(URI endpoint, Plugin plugin) throws DeploymentException, IOException {
        super(endpoint, plugin.getLogger());
    }

}