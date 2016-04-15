package com.ztemt.test.basic.item.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class BorderTouchView extends View {

    private Paint mPaint;
    private Rect  mNorth[];
    private Rect  mSouth[];
    private Rect  mWest[];
    private Rect  mEast[];

    private OnTouchChangedListener mListener;

    private boolean mNorthFlags[];
    private boolean mSouthFlags[];
    private boolean mWestFlags[];
    private boolean mEastFlags[];
    private boolean mDistanceValid;

    private int mMaxDistance;
    private int mX = 0;
    private int mY = 0;
    private int mRectWidth;
    private int mRectHeight;
    private int mNorthHeight;
    private int mSouthHeight;
    private int mWestHeight;
    private int mEastHeight;

    public BorderTouchView(Context context) {
        this(context, null);
    }

    public BorderTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mRectWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40, dm);
        mRectHeight = (int) dm.density * 16;

        // Maximum distance between points
        mMaxDistance = (int) dm.density * 21;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
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
        int l, r, t, b;

        mNorth = new Rect[w / mRectHeight];
        mNorthFlags = new boolean[mNorth.length];

        mSouth = new Rect[mNorth.length];
        mSouthFlags = new boolean[mSouth.length];

        mWest = new Rect[(h - 2 * mRectWidth) / mRectHeight];
        mWestFlags = new boolean[mWest.length];

        mEast = new Rect[mWest.length];
        mEastFlags = new boolean[mEast.length];

        mNorthHeight = w / mNorth.length;
        for (int i = 0; i < mNorth.length; i++) {
            l = mNorthHeight * i;
            r = (i == mNorth.length - 1) ? w : l + mNorthHeight;
            t = 0;
            b = mRectWidth;
            mNorth[i] = new Rect(l + 1, t + 1, r - 1, b - 1);
        }

        mSouthHeight = w / mSouth.length;
        for (int i = 0; i < mSouth.length; i++) {
            l = mSouthHeight * i;
            r = (i == mSouth.length - 1) ? w : l + mSouthHeight;
            t = h - mRectWidth;
            b = h;
            mSouth[i] = new Rect(l + 1, t + 1, r - 1, b - 1);
        }

        mWestHeight = (h - 2 * mRectWidth) / mWest.length;
        for (int i = 0; i < mWest.length; i++) {
            l = 0;
            r = mRectWidth;
            t = mRectWidth + mWestHeight * i;
            b = (i == mWest.length - 1) ? h - mRectWidth : t + mWestHeight;
            mWest[i] = new Rect(l + 1, t + 1, r - 1, b - 1);
        }

        mEastHeight = (h - 2 * mRectWidth) / mEast.length;
        for (int i = 0; i < mEast.length; i++) {
            l = w - mRectWidth;
            r = w;
            t = mRectWidth + mEastHeight * i;
            b = (i == mEast.length - 1) ? h - mRectWidth : t + mEastHeight;
            mEast[i] = new Rect(l + 1, t + 1, r - 1, b - 1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        for (int i = 0; i < mNorth.length; i++) {
            mPaint.setColor(mNorthFlags[i] ? Color.GREEN : Color.WHITE);
            canvas.drawRect(mNorth[i], mPaint);
        }
        for (int i = 0; i < mSouth.length; i++) {
            mPaint.setColor(mSouthFlags[i] ? Color.GREEN : Color.WHITE);
            canvas.drawRect(mSouth[i], mPaint);
        }
        for (int i = 0; i < mWest.length; i++) {
            mPaint.setColor(mWestFlags[i] ? Color.GREEN : Color.WHITE);
            canvas.drawRect(mWest[i], mPaint);
        }
        for (int i = 0; i < mEast.length; i++) {
            mPaint.setColor(mEastFlags[i] ? Color.GREEN : Color.WHITE);
            canvas.drawRect(mEast[i], mPaint);
        }
    }

    private void touchDown(int x, int y) {
        mDistanceValid = true;
        mX = x;
        mY = y;
    }

    private void touchMove(int x, int y) {
        int dx = Math.abs(x - mX);
        int dy = Math.abs(y - mY);

        mX = x;
        mY = y;

        if (mDistanceValid) {
            mDistanceValid = dx < mMaxDistance && dy < mMaxDistance;
        }

        if (mDistanceValid) {
            setBorderFlag(x, y);
        }
    }

    private void touchUp() {
        if (mListener != null && checkBorders()) {
            mListener.onTouchFinish(this);
        }
    }

    private void setBorderFlag(int x, int y) {
        if (y < mRectWidth) {
            // North
            int i = x / mNorthHeight;
            if (i > -1 && i < mNorthFlags.length) {
                mNorthFlags[i] = true;
            }
        } else if (y > getHeight() - mRectWidth) {
            // South
            int i = x / mSouthHeight;
            if (i > -1 && i < mSouthFlags.length) {
                mSouthFlags[i] = true;
            }
        } else if (x < mRectWidth) {
            // West
            int i = (y - mRectWidth) / mWestHeight;
            if (i > -1 && i < mWestFlags.length) {
                mWestFlags[i] = true;
            }
        } else if (x > getWidth() - mRectWidth) {
            // East
            int i = (y - mRectWidth) / mEastHeight;
            if (i > -1 && i < mEastFlags.length) {
                mEastFlags[i] = true;
            }
        }
    }

    private boolean checkBorders() {
        for (int i = 0; i < mNorthFlags.length; i++) {
            if (!mNorthFlags[i]) return false;
        }
        for (int i = 0; i < mSouthFlags.length; i++) {
            if (!mSouthFlags[i]) return false;
        }
        for (int i = 0; i < mWestFlags.length; i++) {
            if (!mWestFlags[i]) return false;
        }
        for (int i = 0; i < mEastFlags.length; i++) {
            if (!mEastFlags[i]) return false;
        }

        return true;
    }
}
