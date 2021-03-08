package com.httpServer.http.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum HttpStatusCode {
    INFORMATION_100_CONTINUE(100, "Continue"),

    // SUCCESS STATUS CODES
    SUCCESS_200_OK(200, "OK"),
    SUCCESS_201_CREATED(201, "Created"),
    SUCCESS_202_ACCEPTED(202, "Accepted"),

    // CLIENT STATUS CODES
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_401_METHOD_NOT_ALLOWED(401, "Method Not Allowed"),
    CLIENT_ERROR_403_FORBIDDEN(403, "Forbidden"),
    CLIENT_ERROR_404_NOT_FOUND(404, "Not Found"),
    CLIENT_ERROR_414_URI_TOO_LONG(414, "URI Too Long"),

    // SERVER STATUS CODES
    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED(501, "Method Not Implemented"),
    ;

    public final int STATUS_CODE;
    public final String STATUS_MESSAGE;
}
