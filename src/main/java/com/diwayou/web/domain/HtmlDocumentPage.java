package com.diwayou.web.domain;

import com.diwayou.web.support.DocumentUtil;
import org.w3c.dom.html.HTMLDocument;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

public class HtmlDocumentPage extends Page {

    private HTMLDocument document;

    private Set<String> resourceUrls;

    public HtmlDocumentPage(Request request, HTMLDocument document, Set<String> resourceUrls) {
        super(request);
        this.document = document;
        this.resourceUrls = resourceUrls;
    }

    @Override
    public String bodyAsString() {
        try {
            return DocumentUtil.toString(document);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public InputStream bodyAsInputStream() {
        return new ByteArrayInputStream(bodyAsString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public int statusCode() {
        return 200;
    }

    @Override
    public HttpHeaders getHttpHeaders() {
        return HttpHeaders.of(Collections.emptyMap(), (a, b) -> true);
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public Set<String> getResourceUrls() {
        return resourceUrls;
    }
}
