package com.sciasv.asv.listeners;

import android.view.View;

/**
 * Created by Geek Nat on 6/2/2016.
 */
public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
