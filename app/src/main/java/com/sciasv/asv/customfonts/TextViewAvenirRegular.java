package com.sciasv.asv.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewAvenirRegular extends TextView {

    public TextViewAvenirRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewAvenirRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewAvenirRegular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirLTStd-Book.otf");
            setTypeface(tf);
        }
    }
}

