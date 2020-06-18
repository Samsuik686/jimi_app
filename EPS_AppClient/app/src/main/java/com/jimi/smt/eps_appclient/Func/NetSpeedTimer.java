package com.jimi.smt.eps_appclient.Func;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;


public class NetSpeedTimer {

    private long defaultDelay = 1000;
    private long defaultPeriod = 1000;
    private static final int ERROR_CODE = -101011010;
    private int mMsgWhat = ERROR_CODE;
    private Context mContext;
    private NetSpeed mNetSpeed;
    private Handler mHandler;

    private SpeedTimerTask mSpeedTimerTask;

    public static final int NET_SPEED_TIMER_DEFAULT = 101010;

    public NetSpeedTimer(Context context, NetSpeed netSpeed, Handler handler) {
        this.mContext = context;
        this.mNetSpeed = netSpeed;
        this.mHandler = handler;
    }

    public NetSpeedTimer setDelayTime(long delay) {
        this.defaultDelay = delay;
        return this;
    }

    public NetSpeedTimer setPeriodTime(long period) {
        this.defaultPeriod = period;
        return this;
    }

    public NetSpeedTimer setHandlerWhat(int what) {
        this.mMsgWhat = what;
        return this;
    }

    /**
     * 开启获取网速定时器
     */
    public void startSpeedTimer() {
        Timer timer = new Timer();
        mSpeedTimerTask = new SpeedTimerTask(mContext, mNetSpeed, mHandler, mMsgWhat);
        timer.schedule(mSpeedTimerTask, defaultDelay, defaultPeriod);
    }

    /**
     * 关闭定时器
     */
    public void stopSpeedTimer() {
        if (null != mSpeedTimerTask) {
            mSpeedTimerTask.cancel();
        }
    }

    /**
     * 内部静态类
     */
    private static class SpeedTimerTask extends TimerTask {
        private Context mContext;
        private NetSpeed mNetSpeed;
        private Handler mHandler;
        private int mWhat;

        public SpeedTimerTask(Context context, NetSpeed netSpeed, Handler handler, int what) {
            this.mContext = context;
            this.mNetSpeed = netSpeed;
            this.mHandler = handler;
            this.mWhat = what;
        }

        @Override
        public void run() {
            if (null != mNetSpeed && null != mHandler) {
                Message msg = mHandler.obtainMessage();
                if (mWhat != ERROR_CODE) {
                    msg.what = mWhat;
                } else {
                    msg.what = NET_SPEED_TIMER_DEFAULT;
                }
                msg.obj = mNetSpeed.getNetSpeed(mContext.getApplicationInfo().uid);
                mHandler.sendMessage(msg);
            }
        }
    }
}