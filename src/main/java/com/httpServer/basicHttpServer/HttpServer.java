package com.httpServer.basicHttpServer;

import com.httpServer.basicHttpServer.config.Config;
import com.httpServer.basicHttpServer.config.ConfigManager;
import com.httpServer.basicHttpServer.threads.ServerConnectionSetupThread;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Main class for the HTTP server
 * The server needs to accomplish the following basic tasks:
 * - Read configuration files
 * - Open a socket to listen at a port
 * - Read HTTP Request messages
 * - Open & read files from the Filesystem for the files browser asks
 * - Write HTTP Response messages
 */
@Slf4j
public class HttpServer {
    private static ConfigManager CONFIG_MANAGER = ConfigManager.getInstance();

    public static void main(String[] args) {
        log.info("Starting HTTP Server...");

        CONFIG_MANAGER.loadConfigFromFile(CONFIG_FILE_PATH);
        Config config = CONFIG_MANAGER.getConfig();

        log.info("Using port: " + config.getPort());
        log.info("Using webRoot: " + config.getWebRoot());

        try {
            ServerConnectionSetupThread serverConnectionSetupThread = new ServerConnectionSetupThread(config.getPort(),
                    config.getWebRoot());
            serverConnectionSetupThread.start();
        }
        catch (IOException e) {
            log.error("Exception while setting up server socket connection ", e);
        }
    }

    private static final String CONFIG_FILE_PATH = "src/main/resources/httpServerConfig.json";
}
