package com.posin.silenceinstall.utils;


import android.content.pm.IPackageDeleteObserver;
import android.util.Log;

/**
 * FileName: MyPackageDeleteObserver
 * Author: Administrators
 * Time: 2018/10/18 10:51
 * Desc: 静默卸载回调
 */
class MyPackageDeleteObserver extends IPackageDeleteObserver.Stub {

    private static final String TAG = "MyPackageDeleteObserver";
    @Override
    public void packageDeleted(String packageName, int returnCode) {
        if (returnCode == 1) {
            Log.e(TAG, "卸载成功...");
        } else {
            Log.e(TAG, "卸载失败...返回码:" + returnCode);
        }
    }
}
