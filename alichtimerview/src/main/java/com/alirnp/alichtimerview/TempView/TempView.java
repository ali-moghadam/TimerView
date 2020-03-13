package com.alirnp.alichtimerview.TempView;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.alirnp.alichtimerview.R;
import com.alirnp.alichtimerview.TimeView.AliChTimerView;
import com.alirnp.alichtimerview.TimeView.OnMoveListener;
import com.alirnp.alichtimerview.TimeView.OnSeekCirclesListener;

import java.util.ArrayList;
import java.util.List;

public class TempView extends View {

    ;
    private static final String DEFAULT_TEXT_TIME = "03:00";
    private static final String DEFAULT_TEXT_STATUS = "Temp";
    private final static int DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.parseColor("#F5F5F5");
    private final static float DEFAULT_BACKGROUND_PROGRESS_RADIUS = dpToPx(120);
    private final static float DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH = dpToPx(20);
    private final static float DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    private final static int DEFAULT_PROGRESS_COLOR = Color.parseColor("#1a8dff");
    private final static int DEFAULT_START_TIME_STROKE_COLOR = Color.parseColor("#00E676");
    private final static int DEFAULT_CLOCK_COLOR = Color.parseColor("#CFD8DC");
    private final static int DEFAULT_TEXT_TIME_COLOR = Color.parseColor("#2196F3");
    private static float DEFAULT_SPACE_TEXT = dpToPx(45);
    private final static int DEFAULT_MIN_VALUE = -10;
    private final static int DEFAULT_MAX_VALUE = 14;
    int tempHour = -1;
    private Context context;
    private float mDegreeValue;
    private float mRadiusBackgroundProgress;
    private float mStrokeWithBackgroundProgress;
    private float mStrokeWithCircles;
    private int mColorProgress;
    private int mColorBackgroundProgress;
    private int mColorStartTime;
    private int mColorTextTime;
    private int mIntegerValue;
    private String mStringTextCenter;
    private String mStringTextStatus;
    private Paint mPaintBackgroundProgress;
    private Paint mPaintStartTime;
    private Paint mPaintTimeProgress;
    private Paint mPaintClock;
    private Paint mPaintTextTime;
    private Paint mPaintTextReaming;
    private RectF mRectProgress;
    private RectF mRectClock;
    private float mFloatCenterXCircleStartTime;
    private float mFloatCenterYCircleStartTime;
    private float mFloatLengthOfClockLines;
    private float mFloatBeginOfClockLines;
    private int mWidthBackgroundProgress;
    private int mHeightBackgroundProgress;
    private List<AliChTimerView.CircleArea> mCircles = new ArrayList<>();
    private AliChTimerView.CircleArea circleArea = new AliChTimerView.CircleArea();
    private AliChTimerView.CircleID currentCircleIDForMove;
    private AliChTimerView.AM_PM mAmPmStart = AliChTimerView.AM_PM.AM;
    private OnSeekCirclesListener onSeekCirclesListener;
    private OnMoveListener onMoveListener;
    private boolean accessMoving;
    private boolean isIndicator;
    private static final String TAG = "TempViewLog";
    private int mIntegerMinValue;
    private int mIntegerMaxValue;

    public TempView(Context context) {
        super(context);

        this.context = context;
    }

