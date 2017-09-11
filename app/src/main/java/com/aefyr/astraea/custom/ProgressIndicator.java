package com.aefyr.astraea.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.aefyr.astraea.R;
import com.aefyr.astraea.utility.MathUtils;

/**
 * Created by Aefyr on 10.09.2017.
 */

public class ProgressIndicator extends View {
    private boolean animated;
    private int width, height;
    private int y, r, xs, xe; //center of circles, radius of circles, left x border, right x border
    private int movePerSecond = 256;
    private int minimalDelta;
    private float deltaTime = 0;
    private int color = Color.CYAN;
    private Paint p;

    private int ghostsCount = 0;

    private int[] colorScheme;
    private boolean colorSchemed = false;
    private float colorInterpolationProgress = 0;
    private int currentColorIndex = 0;
    private int nextColorIndex = 1;

    public ProgressIndicator(Context context) {
        super(context);
        initialize();
    }

    public ProgressIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if(attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressIndicator, 0, 0);
            try{
                movePerSecond = a.getDimensionPixelSize(R.styleable.ProgressIndicator_movementPerSecond, 256);
                color = a.getColor(R.styleable.ProgressIndicator_indicatorColor, Color.CYAN);
                ghostsCount = a.getInt(R.styleable.ProgressIndicator_ghostsCount, 0);
            }finally {
                a.recycle();
            }
        }
        initialize();
    }

    private void initialize(){
        p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(color);
        p.setAntiAlias(true);
        minimalDelta = (int) (1000f/((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRefreshRate());
        deltaTime = minimalDelta/1000;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        calculateValues();
    }

    private void calculateValues(){
        y = height/2 + getPaddingTop() - getPaddingBottom();
        r = height/2 - (getPaddingBottom() + getPaddingTop());
        xs = r + getPaddingLeft();
        xe = width - r - getPaddingRight();
        currentX = xs;
    }

    @Override
    public void setPadding(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        super.setPadding(left, top, right, bottom);
        calculateValues();
    }

    public void setAnimating(boolean animating){
        animated = animating;
        currentX = xs;
        goingBack = false;
        invalidate();
    }

    public void setColorScheme(int... colors){
        if(colors.length<2)
            throw new IllegalArgumentException("ProgressIndicator's colorScheme must contain at least 2 colors. Use setColor if you want to use just 1 color");

        colorScheme = colors;
        colorSchemed = true;
    }

    public void setColor(int color){
        colorSchemed = false;
        this.color = color;
        p.setColor(color);
    }

    private int currentX = 1;
    private boolean goingBack;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long startTime = System.currentTimeMillis();

        if(isInEditMode()){
            drawIndicator(canvas, width/2, y, r);
            return;
        }
        if(animated){
            drawIndicator(canvas, currentX, y, r);
            currentX += (goingBack?-movePerSecond:movePerSecond)*deltaTime;

            if(currentX >= xe)
                goingBack = true;
            else if(currentX <= xs)
                goingBack = false;

            invalidate();
        }

        if(colorSchemed)
            calculateColor();

        deltaTime = System.currentTimeMillis() - startTime;
        if(deltaTime<minimalDelta)
            deltaTime = minimalDelta;
        deltaTime /= 1000;
    }


    private void drawIndicator(Canvas c, int x, int y, int r){
        if(ghostsCount > 0) {
            int distanceTillBorder = MathUtils.vector1Distance(currentX, currentX<ghostsCount*r?xs:xe);
            int ghostOffset = distanceTillBorder>r*ghostsCount?r:distanceTillBorder/ghostsCount;
            for (int i = ghostsCount; i > 0; i--) {
                float t = (float) i / (float) ghostsCount;
                int ghostX = currentX + (goingBack ? ghostOffset : -ghostOffset) * i;
                p.setAlpha(MathUtils.lerp(200, 55, t));
                c.drawCircle(ghostX, y, r, p);
            }
            p.setAlpha(255);
        }

        c.drawCircle(x, y, r, p);

    }

    private void calculateColor(){
        p.setColor(MathUtils.colorLerp(colorScheme[currentColorIndex], colorScheme[nextColorIndex], colorInterpolationProgress));
        colorInterpolationProgress += 1*deltaTime;
        if(colorInterpolationProgress>=1){
            currentColorIndex = (currentColorIndex+1)%colorScheme.length;
            nextColorIndex = (currentColorIndex+1)%colorScheme.length;
            colorInterpolationProgress = 0;
        }
    }
}
