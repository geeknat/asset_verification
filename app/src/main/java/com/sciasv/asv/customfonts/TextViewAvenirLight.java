package com.sciasv.asv.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewAvenirLight extends TextView {

    public TextViewAvenirLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewAvenirLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewAvenirLight(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirLTStd-Light.otf");
            setTypeface(tf);
        }
    }
}

