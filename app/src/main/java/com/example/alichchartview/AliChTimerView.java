package com.example.alichchartview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class AliChTimerView extends View {

    private static final String TAG = "AliChTimerView";

    private Context context;

    private static final String DEFAULT_TEXT_TIME = "3:00";
    private static final String DEFAULT_REAMING_TEXT_TIME = "Reaming Time";
    private final static int DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.parseColor("#F5F5F5");
    private static final float DEFAULT_SPACE = dpToPx(5);
    private final static float DEFAULT_REPEAT_START_TIME = 7;
    private final static float DEFAULT_REPEAT_END_TIME = 10;
    private final static float DEFAULT_BACKGROUND_PROGRESS_RADIUS = dpToPx(80);
    private final static float DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH = dpToPx(20);
    private final static float DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    private static float DEFAULT_SPACE_TEXT = dpToPx(45);
    private final static int DEFAULT_PROGRESS_COLOR = Color.parseColor("#00E676");
    private final static int DEFAULT_START_TIME_STROKE_COLOR = Color.parseColor("#2196F3");
    private final static int DEFAULT_END_TIME_STROKE_COLOR = Color.parseColor("#f44336");
    private final static int DEFAULT_CLOCK_COLOR = Color.parseColor("#CFD8DC");
    private final static int DEFAULT_REPEAT_COLOR = Color.parseColor("#FDD835");
    private final static int DEFAULT_TEXT_TIME_COLOR = Color.parseColor("#2196F3");

    private float mDegreeProgress;
    private float mDegreeStartTime;
    private float mDegreeEndTime;
    private float mDegreeLeftTime;
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

    private int mStartTimeHour;
    private int mStartTimeMinute;
    private int mEndTimeHour;
    private int mEndTimeMinute;
    private int mLeftTimeHour;
    private int mLeftTimeMinute;
    private int mRepeatStartHour;
    private int mRepeatEndHour;


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

    private float mFloatCenterXCircleStartTime;
    private float mFloatCenterYCircleStartTime;
    private float mFloatCenterXCircleEndTime;
    private float mFloatCenterYCircleEndTime;
    private float mFloatCenterXCircleStartRepeat;
    private float mFloatCenterYCircleStartRepeat;
    private float mFloatCenterXCircleEndRepeat;
    private float mFloatCenterYCircleEndRepeat;

    private float mFloatSweepAngelRepeat;

    private float mFloatLengthOfClockLines;
    private float mFloatBeginOfClockLines;

    private int mWidthBackgroundProgress;
    private int mHeightBackgroundProgress;

    private Set<CircleArea> mCircles = new HashSet<>();
    private CircleArea circleArea = new CircleArea();
    private CircleID currentCircleIDForMove;

    private boolean moving;
    private boolean isIndicator;

    private OnSeekCirclesListener onSeekCirclesListener;


    public void setOnSeekCirclesListener(OnSeekCirclesListener onSeekCirclesListener) {
        this.onSeekCirclesListener = onSeekCirclesListener;
    }

    private static void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {

        final float testTextSize = 48f;

        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        float desiredTextSize = testTextSize * desiredWidth / bounds.width();
        DEFAULT_SPACE_TEXT = desiredTextSize * 2;

        paint.setTextSize(desiredTextSize);
    }

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

                setStartTimeHour(a.getInteger(R.styleable.AliChTimerView_atv_value_start_time_hour, 0));
                setStartTimeMinute(a.getInteger(R.styleable.AliChTimerView_atv_value_start_time_minute, 0));

                setEndTimeHour(a.getInteger(R.styleable.AliChTimerView_atv_value_end_time_hour, 7));
                setEndTimeMinute(a.getInteger(R.styleable.AliChTimerView_atv_value_end_time_minute, 0));

                setLeftTimeHour(a.getInteger(R.styleable.AliChTimerView_atv_value_left_time_hour, 0));
                setLeftTimeMinute(a.getInteger(R.styleable.AliChTimerView_atv_value_left_time_minute, 0));

                setRepeatStartHour(a.getInteger(R.styleable.AliChTimerView_atv_value_repeat_start_hour, 0));
                setRepeatEndHour(a.getInteger(R.styleable.AliChTimerView_atv_value_repeat_end_hour, 0));

                isIndicator = a.getBoolean(R.styleable.AliChTimerView_atv_is_indicator, true);

                mColorBackgroundProgress = a.getColor(R.styleable.AliChTimerView_atv_color_background_progress, DEFAULT_BACKGROUND_PROGRESS_COLOR);
                mColorStartTime = a.getColor(R.styleable.AliChTimerView_atv_color_start_time_stroke, DEFAULT_START_TIME_STROKE_COLOR);
                mColorEndTime = a.getColor(R.styleable.AliChTimerView_atv_color_end_time_stroke, DEFAULT_END_TIME_STROKE_COLOR);
                mColorProgress = a.getColor(R.styleable.AliChTimerView_atv_color_progress, DEFAULT_PROGRESS_COLOR);
                mColorRepeat = a.getColor(R.styleable.AliChTimerView_atv_color_repeat_stroke, DEFAULT_REPEAT_COLOR);
                mColorTextTime = a.getColor(R.styleable.AliChTimerView_atv_color_text_time, DEFAULT_TEXT_TIME_COLOR);

                mStringTextTime = a.getString(R.styleable.AliChTimerView_atv_text_time);
                if (mStringTextTime == null)
                    mStringTextTime = DEFAULT_TEXT_TIME;
                if (mStringTextTime.equals(""))
                    mStringTextTime = DEFAULT_TEXT_TIME;

                mStrokeWithCircles = a.getDimension(R.styleable.AliChTimerView_atv_stroke_width_circles, DEFAULT_CIRCLE_STROKE_WIDTH);
                mStrokeWithBackgroundProgress = a.getDimension(R.styleable.AliChTimerView_atv_stroke_width_background_progress, DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH);
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
            mPaintClock.setStrokeWidth(mPaintBackgroundProgress.getStrokeWidth() / 2.3f);
            mPaintClock.setColor(DEFAULT_CLOCK_COLOR);
            mPaintClock.setStyle(Paint.Style.STROKE);
            mPaintClock.setStrokeCap(Paint.Cap.ROUND);

            mPaintTextTime = new Paint();
            mPaintTextTime.setAntiAlias(true);
            mPaintTextTime.setColor(mColorTextTime);
            mPaintTextTime.setTextAlign(Paint.Align.CENTER);
            mPaintTextTime.setStyle(Paint.Style.FILL_AND_STROKE);

            mPaintTextReaming = new Paint();
            mPaintTextReaming.setAntiAlias(true);
            mPaintTextReaming.setColor(mColorTextTime);
            mPaintTextReaming.setTextAlign(Paint.Align.CENTER);
            mPaintTextReaming.setStyle(Paint.Style.FILL_AND_STROKE);
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

        mFloatLengthOfClockLines = mRadiusBackgroundProgress - (mStrokeWithBackgroundProgress) - mStrokeWithCircles * 3;
        mFloatBeginOfClockLines = mRadiusBackgroundProgress - (mStrokeWithBackgroundProgress);

        mRadiusBackgroundRepeat = mFloatLengthOfClockLines - (mPaintBackgroundRepeat.getStrokeWidth() * 2);


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
                ((mWidthBackgroundProgress / 2) - mRadiusBackgroundProgress) + mPaintBackgroundProgress.getStrokeWidth(),
                ((mHeightBackgroundProgress / 2) - mRadiusBackgroundProgress) + mPaintBackgroundProgress.getStrokeWidth(),
                ((mWidthBackgroundProgress / 2) + mRadiusBackgroundProgress) - mPaintBackgroundProgress.getStrokeWidth(),
                ((mHeightBackgroundProgress / 2) + mRadiusBackgroundProgress) - mPaintBackgroundProgress.getStrokeWidth());


        setTextSizeForWidth(mPaintTextTime, mRadiusBackgroundRepeat * 1.1f, mStringTextTime);
        setTextSizeForWidth(mPaintTextReaming, mRadiusBackgroundRepeat * 1.2f, mStringReaming);


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


        mFloatCenterXCircleStartTime = getDrawXOnBackgroundProgress(mDegreeStartTime, mRadiusBackgroundProgress, mWidthBackgroundProgress);
        mFloatCenterYCircleStartTime = getDrawYOnBackgroundProgress(mDegreeStartTime, mRadiusBackgroundProgress, mHeightBackgroundProgress);
        //ADD CIRCLE AREA FOR DETECT TOUCH
        mCircles.add(injectCircleArea(CircleID.CIRCLE_START_TIME, mFloatCenterXCircleStartTime, mFloatCenterYCircleStartTime, mPaintBackgroundProgress.getStrokeWidth() / 2));


        mFloatCenterXCircleEndTime = getDrawXOnBackgroundProgress(mDegreeEndTime, mRadiusBackgroundProgress, mWidthBackgroundProgress);
        mFloatCenterYCircleEndTime = getDrawYOnBackgroundProgress(mDegreeEndTime, mRadiusBackgroundProgress, mHeightBackgroundProgress);
        //ADD CIRCLE AREA FOR DETECT TOUCH
        mCircles.add(injectCircleArea(CircleID.CIRCLE_END_TIME, mFloatCenterXCircleEndTime, mFloatCenterYCircleEndTime, mPaintBackgroundProgress.getStrokeWidth() / 2));


        mFloatCenterXCircleStartRepeat = getDrawXOnBackgroundProgress(mDegreeStartRepeatTime, mRadiusBackgroundRepeat, mWidthBackgroundProgress);
        mFloatCenterYCircleStartRepeat = getDrawYOnBackgroundProgress(mDegreeStartRepeatTime, mRadiusBackgroundRepeat, mHeightBackgroundProgress);
        //ADD CIRCLE AREA FOR DETECT TOUCH
        mCircles.add(injectCircleArea(CircleID.CIRCLE_REPEAT_TIME, mFloatCenterXCircleStartRepeat, mFloatCenterYCircleStartRepeat, mPaintBackgroundRepeat.getStrokeWidth()));

