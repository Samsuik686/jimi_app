package com.jimi.smt.eps_appclient.Func;

import android.net.TrafficStats;

import java.math.BigDecimal;

public class NetSpeed {
    private static String TAG = "NetSpeed";
    private double lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    //获取速率
    public double getNetSpeed(int uid) {
        double nowTotalRxBytes = getTotalRxBytes(uid);
        long nowTimeStamp = System.currentTimeMillis();
        double speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000) / (nowTimeStamp - lastTimeStamp);
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        BigDecimal bg = new BigDecimal(speed);

        return  bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //获取流量
    public double getTotalRxBytes(int uid){
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为kb
    }
}
