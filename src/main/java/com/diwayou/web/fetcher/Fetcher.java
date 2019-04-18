package com.diwayou.web.fetcher;

import com.diwayou.web.domain.FetcherType;
import com.diwayou.web.domain.Page;
import com.diwayou.web.domain.Request;

public interface Fetcher extends AutoCloseable {

    Page fetch(Request request);

    FetcherType getType();
}
