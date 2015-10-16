package com.chart;

import java.util.ArrayList;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class ClientActivity extends Activity{

	PieChart prBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	setContentView(R.layout.activity_main);
	
	prBar=(PieChart)findViewById(R.id.progressArc);
	//prBar.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	//animateY(1500);
	ArrayList<SliceItem> mainArrayList=new ArrayList<SliceItem>();
	mainArrayList.add(new SliceItem(1f, Color.BLUE,"10"));
	mainArrayList.add(new SliceItem(2f, Color.RED,"20"));
	mainArrayList.add(new SliceItem(3f, Color.GRAY,"30"));
	mainArrayList.add(new SliceItem(4f, Color.GREEN,"40"));
	mainArrayList.add(new SliceItem(5f, Color.CYAN,"50"));
		mainArrayList.add(new SliceItem(6f, Color.DKGRAY,"60"));
		mainArrayList.add(new SliceItem(7f, Color.MAGENTA,"90"));
		mainArrayList.add(new SliceItem(8f, Color.LTGRAY,"80"));
		mainArrayList.add(new SliceItem(9f, Color.YELLOW,"70"));

		prBar.redraw(mainArrayList);

		
	}


	public void animateY(int durationMillis) {

		if (android.os.Build.VERSION.SDK_INT < 11)
			return;

		ValueAnimator animatorY = ValueAnimator.ofInt(0, 360);
		animatorY.setDuration(durationMillis);
		//animatorY.setRepeatCount(ObjectAnimator.INFINITE);
		//animatorY.addUpdateListener(prBar);
		animatorY.start();
	}


}
