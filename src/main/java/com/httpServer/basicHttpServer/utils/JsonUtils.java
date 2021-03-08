package com.httpServer.basicHttpServer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * Method to parse a string and convert to JsonNode.
     * @param source the given string
     * @return required JsonNode
     * @throws JsonProcessingException if the given string can't be parsed into a JSONNode.
     */
    public static JsonNode parse(String source) throws JsonProcessingException {
        return mapper.readTree(source);
    }

    /**
     * Method to convert a given JSONNode to an object of a class.
     * @param node given JsonNode
     * @param toClass the class to which the JSONNode needs to be mapped to.
     * @param <T> template type
     * @return object of the required class
     * @throws JsonProcessingException if the given JSONNode can't be converted.
     */
    public static <T> T convertFromJson(JsonNode node, Class<T> toClass) throws JsonProcessingException {
        return mapper.treeToValue(node, toClass);
    }

}
