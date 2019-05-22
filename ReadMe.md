### 1. NFC读网络身份证开发包说明

   1.1



### 2. 二次开发说明

#### 2.1 Android Studio工程配置说明

2.1 在Project视图下，在moudle/libs/下添加aar开发包

2.2 配置moudle build文件

```java

android {
    defaultConfig {
        ....
        //必须1
        targetSdkVersion 22
    }
}



//必须2
repositories {
    flatDir {
        dirs 'libs'   // aar目录
    }
}

dependencies {
    ....
    ////必须3
    implementation(name: 'net-idcard-sdk-20190401', ext: 'aar')

}


```


#### 2.2 AndroidManifest.xml配置说明

```xml

    <uses-permission android:name="android.permission.NFC" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

```


#### 2.3  接口说明

**RegisterAPI 类**


| 注册授权 API接口 | 接口说明 |
| :----- | :---- |
| RegisterAPI | RegisterAPI 类 |
| registerDevice | 注册正式授权（联系商务获取正式授权码） |
| testRegisterDeive | 注册测试授权,测试授权码均为 VMQMDY3NS8HQW8AA |


- public void testRegisterDeive(Context context)

  ```
  申请测试授权
  context:上下文

  ```

- public void registerDevice(Context context, String registerCode)

  ```
    申请正式授权
    context:上下文
    registerCode:商务给的正式授权码，联系商务购买
  ```


**ReadAPI 类**


| 读卡 API接口 | 接口说明 |
| :----- | :---- |
| ReadAPI | ReadAPI 类 |
| ReadAPI(...) | 构造函数 |
| read() | USB读网络身份证 |
|read(Intent)| NFC读网络身份证 |

- public ReadAPI(Context context, int base, int license, String path, ReadListener readListener, Conn conn)

  ```
  构造函数
  context:上下文
  base:  R.raw.base
  license: R.raw.license
  path: 身份证头像图片存储路径
  readListener: 读卡状态结果监听回调
  conn: 接口

  ```

- public void read(Intent intent)

  ```
  NFC读卡方法
  intent:NFC识别到的tag
  ```

- public void read()
  ```
  USB读卡方法，需要CR30S设备
  ```


#### 2.4网络身份证设备注册

注册方式：

- 试用注册
- 购买正式授权码注册



```java
private RegisterAPI api;


@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    api = new RegisterAPI(this);
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

@Override
public void error(int errorCode) {

}

@Override
public void registerSuccess() {
    Toast.makeText(getApplicationContext(), "激活成功！", Toast.LENGTH_SHORT).show();
}
```





#### 2.5 NFC读网络身份证

以下只是最主要的处理流程，具体的还请参考源代码



```

```

```xml

<activity
    android:name=".NFCTestActivity"
    android:launchMode="singleTop">
    <intent-filter>
        <action android:name="android.nfc.action.TECH_DISCOVERED" />
    </intent-filter>
</activity>

```



```java
public class DemoActivity extends BaseActivity implements ReadListener{
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
        ...
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, (new Intent(this, this.getClass())).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        mIntentFilters = new IntentFilter[]{intentFilter};
        mTechLists = new String[][]{new String[]{NfcB.class.getName()}};
        setRootPath();
        Conn conn = new NFCConn();
        api = new ReadAPI(this, R.raw.base, R.raw.license, m_RootPath, this, conn);
        ...
    }
​
    private void setRootPath() {
        PackageManager packageManager = this.getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(this.getPackageName(), 0);
            this.m_RootPath = info.applicationInfo.dataDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
​
​
    @Override
    protected void onResume() {
        super.onResume();
      mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mTechLists);
    }
​
​
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "-----------------onNewIntent----------------");
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            if (api != null) {
                api.read(intent);
            } else {
                Log.d(Constants.TAG, "api 未初始化");
            }
        }
    }
​
    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }
​
    @Override
    public void error(final int errorCode) {
        ....//错误码
    }
​
    @Override
    public void readSuccess(IdCardInfo idCardInfo) {
       ....//读卡成功处理
        updateIdCardInfo(idCardInfo);
    }

    @Override
    public void exception(final String ex) {
        Toast.makeText(getApplicationContext(), ex, Toast.LENGTH_SHORT).show();
    }
​
   @Override
    public void start() {
        //检测到有卡
    }
}

```

