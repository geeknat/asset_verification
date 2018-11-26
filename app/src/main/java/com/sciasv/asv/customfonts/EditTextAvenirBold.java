package com.sciasv.asv.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextAvenirBold extends EditText {
    public EditTextAvenirBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextAvenirBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextAvenirBold(Context context) {
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
