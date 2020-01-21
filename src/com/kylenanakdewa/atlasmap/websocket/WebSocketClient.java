package com.kylenanakdewa.atlasmap.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * A simple websocket client.
 *
 * @author Kyle Nanakdewa
 */
@ClientEndpoint
public class WebSocketClient {

    /** The plugin instance. */
    private final Logger logger;

    /** The websocket session. */
    private Session session;

    /**
     * Creates a new websocket client, and opens the connection to the server.
     *
     * @param endpoint the websocket server to connect to
     * @param logger   the logger to send messages to
     * @throws DeploymentException if the annotated endpoint instance is not valid
     * @throws IOException         if there was a network or protocol problem that
     *                             prevented the client endpoint being connected to
     *                             its server
     */
    public WebSocketClient(URI endpoint, Logger logger) throws DeploymentException, IOException {
        this.logger = logger;

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, endpoint);
    }

    /**
     * Fired when the websocket is opened.
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;

        String secureMsg = session.isSecure() ? "secure" : "insecure";
        logger.info("Connected to " + secureMsg + " websocket server at " + session.getRequestURI() + " with ID "
                + session.getId());
    }

    /**
     * Fired when the websocket is closed.
     */
    @OnClose
    public void onClose(CloseReason reason) {
        logger.info("Disconnected from websocket server: " + reason.getCloseCode() + " " + reason.getReasonPhrase());
    }

    /**
     * Fired when a websocket message is received from the server.
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info("Received message from websocket server: " + message);
    }

    /**
     * Sends a websocket message to the server.
     */
    public void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }

    /**
     * Closes the websocket connection.
     */
    public void close() {
        try {
            session.close();
        } catch (IOException e) {
            logger.warning("Failed to close websocket connection: " + e.getLocalizedMessage());
        }
    }

}