    public TempView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        init(attrs);
    }

    public TempView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);

    }


    private static void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {

        final float testTextSize = dpToPx(48f);

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


    private void init(AttributeSet attrs) {

        mStringTextCenter = DEFAULT_TEXT_TIME;
        mStringTextStatus = DEFAULT_TEXT_STATUS;

        if (attrs != null) {

            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TempView, 0, 0);

            try {

                isIndicator = a.getBoolean(R.styleable.TempView_tv_is_indicator, true);

                mColorBackgroundProgress = a.getColor(R.styleable.TempView_tv_color_background_progress, DEFAULT_BACKGROUND_PROGRESS_COLOR);
                mColorStartTime = a.getColor(R.styleable.TempView_tv_color_start_time_stroke, DEFAULT_START_TIME_STROKE_COLOR);
                mColorProgress = a.getColor(R.styleable.TempView_tv_color_progress, DEFAULT_PROGRESS_COLOR);
                mColorTextTime = a.getColor(R.styleable.TempView_tv_color_text_time, DEFAULT_TEXT_TIME_COLOR);

                mStrokeWithCircles = a.getDimension(R.styleable.TempView_tv_stroke_width_circles, DEFAULT_CIRCLE_STROKE_WIDTH);
                mStrokeWithBackgroundProgress = a.getDimension(R.styleable.TempView_tv_stroke_width_background_progress, DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH);

                mStringTextCenter = a.getString(R.styleable.TempView_tv_text_center);
                if (mStringTextCenter == null)
                    mStringTextCenter = DEFAULT_TEXT_TIME;


                mStringTextStatus = a.getString(R.styleable.TempView_tv_text_status);
                if (mStringTextStatus == null)
                    mStringTextStatus = DEFAULT_TEXT_STATUS;


                mIntegerMinValue = a.getInteger(R.styleable.TempView_tv_min_value, DEFAULT_MIN_VALUE);
                mIntegerMaxValue = a.getInteger(R.styleable.TempView_tv_max_value, DEFAULT_MAX_VALUE);

                setCurrentValue(a.getInteger(R.styleable.TempView_tv_current_value, 0));



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
            mPaintStartTime.setStyle(Paint.Style.FILL_AND_STROKE);

            mPaintTimeProgress = new Paint();
            mPaintTimeProgress.setAntiAlias(true);
            mPaintTimeProgress.setStrokeWidth(mPaintBackgroundProgress.getStrokeWidth());
            mPaintTimeProgress.setColor(mColorProgress);
            mPaintTimeProgress.setStrokeCap(Paint.Cap.ROUND);
            mPaintTimeProgress.setStyle(Paint.Style.STROKE);


            mPaintClock = new Paint();
            mPaintClock.setAntiAlias(true);
            mPaintClock.setStrokeWidth(mPaintBackgroundProgress.getStrokeWidth() / 3.2f);
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
            mRectClock = new RectF();

        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mFloatBeginOfClockLines = mRadiusBackgroundProgress - (mStrokeWithBackgroundProgress);
        mFloatLengthOfClockLines = mRadiusBackgroundProgress - (mStrokeWithBackgroundProgress) - mStrokeWithCircles * 3;


        mRectProgress.set(
                (float) mWidthBackgroundProgress / 2 - mRadiusBackgroundProgress,
                (float) mHeightBackgroundProgress / 2 - mRadiusBackgroundProgress,
                (float) mWidthBackgroundProgress / 2 + mRadiusBackgroundProgress,
                (float) mHeightBackgroundProgress / 2 + mRadiusBackgroundProgress);


        mRectClock.set(
                ((mWidthBackgroundProgress / 2) - mRadiusBackgroundProgress) + mPaintBackgroundProgress.getStrokeWidth(),
                ((mHeightBackgroundProgress / 2) - mRadiusBackgroundProgress) + mPaintBackgroundProgress.getStrokeWidth(),
                ((mWidthBackgroundProgress / 2) + mRadiusBackgroundProgress) - mPaintBackgroundProgress.getStrokeWidth(),
                ((mHeightBackgroundProgress / 2) + mRadiusBackgroundProgress) - mPaintBackgroundProgress.getStrokeWidth());


        if (mStringTextStatus.equals("")) {
            setTextSizeForWidthSingleText(mPaintTextTime, mRadiusBackgroundProgress, mStringTextCenter);
        } else {

            setTextSizeForWidth(mPaintTextReaming, mRadiusBackgroundProgress / 1.3f, mStringTextStatus);
            setTextSizeForWidth(mPaintTextTime, mRadiusBackgroundProgress / 1.3f, mStringTextCenter);
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

        mFloatCenterXCircleStartTime = getDrawXOnBackgroundProgress(mDegreeValue, mRadiusBackgroundProgress, mWidthBackgroundProgress);
        mFloatCenterYCircleStartTime = getDrawYOnBackgroundProgress(mDegreeValue, mRadiusBackgroundProgress, mHeightBackgroundProgress);
        //ADD CIRCLE AREA FOR DETECT TOUCH
        mCircles.add(injectCircleArea(AliChTimerView.CircleID.CIRCLE_START_TIME, mFloatCenterXCircleStartTime, mFloatCenterYCircleStartTime, mPaintBackgroundProgress.getStrokeWidth()));


        //BACKGROUNDS
        canvas.drawCircle(mWidthBackgroundProgress / 2, mHeightBackgroundProgress / 2, mRadiusBackgroundProgress, mPaintBackgroundProgress);


        float sweep = getSweepProgressArc();
        //PROGRESS TIME
        canvas.drawArc(mRectProgress, 270, sweep, false, mPaintTimeProgress);


        //TEXT
        canvas.drawText(mStringTextCenter, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) + DEFAULT_SPACE_TEXT, mPaintTextTime);
        canvas.drawText(mStringTextStatus, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) - DEFAULT_SPACE_TEXT, mPaintTextReaming);


        //CIRCLE START TIME
        canvas.drawCircle(mFloatCenterXCircleStartTime, mFloatCenterYCircleStartTime, mPaintBackgroundProgress.getStrokeWidth() / 2, mPaintStartTime);

        //LINES
        float angel = 270;
        float x1, y1, x2, y2;

        int count = getHandCount();
        float degreePerHand = getDegreePerHand();

        for (int i = 0; i < count; i++) {

            angel += degreePerHand;

            if (i % 2 != 0) {
                x1 = (float) (Math.cos(Math.toRadians(angel))) * (mFloatBeginOfClockLines - 0) + (float) (mWidthBackgroundProgress / 2);
                y1 = (float) (Math.sin(Math.toRadians(angel))) * (mFloatBeginOfClockLines - 0) + (float) (mHeightBackgroundProgress / 2);
                x2 = (float) (Math.cos(Math.toRadians(angel))) * (mFloatLengthOfClockLines - 10) + (float) (mWidthBackgroundProgress / 2);
                y2 = (float) (Math.sin(Math.toRadians(angel))) * (mFloatLengthOfClockLines - 10) + (float) (mHeightBackgroundProgress / 2);

            } else {
                x1 = (float) (Math.cos(Math.toRadians(angel))) * (mFloatBeginOfClockLines - 10) + (float) (mWidthBackgroundProgress / 2);
                y1 = (float) (Math.sin(Math.toRadians(angel))) * (mFloatBeginOfClockLines - 10) + (float) (mHeightBackgroundProgress / 2);
                x2 = (float) (Math.cos(Math.toRadians(angel))) * (mFloatLengthOfClockLines - 0) + (float) (mWidthBackgroundProgress / 2);
                y2 = (float) (Math.sin(Math.toRadians(angel))) * (mFloatLengthOfClockLines - 0) + (float) (mHeightBackgroundProgress / 2);
            }


            canvas.drawLine(x1, y1, x2, y2, mPaintClock);
        }

    }

    private float getDegreePerHand() {
        return 360 / (float) getHandCount();
    }

    private int getHandCount() {
        int left = (mIntegerMaxValue - mIntegerMinValue);
        return left % 2 == 0 ? left : left + 1;
    }

    /*  private float getSweepProgressArc() {

          float d = 5;

          float max = (d > mDegreeValue) ? d - mDegreeValue : (360 - mDegreeValue) + d;
          float sweep = (mDegreeCurrentTime > mDegreeValue) ? mDegreeCurrentTime - mDegreeValue : 360 - (mDegreeValue - mDegreeCurrentTime);

          if (sweep > max)
              sweep = max;

          if (mDegreeValue == mDegreeCurrentTime)
              sweep = 1;


          return sweep;
      }*/
    private float getSweepProgressArc() {


        float startProgress = 270;

        float sweep = (startProgress < mDegreeValue) ? mDegreeValue - startProgress : 360 - (startProgress - mDegreeValue);


        if (mDegreeValue == startProgress)
            sweep = 1;


        Log.i(TAG, "min: " + mIntegerMinValue
                + "\tmax: " + mIntegerMaxValue
                //  +"\tmax progress: "+max
                + "\tsweep: " + sweep
                + "\tmDegreeValue: " + mDegreeValue
                + "\tstartProgress: " + startProgress
        );
        return sweep;
    }

    private AliChTimerView.CircleArea injectCircleArea(AliChTimerView.CircleID circleID, float centerX, float centerY, float radius) {

        radius = radius + (radius / 2);
        circleArea = new AliChTimerView.CircleArea();
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

    public void setCurrentValue(int value) {

        value = validateValue(value);
        value = rotateValue(value);

        this.mIntegerValue = value;
        mDegreeValue = (float) ((mIntegerValue * 360) / getHandCount());

        invalidate();
    }

    private int rotateValue(int value) {

        int _25 = getHandCount() / 4;
        int _75 = _25 * 3;

        if (value <= _25)
            value = value + _75;

        else
            value = value - _25;

        return value;
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
            max = getHandCount() - (start - end);

        return max;
    }

    private int validateValue(int value) {
        if (value < mIntegerMinValue)
            value = mIntegerMinValue;

        if (value > mIntegerMaxValue)
            value = mIntegerMaxValue;

        return value;
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
                for (AliChTimerView.CircleArea circleArea : mCircles) {

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
                        currentCircleIDForMove = AliChTimerView.CircleID.NONE;
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
                            mDegreeValue = (float) angel;

                            if (onSeekCirclesListener != null)
                                onSeekCirclesListener.OnSeekChange(currentCircleIDForMove, mAmPmStart, hour, minute);

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

                    }

                }
                accessMoving = false;
                currentCircleIDForMove = AliChTimerView.CircleID.NONE;
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

                if (mAmPmStart == AliChTimerView.AM_PM.AM)
                    mAmPmStart = AliChTimerView.AM_PM.PM;
                else
                    mAmPmStart = AliChTimerView.AM_PM.AM;
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

    public void setmAmPmStart(AliChTimerView.AM_PM mAmPmStart) {
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


    private static class CircleArea {

        private AliChTimerView.CircleID circleID;
        private float xStart;
        private float xEnd;

        private float yStart;
        private float yEnd;

        public AliChTimerView.CircleID getCircleID() {
            return circleID;
        }

        public void setCircleID(AliChTimerView.CircleID circleID) {
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
