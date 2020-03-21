package com.kylenanakdewa.atlasmap;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.kylenanakdewa.atlasmap.listeners.PlayerMovementListener;
import com.kylenanakdewa.atlasmap.listeners.ServerEventListener;
import com.kylenanakdewa.atlasmap.listeners.WorldListener;
import com.kylenanakdewa.atlasmap.websocket.AtlasWebSocketClient;

import com.neovisionaries.ws.client.WebSocketException;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for Atlas.
 *
 * @author Kyle Nanakdewa
 */
public final class AtlasPlugin extends JavaPlugin {

	/** The websocket URL (endpoint) for the Atlas server. */
	private URI wsUri;

	/** The websocket client. */
	private AtlasWebSocketClient wsClient;

	@Override
	public void onEnable() {
		// Main command
		getCommand("atlas").setExecutor(new AtlasCommands(this));

		// Run all reload tasks
		reload();
	}

	@Override
	public void onDisable() {
		// Close the websocket connection
		if (wsClient != null) {
			wsClient.close();
		}

		// Unregister all listeners
		HandlerList.unregisterAll(this);
	}

	/** Reloads the plugin. */
	void reload() {
		// Disable/cleanup
		onDisable();

		// Load config
		saveDefaultConfig();
		loadConfig();

		// Open websocket connection
		try {
			wsClient = new AtlasWebSocketClient(wsUri, this);
		} catch (IOException | WebSocketException e) {
			getLogger().warning("Failed to connect to websocket server: " + e.getLocalizedMessage());
		}

		if (wsClient != null) {
			// Register event listeners
			getServer().getPluginManager().registerEvents(new ServerEventListener(wsClient), this);
			getServer().getPluginManager().registerEvents(new WorldListener(wsClient), this);
			getServer().getPluginManager().registerEvents(new PlayerMovementListener(wsClient), this);
		}
	}

	/** Retrieve values from config. */
	private void loadConfig() {
		reloadConfig();

		try {
			wsUri = new URI(getConfig().getString("ws-url"));
		} catch (URISyntaxException e) {
			getLogger().warning("Invalid websocket URL: " + getConfig().getString("ws-url"));
		}
	}

	/**
	 * Gets the websocket client.
	 */
	public AtlasWebSocketClient getWsClient() {
		return wsClient;
	}

}