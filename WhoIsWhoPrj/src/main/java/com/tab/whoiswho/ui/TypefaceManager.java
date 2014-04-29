package com.tab.whoiswho.ui;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class TypefaceManager {
    public static final int ROBOTO_REGULAR = 0;
    public static final int ROBOTO_BOLD = 1;
    private static final int ROBOTO_ITALIC = 2;
    private static final int ROBOTO_BOLD_ITALIC = 3;

    private static final String ROBOTO_REGULAR_ASSET = "Roboto-Regular.ttf";
    private static final String ROBOTO_BOLD_ASSET = "Roboto-Bold.ttf";
    private static final String ROBOTO_ITALIC_ASSET = "Roboto-Italic.ttf";
    private static final String ROBOTO_BOLD_ITALIC_ASSET = "Roboto-BoldItalic.ttf";


    private static final Map<Integer,Typeface> mTypefaces = new HashMap<Integer, Typeface>(4);

    public static Typeface obtainTypeface(Context context, int typefaceValue){
        Typeface typeface = mTypefaces.get(typefaceValue);

        if (typeface == null){
            typeface = createTypeface(context,typefaceValue);
        }

        return typeface;
    }

    private static Typeface createTypeface(Context context, int typefaceValue) {

        Typeface typeface = null;

        switch (typefaceValue){
            case ROBOTO_REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(),ROBOTO_REGULAR_ASSET);
                break;
            case ROBOTO_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(),ROBOTO_BOLD_ASSET);
                break;
            case ROBOTO_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(),ROBOTO_ITALIC_ASSET);
                break;
            case ROBOTO_BOLD_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(),ROBOTO_BOLD_ITALIC_ASSET);
                break;
            default:
                throw new IllegalArgumentException("Unknown typeface attribute value " + typefaceValue);
        }
        return typeface;
    }
}
