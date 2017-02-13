package com.newtech.jobnow.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 08/02/2017.
 */

public class CustomEditextHelveticaneue extends EditText {
    public CustomEditextHelveticaneue(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "helveticaneue.ttf"));
    }
}