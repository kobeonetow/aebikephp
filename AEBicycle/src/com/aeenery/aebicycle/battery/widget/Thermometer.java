package com.aeenery.aebicycle.battery.widget;

import com.aeenery.aebicycle.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Thermometer extends View {

	Bitmap thermometer;

	private float currentValue = 0;

	private static final float MIN_TEMP = -20f;
	private static final float MAX_TEMP = 120f;
	
	public Thermometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initThermometer();
	}

	public Thermometer(Context context) {
		super(context);
		initThermometer();
	}

	private void initThermometer() {
		Options opt = new Options();
		opt.inScaled = true;
		thermometer = BitmapFactory.decodeResource(getContext().getResources(),
				R.drawable.thermometer_only, opt);
		setStartPosition();
		this.invalidate();
	}

	private void setStartPosition() {
		update(MIN_TEMP);
	}

	public void update(float value) {
		if (value < MIN_TEMP)
			update(MIN_TEMP);
		else if (value > MAX_TEMP)
			update(MAX_TEMP);
		else{
			currentValue = value;
			this.invalidate();
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		canvas.drawBitmap(thermometer, 0, 0, null);
		drawRegion(canvas, initRed());
		canvas.drawBitmap(thermometer, 0, 0, null);
	}

	private void drawRegion(Canvas canvas, int color) {
		Paint paint = new Paint();
        RectF rect = new RectF();

        float height = (float) (this.getHeight() * 0.9 - 22.5f);
        float cylinderHeight = 0f;
        if(currentValue <= 0f){
//        	cylinderHeight = height - 97f;//97f is 0C
        	cylinderHeight = height + (97f*currentValue/20f);//97f is 0C
        }else if(currentValue >= 100f){
        	//638 is the max value
        	//568f is 100C
        	cylinderHeight = height - 568f - ((638f-568f)*(currentValue - 100f)/20f);
        }else{
        	cylinderHeight = height - 97f - ((568f - 97f)*currentValue/100f);
        }
        
        System.out.println(height + ","  +  cylinderHeight);
        float widthleft = (float)(this.getWidth()*0.35);
        float widthright = (float)(this.getWidth()*0.58);
        rect.set(
        		widthleft,
        		cylinderHeight,
                widthright,
                height); 

        paint.setColor(color);
//        paint.setStrokeWidth(getResources().getDimension(R.dimen.stroke_width_thermo));
        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect, paint);
//        canvas.drawArc(rect, startConer, mainConer, false, paint);
	}

	private int initRed() {
		return getResources().getColor(R.color.red);
	}

}
