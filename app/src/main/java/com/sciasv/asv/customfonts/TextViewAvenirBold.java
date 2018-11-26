package com.sciasv.asv.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewAvenirBold extends TextView {

    public TextViewAvenirBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewAvenirBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewAvenirBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNext-Bold-01.ttf");
            setTypeface(tf);
        }
    }
}

