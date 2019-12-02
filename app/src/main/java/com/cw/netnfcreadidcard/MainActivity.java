package com.cw.netnfcreadidcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cw.netnfcreadidcardlib.Interface.RegisterListener;
import com.cw.netnfcreadidcardlib.RegisterAPI;

import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_CALLBACK_GETKEY_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_CALLBACK_IDCONTENT_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_CALLBACK_READSUCCESS_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_CALLBACK_SENDDATA_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_CHECK_LICENSE_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_DECODE_FAIL;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_GET_UID_FAIL;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_ILLEGAL_DEVICE;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_LICENSE_OVERDUE;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_NET_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_NO_SERVER;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_READ_IMEI_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_READ_PACKAGE_RESPONSE_ERROR1;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_READ_REGIST_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_READ_WIFI_MAC_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_REGIST_CODE_ERROR1;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_REGIST_CODE_ERROR2;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_REGIST_DEVICE_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_REGIST_DEVICE_EXISTS;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_REGIST_PARAM_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_REGIST_SYSTEM_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_RESPONSE_JSON_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_RESPONSE_PARAM_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_RF_CONNECT_FAIL;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_SAVE_LICENSE_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_SOCKET_CONNECT_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_SOCKET_DISCONNECT_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_SOCKET_SENDDATA_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_SOCKET_TIMEOUT_ERROR;
import static com.cw.netnfcreadidcardlib.Interface.ReadListener.RET_UNKNOW_ERROR;

/**
 * 作者：李阳
 * 时间：2019/3/12
 * 描述：
 */
public class MainActivity extends Activity implements RegisterListener {



    private static final String TAG = "MainActivity";


    private EditText mEtRegistrationCode;

    private TextView tvVersion;

    private RegisterAPI api;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CrashHandler.getInstance().init(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mEtRegistrationCode = (EditText) findViewById(R.id.et_registrationCode);
        tvVersion = findViewById(R.id.tv_version);

        api = new RegisterAPI(this);


        tvVersion.setText("so version: " + api.getSoVersion() + " buildDate: " + api.getDate());


    }

    public void NFC(View view) {

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "读身份证需要联网!", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(this, NFCTestActivity.class));
    }

    public void USB(View view) {

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "读身份证需要联网!", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(this, USBTestActivity.class));
    }

    @Override
    public void error(int errorCode) {
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
            default:
                break;
        }
    }

    @Override
    public void registerSuccess() {
        Toast.makeText(getApplicationContext(), "激活成功！", Toast.LENGTH_SHORT).show();
    }

    public void bt_register(View view) {


        if (!isNetworkAvailable()) {
            Toast.makeText(this, "授权需要联网!", Toast.LENGTH_SHORT).show();
            return;
        }


        api.registerDevice(MainActivity.this, mEtRegistrationCode.getText().toString());
    }

    public void bt_try_register(View view) {

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "授权需要联网!", Toast.LENGTH_SHORT).show();
            return;
        }

        api.testRegisterDeive(MainActivity.this);
    }


    /**
     * 检测网络是否连接
     *
     * @return
     */
    public boolean isNetworkAvailable() {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;

    }

}
