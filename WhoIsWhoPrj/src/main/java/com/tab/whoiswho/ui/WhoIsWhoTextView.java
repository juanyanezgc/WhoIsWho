package com.tab.whoiswho.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tab.whoiswho.R;

public class WhoIsWhoTextView extends TextView {

    public WhoIsWhoTextView(Context context) {
        super(context);
        setCustomFont(context, null, 0);
    }

    public WhoIsWhoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs, 0);
    }

    public WhoIsWhoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs, defStyle);

    }

    private void setCustomFont(Context context, AttributeSet attrs, int defStyle) {
        if (isInEditMode()) {
            return;
        }

        int typefaceValue = TypefaceManager.ROBOTO_REGULAR;

        if (attrs != null) {
            TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.WhoIsWhoTextView, defStyle, 0);
            if (values != null) {
                typefaceValue = values.getInt(R.styleable.WhoIsWhoTextView_typeface, typefaceValue);
                values.recycle();
            }
        }

        Typeface typeface = TypefaceManager.obtainTypeface(context, typefaceValue);
        setTypeface(typeface);

    }
}
