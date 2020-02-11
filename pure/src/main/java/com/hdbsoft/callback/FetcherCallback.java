package com.hdbsoft.callback;

public interface FetcherCallback {
    void onData(Data data);
    void onError(Throwable cause);
}
