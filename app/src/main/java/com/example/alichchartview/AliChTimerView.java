package com.example.alichchartview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class AliChTimerView extends View {


    private Context context;

    private static final String DEFAULT_TEXT_TIME = "3:00";
    private static final String DEFAULT_REAMING_TEXT_TIME = "Reaming Time";
    private static final float DEFAULT_TEXT_TIME_SIZE = 70;
    private static final float DEFAULT_TEXT_REAMING_TIME_SIZE = 30;
    private static final float DEFAULT_SPACE_TEXT = dpToPx(45);
    private static final float DEFAULT_SPACE = dpToPx(5);
    private final static float DEFAULT_CURRENT_TIME = 2;
    private final static float DEFAULT_START_TIME = 0;
    private final static float DEFAULT_END_TIME = 6;
    private final static float DEFAULT_REPEAT_START_TIME = 7;
    private final static float DEFAULT_REPEAT_END_TIME = 10;
    private final static float DEFAULT_BACKGROUND_PROGRESS_RADIUS = dpToPx(80);
    private final static float DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH = dpToPx(20);
    private final static float DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    private final static int DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.GRAY;
    private final static int DEFAULT_PROGRESS_COLOR = Color.parseColor("#00E676");
    private final static int DEFAULT_START_TIME_STROKE_COLOR = Color.parseColor("#2196F3");
    private final static int DEFAULT_END_TIME_STROKE_COLOR = Color.parseColor("#f44336");
    private final static int DEFAULT_CLOCK_COLOR = Color.parseColor("#9C27B0");
    private final static int DEFAULT_REPEAT_COLOR = Color.parseColor("#FDD835");
    private final static int DEFAULT_TEXT_TIME_COLOR = Color.parseColor("#2196F3");

    private float mDegreeProgress;
    private float mDegreeStartTime;
    private float mDegreeEndTime;
    private float mDegreeStartRepeatTime;
    private float mDegreeEndRepeatTime;

    private float mRadiusBackgroundProgress;
    private float mRadiusBackgroundRepeat;

    private float mStrokeWithBackgroundProgress;
    private float mStrokeWithCircles;

    private int mColorProgress;
    private int mColorBackgroundProgress;
    private int mColorRepeat;
    private int mColorStartTime;
    private int mColorEndTime;
    private int mColorTextTime;

    private float mSizeTextTime;
    private float mSizeReamingTextTime;

    private String mStringTextTime;
    private String mStringReaming;

    private Paint mPaintBackgroundProgress;
    private Paint mPaintBackgroundRepeat;
    private Paint mPaintStartTime;
    private Paint mPaintEndTime;
    private Paint mPaintTimeProgress;
    private Paint mPaintRepeatProgress;
    private Paint mPaintClock;
    private Paint mPaintRepeat;
    private Paint mPaintTextTime;
    private Paint mPaintTextReaming;

    private RectF mRectProgress;
    private RectF mRectRepeatProgress;
    private RectF mRectClock;

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

        mStringTextTime = DEFAULT_TEXT_TIME;
        mStringReaming = DEFAULT_REAMING_TEXT_TIME;

        if (attrs != null) {

            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AliChTimerView, 0, 0);

            try {
                //TODO add validation
                mDegreeProgress = hourToDegree(a.getFloat(R.styleable.AliChTimerView_atv_value_current_time, DEFAULT_CURRENT_TIME));
                mDegreeStartTime = hourToDegree(a.getFloat(R.styleable.AliChTimerView_atv_value_start_time, DEFAULT_START_TIME));
                mDegreeEndTime = hourToDegree(a.getFloat(R.styleable.AliChTimerView_atv_value_end_time, DEFAULT_END_TIME));
                mDegreeStartRepeatTime = hourToDegree(a.getFloat(R.styleable.AliChTimerView_atv_value_repeat_start_time, DEFAULT_REPEAT_START_TIME));
                mDegreeEndRepeatTime = hourToDegree(a.getFloat(R.styleable.AliChTimerView_atv_value_repeat_end_time, DEFAULT_REPEAT_END_TIME));

                mColorBackgroundProgress = a.getColor(R.styleable.AliChTimerView_atv_background_progress_color, DEFAULT_BACKGROUND_PROGRESS_COLOR);
                mColorStartTime = a.getColor(R.styleable.AliChTimerView_atv_start_time_stroke_color, DEFAULT_START_TIME_STROKE_COLOR);
                mColorEndTime = a.getColor(R.styleable.AliChTimerView_atv_end_time_stroke_color, DEFAULT_END_TIME_STROKE_COLOR);
                mColorProgress = a.getColor(R.styleable.AliChTimerView_atv_progress_color, DEFAULT_PROGRESS_COLOR);
                mColorRepeat = a.getColor(R.styleable.AliChTimerView_atv_repeat_stroke_color, DEFAULT_REPEAT_COLOR);
                mColorTextTime = a.getColor(R.styleable.AliChTimerView_atv_text_time_color, DEFAULT_TEXT_TIME_COLOR);

                mStringTextTime = a.getString(R.styleable.AliChTimerView_atv_value_text_time);

                mSizeTextTime = dpToPx(a.getFloat(R.styleable.AliChTimerView_atv_text_time_size, DEFAULT_TEXT_TIME_SIZE));
                mSizeReamingTextTime = dpToPx(a.getFloat(R.styleable.AliChTimerView_atv_reaming_text_time_size, DEFAULT_TEXT_REAMING_TIME_SIZE));

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

            mPaintBackgroundRepeat = new Paint();
            mPaintBackgroundRepeat.setAntiAlias(true);
            mPaintBackgroundRepeat.setColor(mColorBackgroundProgress);
            mPaintBackgroundRepeat.setStrokeWidth(mStrokeWithBackgroundProgress / 3);
            mPaintBackgroundRepeat.setStyle(Paint.Style.STROKE);

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

            mPaintRepeat = new Paint();
            mPaintRepeat.setAntiAlias(true);
            mPaintRepeat.setStrokeWidth(mStrokeWithCircles);
            mPaintRepeat.setColor(mColorRepeat);
            mPaintRepeat.setStyle(Paint.Style.STROKE);

            mPaintTimeProgress = new Paint();
            mPaintTimeProgress.setAntiAlias(true);
            mPaintTimeProgress.setStrokeWidth(mPaintBackgroundProgress.getStrokeWidth());
            mPaintTimeProgress.setColor(mColorProgress);
            mPaintTimeProgress.setStrokeCap(Paint.Cap.ROUND);
            mPaintTimeProgress.setStyle(Paint.Style.STROKE);

            mPaintRepeatProgress = new Paint();
            mPaintRepeatProgress.setAntiAlias(true);
            mPaintRepeatProgress.setStrokeWidth(mPaintBackgroundRepeat.getStrokeWidth());
            mPaintRepeatProgress.setColor(mColorRepeat);
            mPaintRepeatProgress.setStrokeCap(Paint.Cap.ROUND);
            mPaintRepeatProgress.setStyle(Paint.Style.STROKE);

            mPaintClock = new Paint();
            mPaintClock.setAntiAlias(true);
            mPaintClock.setStrokeWidth(mPaintBackgroundProgress.getStrokeWidth());
            mPaintClock.setColor(DEFAULT_CLOCK_COLOR);
            mPaintClock.setStyle(Paint.Style.STROKE);

            mPaintTextTime = new Paint();
            mPaintTextTime.setAntiAlias(true);
            mPaintTextTime.setColor(mColorTextTime);
            mPaintTextTime.setTextSize(mSizeTextTime);
            mPaintTextTime.setTextAlign(Paint.Align.CENTER);
            mPaintTextTime.setStyle(Paint.Style.STROKE);

            mPaintTextReaming = new Paint();
            mPaintTextReaming.setAntiAlias(true);
            mPaintTextReaming.setColor(mColorTextTime);
            mPaintTextReaming.setTextSize(mSizeReamingTextTime);
            mPaintTextReaming.setTextAlign(Paint.Align.CENTER);
            mPaintTextReaming.setStyle(Paint.Style.STROKE);
            mPaintTextReaming.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));

            mRectProgress = new RectF();
            mRectRepeatProgress = new RectF();
            mRectClock = new RectF();

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

        mRectRepeatProgress.set(
                (float) mWidthBackgroundProgress / 2 - mRadiusBackgroundRepeat,
                (float) mHeightBackgroundProgress / 2 - mRadiusBackgroundRepeat,
                (float) mWidthBackgroundProgress / 2 + mRadiusBackgroundRepeat,
                (float) mHeightBackgroundProgress / 2 + mRadiusBackgroundRepeat);

        mRectClock.set(
                (float) ((mWidthBackgroundProgress / 2) - mRadiusBackgroundProgress) + mPaintBackgroundProgress.getStrokeWidth(),
                (float) ((mHeightBackgroundProgress / 2) - mRadiusBackgroundProgress) + mPaintBackgroundProgress.getStrokeWidth(),
                (float) ((mWidthBackgroundProgress / 2) + mRadiusBackgroundProgress) - mPaintBackgroundProgress.getStrokeWidth(),
                (float) ((mHeightBackgroundProgress / 2) + mRadiusBackgroundProgress) - mPaintBackgroundProgress.getStrokeWidth());

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

        mRadiusBackgroundRepeat = mRadiusBackgroundProgress - (mPaintBackgroundProgress.getStrokeWidth() + DEFAULT_SPACE);

        setMeasuredDimension(mWidthBackgroundProgress, mHeightBackgroundProgress);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float drawX;
        float drawY;

        //BACKGROUNDS
        canvas.drawCircle(mWidthBackgroundProgress / 2, mHeightBackgroundProgress / 2, mRadiusBackgroundProgress, mPaintBackgroundProgress);
        canvas.drawCircle(mWidthBackgroundProgress / 2, mHeightBackgroundProgress / 2, mRadiusBackgroundRepeat, mPaintBackgroundRepeat);


        canvas.drawText(mStringTextTime, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) + DEFAULT_SPACE_TEXT, mPaintTextTime);
        canvas.drawText(mStringReaming, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) - DEFAULT_SPACE_TEXT, mPaintTextReaming);


        //PROGRESS TIME
        if (mDegreeProgress > mDegreeEndTime)
            mDegreeProgress = mDegreeEndTime;
        float sweepAngelTime = (mDegreeStartTime < mDegreeProgress) ? mDegreeProgress - mDegreeStartTime : 360 - mDegreeStartTime + mDegreeProgress;
        canvas.drawArc(mRectProgress, mDegreeStartTime, sweepAngelTime, false, mPaintTimeProgress);        //PROGRESS

        //PROGRESS REPEAT TIME
        float temp = mDegreeStartTime;
        if (temp > mDegreeEndRepeatTime)
            temp = mDegreeEndRepeatTime;
        float sweepAngelRepeat = (mDegreeStartRepeatTime < temp) ? temp - mDegreeStartRepeatTime : 360 - mDegreeStartRepeatTime + temp;
        canvas.drawArc(mRectRepeatProgress, mDegreeStartRepeatTime, sweepAngelRepeat, false, mPaintRepeatProgress);

        //START TIME
        drawX = getDrawXOnBackgroundProgress(mDegreeStartTime, mRadiusBackgroundProgress, mWidthBackgroundProgress);
        drawY = getDrawYOnBackgroundProgress(mDegreeStartTime, mRadiusBackgroundProgress, mHeightBackgroundProgress);
        if (isInEditMode())
            canvas.drawCircle(drawX, drawY, mPaintBackgroundProgress.getStrokeWidth() / 2, mPaintStartTime);
        else
            fillCircleStrokeBorder(canvas, drawX, drawY, mPaintBackgroundProgress.getStrokeWidth() / 2, Color.WHITE, mStrokeWithCircles, mColorStartTime, mPaintStartTime);


        //END TIME
        drawX = getDrawXOnBackgroundProgress(mDegreeEndTime, mRadiusBackgroundProgress, mWidthBackgroundProgress);
        drawY = getDrawYOnBackgroundProgress(mDegreeEndTime, mRadiusBackgroundProgress, mHeightBackgroundProgress);
        if (isInEditMode())
            canvas.drawCircle(drawX, drawY, mPaintBackgroundProgress.getStrokeWidth() / 2, mPaintEndTime);
        else
            fillCircleStrokeBorder(canvas, drawX, drawY, mPaintBackgroundProgress.getStrokeWidth() / 2, Color.WHITE, mStrokeWithCircles, mColorEndTime, mPaintEndTime);


        //START REPEAT
        drawX = getDrawXOnBackgroundProgress(mDegreeStartRepeatTime, mRadiusBackgroundRepeat, mWidthBackgroundProgress);
        drawY = getDrawYOnBackgroundProgress(mDegreeStartRepeatTime, mRadiusBackgroundRepeat, mHeightBackgroundProgress);
        if (isInEditMode())
            canvas.drawCircle(drawX, drawY, mPaintBackgroundRepeat.getStrokeWidth(), mPaintRepeat);
        else
            fillCircleStrokeBorder(canvas, drawX, drawY, mPaintBackgroundRepeat.getStrokeWidth(), Color.WHITE, mStrokeWithCircles / 2, mColorRepeat, mPaintRepeat);


        //END REPEAT
        drawX = getDrawXOnBackgroundProgress(mDegreeEndRepeatTime, mRadiusBackgroundRepeat, mWidthBackgroundProgress);
        drawY = getDrawYOnBackgroundProgress(mDegreeEndRepeatTime, mRadiusBackgroundRepeat, mHeightBackgroundProgress);
        if (isInEditMode())
            canvas.drawCircle(drawX, drawY, mPaintBackgroundRepeat.getStrokeWidth() / 2, mPaintRepeat);
        else
            fillCircleStrokeBorder(canvas, drawX, drawY, mPaintBackgroundRepeat.getStrokeWidth() / 2, Color.WHITE, mStrokeWithCircles / 2, mColorRepeat, mPaintRepeat);


        //TODO complete it :)
        //CLOCK
        //canvas.drawArc(mRectClock, 0, 360, false, mPaintClock);


    }

    private float getDrawXOnBackgroundProgress(float degree, float backgroundRadius, float backgroundWidth) {
        float drawX = (float) Math.cos(Math.toRadians(degree));
        drawX *= backgroundRadius;
        drawX += backgroundWidth / 2;
        return drawX;
    }


    private float getDrawYOnBackgroundProgress(float degree, float backgroundRadius, float backgroundHeight) {
        float drawY = (float) Math.sin(Math.toRadians(degree));
        drawY *= backgroundRadius;
        drawY += backgroundHeight / 2;
        return drawY;
    }

    private float hourToDegree(float hour) {
        if (hour < 0) {
            hour = 12.1f;
        } else if (hour > 12) {
            hour = 11.99f;
        }
        hour = changeHourForRotate(hour);
        return (hour * 360) / 12;
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