/*
        mFloatCenterXCircleEndRepeat = getDrawXOnBackgroundProgress(mDegreeEndRepeatTime, mRadiusBackgroundRepeat, mWidthBackgroundProgress);
        mFloatCenterYCircleEndRepeat = getDrawYOnBackgroundProgress(mDegreeEndRepeatTime, mRadiusBackgroundRepeat, mHeightBackgroundProgress);
        //ADD CIRCLE AREA FOR DETECT TOUCH
        mCircles.add(injectCircleArea(CircleID.CIRCLE_REPEAT_TIME, mFloatCenterXCircleStartRepeat, mFloatCenterYCircleStartRepeat, mPaintBackgroundRepeat.getStrokeWidth()));
*/

        //BACKGROUNDS
        canvas.drawCircle(mWidthBackgroundProgress / 2, mHeightBackgroundProgress / 2, mRadiusBackgroundProgress, mPaintBackgroundProgress);
        canvas.drawCircle(mWidthBackgroundProgress / 2, mHeightBackgroundProgress / 2, mRadiusBackgroundRepeat, mPaintBackgroundRepeat);


        //TEXT

        canvas.drawText(mStringTextTime, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) + DEFAULT_SPACE_TEXT, mPaintTextTime);
        canvas.drawText(mStringReaming, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) - DEFAULT_SPACE_TEXT, mPaintTextReaming);


        if (isIndicator) {

            //PROGRESS TIME
            canvas.drawArc(mRectProgress, mDegreeStartTime, mDegreeLeftTime, false, mPaintTimeProgress);        //PROGRESS


            //PROGRESS REPEAT TIME
            float temp = mDegreeStartRepeatTime;
            if (temp > mDegreeEndRepeatTime)
                temp = mDegreeEndRepeatTime;
            mFloatSweepAngelRepeat = (mDegreeStartRepeatTime < temp) ? temp - mDegreeStartRepeatTime : 360 - mDegreeStartRepeatTime + temp;
            canvas.drawArc(mRectRepeatProgress, mDegreeStartRepeatTime, 15, false, mPaintRepeatProgress);

        }


        //CIRCLE START TIME
        if (isInEditMode())
            canvas.drawCircle(mFloatCenterXCircleStartTime, mFloatCenterYCircleStartTime, mPaintBackgroundProgress.getStrokeWidth() / 2, mPaintStartTime);
        else
            fillCircleStrokeBorder(canvas, mFloatCenterXCircleStartTime, mFloatCenterYCircleStartTime, mPaintBackgroundProgress.getStrokeWidth() / 2, Color.WHITE, mStrokeWithCircles, mColorStartTime, mPaintStartTime);


        //CIRCLE END TIME

        if (isInEditMode())
            canvas.drawCircle(mFloatCenterXCircleEndTime, mFloatCenterYCircleEndTime, mPaintBackgroundProgress.getStrokeWidth() / 2, mPaintEndTime);
        else
            fillCircleStrokeBorder(canvas, mFloatCenterXCircleEndTime, mFloatCenterYCircleEndTime, mPaintBackgroundProgress.getStrokeWidth() / 2, Color.WHITE, mStrokeWithCircles, mColorEndTime, mPaintEndTime);


        //CIRCLE START REPEAT
        if (isInEditMode())
            canvas.drawCircle(mFloatCenterXCircleStartRepeat, mFloatCenterYCircleStartRepeat, mPaintBackgroundRepeat.getStrokeWidth(), mPaintRepeat);
        else
            fillCircleStrokeBorder(canvas, mFloatCenterXCircleStartRepeat, mFloatCenterYCircleStartRepeat, mPaintBackgroundRepeat.getStrokeWidth(), Color.WHITE, mStrokeWithCircles / 2, mColorRepeat, mPaintRepeat);

