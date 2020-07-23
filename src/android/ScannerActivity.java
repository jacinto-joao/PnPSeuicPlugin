package com.seuic.js;

import android.os.Bundle;
import android.util.Log;

import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;

/**
 * Created by Administrator on 2017/3/27 0027.
 *
 * 封装的该类，实现了初始化扫描api的服务，监听了巴枪的扫描按键等
 */
public class ScannerActivity extends CordovaActivity implements DecodeInfoCallBack{

    public static Scanner mScanner;
    public static CordovaWebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScanner = ScannerFactory.getScanner(this);
        mScanner.setDecodeInfoCallBack(this);
        mScanner.open();
        new Thread(runnable).start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //初始化webview，放在oncreate中达不到效果
        mWebView = appView;
    }

    /**
     * 扫描的回调, 提供给继承的页面重写
     * @param decodeInfo
     */
    @Override
    public void onDecodeComplete(DecodeInfo decodeInfo) {
        Log.d(TAG, "onDecodeComplete: " + decodeInfo.barcode);
        //TODO
    }

    /**
     * 监听了巴枪的扫描按键
     */
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            int ret1 = ScannerKey.open();
            if (ret1 > -1) {
                while (true) {
                    int ret = ScannerKey.getKeyEvent();
                    if (ret > -1) {
                        switch (ret) {
                            case ScannerKey.KEY_DOWN:
                                mScanner.startScan();
                                break;
                            case ScannerKey.KEY_UP:
                                mScanner.stopScan();
                                break;
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        mScanner.stopScan();
        mScanner.close();
        super.onDestroy();
    }

}
