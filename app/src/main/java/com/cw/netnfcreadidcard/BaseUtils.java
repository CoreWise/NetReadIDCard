package com.cw.netnfcreadidcard;

import android.content.Context;

/**
 * 作者：李阳
 * 时间：2019/4/1
 * 描述：
 */
public class BaseUtils {


    /**
     * 将dp转换成px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将像素转换成dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context,float pxValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
