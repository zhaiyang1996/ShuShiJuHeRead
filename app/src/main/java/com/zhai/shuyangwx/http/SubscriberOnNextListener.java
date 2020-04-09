package com.zhai.shuyangwx.http;


public interface SubscriberOnNextListener<T> {
    void onNext(T t);

    void onError(Throwable e);

    void onCompleted();
}
