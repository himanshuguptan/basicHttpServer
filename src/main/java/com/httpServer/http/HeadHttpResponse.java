package com.httpServer.http;

import com.httpServer.http.utils.HttpStatusCode;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

@Slf4j
public class HeadHttpResponse extends FileHttpResponse {

    public HeadHttpResponse(String httpVersion, HttpStatusCode httpStatusCode, File inputFile) {
        super(httpVersion, httpStatusCode, inputFile);
    }

    @Override
    public void write(OutputStream out) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(getResponseLine());
            writer.write("\r\n");

            for (String key: headers.keySet()) {
                writer.write(key + ":" + headers.get(key));
                writer.write("\r\n");
            }
            writer.write("\r\n");

            writer.flush();
        } catch (IOException e) {
            log.error("Exception while writing HTTP Response to output stream ", e);
        }
    }
}
