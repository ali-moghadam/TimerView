package com.example.alichchartview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class AliChTimerView extends View {

    private Context context;

    private final static float DEFAULT_CURRENT_TIME = 2;
    private final static float DEFAULT_START_TIME = 0;
    private final static float DEFAULT_END_TIME = 6;
    private final static float DEFAULT_BACKGROUND_PROGRESS_RADIUS = dpToPx(80);
    private final static float DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH = dpToPx(20);
    private final static float DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    private final static int DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.GRAY;
    private final static int DEFAULT_PROGRESS_COLOR = Color.parseColor("#00E676");
    private final static int DEFAULT_START_TIME_STROKE_COLOR = Color.parseColor("#2196F3");
    private final static int DEFAULT_END_TIME_STROKE_COLOR = Color.parseColor("#f44336");

    private float mDegreeProgress;
    private float mDegreeStartTime;
    private float mDegreeEndTime;

    private float mRadiusBackgroundProgress;

    private float mStrokeWithBackgroundProgress;
    private float mStrokeWithCircles;

    private int mColorProgress;
    private int mColorBackgroundProgress;
    private int mColorStartTime;
    private int mColorEndTime;

    private Paint mPaintBackgroundProgress;
    private Paint mPaintStartTime;
    private Paint mPaintEndTime;
    private Paint mPaintProgress;

    private RectF mRectProgress;

    private int mWidthBackgroundProgress;
    private int mHeightBackgroundProgress;


    public AliChTimerView(Context context) {
        super(context);

    }

    public AliChTimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public AliChTimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {

            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AliChTimerView, 0, 0);

            try {
                //TODO add validation
                mDegreeProgress = hourToDegree(a.getFloat(R.styleable.AliChTimerView_atv_value_current_time, DEFAULT_CURRENT_TIME));
                mDegreeStartTime = hourToDegree( a.getFloat(R.styleable.AliChTimerView_atv_value_start_time, DEFAULT_START_TIME));
                mDegreeEndTime = hourToDegree(a.getFloat(R.styleable.AliChTimerView_atv_value_end_time, DEFAULT_END_TIME));

                mColorBackgroundProgress = a.getColor(R.styleable.AliChTimerView_atv_background_progress_color, DEFAULT_BACKGROUND_PROGRESS_COLOR);
                mColorStartTime = a.getColor(R.styleable.AliChTimerView_atv_start_time_stroke_color, DEFAULT_START_TIME_STROKE_COLOR);
                mColorEndTime = a.getColor(R.styleable.AliChTimerView_atv_end_time_stroke_color, DEFAULT_END_TIME_STROKE_COLOR);
                mColorProgress = a.getColor(R.styleable.AliChTimerView_atv_progress_color, DEFAULT_PROGRESS_COLOR);

                mStrokeWithCircles = a.getFloat(R.styleable.AliChTimerView_atv_circles_stroke_width, DEFAULT_CIRCLE_STROKE_WIDTH);
                mStrokeWithBackgroundProgress = validateStrokeWithBackground(a.getFloat(R.styleable.AliChTimerView_atv_background_progress_stroke_width, DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH));
            } finally {
                a.recycle();
            }

            mPaintBackgroundProgress = new Paint();
            mPaintBackgroundProgress.setAntiAlias(true);
            mPaintBackgroundProgress.setColor(mColorBackgroundProgress);
            mPaintBackgroundProgress.setStrokeWidth(mStrokeWithBackgroundProgress);
            mPaintBackgroundProgress.setStyle(Paint.Style.STROKE);

            mPaintStartTime = new Paint();
            mPaintStartTime.setAntiAlias(true);
            mPaintStartTime.setStrokeWidth(mStrokeWithCircles);
            mPaintStartTime.setColor(mColorStartTime);
            mPaintStartTime.setStyle(Paint.Style.STROKE);

            mPaintEndTime = new Paint();
            mPaintEndTime.setAntiAlias(true);
            mPaintEndTime.setStrokeWidth(mStrokeWithCircles);
            mPaintEndTime.setColor(mColorEndTime);
            mPaintEndTime.setStyle(Paint.Style.STROKE);

            mPaintProgress = new Paint();
            mPaintProgress.setAntiAlias(true);
            mPaintProgress.setStrokeWidth(30);
            mPaintProgress.setColor(mColorProgress);
            mPaintProgress.setStrokeCap(Paint.Cap.ROUND);
            mPaintProgress.setStyle(Paint.Style.STROKE);

            mRectProgress = new RectF();

        }

    }


    public static void fillCircleStrokeBorder(
            Canvas c, float cx, float cy, float radius,
            int circleColor, float borderWidth, int borderColor, Paint p) {

        int saveColor = p.getColor();
        p.setColor(circleColor);
        Paint.Style saveStyle = p.getStyle();
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(cx, cy, radius, p);
        if (borderWidth > 0) {
            p.setColor(borderColor);
            p.setStyle(Paint.Style.STROKE);
            float saveStrokeWidth = p.getStrokeWidth();
            p.setStrokeWidth(borderWidth);
            c.drawCircle(cx, cy, radius - (borderWidth / 2), p);
            p.setStrokeWidth(saveStrokeWidth);
        }
        p.setColor(saveColor);
        p.setStyle(saveStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectProgress.set(
                (float) mWidthBackgroundProgress / 2 - mRadiusBackgroundProgress,
                (float) mHeightBackgroundProgress / 2 - mRadiusBackgroundProgress,
                (float) mWidthBackgroundProgress / 2 + mRadiusBackgroundProgress,
                (float) mHeightBackgroundProgress / 2 + mRadiusBackgroundProgress);
    }

    private float validateStrokeWithBackground(float strokeWith) {
        if (strokeWith < 0 || strokeWith > 50)
            return DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH;
        else {
            return dpToPx(strokeWith);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);


        switch (widthMeasureMode) {
            case MeasureSpec.UNSPECIFIED:
                mWidthBackgroundProgress = getDesireWidth();
                break;

            case MeasureSpec.EXACTLY:
                mWidthBackgroundProgress = widthMeasureSize + getHorizontalPadding();
                break;

            case MeasureSpec.AT_MOST:
                mWidthBackgroundProgress = Math.min(widthMeasureSize, getDesireWidth() + getHorizontalPadding());
                break;
        }


        switch (heightMeasureMode) {
            case MeasureSpec.UNSPECIFIED:
                mHeightBackgroundProgress = getDesireHeight();
                break;

            case MeasureSpec.EXACTLY:
                mHeightBackgroundProgress = heightMeasureSize + getVerticalPadding();
                break;

            case MeasureSpec.AT_MOST:
                mHeightBackgroundProgress = Math.min(heightMeasureSize, getDesireHeight());
                break;
        }

        if (widthMeasureMode == MeasureSpec.EXACTLY || heightMeasureMode == MeasureSpec.EXACTLY) {
            int size = Math.min(widthMeasureSize - getHorizontalPadding(), heightMeasureSize - getVerticalPadding());
            mRadiusBackgroundProgress = (size - mPaintBackgroundProgress.getStrokeWidth()) / 2;
        } else {
            mRadiusBackgroundProgress = DEFAULT_BACKGROUND_PROGRESS_RADIUS;
        }

        setMeasuredDimension(mWidthBackgroundProgress, mHeightBackgroundProgress);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //BACKGROUND
        canvas.drawCircle(mWidthBackgroundProgress / 2, mHeightBackgroundProgress / 2, mRadiusBackgroundProgress, mPaintBackgroundProgress);

        //PROGRESS
        if (mDegreeProgress > mDegreeEndTime)
            mDegreeProgress = mDegreeEndTime;
        float sweepAngel = (mDegreeStartTime < mDegreeProgress) ? mDegreeProgress - mDegreeStartTime : 360 - mDegreeStartTime + mDegreeProgress;
        canvas.drawArc(mRectProgress, mDegreeStartTime, sweepAngel, false, mPaintProgress);

        //START TIME
        float drawXStartTime = getDrawXOnBackgroundProgress(mDegreeStartTime);
        float drawYStartTime = getDrawYOnBackgroundProgress(mDegreeStartTime);
        if (isInEditMode())
            canvas.drawCircle(drawXStartTime, drawYStartTime, mPaintBackgroundProgress.getStrokeWidth() / 2, mPaintStartTime);
        else
            fillCircleStrokeBorder(canvas, drawXStartTime, drawYStartTime, mPaintBackgroundProgress.getStrokeWidth() / 2, Color.WHITE, mStrokeWithCircles, mColorStartTime, mPaintStartTime);


        //END TIME
        float drawXEndTime = getDrawXOnBackgroundProgress(mDegreeEndTime);
        float drawYEndTime = getDrawYOnBackgroundProgress(mDegreeEndTime);
        if (isInEditMode())
            canvas.drawCircle(drawXEndTime, drawYEndTime, mPaintBackgroundProgress.getStrokeWidth() / 2, mPaintEndTime);
        else
            fillCircleStrokeBorder(canvas, drawXEndTime, drawYEndTime, mPaintBackgroundProgress.getStrokeWidth() / 2, Color.WHITE, mStrokeWithCircles, mColorEndTime, mPaintEndTime);


    }

    private float getDrawXOnBackgroundProgress(float degree) {
        float drawX = (float) Math.cos(Math.toRadians(degree));
        drawX *= mRadiusBackgroundProgress;
        drawX += mWidthBackgroundProgress / 2;
        return drawX;
    }

    private float getDrawYOnBackgroundProgress(float degree) {
        float drawY = (float) Math.sin(Math.toRadians(degree));
        drawY *= mRadiusBackgroundProgress;
        drawY += mHeightBackgroundProgress / 2;
        return drawY;
    }

    private float hourToDegree(float hour){
        if (hour < 0){
            hour = 12.1f ;
        }else if (hour > 12){
            hour = 11.99f ;
        }
        hour = changeHourForRotate(hour);
        return (hour * 360)/12;
    }

    private float changeHourForRotate(float hour) {
        return hour + 9;
    }

    private int getDesireHeight() {
        return (int) ((mRadiusBackgroundProgress * 2) + mPaintBackgroundProgress.getStrokeWidth() + getVerticalPadding());
    }

    private int getVerticalPadding() {
        return getPaddingTop() + getPaddingBottom();
    }

    private int getDesireWidth() {
        return (int) ((mRadiusBackgroundProgress * 2) + mPaintBackgroundProgress.getStrokeWidth() + getHorizontalPadding());
    }

    private int getHorizontalPadding() {
        return getPaddingLeft() + getPaddingRight();
    }

    public static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
