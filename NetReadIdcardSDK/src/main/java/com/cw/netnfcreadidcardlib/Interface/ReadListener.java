package com.cw.netnfcreadidcardlib.Interface;

import com.cw.netnfcreadidcardlib.Bean.IdCardInfo;

public interface ReadListener {
    /**
     * 读取成功
     */
    int RET_READ_SUCCESS = 1;
    /**
     * 设备注册成功
     */
    int RET_REGIST_DEVICE_SUCCESS = 10;
    /**
     * 解析身份证失败
     */
    int RET_DECODE_FAIL = -9;
    /**
     * 获取uid失败
     */
    int RET_GET_UID_FAIL = -8;
    /**
     * 射频连接失败
     */
    int RET_RF_CONNECT_FAIL = -7;
    /**
     * 找不到sendData方法，或此方法返回的数据为null
     */
    int RET_CALLBACK_SENDDATA_ERROR = -6;
    /**
     * 收到的业务服务器的第一个包不符合rev[5]==0x05 && rev[6]==0x00 && rev[7]==0x00，
     */
    int RET_READ_PACKAGE_RESPONSE_ERROR1 = -5;
    /**
     * 收到的业务服务器的第二个包不符合长度大于9并且0x1d == rev[5] && 0x00 == rev[6] && 0x00 == rev[7] && 0x00 == rev[8]
     */
    int RET_READ_PACKAGE_RESPONSE_ERROR2 = -5;
    /**
     * 非法设备
     */
    int RET_ILLEGAL_DEVICE = -4;
    /**
     * 授权过期
     */
    int RET_LICENSE_OVERDUE = -3;
    /**
     * 没有加密模块
     */
    int RET_NO_SERVER = 0;
    /**
     * 未知错误
     */
    int RET_UNKNOW_ERROR = 99;
    /**
     * 读取设备的imei失败
     */
    int RET_READ_IMEI_ERROR = 100;
    /**
     * 读取设备mac失败
     */
    int RET_READ_WIFI_MAC_ERROR = 101;
    /**
     * 请求后台接口失败
     */
    int RET_NET_ERROR = 102;
    /**
     * 后台返回数据非json格式
     */
    int RET_RESPONSE_JSON_ERROR = 103;
    /**
     * 后台返回数据缺少字段
     */
    int RET_RESPONSE_PARAM_ERROR = 104;
    /**
     * 后台返回数据code为sys-err
     */
    int RET_REGIST_SYSTEM_ERROR = 105;
    /**
     * 后台返回数据code为code-err1
     */
    int RET_REGIST_CODE_ERROR1 = 106;
    /**
     * 后台返回数据code为code-err2
     */
    int RET_REGIST_CODE_ERROR2 = 107;
    /**
     * 后台返回数据code为param-err
     */
    int RET_REGIST_PARAM_ERROR = 108;
    /**
     * 后台返回数据code为device-err
     */
    int RET_REGIST_DEVICE_ERROR = 109;
    /**
     * 后台返回数据code为device-exists
     */
    int RET_REGIST_DEVICE_EXISTS = 110;
    /**
     * 交互成功后license文件中的count字段加1时保存文件错误
     */
    int RET_SAVE_LICENSE_ERROR = 111;
    /**
     * 保存的license文件中的imei和mac和读取的本机imei，mac不一致
     */
    int RET_CHECK_LICENSE_ERROR = 112;
    /**
     * 找不到getKey方法，或getKey方法返回null
     */
    int RET_CALLBACK_GETKEY_ERROR = 113;
    /**
     * 与业务服务器连接失败
     */
    int RET_SOCKET_CONNECT_ERROR = 114;
    /**
     * 向业务服务器发送数据失败
     */
    int RET_SOCKET_SENDDATA_ERROR = 115;
    /**
     * 接收业务服务器数据时超时1s未收到数据
     */
    int RET_SOCKET_TIMEOUT_ERROR = 116;
    /**
     * 接收业务服务器数据时断开连接
     */
    int RET_SOCKET_DISCONNECT_ERROR = 117;
    /**
     * 向业务服务器注册失败
     */
    int RET_READ_REGIST_ERROR = 118;
    /**
     * 找不到IDContent方法
     */
    int RET_CALLBACK_IDCONTENT_ERROR = 119;
    /**
     * 找不到readSuccess方法
     */
    int RET_CALLBACK_READSUCCESS_ERROR = 120;


    /**
     * 获取指纹数据回调错误
     */
    int RET_CALLBACK_FINGERDATA_ERROR = 121;

    /**
     * 读取缓存超时错误
     */
    int RET_READ_CACHE_TIMEOUT_ERROR = 122;

    /**
     * 没有授权文件
     */
    int RET_NO_LICENSE_FILE_ERROR = 123;

    /**
     * 读卡开始时候进入
     */
    void start();

    /**
     * 错误
     *
     * @param errorCode 错误代号
     */
    void error(int errorCode);

    /**
     * 激活成功
     */
    //void registerSuccess();

    /**
     * 读卡成功
     *
     * @param idCardInfo 卡的uid号
     */
    void readSuccess(IdCardInfo idCardInfo);

    /**
     * 其他异常
     *
     * @param ex 异常信息
     */
    void exception(String ex);
}