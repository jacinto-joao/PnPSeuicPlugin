package cordova.pnp.seuic.plugin;

import android.app.Activity;
import android.util.Log;

import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class PnPSeuicPlugin extends CordovaPlugin implements DecodeInfoCallBack {


    private static final String TAG = "MyPlugin";
    public static final String ACTION_ADD_CALENDAR_ENTRY = "addCalendarEntry";
    private CallbackContext mCallbackContext;
    private Activity mActivity;
    private CordovaWebView mWebView;

    /**
     * 初始化上下文和webview
     * @param cordova
     * @param webView
     */
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.mActivity = cordova.getActivity();
        this.mWebView = webView;
    }

    /**
     * js默认调用该方法
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return
     * @throws JSONException
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        try {
            if (ACTION_ADD_CALENDAR_ENTRY.equals(action)) {

                MainActivity.mScanner.startScan();
                mCallbackContext = callbackContext;
                return true;
            }
            callbackContext.error("Invalid action");
            return false;
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
    }

    /**
     * 扫描API的回调
     * @param decodeInfo
     */
    @Override
    public void onDecodeComplete(final DecodeInfo decodeInfo) {
        Log.d(TAG, "onDecodeComplete: " + decodeInfo.barcode);
        //回调给js
        callBackToJavascript(decodeInfo);
    }

    /**
     * html页面按钮点击扫描, 回调给js
     *
     * 使用固定方法的回调mCallbackContext
     * @param decodeInfo
     */
    private void callBackToJavascript(final DecodeInfo decodeInfo){
        mCallbackContext.success(decodeInfo.barcode);
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
