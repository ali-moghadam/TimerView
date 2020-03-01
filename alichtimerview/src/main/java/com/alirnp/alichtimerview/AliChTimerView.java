package com.alirnp.alichtimerview;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AliChTimerView extends View {

    private static final String TAG = "AliChTimerView";
    private static final String DEFAULT_TEXT_TIME = "3:00";
    private static final String DEFAULT_TEXT_STATUS = "Reaming Time";
    private final static int DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.parseColor("#F5F5F5");
    private final static float DEFAULT_BACKGROUND_PROGRESS_RADIUS = dpToPx(120);
    private final static float DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH = dpToPx(20);
    private final static float DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    private final static int DEFAULT_PROGRESS_COLOR = Color.parseColor("#00E676");
    private final static int DEFAULT_START_TIME_STROKE_COLOR = Color.parseColor("#2196F3");
    private final static int DEFAULT_END_TIME_STROKE_COLOR = Color.parseColor("#f44336");
    private final static int DEFAULT_CLOCK_COLOR = Color.parseColor("#CFD8DC");
    private final static int DEFAULT_REPEAT_COLOR = Color.parseColor("#FDD835");
    private final static int DEFAULT_TEXT_TIME_COLOR = Color.parseColor("#2196F3");
    private static float DEFAULT_SPACE_TEXT = dpToPx(45);

    private Context context;

    private float mDegreeStartTime;
    private float mDegreeEndTime;
    private float mDegreeStartRepeatTime;
    private float mDegreeCurrentTime;

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
    private int mRepeatStartHour;
    private int mRepeatStartMinute;


    private String mStringTextCenter;
    private String mStringTextStatus;

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

    private float mFloatLengthOfClockLines;
    private float mFloatBeginOfClockLines;

    private int mWidthBackgroundProgress;
    private int mHeightBackgroundProgress;

    private List<CircleArea> mCircles = new ArrayList<>();
    private CircleArea circleArea = new CircleArea();

    private CircleID currentCircleIDForMove;
    private AM_PM mAmPmStart = AM_PM.AM;
    private AM_PM mAmPmEnd = AM_PM.AM;

    int tempHour = -1;

    private boolean accessMoving;
    private boolean isIndicator;
    private boolean isShowRepeat;
    private boolean isShowProgress = true;

    private OnSeekCirclesListener onSeekCirclesListener;
    private OnMoveListener onMoveListener;

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


    private static void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {

        final float testTextSize = 48f;

        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        float desiredTextSize = testTextSize * desiredWidth / bounds.width();
        DEFAULT_SPACE_TEXT = desiredTextSize;

        paint.setTextSize(desiredTextSize);
    }

    private static void setTextSizeForWidthSingleText(Paint paint, float desiredWidth, String text) {

        final float testTextSize = 48f;

        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        float desiredTextSize = testTextSize * desiredWidth / bounds.width();
        DEFAULT_SPACE_TEXT = desiredTextSize / 2;

        paint.setTextSize(desiredTextSize);
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

    public static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void setOnSeekCirclesListener(OnSeekCirclesListener onSeekCirclesListener) {
        this.onSeekCirclesListener = onSeekCirclesListener;
    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    public void setIsIndicator(boolean isIndicator) {
        this.isIndicator = isIndicator;
    }

    public void setTextCenter(String time) {
        this.mStringTextCenter = time;
    }

    public void setTextStatus(String status) {
        this.mStringTextStatus = status;
    }

    public void setIsShowProgress(boolean isShow) {
        this.isShowProgress = isShow;
    }

    private void init(AttributeSet attrs) {

        mStringTextCenter = DEFAULT_TEXT_TIME;
        mStringTextStatus = DEFAULT_TEXT_STATUS;

        if (attrs != null) {

            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AliChTimerView, 0, 0);

            try {

                setStartTimeHour(a.getInteger(R.styleable.AliChTimerView_atv_value_start_time_hour, 0));
                setStartTimeMinute(a.getInteger(R.styleable.AliChTimerView_atv_value_start_time_minute, 0));

                setEndTimeHour(a.getInteger(R.styleable.AliChTimerView_atv_value_end_time_hour, 7));
                setEndTimeMinute(a.getInteger(R.styleable.AliChTimerView_atv_value_end_time_minute, 0));

                setRepeatStartHour(a.getInteger(R.styleable.AliChTimerView_atv_value_repeat_start_hour, 0));

                isIndicator = a.getBoolean(R.styleable.AliChTimerView_atv_is_indicator, true);
                isShowRepeat = a.getBoolean(R.styleable.AliChTimerView_atv_is_show_repeat, true);

                mColorBackgroundProgress = a.getColor(R.styleable.AliChTimerView_atv_color_background_progress, DEFAULT_BACKGROUND_PROGRESS_COLOR);
                mColorStartTime = a.getColor(R.styleable.AliChTimerView_atv_color_start_time_stroke, DEFAULT_START_TIME_STROKE_COLOR);
                mColorEndTime = a.getColor(R.styleable.AliChTimerView_atv_color_end_time_stroke, DEFAULT_END_TIME_STROKE_COLOR);
                mColorProgress = a.getColor(R.styleable.AliChTimerView_atv_color_progress, DEFAULT_PROGRESS_COLOR);
                mColorRepeat = a.getColor(R.styleable.AliChTimerView_atv_color_repeat_stroke, DEFAULT_REPEAT_COLOR);
                mColorTextTime = a.getColor(R.styleable.AliChTimerView_atv_color_text_time, DEFAULT_TEXT_TIME_COLOR);

                mStrokeWithCircles = a.getDimension(R.styleable.AliChTimerView_atv_stroke_width_circles, DEFAULT_CIRCLE_STROKE_WIDTH);
                mStrokeWithBackgroundProgress = a.getDimension(R.styleable.AliChTimerView_atv_stroke_width_background_progress, DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH);

                mStringTextCenter = a.getString(R.styleable.AliChTimerView_atv_text_center);
                if (mStringTextCenter == null)
                    mStringTextCenter = DEFAULT_TEXT_TIME;


                mStringTextStatus = a.getString(R.styleable.AliChTimerView_atv_text_status);
                if (mStringTextStatus == null)
                    mStringTextStatus = DEFAULT_TEXT_STATUS;


                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                mDegreeCurrentTime = calculateDegreeFromHour(hour, minute);


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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mFloatBeginOfClockLines = mRadiusBackgroundProgress - (mStrokeWithBackgroundProgress);
        mFloatLengthOfClockLines = mRadiusBackgroundProgress - (mStrokeWithBackgroundProgress) - mStrokeWithCircles * 3;

        mRadiusBackgroundRepeat = mFloatLengthOfClockLines - (mPaintBackgroundRepeat.getStrokeWidth() * 2) - mPaintBackgroundProgress.getStrokeWidth() / 2;


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


        if (mStringTextStatus.equals("")) {
            setTextSizeForWidthSingleText(mPaintTextTime, mRadiusBackgroundRepeat, mStringTextCenter);
        } else {

            setTextSizeForWidth(mPaintTextReaming, mRadiusBackgroundRepeat * 1.2f, mStringTextStatus);
            setTextSizeForWidth(mPaintTextTime, mRadiusBackgroundRepeat * 1.1f, mStringTextCenter);
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
                mHeightBackgroundProgress = Math.min(heightMeasureSize, getDesireHeight() + getHorizontalPadding());

                break;
        }

        if (widthMeasureMode == MeasureSpec.EXACTLY || heightMeasureMode == MeasureSpec.EXACTLY) {
            int size = Math.min(widthMeasureSize - getHorizontalPadding(), heightMeasureSize - getVerticalPadding());
            mRadiusBackgroundProgress = (size - mPaintBackgroundProgress.getStrokeWidth()) / 2;
        } else {
            mRadiusBackgroundProgress = DEFAULT_BACKGROUND_PROGRESS_RADIUS;
        }

        int length = Math.min(mWidthBackgroundProgress, mHeightBackgroundProgress);
        setMeasuredDimension(length, length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCircles.clear();

        mFloatCenterXCircleStartTime = getDrawXOnBackgroundProgress(mDegreeStartTime, mRadiusBackgroundProgress, mWidthBackgroundProgress);
        mFloatCenterYCircleStartTime = getDrawYOnBackgroundProgress(mDegreeStartTime, mRadiusBackgroundProgress, mHeightBackgroundProgress);
        //ADD CIRCLE AREA FOR DETECT TOUCH
        mCircles.add(injectCircleArea(CircleID.CIRCLE_START_TIME, mFloatCenterXCircleStartTime, mFloatCenterYCircleStartTime, mPaintBackgroundProgress.getStrokeWidth()));


        mFloatCenterXCircleEndTime = getDrawXOnBackgroundProgress(mDegreeEndTime, mRadiusBackgroundProgress, mWidthBackgroundProgress);
        mFloatCenterYCircleEndTime = getDrawYOnBackgroundProgress(mDegreeEndTime, mRadiusBackgroundProgress, mHeightBackgroundProgress);
        //ADD CIRCLE AREA FOR DETECT TOUCH
        mCircles.add(injectCircleArea(CircleID.CIRCLE_END_TIME, mFloatCenterXCircleEndTime, mFloatCenterYCircleEndTime, mPaintBackgroundProgress.getStrokeWidth()));


        mFloatCenterXCircleStartRepeat = getDrawXOnBackgroundProgress(mDegreeStartRepeatTime, mRadiusBackgroundRepeat, mWidthBackgroundProgress);
        mFloatCenterYCircleStartRepeat = getDrawYOnBackgroundProgress(mDegreeStartRepeatTime, mRadiusBackgroundRepeat, mHeightBackgroundProgress);
        //ADD CIRCLE AREA FOR DETECT TOUCH
        mCircles.add(injectCircleArea(CircleID.CIRCLE_REPEAT_TIME, mFloatCenterXCircleStartRepeat, mFloatCenterYCircleStartRepeat, mPaintBackgroundRepeat.getStrokeWidth()));

        //BACKGROUNDS
        canvas.drawCircle(mWidthBackgroundProgress / 2, mHeightBackgroundProgress / 2, mRadiusBackgroundProgress, mPaintBackgroundProgress);

        if (isShowRepeat)
            canvas.drawCircle(mWidthBackgroundProgress / 2, mHeightBackgroundProgress / 2, mRadiusBackgroundRepeat, mPaintBackgroundRepeat);


        if (isIndicator) {

            float sweep = getSweepProgressArc();

            //PROGRESS TIME
            if (isShowProgress)
                canvas.drawArc(mRectProgress, mDegreeStartTime, sweep, false, mPaintTimeProgress);

            //PROGRESS REPEAT TIME
            canvas.drawArc(mRectRepeatProgress, mDegreeStartRepeatTime, 10, false, mPaintRepeatProgress);

            //TEXT
            canvas.drawText(mStringTextCenter, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) + DEFAULT_SPACE_TEXT, mPaintTextTime);
            canvas.drawText(mStringTextStatus, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) - DEFAULT_SPACE_TEXT, mPaintTextReaming);


        } else {
            //TEXT
            canvas.drawText(mStringTextCenter, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) + DEFAULT_SPACE_TEXT, mPaintTextTime);

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


        if (isShowRepeat) {
            //CIRCLE START REPEAT
            if (isInEditMode())
                canvas.drawCircle(mFloatCenterXCircleStartRepeat, mFloatCenterYCircleStartRepeat, mPaintBackgroundProgress.getStrokeWidth() / 2, mPaintRepeat);
            else
                fillCircleStrokeBorder(canvas, mFloatCenterXCircleStartRepeat, mFloatCenterYCircleStartRepeat, mPaintBackgroundProgress.getStrokeWidth() / 2, Color.WHITE, mStrokeWithCircles / 2, mColorRepeat, mPaintRepeat);
        }

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

    private float getSweepProgressArc() {

        float max = (mDegreeEndTime > mDegreeStartTime) ? mDegreeEndTime - mDegreeStartTime : (360 - mDegreeStartTime) + mDegreeEndTime;
        float sweep = (mDegreeCurrentTime > mDegreeStartTime) ? mDegreeCurrentTime - mDegreeStartTime : 360 - (mDegreeStartTime - mDegreeCurrentTime);

        if (sweep > max)
            sweep = max;

        if (mDegreeStartTime == mDegreeCurrentTime)
            sweep = 1;

        return sweep;
    }

    private CircleArea injectCircleArea(CircleID circleID, float centerX, float centerY, float radius) {

        radius = radius + (radius / 2);
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
        int degreeOfHour = ((mStartTimeHour * 360) / 12);
        int degreeOfMinute = ((mStartTimeMinute * degreeOfPerHour) / 60);
        mDegreeStartTime = degreeOfHour + degreeOfMinute;

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

    public void setRepeatStartHour(int hour) {

        hour = rotateHour(hour);
        hour = validateHour(hour);
        this.mRepeatStartHour = validateHour(hour);

        int degreeOfPerHour = 30;
        mDegreeStartRepeatTime = ((mRepeatStartHour * 360) / 12) + ((mRepeatStartHour * degreeOfPerHour) / 60);
        invalidate();
    }

    public void setRepeatStartMinutes(int minute) {

        mRepeatStartMinute = validateMinute(minute);

        int degreeOfPerHour = 30;
        int degreeOfMinute = (mRepeatStartMinute * degreeOfPerHour) / 60;
        mDegreeStartRepeatTime = getDegreeFromHour(mRepeatStartHour) + degreeOfMinute;
        invalidate();
    }

    public void setShowRepeat(boolean show) {
        isShowRepeat = show;
        invalidate();
    }

    public void setStartAmPm(AM_PM amPm) {
        this.mAmPmStart = amPm;
    }

    public void setStartAmPm(String amPm) {
        if (amPm.equalsIgnoreCase("pm"))
            this.mAmPmStart = AM_PM.PM;
        else
            this.mAmPmStart = AM_PM.AM;
    }

    public void setEndAmPm(AM_PM amPm) {
        this.mAmPmEnd = amPm;
    }

    public void setEndAmPm(String amPm) {
        if (amPm.equalsIgnoreCase("pm"))
            this.mAmPmEnd = AM_PM.PM;
        else
            this.mAmPmEnd = AM_PM.AM;
    }

    public String getStartTime() {

        return addZeroBeforeTime(rotateHourRevert(mStartTimeHour)) + ":" + addZeroBeforeTime(mStartTimeMinute);
    }

    public String getEndTime() {
        return addZeroBeforeTime(rotateHourRevert(mEndTimeHour)) + ":" + addZeroBeforeTime(mEndTimeMinute);
    }

    private float calculateDegreeFromHour(int hour, int minute) {
        hour = rotateHour(hour);
        int degreeOfPerHour = 30;
        int degreeOfHour = ((hour * 360) / 12);
        int degreeOfMinute = ((minute * degreeOfPerHour) / 60);
        return degreeOfHour + degreeOfMinute;
    }


    private int rotateHour(int hour) {
        if (hour <= 3)
            hour = hour + 9;
        else
            hour = hour - 3;

        return hour;
    }

    private int rotateHourRevert(int hour) {
        if (hour <= 9)
            hour = hour + 3;

        else
            hour = (hour + 3) - 12;

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

        int hour;
        int minute;

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
                        accessMoving = true;
                        currentCircleIDForMove = circleArea.getCircleID();
                        break;
                    } else {
                        accessMoving = false;
                        currentCircleIDForMove = CircleID.NONE;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:

                if (accessMoving) {
                    angel = getAngleFromPoint((double) mWidthBackgroundProgress / 2, (double) mHeightBackgroundProgress / 2, (double) x, (double) y) - 90;

                    hour = getHourFromAngel(angel);
                    minute = getMinuteFromAngel(angel);

                    changeAmPm(hour, tempHour);
                    tempHour = hour;

                    switch (currentCircleIDForMove) {
                        case CIRCLE_START_TIME:
                            mDegreeStartTime = (float) angel;

                            if (onSeekCirclesListener != null)
                                onSeekCirclesListener.OnSeekChange(currentCircleIDForMove, mAmPmStart, hour, minute);

                            break;
                        case CIRCLE_END_TIME:
                            mDegreeEndTime = (float) angel;
                            if (onSeekCirclesListener != null)
                                onSeekCirclesListener.OnSeekChange(currentCircleIDForMove, mAmPmEnd, hour, minute);

                            break;
                        case CIRCLE_REPEAT_TIME:
                            mDegreeStartRepeatTime = (float) angel;
                            if (onSeekCirclesListener != null)
                                onSeekCirclesListener.OnSeekChange(currentCircleIDForMove, AM_PM.NONE, hour, minute);
                            break;
                    }

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:

                if (onSeekCirclesListener != null) {
                    angel = getAngleFromPoint((double) mWidthBackgroundProgress / 2, (double) mHeightBackgroundProgress / 2, (double) x, (double) y) - 90;

                    switch (currentCircleIDForMove) {
                        case CIRCLE_START_TIME:
                            onSeekCirclesListener.OnSeekComplete(currentCircleIDForMove, mAmPmStart, getHourFromAngel(angel), getMinuteFromAngel(angel));
                            break;

                        case CIRCLE_END_TIME:
                            onSeekCirclesListener.OnSeekComplete(currentCircleIDForMove, mAmPmEnd, getHourFromAngel(angel), getMinuteFromAngel(angel));
                            break;

                        case CIRCLE_REPEAT_TIME:
                            onSeekCirclesListener.OnSeekComplete(currentCircleIDForMove, AM_PM.NONE, getHourFromAngel(angel), getMinuteFromAngel(angel));
                            break;
                    }

                }
                accessMoving = false;
                currentCircleIDForMove = CircleID.NONE;
                break;
        }

        if (onMoveListener != null) {
            onMoveListener.OnMove(accessMoving);
        }

        return true;
    }

    private void changeAmPm(int hour, int tempHour) {
        if (tempHour == -1) tempHour = hour;
        if (hour == 12) {
            if (tempHour == 11)
                revertAmPm();

        } else if (hour == 11) {
            if (tempHour == 12)
                revertAmPm();
        }
    }

    private void revertAmPm() {
        switch (currentCircleIDForMove) {
            case CIRCLE_START_TIME:

                if (mAmPmStart == AM_PM.AM)
                    mAmPmStart = AM_PM.PM;
                else
                    mAmPmStart = AM_PM.AM;
                break;

            case CIRCLE_END_TIME:

                if (mAmPmEnd == AM_PM.AM)
                    mAmPmEnd = AM_PM.PM;
                else
                    mAmPmEnd = AM_PM.AM;

                break;
        }

    }


    private int getHourFromAngel(double angel) {
        int hour = (int) (angel + 90) / 30;
        if (hour == 0) {
            hour = 12;
        }
        return hour;
    }


    private int getMinuteFromAngel(double angel) {
        int maxMinute = 59;
        int maxMinuteDegree = 29;
        int perClockDegree = 30;
        int minute = (int) (((angel + 90) % perClockDegree) * maxMinute) / maxMinuteDegree;
        if (minute == 60)
            minute = 59;
        return minute;
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

    public void setmAmPmStart(AM_PM mAmPmStart) {
        this.mAmPmStart = mAmPmStart;
    }


    private String addZeroBeforeTime(int time) {
        if (time <= 9)
            return "0" + time;
        else
            return String.valueOf(time);
    }


    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public double getAngleFromPoint(double firstPointX, double firstPointY, double secondPointX, double secondPointY) {

        if ((secondPointX > firstPointX)) {

            return (Math.atan2((secondPointX - firstPointX), (firstPointY - secondPointY)) * 180 / Math.PI);

        } else if ((secondPointX < firstPointX)) {

            return 360 - (Math.atan2((firstPointX - secondPointX), (firstPointY - secondPointY)) * 180 / Math.PI);

        }

        return Math.atan2(0, 0);

    }

    public enum CircleID {
        NONE,
        CIRCLE_START_TIME,
        CIRCLE_END_TIME,
        CIRCLE_REPEAT_TIME
    }

    public enum AM_PM {
        NONE,
        AM,
        PM
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
