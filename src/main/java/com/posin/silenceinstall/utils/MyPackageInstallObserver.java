package com.posin.silenceinstall.utils;

import android.content.pm.IPackageInstallObserver;
import android.util.Log;

/**
 * FileName: MyPackageInstallObserver
 * Author: Administrators
 * Time: 2018/10/18 12:56
 * Desc: 静默安装回调
 */
class MyPackageInstallObserver extends IPackageInstallObserver.Stub{

    @Override
    public void packageInstalled(String packageName, int returnCode) {
        if (returnCode == 1) {
            Log.e("DEMO","安装成功");
        }else{
            Log.e("DEMO","安装失败,返回码是:"+returnCode);
        }
    }
}

