package com.sciasv.asv.listeners;

/**
 * Created by Geek Nat on 5/16/2016.
 */
public interface ConnectionListener {
    void onStart();

    void onComplete();

    void onSuccess(String result);

    void onError(String error);
}



