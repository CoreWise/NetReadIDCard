package com.cw.netnfcreadidcardlib;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Handler;
import android.util.Log;

import com.corewise.idcardreadersdk.support.NativeSupport;
import com.cw.netnfcreadidcardlib.Bean.IdCardInfo;
import com.cw.netnfcreadidcardlib.Interface.Conn;
import com.cw.netnfcreadidcardlib.Interface.ReadListener;
import com.cw.netnfcreadidcardlib.SFZ.AnalysisIDCard;
import com.cw.netnfcreadidcardlib.Utils.DataUtils;

public class ReadAPI {


    private static final String TAG = Constants.TAG + "ReadAPI";

    private Context mContext;
    private ReadListener readListener;
    private AnalysisIDCard analysisIDCard;
    private Conn mConn;
    private byte[] NFCID;
    private boolean isPrepare = true;


    int ret = -888;


    private Handler mHandler;


    public ReadAPI(Context context, int base, int license, String path, ReadListener readListener, Conn conn) {
        //NativeSupport.setWorkAllocUrl("https://101.132.142.76:8080/idbalance_war/worker/alloc");

        this.mContext = context;
        this.readListener = readListener;
        this.analysisIDCard = new AnalysisIDCard(context, base, license, path);
        this.mConn = conn;
        mHandler = new Handler();

    }

    /*public void read(Intent intent) {
        readListener.start();
        NFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

        mConn.setTag((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));

        boolean isConn = mConn.connect();

        if (isConn) {
            if (isPrepare) {
                readIDCard();
            }
        } else {
            mConn.close();
            readListener.error(ReadListener.RET_RF_CONNECT_FAIL);
        }

    }*/

    public byte[] read(Intent intent) {
        readListener.start();
        NFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

        if (!NFCID.equals("00000000")) {

        }

        mConn.setTag((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));

        boolean isConn = mConn.connect();

        if (isConn) {
            if (isPrepare) {
                readIDCard();
            }
        } else {
            mConn.close();
            readListener.error(ReadListener.RET_RF_CONNECT_FAIL);
        }
        return NFCID;
    }

    public byte[] read() {
        readListener.start();

        boolean isConn = mConn.connect();
        if (isConn) {
            NFCID = mConn.getUID();
            if (NFCID != null) {
                if (isPrepare) {
                    readIDCard();
                }
            } else {
                mConn.close();
                readListener.error(ReadListener.RET_GET_UID_FAIL);
            }
        } else {
            mConn.close();
            readListener.error(ReadListener.RET_RF_CONNECT_FAIL);
        }
        return NFCID;
    }

    /*@Deprecated
    public void testRegisterDeive(Context context) {
        int ret = NativeSupport.registDeviceForTry(context.getApplicationContext());
        if (ret == ReadListener.RET_REGIST_DEVICE_SUCCESS) {
            //readListener.registerSuccess();
        } else {
            readListener.error(ret);
        }
    }

    public void registerDevice(Context context, String registerCode) {
        int ret = NativeSupport.registDevice(context.getApplicationContext(), registerCode);
        if (ret == ReadListener.RET_REGIST_DEVICE_SUCCESS) {
            //readListener.registerSuccess();
        } else {
            readListener.error(ret);
        }
    }*/





    private void readIDCard() {
        isPrepare = false;
        new Thread(new Runnable() {

            @Override
            public void run() {

                NativeSupport.init(NFCID, mConn);

                try {
                    ret = NativeSupport.read(mContext);
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (ret == ReadListener.RET_READ_SUCCESS) {
                                decodeSFZ(NativeSupport.getSFZData(), NativeSupport.getFingetModleData());
                            } else {
                                readListener.error(ret);
                            }
                        }
                    });
                } catch (final Exception e) {

                    if (mConn != null) {
                        mConn.close();
                        Log.e(TAG, "nfc disconnect!");
                    }
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            readListener.exception(e.getMessage());
                            Log.e(TAG, e.getMessage());
                        }
                    });
                }

                isPrepare = true;
                Log.d(TAG, "read return " + ret);
            }
        }).start();
    }

    /**
     * 解析身份证
     *
     * @param sfzData 身份证原数据
     */
    private void decodeSFZ(byte[] sfzData, byte[] fingerModleData) {
        IdCardInfo idCardInfo = analysisIDCard.decodeIdCardInfo(sfzData);
        if (null == idCardInfo) // 解析身份证数据失败
        {
            readListener.error(ReadListener.RET_DECODE_FAIL);
        } else { // 读卡成功返回
            idCardInfo.setUid(DataUtils.toHexString(NFCID));
            idCardInfo.setM_FingerModle(fingerModleData);
            readListener.readSuccess(idCardInfo);
        }
    }
}