package com.ztemt.test.basic.item.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class CenterTouchView extends View {

    private static final int LINES = 10;
    private static final Point[][] POINTS = new Point[LINES][2];

    private Path  mPath;
    private Paint mPaint;
    private Paint mGuideLinePaint;
    private Paint mBackgroundPaint;
    private Point mPoint;

    private OnTouchChangedListener mListener;

    private boolean mFlags[] = new boolean[LINES];
    private int mPadding;
    private int mX, mY;

    public CenterTouchView(Context context) {
        this(context, null);
    }

    public CenterTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4 * getResources().getDisplayMetrics().density);

        mGuideLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGuideLinePaint.setStyle(Paint.Style.STROKE);
        mGuideLinePaint.setColor(Color.GRAY);
        mGuideLinePaint.setStrokeWidth(4 * getResources().getDisplayMetrics().density);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(Color.WHITE);

        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                40, getResources().getDisplayMetrics());
    }

    public void setOnTouchChangedListener(OnTouchChangedListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            touchDown(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            touchMove(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            touchUp();
            invalidate();
            break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int p = mPadding;

        POINTS[0] = new Point[] { new Point(p, p), new Point(w - p, h - p) };
        POINTS[1] = new Point[] { new Point(w - p, p), new Point(p, h - p) };

        POINTS[2] = new Point[] { new Point((w + p) / 3, p),
                new Point(w - p, (h - 2 * p) * 2 / 3 + p) };
        POINTS[3] = new Point[] { new Point((2 * w - p) / 3, p),
                new Point(w - p, (h - 2 * p) / 3 + p) };
        POINTS[4] = new Point[] { new Point(p, (h + p) / 3),
                new Point((2 * w - p) / 3, h - p) };
        POINTS[5] = new Point[] { new Point(p, (h - 2 * p) * 2 / 3 + p),
                new Point((w + p) / 3, h - p) };

        POINTS[6] = new Point[] { new Point((2 * w - p) / 3, p),
                new Point(p, (h - 2 * p) * 2 / 3 + p) };
        POINTS[7] = new Point[] { new Point((w + p) / 3, p),
                new Point(p, (h - 2 * p) / 3 + p) };
        POINTS[8] = new Point[] { new Point(w - p, (h - 2 * p) / 3 + p),
                new Point((w + p) / 3, h - p) };
        POINTS[9] = new Point[] {new Point(w - p, (h - 2 * p) * 2 / 3 + p),
                new Point((2 * w - p) / 3, h - p) };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawRect(mPadding, mPadding, getWidth() - mPadding,
                getHeight() - mPadding, mBackgroundPaint);

        for (int i = 0; i < LINES; i++) {
            mGuideLinePaint.setColor(mFlags[i] ? Color.GREEN : Color.GRAY);
            canvas.drawLine(POINTS[i][0].x, POINTS[i][0].y, POINTS[i][1].x,
                    POINTS[i][1].y, mGuideLinePaint);
        }

        canvas.drawPath(mPath, mPaint);
    }

    private void touchDown(int x, int y) {
        mPath.reset();

        if ((mPoint = locate(x, y)) != null) {
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
    }

    private void touchMove(int x, int y) {
        if (mPoint != null) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        int index = indexOfLines(mPoint, locate(mX, mY));

        if (index > -1) {
            // set pass flag
            mFlags[index] = true;
        }

        // kill this so we don't double draw
        mPath.reset();

        // pass or not
        if (mListener != null && checkLines()) {
            mListener.onTouchFinish(this);
        }
    }

    private Point locate(int x, int y) {
        int dx, dy;

        for (int i = 0; i < LINES; i++) {
            dx = Math.abs(x - POINTS[i][0].x);
            dy = Math.abs(y - POINTS[i][0].y);
            if (dx < mPadding && dy < mPadding) {
                return POINTS[i][0];
            }

            dx = Math.abs(x - POINTS[i][1].x);
            dy = Math.abs(y - POINTS[i][1].y);
            if (dx < mPadding && dy < mPadding) {
                return POINTS[i][1];
            }
        }
        return null;
    }

    private int indexOfLines(Point start, Point end) {
        for (int i = 0; i < LINES; i++) {
            if (POINTS[i][0].equals(start) && POINTS[i][1].equals(end)
                    || POINTS[i][0].equals(end)
                    && POINTS[i][1].equals(start)) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkLines() {
        for (int i = 0; i < LINES; i++) {
            if (!mFlags[i]) return false;
        }
        return true;
    }
}