#### 2.6 USB读网络身份证

以下只是最主要的处理流程，具体的还请参考源代码

```xml

<activity
    android:name=".USBTestActivity"
    android:launchMode="singleTop">
    <intent-filter>
        <action android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED" />
    </intent-filter>
    <meta-data
        android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED"
        android:resource="@xml/device_filter" />
</activity>
```


  ```java
public class DemoActivity extends BaseActivity implements ReadListener{
​
   ......


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        ......
        setRootPath();
        Conn conn = new USBConn(getApplicationContext());
        api = new ReadAPI(getApplicationContext(), R.raw.base, R.raw.license, m_RootPath, this, conn);
    }
​
    @Override
    public void error(final int errorCode) {
        //错误码
    }
​
    @Override
    public void readSuccess(final IdCardInfo idCardInfo) {
        //读卡成功
        updateIdCardInfo(idCardInfo)；
    }
​
    @Override
    public void exception(final String ex) {
      //操作异常
        Toast.makeText(getApplicationContext(), ex, Toast.LENGTH_SHORT).show();
    }
​
​
    @Override
    public void start() {
        //检测到有卡
    }
}
  ```

#### 2.7 身份证数据说明

|         **调用方法**          | **说明**               |
| :---------------------------: | :--------------------- |
|     IdCardInfo.getName()      | 获取姓名               |
|      IdCardInfo.getSex()      | 获取性别               |
|    IdCardInfo.getNation()     | 获取名族               |
|   IdCardInfo.getBirthday()    | 获取出生日期           |
|    IdCardInfo.getAddress()    | 获取住址               |
|     IdCardInfo.getIdNum()     | 获取身份证号           |
|     IdCardInfo.getIssue()     | 获取签证机关           |
|   IdCardInfo.getStartDate()   | 获取身份证有效起始日期 |
|    IdCardInfo.getEndDate()    | 获取身份证有效截止日期 |
|     IdCardInfo.getPhoto()     | 获取身份证头像         |
| IdCardInfo.getM_FingerModle() | 获取身份证指纹模板     |

#### 2.8 错误码说明

| **错误值** |            **说明**             |
| :--------- | :-----------------------------: |
| -9         |         解析身份证失败          |
| -8         |        获取身份证uid失败        |
| -7         |          射频连接失败           |
| -6         |          射频交互失败           |
| -5         |        和服务器交互错误         |
| -4         |            非法设备             |
| -3         |            授权过期             |
| 0          |          没有加密模块           |
| 99         |            未知错误             |
| 100        |        获取设备imei失败         |
| 101        |         获取设备mac失败         |
| 102        |        请求后台接口失败         |
| 103        |     后台返回数据非json格式      |
| 104        |      后台返回数据缺少字段       |
| 105        |    后台返回数据code为sys-err    |
| 106        |   后台返回数据code为code-err1   |
| 107        |   后台返回数据code为code-err2   |
| 108        |   后台返回数据code为param-err   |
| 109        |  后台返回数据code为device-err   |
| 110        | 后台返回数据code为device-exisit |
| 111        |        更新授权文件出错         |
| 112        |    授权文件imei和本机不一致     |
| 113        |           获取uid失败           |
| 114        |       与业务服务连接失败        |
| 115        |    向业务服务器发送数据失败     |
| 116        |      与业务服务器交互超时       |
| 117        |      与业务服务器异常断开       |
| 118        |      与业务服务器注册失败       |
| 119        |       找不到IDContent方法       |
| 120        |      找不到readSuccess方法      |
