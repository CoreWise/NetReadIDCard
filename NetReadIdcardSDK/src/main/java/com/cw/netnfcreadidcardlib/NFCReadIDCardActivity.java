package com.cw.netnfcreadidcardlib;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cw.netnfcreadidcardlib.Bean.IdCardInfo;
import com.cw.netnfcreadidcardlib.Interface.Conn;
import com.cw.netnfcreadidcardlib.Interface.ReadListener;
import com.cw.netnfcreadidcardlib.NFC.NFCConn;
import com.cw.netnfcreadidcardlib.Utils.DataUtils;


public abstract class NFCReadIDCardActivity extends BaseNFCActivity implements ReadListener {


    private String m_RootPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRootPath();

        Conn conn = new NFCConn();

        api = new ReadAPI(this, R.raw.base, R.raw.license, m_RootPath, this, conn);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void start() {
        onReadIDCardStart();
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
            default:
                break;
        }
        onReadIDCardFailure(errorCode);
    }



    @Override
    public void readSuccess(IdCardInfo idCardInfo) {
        onReadIDCardSuccess(idCardInfo);
    }

   


    @Override
    public void exception(final String ex) {
        Toast.makeText(getApplicationContext(), ex, Toast.LENGTH_SHORT).show();
    }


    public abstract void onReadIDCardStart();

    public abstract void  onReadIDCardSuccess(IdCardInfo peopleBean);

    public abstract  void onReadIDCardFailure(int s);

    public abstract  void onReadIDCardUID(byte[] bytes);

}