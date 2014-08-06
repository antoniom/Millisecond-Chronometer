package com.elacarte.game.tilegame;

import java.text.DecimalFormat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

// credit: https://github.com/antoniom/Millisecond-Chronometer
public class Chronometer extends TextView {
    
    private static final String TAG = "Chronometer";

    public interface OnChronometerTickListener {
        void onChronometerTick(Chronometer chronometer);
    }

    private int hours = 0, minutes = 0, seconds = 0, milliseconds = 0;
    private long mBase;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
    private OnChronometerTickListener mOnChronometerTickListener;
    private int[] gameTimeArray = new int[]{0, 0, 0};
    
    private static final int TICK_WHAT = 2;

    private long timeElapsed;
    
    public Chronometer(Context context) {
        this (context, null, 0);
    }

    public Chronometer(Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public Chronometer(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);

        init();
    }

    private void init() {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    public long getBase() {
        return mBase;
    }

    public void setOnChronometerTickListener(
            OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }

    public void start() {
        mBase = SystemClock.elapsedRealtime();
        mStarted = true;
        updateRunning();
    }

    // set game timer to stopping time 
    public void stop() {
        mStarted = false;
        updateRunning();
    }


    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }
    
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    private synchronized void updateText(long now) {
        timeElapsed = now - mBase;
        
        DecimalFormat df = new DecimalFormat("00");
        
        hours = (int)(timeElapsed / (3600 * 1000));
        int remaining = (int)(timeElapsed % (3600 * 1000));
        
        minutes = (int)(remaining / (60 * 1000));
        remaining = (int)(remaining % (60 * 1000));
        
        seconds = (int)(remaining / 1000);
        remaining = (int)(remaining % (1000));
        
        milliseconds = (int)(((int)timeElapsed % 1000) / 100);
        
        String text = "";
        
        if (hours > 0) {
            text += df.format(hours) + ":";
        }
        
        text += df.format(minutes) + ":";
        text += df.format(seconds) + ":";
        text += Integer.toString(milliseconds);
        
        setText(text);
        
        setGameTime(minutes, seconds, milliseconds);
    }
    
    private void setGameTime(int minutes, int seconds, int milliseconds) {
        gameTimeArray[0] = this.minutes;
        gameTimeArray[1] = this.seconds;
        gameTimeArray[2] = this.milliseconds;
    }
    
    int[] getGameTime() {
        return gameTimeArray;
    }
    
    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler,
                        TICK_WHAT), 100);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                sendMessageDelayed(Message.obtain(this , TICK_WHAT),
                        100);
            }
        }
    };

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }
}
