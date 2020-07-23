/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package cordova.pnp.seuic.plugin;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.seuic.scanner.DecodeInfo;

import org.apache.cordova.engine.SystemWebView;

public class MainActivity extends ScannerActivity{

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

        // Set by <content src="index.html" /> in config.xml
        //H5 程序的入口
        loadUrl(launchUrl);
    }

    /**
     * 扫描api的回调
     * @param decodeInfo
     */
    @Override
    public void onDecodeComplete(DecodeInfo decodeInfo) {
        super.onDecodeComplete(decodeInfo);
        webViewCallJavascript(decodeInfo);
    }

    /***
     * webView.loadUrl实现了直接调用js的功能, 所以就不需要回调mCallbackContext了
     *
     * loadUrl方法需要工作在ui主线程
     * @param decodeInfo
     */
    private void webViewCallJavascript(final DecodeInfo decodeInfo){
        if(mWebView != null){
            mWebView.loadUrl("javascript:"+ "javaCalljs(\'"+ decodeInfo.barcode +"\')");
        }else{
            Log.d(TAG, "1webViewCallJavascript: mwebview is null !");
        }
    }

    @Override
    public void onDestroy() {

        //解除webview绑定在父布局的情况，不用时，需要释放
        ViewGroup viewGroup = (ViewGroup)this.findViewById(android.R.id.content);
        SystemWebView webView = (SystemWebView) viewGroup.getChildAt(0);
        viewGroup.removeView(webView);
        webView.removeAllViews();

        mWebView = null;
        super.onDestroy();
    }
}
