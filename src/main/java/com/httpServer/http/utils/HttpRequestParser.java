package com.httpServer.http.utils;

import com.httpServer.http.HttpMethod;
import com.httpServer.http.HttpRequest;
import com.httpServer.http.exceptions.HttpParsingException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Class to parse a given HTTP Request based on the HTTP Protocol specifications
 */
@Slf4j
public class HttpRequestParser {

    /**
     * Method to parse a given input stream into a valid HTTP Request.
     * @param inputStream given input stream comprising of the HTTP request info
     * @return the required HTTP request object
     * @throws HttpParsingException if the given request is invalid, bad or is not parsable.
     */
    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
        }
        catch (IOException e) {
            log.info("Exception while parsing request line for Http Request ", e);
        }
        return request;
    }

    /**
     * Method to parse HTTP Request line as per the HTTP protocol specs.
     * @param reader given input stream comprising of the HTTP request info
     * @param request HTTP request object
     * @throws IOException if unable to work with input stream
     * @throws HttpParsingException if the given request is invalid, bad or is not parsable.
     */
    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException,
            HttpParsingException {
        HashMap<String, String> requestLine = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        int _byte;

        while ((_byte = reader.read()) >= 0) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    requestLine.put(VERSION_FIELD, sb.toString());
                    if (!requestLine.containsKey(METHOD_FIELD) || !requestLine.containsKey(REQ_TARGET_FIELD)) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    break;
                }
            }
            if (_byte == SP) {
                if (!requestLine.containsKey(METHOD_FIELD)) {
                    requestLine.put(METHOD_FIELD, sb.toString());
                } else if (!requestLine.containsKey(REQ_TARGET_FIELD)) {
                    requestLine.put(REQ_TARGET_FIELD, sb.toString());
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                sb.delete(0, sb.length());
            } else {
                sb.append((char) _byte);
                if (!requestLine.containsKey(METHOD_FIELD)) {
                    if (sb.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED);
                    }
                }
            }
        }

        request.setMethod(requestLine.get(METHOD_FIELD));
        request.setRequestTarget(requestLine.get(REQ_TARGET_FIELD));
        request.setHttpVersion(requestLine.get(VERSION_FIELD));
    }

    private static final int CR = 0x0D;
    private static final int LF = 0x0A;
    private static final int SP = 0x20;
    private static final String METHOD_FIELD = "METHOD";
    private static final String REQ_TARGET_FIELD = "REQUEST_TARGET";
    private static final String VERSION_FIELD = "VERSION";
}
