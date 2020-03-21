package com.kylenanakdewa.atlasmap.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;

/**
 * A simple websocket client.
 *
 * @author Kyle Nanakdewa
 */
public class WebSocketClient {

    /** The plugin instance. */
    private final Logger logger;

    /** The websocket. */
    private WebSocket websocket;

    /**
     * Creates a new websocket client, and opens the connection to the server.
     *
     * @param endpoint the websocket server to connect to
     * @param logger   the logger to send messages to
     */
    public WebSocketClient(URI endpoint, Logger logger) throws IOException, WebSocketException {
        this.logger = logger;

        WebSocketFactory factory = new WebSocketFactory();
        //factory.setVerifyHostname(false);
        //factory.setServerName("map.akenland.com");

        websocket = factory.createSocket(endpoint);
        websocket.addListener(listener);
        websocket.connect();
    }

    private WebSocketListener listener = new WebSocketAdapter() {
        @Override
        public void onConnected(WebSocket ws, Map<String, List<String>> headers) {
            onOpen(ws);
        }

        @Override
        public void onDisconnected(WebSocket ws, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                boolean closedByServer) {
            onClose(clientCloseFrame);
        }

        @Override
        public void onTextMessage(WebSocket ws, String message) {
            onMessage(message);
        }
    };

    /**
     * Fired when the websocket is opened.
     */
    public void onOpen(WebSocket ws) {
        logger.info("Connected to websocket server at " + ws.getURI());
    }

    /**
     * Fired when the websocket is closed.
     */
    public void onClose(WebSocketFrame reason) {
        logger.info("Disconnected from websocket server: " + reason.getCloseCode() + " " + reason.getCloseReason());
    }

    /**
     * Fired when a websocket message is received from the server.
     */
    public void onMessage(String message) {
        logger.info("Received message from websocket server: " + message);
    }

    /**
     * Sends a websocket message to the server.
     */
    public void sendMessage(String message) {
        websocket.sendText(message);
    }

    /**
     * Closes the websocket connection.
     */
    public void close() {
        websocket.disconnect();
    }

}