package com.httpServer.http;

import com.httpServer.http.exceptions.HttpParsingException;
import com.httpServer.http.utils.HttpStatusCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequest extends HttpMessage {
    @Getter
    private HttpMethod method;

    @Getter @Setter
    private String requestTarget;

    @Getter @Setter
    private String httpVersion;

    public void setMethod(String method) throws HttpParsingException {
        try {
            for(HttpMethod m: HttpMethod.values()) {
                if(method.equals(m.name())) {
                    this.method = m;
                    return;
                }
            }
        }
        catch (Exception e) {
            log.error("Exception while setting METHOD for HttpRequest ", e);
        }
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED);
    }

}
