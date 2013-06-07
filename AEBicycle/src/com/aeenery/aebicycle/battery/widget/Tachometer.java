package com.aeenery.aebicycle.battery.widget;

import com.aeenery.aebicycle.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Tachometer extends View {
    public static final String TAG = Tachometer.class.getSimpleName();

    private Bitmap tachometer;
    private Bitmap arrow;

    private float rotateDegree = 0;
    private float currentValue = 0;
    private float pxDp = getResources().getDimension(R.dimen.one_dp);

    private static final float MB = 214f/100f;
    private static final float MIN_SPEED = 0f;
    private static final float MAX_SPEED = 100f;


    public Tachometer(Context context) {
        super(context);
        initTachometer();
    }

   public Tachometer (Context context, AttributeSet attrs) {
        super(context, attrs);
        initTachometer();
    }

    private void initTachometer () {
        Options opt = new Options();
        opt.inScaled = true;
        tachometer = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tachometer_custom_power, opt);
        arrow = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.arrow_custom , opt);
        setStartPosition();
        this.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree, arrow.getWidth() / 2, arrow.getHeight() - 7.5f*pxDp);
        matrix.postTranslate((tachometer.getWidth()/2) - (arrow.getWidth()/2), 25*pxDp);

        drawRegion_1(canvas, initRed());
        drawRegion_2(canvas, initOrange());
        drawRegion_3(canvas, initYellow());
        drawRegion_4(canvas, initGreen());

        canvas.drawBitmap(tachometer, 0, 0, null);
        canvas.drawBitmap(arrow, matrix , null);
    }

    public void update (float value) {
        if (value < MIN_SPEED) update(MIN_SPEED); else
            if (value > MAX_SPEED) update(MAX_SPEED); else {
                rotateDegree = (value * MB + 253f)%360f;
            }
        currentValue = value;
        this.invalidate();
    }

    private void setStartPosition () {
        update(MIN_SPEED);
    }

    private void drawRegions (Canvas canvas , float startConer , float mainConer , int color) {
        Paint paint = new Paint();
        RectF rect = new RectF();

        rect.set((getResources().getDimension(R.dimen.left) - (getResources().getDimension(R.dimen.x37_5_dp))),
                ((getResources().getDimension(R.dimen.top) - (getResources().getDimension(R.dimen.x37_5_dp)))),
                ((getResources().getDimension(R.dimen.right) + (getResources().getDimension(R.dimen.x37_5_dp)))),
                ((getResources().getDimension(R.dimen.bottom) + (getResources().getDimension(R.dimen.x37_5_dp))))); 

        paint.setColor(color);
        paint.setStrokeWidth(getResources().getDimension(R.dimen.stroke_width));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rect, startConer, mainConer, false, paint);
    }

    private void drawRegion_1 (Canvas canvas , int color) {
        drawRegions(canvas , 163f , 60f , color);
    }

    private void drawRegion_2 (Canvas canvas , int color) {
        drawRegions(canvas , 163f+60f , 47f , color);
    }

    private void drawRegion_3 (Canvas canvas , int color) {
        drawRegions(canvas , 163f+60f+47f , 45f , color);
    }

    private void drawRegion_4 (Canvas canvas , int color) {
        drawRegions(canvas , 163f+60f+47f+45f , 62f , color);
    }

    private int initRed () {
        float piece = MB / 8; //~128kb/s

        if (currentValue == 0) return getResources().getColor(R.color.transpert); else 
            if (currentValue < piece) return getResources().getColor(R.color.red_75); else
                if (currentValue < piece * 2) return getResources().getColor(R.color.red_50); else
                    if (currentValue < piece * 3) return getResources().getColor(R.color.red_25); else
                        return getResources().getColor(R.color.red);

    }

    private int initOrange () {
        float piece = MB / 8;//~128kb/s

        if (currentValue < piece * 4) return getResources().getColor(R.color.transpert); else 
            if (currentValue < piece * 5) return getResources().getColor(R.color.orange_75); else
                if (currentValue < piece * 6) return getResources().getColor(R.color.orange_50); else
                    if (currentValue < piece * 7) return getResources().getColor(R.color.orange_25); else
                        return getResources().getColor(R.color.orange);

    }

    private int initYellow () {
        if (currentValue < MB) return getResources().getColor(R.color.transpert); else 
            if (currentValue < MB * 2) return getResources().getColor(R.color.yellow_75); else
                if (currentValue < MB * 3) return getResources().getColor(R.color.yellow_50); else
                    if (currentValue < MB * 4) return getResources().getColor(R.color.yellow_25); else
                        return getResources().getColor(R.color.yellow);

    }

    private int initGreen () {
        float piece = MAX_SPEED / 9; //~1.2 Mb/sec
        float fiveMb = MB * 5;

        if (currentValue < fiveMb) return getResources().getColor(R.color.transpert); else 
            if (currentValue < fiveMb + piece) return getResources().getColor(R.color.green_75); else
                if (currentValue < fiveMb + piece * 2) return getResources().getColor(R.color.green_50); else
                    if (currentValue < fiveMb + piece * 3) return getResources().getColor(R.color.green_25); else
                        return getResources().getColor(R.color.green);

    }
}
