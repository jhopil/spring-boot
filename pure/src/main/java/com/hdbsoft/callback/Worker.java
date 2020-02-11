package com.hdbsoft.callback;

public class Worker {

    public void doWork() {
        Fetcher fetcher = new Fetcher() {
            @Override
            public void fetchData(FetcherCallback callback) {
                for(int idx = 0; idx < 10; ++idx) {
                    callback.onData(new Data());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        fetcher.fetchData(new FetcherCallback() {

            @Override
            public void onData(Data data) {
                System.out.println(data);
            }

            @Override
            public void onError(Throwable cause) {
                System.out.println(cause);
            }
        });
    }

}
