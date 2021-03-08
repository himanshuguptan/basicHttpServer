package com.httpServer.http.utils;

import com.httpServer.http.EmptyHttpResponse;
import com.httpServer.http.FileHttpResponse;
import com.httpServer.http.HeadHttpResponse;
import com.httpServer.http.HttpRequest;
import com.httpServer.http.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class to generate HTTP response for a given HTTP Request depending upon
 * the type of the request method.
 */
@AllArgsConstructor
@Slf4j
public class HttpResponseGenerator {
    private HttpRequest request;
    private String webRoot;

    /**
     * Method to get response for a given HTTP Request depending upon
     * the type of the request method.
     * Currently, the server handles only GET & HEAD requests.
     * @return HttpResponse object
     */
    public HttpResponse getResponse() {
        HttpResponse response;
        switch (request.getMethod()) {
            case GET:
                response = getResponseForGETRequest();
                break;

            case HEAD:
                response = getResponseForHEADRequest();
                break;

            default:
                response = new EmptyHttpResponse(request.getHttpVersion(),
                        HttpStatusCode.SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED);
                break;
        }
        return response;
    }

    /**
     * Method to get response for a given HTTP GET Request
     * @return HTTPResponse object
     */
    private HttpResponse getResponseForGETRequest() {
        HttpResponse response;

        String path = request.getRequestTarget();
        String httpVersion = request.getHttpVersion();
        Path requestedFile = Paths.get(webRoot, path);

        if (requestedFile.normalize().startsWith(Paths.get(webRoot).normalize())) {
            if (Files.exists(requestedFile)) {
                if (Files.isDirectory(requestedFile)) {
                    response = new EmptyHttpResponse(httpVersion, HttpStatusCode.CLIENT_ERROR_403_FORBIDDEN);
                } else {
                    response = new FileHttpResponse(httpVersion, HttpStatusCode.SUCCESS_200_OK,
                            new File(Paths.get(webRoot, path).toString()));
                }
            } else {
                response = new EmptyHttpResponse(httpVersion, HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND);
            }
        } else {
            response = new EmptyHttpResponse(httpVersion, HttpStatusCode.CLIENT_ERROR_403_FORBIDDEN);
        }

        return response;
    }

    /**
     * Method to get response for a given HTTP HEAD Request
     * @return HTTPResponse object
     */
    private HttpResponse getResponseForHEADRequest() {
        HttpResponse response;

        String path = request.getRequestTarget();
        String httpVersion = request.getHttpVersion();

        if (Files.exists(Paths.get(webRoot, path))) {
            response = new HeadHttpResponse(httpVersion, HttpStatusCode.SUCCESS_200_OK,
                    new File(Paths.get(webRoot, path).toString()));
        } else {
            response = new EmptyHttpResponse(httpVersion, HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND);
        }

        return response;
    }

}
