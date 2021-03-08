package com.httpServer.http;

import com.httpServer.http.exceptions.HttpParsingException;
import com.httpServer.http.utils.HttpRequestParser;
import com.httpServer.http.utils.HttpStatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.httpServer.http.HttpParserTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class HttpRequestParserTest {

    @BeforeEach
    public void setup() {
        httpRequestParser = new HttpRequestParser();
    }

    @Test
    void testParseHttpRequest_validRequest() {
        HttpRequest request = null;
        try {
            request = httpRequestParser.parseHttpRequest(generateTestCase_validRequest());
        }
        catch (HttpParsingException e) {
            fail();
        }

        assertEquals(request.getMethod(), HttpMethod.GET);
    }

    @Test
    void testParseHttpRequest_badMethodName() {
        try {
            httpRequestParser.parseHttpRequest(generateTestCase_badMethodName());
            fail();
        }
        catch (HttpParsingException e) {
            assertEquals(e.getStatusCode(), HttpStatusCode.SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED);
        }
    }

    @Test
    void testParseHttpRequest_methodNameTooLong() {
        try {
            httpRequestParser.parseHttpRequest(generateTestCase_methodNameTooLong());
            fail();
        }
        catch (HttpParsingException e) {
            assertEquals(e.getStatusCode(), HttpStatusCode.SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED);
        }
    }

    @Test
    void testParseHttpRequest_invalidItemsInRequestLine() {
        try {
            httpRequestParser.parseHttpRequest(generateTestCase_invalidItemsInRequestLine());
            fail();
        }
        catch (HttpParsingException e) {
            assertEquals(e.getStatusCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void testParseHttpRequest_emptyRequestLine() {
        try {
            httpRequestParser.parseHttpRequest(generateTestCase_emptyRequestLine());
            fail();
        }
        catch (HttpParsingException e) {
            assertEquals(e.getStatusCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void testParseHttpRequest_missingLFInRequestLine() {
        try {
            httpRequestParser.parseHttpRequest(generateTestCase_missingLFInRequestLine());
            fail();
        }
        catch (HttpParsingException e) {
            assertEquals(e.getStatusCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void testParseHttpRequest_validRequestTarget() {
        try {
            HttpRequest request = httpRequestParser.parseHttpRequest(generateTestCase_validRequestTarget());
            assertEquals(request.getMethod(), HttpMethod.GET);
            assertEquals(request.getRequestTarget(), "/newFile/testPage.html");
            assertEquals(request.getHttpVersion(), "HTTP/1.1");
        }
        catch (HttpParsingException e) {
            e.printStackTrace();
            fail();
        }
    }

    private HttpRequestParser httpRequestParser;
}