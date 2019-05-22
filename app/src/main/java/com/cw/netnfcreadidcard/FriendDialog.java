package com.cw.netnfcreadidcard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者：李阳
 * 时间：2019/4/1
 * 描述：
 */
public class FriendDialog extends Dialog {


    private Timer timer;

    private int second = 8;

    private TextView tv_know;
    private LinearLayout linearLayout;


    private final int enable = 12345;
    private final int disabble = 123457;
    private final int update = 32145;
    private final int destorytimer = 321456;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case update:
                    tv_know.setText("知道了( " + second + "s )");
                    break;
                case enable:
                    linearLayout.setEnabled(true);
                    tv_know.setText("知道了");


                    break;
                case disabble:
                    linearLayout.setEnabled(false);

                    break;
                case destorytimer:
                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                        timer = null;
                    }
                    break;
            }


        }
    };


    public FriendDialog(@NonNull Context context, int width, int height, @IdRes int layout, int style) {
        super(context);
        View view = getLayoutInflater().inflate(R.layout.dialog_friend, null);

        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.height = context.getResources().getDisplayMetrics().heightPixels - 60;
        params.width = context.getResources().getDisplayMetrics().widthPixels - 60;


        //去背景
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(params);

        setCancelable(false);

        linearLayout = view.findViewById(R.id.ll_btn);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(destorytimer);
                dismiss();
            }
        });

        tv_know = view.findViewById(R.id.btn_know);
        tv_know.setText("知道了( " + second + "s )");


        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                second--;

                if (second == -1) {

                    mHandler.sendEmptyMessage(enable);
                    mHandler.sendEmptyMessage(destorytimer);

                } else {
                    mHandler.sendEmptyMessage(disabble);
                    mHandler.sendEmptyMessage(update);

                }


            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}
