package com.posin.silenceinstall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.posin.silenceinstall.global.PackagesConfig;
import com.posin.silenceinstall.utils.AppManagerUtils;
import com.posin.silenceinstall.utils.PackagesUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        List<String> allInstallApps = PackagesUtils.loadAllInstallApp(this);
        //判断系统是否已安装要卸载的APP
        if (allInstallApps.contains(PackagesConfig.UNINSTALL_TARGET_APP)) {
            AppManagerUtils.silentUnInstall(this, PackagesConfig.UNINSTALL_TARGET_APP);
            Log.d(TAG, "系统已安装目标APP，卸载：" + PackagesConfig.UNINSTALL_TARGET_APP);
        } else {
            Log.d(TAG, "系统没有安装目标APP，无需卸载...");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @OnClick({R.id.btn_uninstall,R.id.btn_install})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_uninstall:
//                AppManagerUtils.uninstall(this,"要卸载的包名");
                break;
            case R.id.btn_install:
//                AppManagerUtils.install(this,"APK文件路径");
                break;
            default:
                break;
        }
    }
}
