package com.httpServer.http;

import com.httpServer.http.utils.HttpStatusCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Slf4j
@Getter @Setter
public abstract class HttpResponse extends HttpMessage {

    protected String httpVersion;

    protected HttpStatusCode httpStatusCode;

    protected HashMap<String, String> headers;

    public HttpResponse(String httpVersion, HttpStatusCode httpStatusCode) {
        this.httpVersion = httpVersion;
        this.httpStatusCode = httpStatusCode;
        this.headers = new HashMap<>();
    }

    abstract public void write(OutputStream out);

    public String getResponseLine() {
        return httpVersion +
                SPACE +
                httpStatusCode.STATUS_CODE +
                SPACE +
                httpStatusCode.STATUS_MESSAGE;
    }

    private static final String SPACE = " ";
}
