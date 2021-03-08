package com.httpServer.http;

import com.httpServer.http.utils.HttpStatusCode;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileHttpResponse extends HttpResponse {

    private File inputFile;

    public FileHttpResponse(String httpVersion, HttpStatusCode httpStatusCode, File inputFile) {
        super(httpVersion, httpStatusCode);
        this.inputFile = inputFile;

        try {
            this.setContentType();
            this.setContentLength();
        }
        catch (IOException e) {
            log.error("Error setting HTTP Content Type", e);
        }
    }

    private void setContentLength() {
        headers.put(CONTENT_LENGTH, String.valueOf(this.inputFile.length()));
    }

    private void setContentType() throws IOException {
        Path source = Paths.get(this.inputFile.toURI());
        String contentType = Files.probeContentType(source);
        if (contentType != null) {
            headers.put(CONTENT_TYPE, contentType);
        }
    }

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";

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

            if (inputFile != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
                char[] buffer = new char[1024];
                int read;
                while ((read = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, read);
                }
                reader.close();
            }

            writer.flush();
        } catch (IOException e) {
            log.error("Exception while writing HTTP Response to output stream ", e);
        }
    }
}
