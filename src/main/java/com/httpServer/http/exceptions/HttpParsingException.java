package com.httpServer.http.exceptions;

import com.httpServer.http.utils.HttpStatusCode;
import lombok.Getter;

public class HttpParsingException extends Exception {
    @Getter
    private final HttpStatusCode statusCode;

    public HttpParsingException(HttpStatusCode statusCode) {
        super(statusCode.STATUS_MESSAGE);
        this.statusCode = statusCode;
    }

}
