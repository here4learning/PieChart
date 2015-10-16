package com.chart;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by arusengu on 7/28/2015.
 */
public class SliceText {
    public String text;
    private int textColor ;
    private float strokeWidth;
    private Paint paint;
    public SliceText(String text,int textColor,float strokeWidth){
        this.text = text;
        this.textColor = textColor;
        this.strokeWidth = strokeWidth;
        initPaint();
    }

    public SliceText(String text,float strokeWidth){
        this(text, Color.WHITE,strokeWidth);
    }

    public SliceText(String text){
         this(text,25f);
    }

    private void initPaint(){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        paint.setTextSize(strokeWidth);
    }

    public Paint getPaint(){
        return paint;
    }
}
