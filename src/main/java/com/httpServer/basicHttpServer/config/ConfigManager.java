package com.httpServer.basicHttpServer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.httpServer.basicHttpServer.config.exceptions.HttpConfigException;
import com.httpServer.basicHttpServer.utils.JsonUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Singleton class that holds the configuration information
 * for the HTTP server
 */
public class ConfigManager {
    private static ConfigManager configManager;
    private static Config config;

    private ConfigManager() {
    }

    private String readConfigFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(filePath);
            int in;
            while((in = fileReader.read()) != -1) {
                sb.append((char)in);
            }

            fileReader.close();
        }
        catch (FileNotFoundException e) {
            throw new HttpConfigException("Could not find HTTP Server config file.");
        }
        catch (IOException e) {
            throw new HttpConfigException("Could not read HTTP Server config file.");
        }
        return sb.toString();
    }

    public static ConfigManager getInstance() {
        if(configManager == null)
            configManager = new ConfigManager();
        return configManager;
    }

    /**
     * Method to load the HTTP Server Config file.
     * @param filePath path of the config file
     */
    public void loadConfigFromFile(String filePath) {
        String configString = readConfigFile(filePath);

        JsonNode configInJson;
        try {
            configInJson = JsonUtils.parse(configString);
        }
        catch (JsonProcessingException e) {
            throw new HttpConfigException("Could not parse HTTP Server config file.");
        }
        try {
            config = JsonUtils.convertFromJson(configInJson, Config.class);
        }
        catch (JsonProcessingException e) {
            throw new HttpConfigException("Could not convert HTTP Server config file to Config class.");
        }
    }

    /**
     * Method to return the Config for the HTTP Server.
     */
    public Config getConfig() {
        if(config == null) {
            throw new HttpConfigException("Missing configuration for the HTTP Server. ");
        }
        return config;
    }
}
