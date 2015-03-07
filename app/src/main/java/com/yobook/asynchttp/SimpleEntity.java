package com.yobook.asynchttp;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 *
 */
public class SimpleEntity implements HttpEntity {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    public SimpleEntity(String content) throws IOException {
        out.write(content.getBytes(Charset.forName("UTF-8")));
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public long getContentLength() {
        return out.toByteArray().length;
    }

    @Override
    public Header getContentType() {
        return new BasicHeader("content-type", "application/json");
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(out.toByteArray());
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
                    "Streaming entity does not implement #consumeContent()");
        }
    }
}
