package com.ratna.foosip;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ratna on 9/12/2016.
 */
public class CustomFont extends TextView {

    public CustomFont(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CustomFont(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public CustomFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Lato-Regular.ttf", context);
        setTypeface(customFont);
    }
}