/*
        //CIRCLE END REPEAT
        if (isIndicator) {
            if (isInEditMode())
                canvas.drawCircle(mFloatCenterXCircleEndRepeat, mFloatCenterYCircleEndRepeat, mPaintBackgroundRepeat.getStrokeWidth() / 2, mPaintRepeat);
            else
                fillCircleStrokeBorder(canvas, mFloatCenterXCircleEndRepeat, mFloatCenterYCircleEndRepeat, mPaintBackgroundRepeat.getStrokeWidth() / 2, Color.WHITE, mStrokeWithCircles / 2, mColorRepeat, mPaintRepeat);
        }
*/
        //CLOCK LINES
        float angel = 0;
        for (int i = 0; i < 12; i++) {
            angel += 30;

            float x1 = (float) (Math.cos(Math.toRadians(angel))) * mFloatBeginOfClockLines + (float) (mWidthBackgroundProgress / 2);
            float y1 = (float) (Math.sin(Math.toRadians(angel))) * mFloatBeginOfClockLines + (float) (mHeightBackgroundProgress / 2);
            float x2 = (float) (Math.cos(Math.toRadians(angel))) * mFloatLengthOfClockLines + (float) (mWidthBackgroundProgress / 2);
            float y2 = (float) (Math.sin(Math.toRadians(angel))) * mFloatLengthOfClockLines + (float) (mHeightBackgroundProgress / 2);

            canvas.drawLine(x1, y1, x2, y2, mPaintClock);
        }

    }


    private CircleArea injectCircleArea(CircleID circleID, float centerX, float centerY, float radius) {
        circleArea = new CircleArea();
        circleArea.setCircleID(circleID);
        circleArea.setXStart(centerX - radius);
        circleArea.setXEnd(centerX + radius);
        circleArea.setYStart(centerY - radius);
        circleArea.setYEnd(centerY + radius);
        return circleArea;
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


    public void setStartTimeHour(@IntRange(from = 0, to = 12) int hour) {

        hour = validateHour(hour);

        hour = rotateHour(hour);

        this.mStartTimeHour = hour;

        int degreeOfPerHour = 30;
        mDegreeStartTime = ((mStartTimeHour * 360) / 12) + ((mStartTimeMinute * degreeOfPerHour) / 60);

        invalidate();
    }


    public void setStartTimeMinute(@IntRange(from = 0, to = 59) int minute) {

        mStartTimeMinute = validateMinute(minute);

        int degreeOfPerHour = 30;
        int degreeOfMinute = (mStartTimeMinute * degreeOfPerHour) / 60;
        mDegreeStartTime = getDegreeFromHour(mStartTimeHour) + degreeOfMinute;
        invalidate();

    }


    public void setEndTimeHour(@IntRange(from = 0, to = 12) int hour) {

        hour = validateHour(hour);
        hour = rotateHour(hour);

        this.mEndTimeHour = hour;

        int degreeOfPerHour = 30;
        mDegreeEndTime = ((mEndTimeHour * 360) / 12) + ((mEndTimeMinute * degreeOfPerHour) / 60);

        invalidate();
    }


    public void setEndTimeMinute(@IntRange(from = 0, to = 59) int minute) {

        mEndTimeMinute = validateMinute(minute);

        int degreeOfPerHour = 30;
        int degreeOfMinute = (mEndTimeMinute * degreeOfPerHour) / 60;
        mDegreeEndTime = getDegreeFromHour(mEndTimeHour) + degreeOfMinute;
        invalidate();

    }

    public void setLeftTimeHour(int hour) {

        this.mLeftTimeHour = validateHour(hour);
        int max = getMaxProgress(mStartTimeHour, mEndTimeHour);

        if (mLeftTimeHour >= max) {
            mLeftTimeHour = max;
            mLeftTimeMinute = 0;
        }

        int degreeOfPerHour = 30;
        mDegreeLeftTime = ((mLeftTimeHour * 360) / 12) + ((mLeftTimeMinute * degreeOfPerHour) / 60);
        invalidate();
    }


    public void setLeftTimeMinute(@IntRange(from = 0, to = 59) int minute) {

        minute = validateMinute(minute);
        int max = getMaxProgress(mStartTimeHour, mEndTimeHour);

        if (mLeftTimeHour >= max) {
            minute = 0;
        }

        mLeftTimeMinute = minute;

        int degreeOfPerHour = 30;
        int degreeOfMinute = (mLeftTimeMinute * degreeOfPerHour) / 60;
        mDegreeLeftTime = getDegreeFromHour(mLeftTimeHour) + degreeOfMinute;
        invalidate();

    }


    public void setRepeatStartHour(int hour) {

        hour = rotateHour(hour);
        hour = validateHour(hour);
        this.mRepeatStartHour = validateHour(hour);

        int degreeOfPerHour = 30;
        mDegreeStartRepeatTime = ((mRepeatStartHour * 360) / 12) + ((mRepeatStartHour * degreeOfPerHour) / 60);
        invalidate();
    }

    public void setRepeatEndHour(int hour) {

        hour = rotateHour(hour);
        hour = validateHour(hour);
        this.mRepeatEndHour = validateHour(hour);

        int degreeOfPerHour = 30;
        mDegreeEndRepeatTime = ((mRepeatEndHour * 360) / 12) + ((mRepeatEndHour * degreeOfPerHour) / 60);
        invalidate();
    }


    private int rotateHour(int hour) {
        if (hour <= 3)
            hour = hour + 9;
        else
            hour = hour - 3;

        return hour;
    }

    private int getMaxProgress(int start, int end) {
        int max = 0;
        if (end > start) {
            max = end - start;
        } else if (end < start)
            max = 12 - (start - end);

        return max;
    }

    private int validateHour(int hour) {
        if (hour < 0 || hour > 12)
            hour = 12;

        return hour;
    }

    private int validateMinute(int minute) {
        if (minute < 0)
            minute = 0;

        else if (minute > 59)
            minute = 59;

        return minute;
    }


    private float getDegreeFromHour(int hour) {
        return (hour * 360) / 12;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isIndicator)
            return false;

        int x = (int) event.getX();
        int y = (int) event.getY();
        double angel;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (CircleArea circleArea : mCircles) {

                    boolean found = (x >= circleArea.getXStart()
                            && x <= circleArea.getXEnd()
                            && y >= circleArea.getYStart()
                            && y <= circleArea.getYEnd());

                    if (found) {
                        moving = true;
                        currentCircleIDForMove = circleArea.getCircleID();
                        break;
                    }

                }

                break;
            case MotionEvent.ACTION_MOVE:

                if (moving) {
                    angel = getAngleFromPoint((double) mWidthBackgroundProgress / 2, (double) mHeightBackgroundProgress / 2, (double) x, (double) y) - 90;

                    int hour = (int) (angel + 90) / 30;
                    if (hour == 0) {
                        hour = 12;
                    }

                    switch (currentCircleIDForMove) {
                        case CIRCLE_START_TIME:
                            mDegreeStartTime = (float) angel;

                            if (onSeekCirclesListener != null)
                                onSeekCirclesListener.OnSeekChangeStartHour(hour);

                            break;
                        case CIRCLE_END_TIME:
                            mDegreeEndTime = (float) angel;

                            if (onSeekCirclesListener != null)
                                onSeekCirclesListener.OnSeekChangeEndHour(hour);

                            break;
                        case CIRCLE_REPEAT_TIME:
                            mDegreeStartRepeatTime = (float) angel;
                            if (onSeekCirclesListener != null)
                                onSeekCirclesListener.OnSeekChangeRepeat(hour);
                            break;
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                moving = false;
                currentCircleIDForMove = CircleID.NONE;
                break;
        }

        //
        return true;
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

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public double getAngleFromPoint(double firstPointX, double firstPointY, double secondPointX, double secondPointY) {

        if ((secondPointX > firstPointX)) {//above 0 to 180 degrees

            return (Math.atan2((secondPointX - firstPointX), (firstPointY - secondPointY)) * 180 / Math.PI);

        } else if ((secondPointX < firstPointX)) {//above 180 degrees to 360/0

            return 360 - (Math.atan2((firstPointX - secondPointX), (firstPointY - secondPointY)) * 180 / Math.PI);

        }//End if((secondPoint.x > firstPoint.x) && (secondPoint.y <= firstPoint.y))

        return Math.atan2(0, 0);

    }//End public float getAngleFromPoint(Point firstPoint, Point secondPoint)

    public enum CircleID {
        NONE,
        CIRCLE_START_TIME,
        CIRCLE_END_TIME,
        CIRCLE_REPEAT_TIME
    }

    private static class CircleArea {

        private CircleID circleID;
        private float xStart;
        private float xEnd;

        private float yStart;
        private float yEnd;

        public CircleID getCircleID() {
            return circleID;
        }

        public void setCircleID(CircleID circleID) {
            this.circleID = circleID;
        }

        public float getXStart() {
            return xStart;
        }

        public void setXStart(float xStart) {
            this.xStart = xStart;
        }

        public float getXEnd() {
            return xEnd;
        }

        public void setXEnd(float xEnd) {
            this.xEnd = xEnd;
        }

        public float getYStart() {
            return yStart;
        }

        public void setYStart(float yStart) {
            this.yStart = yStart;
        }

        public float getYEnd() {
            return yEnd;
        }

        public void setYEnd(float yEnd) {
            this.yEnd = yEnd;
        }
    }


}
