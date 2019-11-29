package com.cw.netnfcreadidcardlib.Interface;


public interface RegisterListener {



    /**
     * 设备注册成功
     */
    int RET_REGIST_DEVICE_SUCCESS = 10;


    /**
     * 错误
     *
     * @param errorCode 错误代号
     */
    void error(int errorCode);

    /**
     * 激活成功
     */
    void registerSuccess();

}