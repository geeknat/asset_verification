package com.sciasv.asv.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewAvenirRoman extends TextView {

    public TextViewAvenirRoman(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewAvenirRoman(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewAvenirRoman(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirLTStd-Roman.otf");
            setTypeface(tf);
        }
    }
}

