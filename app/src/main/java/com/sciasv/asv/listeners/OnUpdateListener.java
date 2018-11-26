package com.sciasv.asv.listeners;

/**
 * Created by @GeekNat on 10/25/17.
 * All is possible
 */

public interface OnUpdateListener {
    void done(int position, String phone, String notes);

    void remove(int position);
}
