package com.posin.silenceinstall.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * FileName: AppManagerUtils
 * Author: Administrators
 * Time: 2018/10/18 10:23
 * Desc: TODO
 */
public class AppManagerUtils {

    private static final String TAG = "AppManagerUtils";

    /**
     * 静默安装,通过反射实现
     *
     * @param context context
     * @param apkFile APK文件路径
     */
    public static void silentInstall(Context context, String apkFile) {
        File installFile = new File(apkFile);
        Uri uri = Uri.fromFile(installFile);
        int installFlags = 2;//覆盖安装
        PackageManager pm = context.getPackageManager();
        MyPackageInstallObserver observer = new MyPackageInstallObserver();
        //通过 getMethod 只能获取公有方法，如果获取私有方法则会抛出异常
        try {
            Method method = pm.getClass().getMethod("installPackage", Uri.class,
                    IPackageInstallObserver.class, int.class, String.class);
            method.invoke(pm, uri, observer, installFlags, apkFile);
        } catch (Exception e) {
            Log.d("install fail", e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 静默卸载,通过反射实现
     *
     * @param context     context
     * @param packageName 被卸载的应用包名
     */
    public static void silentUnInstall(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            MyPackageDeleteObserver observer = new MyPackageDeleteObserver();
            Method method = pm.getClass().getMethod("deletePackage",
                    String.class, IPackageDeleteObserver.class, int.class);
            //第四个参数为2时表示删除所有数据
            method.invoke(pm, packageName, observer, 2);
        } catch (Exception e) {
            Log.e(TAG, "uninstall fail: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 安装APP，普通方式，会弹出确认卸载对话框
     *
     * @param context context
     * @param apkFile APK文件路径
     */
    public static void install(Context context, String apkFile) {
        apkFile = "client-debug.apk";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                File.separator + apkFile)), "application/vnd.android.package-archive");
        // 如果没有在Activity环境下启动卸载,需设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载APP，普通方式，会弹出确认卸载对话框
     *
     * @param context     context
     * @param packageName 应用包名
     */
    public static void uninstall(Context context, String packageName) {

        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);

        // 如果没有在Activity环境下启动卸载,需设置启动模式
        uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(uninstallIntent);

    }

    /**
     * 静默安装，需要申请管理员权限（需要root）
     *
     * @param apkPath 要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public boolean rootInstall(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 静默卸载，需要申请管理员权限（需要root）
     *
     * @param packageName 要卸载的APP包名
     * @return 卸载成功返回true，卸载失败返回false。
     */
    public boolean rootUnInstall(String packageName) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm uninstall " + packageName + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "uninstall msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是卸载失败，否则就认为卸载成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }
}
