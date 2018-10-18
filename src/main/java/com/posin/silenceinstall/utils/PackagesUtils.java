package com.posin.silenceinstall.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: PackagesUtils
 * Author: Administrators
 * Time: 2018/10/18 9:00
 * Desc: TODO
 */
public class PackagesUtils {

    private static final String TAG = "PackagesUtils";

    /**
     * 获取所有已安装的APP
     *
     * @param context context
     * @return 应用包名集合
     */
    public static List<String> loadAllInstallApp(Context context) {
        List<String> listApps = new ArrayList<>();

        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            // 非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                Log.d(TAG, "packageName: " + packageInfo.packageName);
                listApps.add(packageInfo.packageName);
            }
        }
        return listApps;
    }

}
