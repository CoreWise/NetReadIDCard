package com.cw.netnfcreadidcard;

import android.app.Activity;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 作者：李阳
 * 时间：2019/3/14
 * 描述：
 */
public abstract class BaseActivity extends AppCompatActivity {


    private static final String TAG = "BaseActivity";


    private NetWorkSpeedUtils netWorkSpeedUtils;


    private Handler mHnadler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 100:
                    //tv_speed.setText("当前网速： " + msg.obj.toString());
                    // Log.e(TAG, "当前网速： " + msg.obj.toString());////

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTitle("当前网速： " + msg.obj.toString());

                        }
                    });
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        netWorkSpeedUtils = new NetWorkSpeedUtils(this, mHnadler);

        netWorkSpeedUtils.startShowNetSpeed();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        netWorkSpeedUtils.stopShowNetSpeed();

    }

    class NetWorkSpeedUtils {

        private Context context;
        private Handler mHandler;

        private long lastTotalRxBytes = 0;
        private long lastTimeStamp = 0;

        private Timer timer;


        public NetWorkSpeedUtils(Context context, Handler mHandler) {
            this.context = context;
            this.mHandler = mHandler;
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                showNetSpeed();
            }
        };

        public void startShowNetSpeed() {
            lastTotalRxBytes = getTotalRxBytes();
            lastTimeStamp = System.currentTimeMillis();
            timer = new Timer();

            timer.schedule(task, 1000, 1000); // 1s后启动任务，每2s执行一次

        }


        public void stopShowNetSpeed() {

            if (timer != null) {
                timer.purge();
                timer.cancel();
                timer = null;
            }
        }

        private long getTotalRxBytes() {
            return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
        }

        private void showNetSpeed() {
            long nowTotalRxBytes = getTotalRxBytes();
            long nowTimeStamp = System.currentTimeMillis();
            long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
            long speed2 = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 % (nowTimeStamp - lastTimeStamp));//毫秒转换

            lastTimeStamp = nowTimeStamp;
            lastTotalRxBytes = nowTotalRxBytes;

            Message msg = mHandler.obtainMessage();
            msg.what = 100;
            msg.obj = String.valueOf(speed) + "." + String.valueOf(speed2) + " kb/s";
            mHandler.sendMessage(msg);//更新界面
        }

    }


}
