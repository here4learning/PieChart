package com.chart;

import android.graphics.Paint;

/**
 * Created by arusengu on 7/28/2015.
 */
public class SliceItem {
    private float startAngle;
    private float endAngle;
    private float angle;
    private float percentage;
    private SliceText sliceText;
    private int sliceColor;
    private float increasedBy;
    private boolean isSelected;

    private Paint paint;
    public SliceItem(float percentage,int sliceColor,SliceText sliceText){
        this.percentage = percentage;
        this.sliceColor = sliceColor;
        this.sliceText = sliceText;
        initPaint();
    }

    public SliceItem(float percentage,int sliceColor,String sliceText){
        this(percentage, sliceColor, new SliceText(sliceText));
    }

    public SliceItem(float percentage,int sliceColor,String sliceText,int textColor){
        this(percentage, sliceColor, new SliceText(sliceText, textColor));
    }

    public void initAngle(float startAngle,float angle){
        this.startAngle = startAngle;
        this.angle = angle;
        this.endAngle = startAngle + angle;
    }

    public float getPercentage(){
        return percentage;
    }

    public float getStartAngle(){
        return startAngle;
    }
    public float getEndAngle() {
        return endAngle;
    }
    public float getAngle(){
        return angle;
    }

    private void initPaint(){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(sliceColor);
        paint.setAntiAlias(true);
    }

    public Paint getPaint(){
        return paint;
    }

    public SliceText getSliceText(){
        return sliceText;
    }

    public void setIncreasedBy(float increasedBy){
        this.increasedBy = increasedBy;
    }

    public float getIncreasedBy(){
        return increasedBy;
    }

    public void setSelected(boolean bool){
        this.isSelected = bool;
    }

    public boolean isSelected(){
        return isSelected;
    }


}
