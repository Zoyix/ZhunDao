package com.zhaohe.app.utils;

import android.hardware.Camera;

/**
 * @Description:相机工具类
 * @Author:邹苏隆
 * @Since:2017/3/21 11:55
 */
public class CameraUtils {
    //相机是否授权
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }
}
