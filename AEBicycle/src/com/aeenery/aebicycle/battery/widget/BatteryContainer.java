package com.aeenery.aebicycle.battery.widget;

import com.aeenery.aebicycle.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.BitmapFactory.Options;

public class BatteryContainer extends View {

	Bitmap battery;
	Bitmap text;
	
	private float currentHeight = 0;
	private float MIN = 0f;
	private float MAX = 100f;
	private float currentValue = 0f;
	
	public BatteryContainer(Context context) {
		super(context);
		initBattery();
	}

	public BatteryContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initBattery();
	}

	private void initBattery() {
		Options opt = new Options();
		opt.inScaled = true;
		battery = BitmapFactory.decodeResource(getContext().getResources(),
				R.drawable.battery_2, opt);
		text = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tinybox,opt);
		setStartPosition();
		this.invalidate();
	}

	private void setStartPosition() {
		update(100f);
	}

	public void update(float f) {
		if(f < MIN) update(MIN);
		else if(f>MAX) update(MAX);
		else{
			currentValue = f;
			this.invalidate();
		}
			
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(battery, 0, 0, null);
		drawRegion(canvas, initGreen());
		canvas.drawBitmap(text, this.getWidth()*0.75f, currentHeight-(float)text.getHeight(),null);
		drawText(canvas, initBlack());
	}


	private void drawRegion(Canvas canvas, int color) {
		Paint paint = new Paint();
        RectF rect = new RectF();

        float height = (float) (this.getHeight() * 0.7);
        float bottum = height * 1.32f;
        float top = bottum - height * currentValue/100f;
        
        float widthleft = (float)(this.getWidth()*0.13);
        float widthright = (float)(this.getWidth()*0.635);
        
        
        rect.set(
        		widthleft,
        		top,
                widthright,
                bottum); 

        currentHeight = top;
        
        paint.setColor(color);
//        paint.setStrokeWidth(getResources().getDimension(R.dimen.stroke_width_thermo));
        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(rect, paint);
        canvas.drawRoundRect(rect, 10, 10, paint);
//        canvas.drawArc(rect, startConer, mainConer, false, paint);
	}

	
	private void drawText(Canvas canvas, int color){
		Paint paint = new Paint();
		paint.setColor(color);
//		paint.setStrokeWidth(getResources().getDimension(R.dimen.stroke_width_thermo));
		paint.setTextSize(28f);
		canvas.drawText(((int)currentValue)+"%", this.getWidth()*0.81f, currentHeight-(float)text.getHeight()+38f, paint);
	}
	
	private int initGreen() {
		return getResources().getColor(R.color.batterygreen);
	}

	private int initBlack() {
		return getResources().getColor(R.color.black);
	}
}
