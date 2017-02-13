package com.newtech.jobnow.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 07/02/2017.
 */

public class CustomTextViewLato extends TextView {
    public CustomTextViewLato(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "Lato-Bold.ttf"));
    }
}
