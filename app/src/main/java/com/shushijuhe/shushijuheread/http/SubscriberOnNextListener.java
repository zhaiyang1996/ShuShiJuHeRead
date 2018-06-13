package com.shushijuhe.shushijuheread.http;


public interface SubscriberOnNextListener<T> {
    void onNext(T t);

    void onError(Throwable e);

    void onCompleted();
}
