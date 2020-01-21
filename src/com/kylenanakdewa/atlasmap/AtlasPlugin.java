package com.kylenanakdewa.atlasmap;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.kylenanakdewa.atlasmap.websocket.AtlasWebSocketClient;

import com.neovisionaries.ws.client.WebSocketException;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for Atlas.
 *
 * @author Kyle Nanakdewa
 */
public final class AtlasPlugin extends JavaPlugin {

	/** The websocket URL (endpoint) for the Atlas server. */
	private URI websocketUri;

	/** The websocket client. */
	private AtlasWebSocketClient websocketClient;

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
		if (websocketClient != null)
			websocketClient.close();
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
			websocketClient = new AtlasWebSocketClient(websocketUri, this);
		} catch (IOException | WebSocketException e) {
			getLogger().warning("Failed to connect to websocket server: " + e.getLocalizedMessage());
		}

		// Register event listener
		getServer().getPluginManager().registerEvents(websocketClient, this);
	}

	/** Retrieve values from config. */
	private void loadConfig() {
		reloadConfig();

		try {
			websocketUri = new URI(getConfig().getString("ws-url"));
		} catch (URISyntaxException e) {
			getLogger().warning("Invalid websocket URL: " + getConfig().getString("ws-url"));
		}

	}

	/**
	 * Gets the websocket client.
	 */
	public AtlasWebSocketClient getWsClient() {
		return websocketClient;
	}

}