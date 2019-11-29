package com.cw.netnfcreadidcard;


import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cw.netnfcreadidcardlib.Bean.IdCardInfo;
import com.cw.netnfcreadidcardlib.Interface.Conn;
import com.cw.netnfcreadidcardlib.Interface.ReadListener;
import com.cw.netnfcreadidcardlib.ReadAPI;
import com.cw.netnfcreadidcardlib.USB.USBConn;
import com.cw.netnfcreadidcardlib.Utils.DataUtils;

public class USBTestActivity extends BaseActivity implements ReadListener {


    private String m_RootPath;
    private int success = 0;
    private int sum = 0;

    // view
    private TextView txtIdName;
    private TextView txtIdSex;
    private TextView txtIdNation;
    private TextView txtIdYear;
    private TextView txtIdMouth;
    private TextView txtIdDay;
    private TextView txtIdAddress;
    private TextView txtIdNum;
    private TextView mTvFingerData;
    private ImageView imgVIdPhoto;

    private TextView mTvResult;
    private EditText mEtRegistrationCode;

    private TextView mTvUID;

    /**
     * 是否是连续读取
     */
    private boolean isSequentialRead = false;
    private ReadAPI api;
    private Handler mHandler = null;

    private long startMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);


        initView();

        setRootPath();

        Conn conn = new USBConn(getApplicationContext());
        api = new ReadAPI(getApplicationContext(), R.raw.base, R.raw.license, m_RootPath, this, conn);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        mHandler = new Handler();

        this.txtIdName = (TextView) findViewById(R.id.Txt_IdName);
        this.txtIdSex = (TextView) findViewById(R.id.Txt_IdSex);
        this.txtIdNation = (TextView) findViewById(R.id.Txt_IdNation);
        this.txtIdYear = (TextView) findViewById(R.id.Txt_IdYear);
        this.txtIdMouth = (TextView) findViewById(R.id.Txt_IdMouth);
        this.txtIdDay = (TextView) findViewById(R.id.Txt_IdDay);
        this.txtIdAddress = (TextView) findViewById(R.id.Txt_IdAddress);
        this.txtIdNum = (TextView) findViewById(R.id.Txt_IdNum);
        this.mTvFingerData = (TextView) findViewById(R.id.tv_fingerData);
        this.imgVIdPhoto = (ImageView) findViewById(R.id.ImgV_IdPhoto);
        mEtRegistrationCode = (EditText) findViewById(R.id.et_registrationCode);

        mTvUID = (TextView) findViewById(R.id.tv_uid);

        mTvResult = (TextView) findViewById(R.id.tv_result);

        final Button mBtStop = (Button) findViewById(R.id.bt_stop);


        Button mBtRead = (Button) findViewById(R.id.bt_read);
        mBtRead.setVisibility(View.VISIBLE);
        mBtRead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                final byte[] uid = api.read();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvUID.setText("UID: " + DataUtils.toHexString(uid));
                    }
                });


            }
        });

        final Button mBtReadCon = (Button) findViewById(R.id.bt_read_con);
        mBtReadCon.setVisibility(View.VISIBLE);
        mBtReadCon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                clear();
                isSequentialRead = true;
                success = 0;
                sum = 0;
                mHandler.post(task);
                mBtReadCon.setEnabled(false);
                mBtStop.setEnabled(true);
            }
        });

        mBtStop.setVisibility(View.VISIBLE);
        mBtStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(task);
                isSequentialRead = false;
                mBtReadCon.setEnabled(true);
                mBtStop.setEnabled(false);
            }

        });

    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            api.read();
        }
    };

    private void setRootPath() {
        PackageManager packageManager = this.getPackageManager();

        try {
            PackageInfo info = packageManager.getPackageInfo(this.getPackageName(), 0);
            this.m_RootPath = info.applicationInfo.dataDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void error(final int errorCode) {

        switch (errorCode) {
            case ReadListener.RET_DECODE_FAIL:
                Toast.makeText(getApplicationContext(), "解析身份证失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_GET_UID_FAIL:
                Toast.makeText(getApplicationContext(), "获取uid失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_RF_CONNECT_FAIL:
                Toast.makeText(getApplicationContext(), "射频连接失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_CALLBACK_SENDDATA_ERROR:
                Toast.makeText(getApplicationContext(), "射频交互失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_READ_PACKAGE_RESPONSE_ERROR1:
                Toast.makeText(getApplicationContext(), "和业务服务器交互出错", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_ILLEGAL_DEVICE:
                Toast.makeText(getApplicationContext(), "非法设备", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_LICENSE_OVERDUE:
                Toast.makeText(getApplicationContext(), "授权过期", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_NO_SERVER:
                Toast.makeText(getApplicationContext(), "没有加密模块", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_UNKNOW_ERROR:
                Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_READ_IMEI_ERROR:
                Toast.makeText(getApplicationContext(), "读取设备的imei失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_READ_WIFI_MAC_ERROR:
                Toast.makeText(getApplicationContext(), "读取设备mac失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_NET_ERROR:
                Toast.makeText(getApplicationContext(), "请求后台接口失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_RESPONSE_JSON_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据非json格式", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_RESPONSE_PARAM_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据缺少字段", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_REGIST_SYSTEM_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据code为sys-err", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_REGIST_CODE_ERROR1:
                Toast.makeText(getApplicationContext(), "后台返回数据code为code-err1", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_REGIST_CODE_ERROR2:
                Toast.makeText(getApplicationContext(), "后台返回数据code为code-err2", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_REGIST_PARAM_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据code为param-err", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_REGIST_DEVICE_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据code为device-err", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_REGIST_DEVICE_EXISTS:
                Toast.makeText(getApplicationContext(), "后台返回数据code为device-exists", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_SAVE_LICENSE_ERROR:
                Toast.makeText(getApplicationContext(), "交互成功后license文件中的count字段加1时保存文件错误", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_CHECK_LICENSE_ERROR:
                Toast.makeText(getApplicationContext(), "保存的license文件中的imei和mac和读取的本机imei，mac不一致", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_CALLBACK_GETKEY_ERROR:
                Toast.makeText(getApplicationContext(), "找不到getKey方法，或getKey方法返回null", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_SOCKET_CONNECT_ERROR:
                Toast.makeText(getApplicationContext(), "与业务服务器连接失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_SOCKET_SENDDATA_ERROR:
                Toast.makeText(getApplicationContext(), "向业务服务器发送数据失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_SOCKET_TIMEOUT_ERROR:
                Toast.makeText(getApplicationContext(), "接收业务服务器数据时超时1s未收到数据", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_SOCKET_DISCONNECT_ERROR:
                Toast.makeText(getApplicationContext(), "接收业务服务器数据时断开连接", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_READ_REGIST_ERROR:
                Toast.makeText(getApplicationContext(), "向业务服务器注册失败", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_CALLBACK_IDCONTENT_ERROR:
                Toast.makeText(getApplicationContext(), "找不到IDContent方法", Toast.LENGTH_SHORT).show();
                break;
            case ReadListener.RET_CALLBACK_READSUCCESS_ERROR:
                Toast.makeText(getApplicationContext(), "找不到readSuccess方法", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        if (isSequentialRead) {
            mHandler.postDelayed(task, 1000);
        }
    }


    @Override
    public void readSuccess(final IdCardInfo idCardInfo) {
        success++;
        clear();
        updateIdCardInfo(idCardInfo);
        if (isSequentialRead) {
            mHandler.postDelayed(task, 1000);
        }
        final long readTime = System.currentTimeMillis() - startMillis;

        mTvResult.setText("读卡时间: " + readTime + "  总数: " + sum + " 成功: " + success);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void updateIdCardInfo(IdCardInfo idCardInfo) {
        this.txtIdName.setText(idCardInfo.getName());
        this.txtIdSex.setText(idCardInfo.getSex());
        this.txtIdNation.setText(idCardInfo.getNation());
        this.txtIdYear.setText(idCardInfo.getBirthday().substring(0, 4));
        this.txtIdMouth.setText(idCardInfo.getBirthday().substring(4, 6));
        this.txtIdDay.setText(idCardInfo.getBirthday().substring(6));
        this.txtIdAddress.setText(idCardInfo.getAddress());
        this.txtIdNum.setText(idCardInfo.getIdNum());

        if (null != idCardInfo.getPhoto()) {
            Bitmap photo = BitmapFactory.decodeByteArray(idCardInfo.getPhoto(), 0, idCardInfo.getPhoto().length);
            this.imgVIdPhoto.setBackground(new BitmapDrawable(null, photo));
        }

        if (null != idCardInfo.getM_FingerModle()) {
            mTvFingerData.setText(DataUtils.toHexString(idCardInfo.getM_FingerModle()));
        } else {
            mTvFingerData.setText("没有指纹数据");
        }
    }

    private void clear() {
        this.txtIdName.setText("");
        this.txtIdSex.setText("");
        this.txtIdNation.setText("");
        this.txtIdYear.setText("");
        this.txtIdMouth.setText("");
        this.txtIdDay.setText("");
        this.txtIdAddress.setText("");
        this.txtIdNum.setText("");
        this.imgVIdPhoto.setBackgroundColor(0);
        this.mTvFingerData.setText("");
    }

    @Override
    public void exception(final String ex) {
        Toast.makeText(getApplicationContext(), ex, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void start() {
        clear();
        sum++;
        startMillis = System.currentTimeMillis();

    }
}