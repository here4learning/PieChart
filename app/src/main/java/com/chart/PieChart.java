
package com.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

public class PieChart extends View implements ValueAnimator.AnimatorUpdateListener{

    private final float SHIFT_AMOUNT = 20f;
	private Context mcontext;
    private float outerRadius = 150f;
	private float innerRadius = 100;//0f;
	private boolean isHoleEnable = true;

	private float totalValue;

    private Paint mInnerPaint = new Paint();
	private List<SliceItem> mSliceItemList ;

	private float margin = 40;

	private float startAngle = 0;
	private float endAngle = 360;

	private RectF mOuterRect;

	private boolean isAnimationEnable = true;
	private long mAnimationDuration = 1500l;
	private float prevVal;
	private float interval;
	private float arcIncrement;

	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		mcontext = context;
		init();
	}

	public PieChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mcontext = context;
		init();
	}

	private void init(){
	}


	public void setInnerRadius(float innerRadius) {
		this.innerRadius = innerRadius;
		invalidate();
	}

	public void setHoleEnable(boolean bool) {
		this.isHoleEnable = bool;
		invalidate();
	}

	public void setAnimationEnable(boolean bool){
		this.isAnimationEnable = bool;
	}

	public void redraw(List<SliceItem> sliceItemList){
		mInnerPaint.setColor(Color.WHITE);
		mInnerPaint.setAntiAlias(true);

		if(sliceItemList!=null){
			mSliceItemList = sliceItemList;
			totalValue = 0;
			for(SliceItem item : sliceItemList){
				totalValue = totalValue + item.getPercentage();
			}

//			float startAngle = 0f;
//			for(SliceItem item : sliceItemList){
//				float angle = 360 * (item.getPercentage()/sum);
//				item.initAngle(startAngle,angle);
//				startAngle = startAngle + angle;
//				if(!isAnimationEnable)
//					item.setIncreasedBy(angle);
//			}
		}
        if(!isAnimationEnable){
			arcIncrement = 360;
		}

		invalidate();
		if(isAnimationEnable) {
			startToAnimate();
			isAnimationEnable = false;
		}

	}

	private float convertDpToPixel(float dp) {
		DisplayMetrics metrics = mcontext.getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int px = mcontext.getResources().getDisplayMetrics().widthPixels;//(int)convertDpToPixel(outerRadius);
		final int desiredWSpec = MeasureSpec.makeMeasureSpec(px, MeasureSpec.EXACTLY);
		this.setMeasuredDimension(desiredWSpec, desiredWSpec);
		super.onMeasure(desiredWSpec, desiredWSpec);

	}


	@Override
	protected void onDraw(Canvas canvas) {

		int height = getHeight() ;
		int width = getWidth();

		mOuterRect  = new RectF(margin  , margin, width - margin ,  width - margin );
		float startAngle = 0f;
		for(SliceItem item : mSliceItemList){
			float angle = arcIncrement * (item.getPercentage()/totalValue);
			item.initAngle(startAngle,angle);
			startAngle = startAngle + angle;
			item.setIncreasedBy(angle);

//			if(arcIncrement>=item.getStartAngle()) {
//				if (item.getIncreasedBy() + interval > item.getAngle()) {
//					item.setIncreasedBy(item.getAngle());
//				} else {
//					item.setIncreasedBy(item.getIncreasedBy() + interval);
//				}
//			}
			float dx = 0;
			float dy = 0;
			if(item.isSelected()){
				float fAngle = item.getStartAngle() +  item.getAngle() / 2;
				double dxRadius = Math.toRadians((fAngle + 360) % 360);
				float fY = (float) Math.sin(dxRadius);
				float fX = (float) Math.cos(dxRadius);
				dx = fX * SHIFT_AMOUNT;
				dy = fY * SHIFT_AMOUNT;

				RectF highlightedRect  = new RectF(margin + dx  , margin  + dy , width - margin + dx ,  width - margin + dy );
				canvas.drawArc(highlightedRect,item.getStartAngle(), item.getIncreasedBy(),true, item.getPaint());

			}else {
				canvas.drawArc(mOuterRect, item.getStartAngle(), item.getIncreasedBy(), true, item.getPaint());
			}
			if((int)item.getIncreasedBy()>=(int)item.getAngle()) {
				drawText(canvas, dx,dy,item.getStartAngle(), item.getAngle(), item.getSliceText());
			}

		}
		if(isHoleEnable)
		canvas.drawCircle(mOuterRect.centerX(),mOuterRect.centerY(),innerRadius,mInnerPaint);


	}

	private void drawText(Canvas canvas,float shiftX,float shiftY,float startAngle,float angle,SliceText sText) {
		Rect bounds = new Rect();
		Paint paint = sText.getPaint();
		paint.getTextBounds(sText.text, 0, sText.text.length(), bounds);
		float endAngle = startAngle + angle/2 ;
		double textWidth = bounds.width();
		double textHeight = bounds.height();
		double radians = Math.toRadians((360 + endAngle) % 360);
		float radius = mOuterRect.width()/2;
		float distance = radius/2;
		if(this.isHoleEnable){
			distance = innerRadius +  ((radius - innerRadius)/2) ;
		}
		float centerX = mOuterRect.centerX() + shiftX;
		float centerY = mOuterRect.centerY() + shiftY;

		float fX = (float) Math.cos(radians) * distance ;
		float fY = (float) Math.sin(radians) * distance;
		float midAngle = startAngle + angle/2;
		canvas.drawText(sText.text, (int) ((centerX + fX) - textWidth/2),	(int)  ((centerY + fY) + textHeight/2),paint);

	}

	private void startToAnimate() {
    	if (android.os.Build.VERSION.SDK_INT < 11)
			return;

		ValueAnimator vAnimator = ValueAnimator.ofFloat(startAngle, endAngle);
		vAnimator.setInterpolator(new DecelerateInterpolator());
		vAnimator.setDuration(mAnimationDuration);
		vAnimator.addUpdateListener(this);
		vAnimator.start();
	}

	@Override
	public void onAnimationUpdate(ValueAnimator valueAnimator) {
		float val = ((Float) valueAnimator.getAnimatedValue()).floatValue();
		interval = val - prevVal ;
		prevVal = val;
		arcIncrement = arcIncrement + interval;
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		double  pointY = event.getY() - mOuterRect.centerY();
		double  pointX =  event.getX() -  mOuterRect.centerX();

		double angle = Math.toDegrees(Math.atan2(pointY, pointX));
		if (angle < 0.0) {
			angle += 360.0;
		}

		double val = pointX * pointX + pointY * pointY;
		double radius = Math.sqrt(val);

		for(SliceItem item : mSliceItemList){
			float startPoint = isHoleEnable? this.innerRadius : 0;
			if( angle >= (double)item.getStartAngle() && angle <=(double)item.getEndAngle() && radius>startPoint &&radius<=mOuterRect.height()/2 ){
				item.setSelected(!item.isSelected());
			}else{
				item.setSelected(false);
			}
		}
		invalidate();

		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}
}
