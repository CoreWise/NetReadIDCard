package com.cw.netnfcreadidcard;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcB;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cw.netnfcreadidcardlib.BaseNFCActivity;
import com.cw.netnfcreadidcardlib.Bean.IdCardInfo;
import com.cw.netnfcreadidcardlib.Constants;
import com.cw.netnfcreadidcardlib.Interface.Conn;
import com.cw.netnfcreadidcardlib.Interface.ReadListener;
import com.cw.netnfcreadidcardlib.NFC.NFCConn;
import com.cw.netnfcreadidcardlib.ReadAPI;
import com.cw.netnfcreadidcardlib.Utils.DataUtils;

import android.app.Activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class NFCTestActivity extends BaseActivity implements ReadListener {

    private static final String TAG = "NFCTestActivity";

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
    private ImageView imgVIdPhoto;
    private TextView mTvFingerData;

    private TextView mTvResult;
    private TextView mTvUID;

    private EditText mEtRegistrationCode;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mTechLists;
    public ReadAPI api;

    private long startMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_idcard);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);

        mPendingIntent = PendingIntent.getActivity(this, 0, (new Intent(this, this.getClass())).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        mIntentFilters = new IntentFilter[]{intentFilter};
        mTechLists = new String[][]{new String[]{NfcB.class.getName()}};


        initView();

        setRootPath();

        Conn conn = new NFCConn();

        api = new ReadAPI(this, R.raw.base, R.raw.license, m_RootPath, this, conn);


        @SuppressLint("ResourceType")
        FriendDialog mFriendDialog = new FriendDialog(this, BaseUtils.dip2px(this, 350f), BaseUtils.dip2px(this, 500f), R.layout.dialog_friend, R.style.DialogTheme);
        mFriendDialog.show();


    }

    private void initView() {

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
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mTvUID = (TextView) findViewById(R.id.tv_uid);


    }

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
    protected void onResume() {
        super.onResume();

        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mTechLists);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mTvUID.setText("");

        Log.e(TAG, "-----------------onNewIntent----------------");

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            if (api != null) {
                byte[] UID = api.read(intent);
                mTvUID.setText("UID: " + DataUtils.toHexString(UID));

            } else {
                Log.d(Constants.TAG, "api 未初始化");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void start() {
        clear();
        sum++;
        startMillis = System.currentTimeMillis();

    }

    @Override
    public void error(final int errorCode) {

        switch (errorCode) {
            case RET_DECODE_FAIL:
                Toast.makeText(getApplicationContext(), "解析身份证失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_GET_UID_FAIL:
                Toast.makeText(getApplicationContext(), "获取uid失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_RF_CONNECT_FAIL:
                Toast.makeText(getApplicationContext(), "射频连接失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_CALLBACK_SENDDATA_ERROR:
                Toast.makeText(getApplicationContext(), "射频交互失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_READ_PACKAGE_RESPONSE_ERROR1:
                Toast.makeText(getApplicationContext(), "和业务服务器交互出错", Toast.LENGTH_SHORT).show();
                break;
            case RET_ILLEGAL_DEVICE:
                Toast.makeText(getApplicationContext(), "非法设备", Toast.LENGTH_SHORT).show();
                break;
            case RET_LICENSE_OVERDUE:
                Toast.makeText(getApplicationContext(), "授权过期", Toast.LENGTH_SHORT).show();
                break;
            case RET_NO_SERVER:
                Toast.makeText(getApplicationContext(), "没有加密模块", Toast.LENGTH_SHORT).show();
                break;
            case RET_UNKNOW_ERROR:
                Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                break;
            case RET_READ_IMEI_ERROR:
                Toast.makeText(getApplicationContext(), "读取设备的imei失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_READ_WIFI_MAC_ERROR:
                Toast.makeText(getApplicationContext(), "读取设备mac失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_NET_ERROR:
                Toast.makeText(getApplicationContext(), "请求后台接口失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_RESPONSE_JSON_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据非json格式", Toast.LENGTH_SHORT).show();
                break;
            case RET_RESPONSE_PARAM_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据缺少字段", Toast.LENGTH_SHORT).show();
                break;
            case RET_REGIST_SYSTEM_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据code为sys-err", Toast.LENGTH_SHORT).show();
                break;
            case RET_REGIST_CODE_ERROR1:
                Toast.makeText(getApplicationContext(), "后台返回数据code为code-err1", Toast.LENGTH_SHORT).show();
                break;
            case RET_REGIST_CODE_ERROR2:
                Toast.makeText(getApplicationContext(), "后台返回数据code为code-err2", Toast.LENGTH_SHORT).show();
                break;
            case RET_REGIST_PARAM_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据code为param-err", Toast.LENGTH_SHORT).show();
                break;
            case RET_REGIST_DEVICE_ERROR:
                Toast.makeText(getApplicationContext(), "后台返回数据code为device-err", Toast.LENGTH_SHORT).show();
                break;
            case RET_REGIST_DEVICE_EXISTS:
                Toast.makeText(getApplicationContext(), "后台返回数据code为device-exists", Toast.LENGTH_SHORT).show();
                break;
            case RET_SAVE_LICENSE_ERROR:
                Toast.makeText(getApplicationContext(), "交互成功后license文件中的count字段加1时保存文件错误", Toast.LENGTH_SHORT).show();
                break;
            case RET_CHECK_LICENSE_ERROR:
                Toast.makeText(getApplicationContext(), "保存的license文件中的imei和mac和读取的本机imei，mac不一致", Toast.LENGTH_SHORT).show();
                break;
            case RET_CALLBACK_GETKEY_ERROR:
                Toast.makeText(getApplicationContext(), "找不到getKey方法，或getKey方法返回null", Toast.LENGTH_SHORT).show();
                break;
            case RET_SOCKET_CONNECT_ERROR:
                Toast.makeText(getApplicationContext(), "与业务服务器连接失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_SOCKET_SENDDATA_ERROR:
                Toast.makeText(getApplicationContext(), "向业务服务器发送数据失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_SOCKET_TIMEOUT_ERROR:
                Toast.makeText(getApplicationContext(), "接收业务服务器数据时超时1s未收到数据", Toast.LENGTH_SHORT).show();
                break;
            case RET_SOCKET_DISCONNECT_ERROR:
                Toast.makeText(getApplicationContext(), "接收业务服务器数据时断开连接", Toast.LENGTH_SHORT).show();
                break;
            case RET_READ_REGIST_ERROR:
                Toast.makeText(getApplicationContext(), "向业务服务器注册失败", Toast.LENGTH_SHORT).show();
                break;
            case RET_CALLBACK_IDCONTENT_ERROR:
                Toast.makeText(getApplicationContext(), "找不到IDContent方法", Toast.LENGTH_SHORT).show();
                break;
            case RET_CALLBACK_READSUCCESS_ERROR:
                Toast.makeText(getApplicationContext(), "找不到readSuccess方法", Toast.LENGTH_SHORT).show();
                break;

            case RET_CALLBACK_FINGERDATA_ERROR:
                Toast.makeText(getApplicationContext(), "获取指纹数据回调错误", Toast.LENGTH_SHORT).show();
                break;
            case RET_READ_CACHE_TIMEOUT_ERROR:
                Toast.makeText(getApplicationContext(), "读取缓存超时错误", Toast.LENGTH_SHORT).show();
                break;
            case RET_NO_LICENSE_FILE_ERROR:
                Toast.makeText(getApplicationContext(), "没有授权文件", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }


    @Override
    public void readSuccess(IdCardInfo idCardInfo) {
        success++;
        clear();
        updateIdCardInfo(idCardInfo);
        final long readTime = System.currentTimeMillis() - startMillis;

        mTvResult.setText("读卡时间: " + readTime + "  总数: " + sum + " 成功: " + success);
    }


    @Override
    public void exception(final String ex) {

        Toast.makeText(getApplicationContext(), ex, Toast.LENGTH_SHORT).show();
    }


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

}