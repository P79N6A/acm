package com.diwayou.web.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringPage extends Page {

    private String body;

    public StringPage(Request request, String body) {
        super(request);
        this.body = body;
    }

    @Override
    public String bodyAsString() {
        return body;
    }

    @Override
    public InputStream bodyAsInputStream() {
        return new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public int statusCode() {
        return 200;
    }
}
