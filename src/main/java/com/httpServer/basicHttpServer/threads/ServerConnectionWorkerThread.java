package com.httpServer.basicHttpServer.threads;

import com.httpServer.http.HttpRequest;
import com.httpServer.http.HttpResponse;
import com.httpServer.http.exceptions.HttpParsingException;
import com.httpServer.http.utils.HttpRequestParser;
import com.httpServer.http.utils.HttpResponseGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Thread class to perform the actual parsing and response generation
 * for the request.
 */
@Slf4j
public class ServerConnectionWorkerThread extends Thread {

    private final Socket socket;
    private final HttpRequestParser parser;
    private final String webRoot;

    public ServerConnectionWorkerThread(Socket socket, String webRoot) {
        this.socket = socket;
        this.parser = new HttpRequestParser();
        this.webRoot = webRoot;
    }

    @Override
    public void run() {
        log.info("Connection processing started...");
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            HttpRequest request = parser.parseHttpRequest(inputStream);

            HttpResponseGenerator responseGenerator = new HttpResponseGenerator(request, webRoot);
            HttpResponse response = responseGenerator.getResponse();
            response.write(outputStream);
        }
        catch (IOException e) {
            log.error("Exception while getting input or output streams from socket ", e);
        }
        catch (HttpParsingException e) {
            log.error("Exception while parsing HTTP request ", e);
        }
        finally {
            // Close all streams and connections
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                socket.close();
            }
            catch (IOException e) {
                log.error("Exception while trying to close the input/output streams. ");
            }
        }

        log.info("Connection processing finished...");
    }
}
