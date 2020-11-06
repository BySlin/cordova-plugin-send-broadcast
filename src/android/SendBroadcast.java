package com.byslin.cordova.plugin;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.hdhe.uhf.reader.UhfReader;
import com.android.hdhe.uhf.readerInterface.TagModel;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.pda.serialport.Tools;

public class SendBroadcast extends CordovaPlugin {

    private static final String LOG_TAG = "SendBroadcast";

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "start":
            String action = args.getString(0);
                cordova.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent();
                        intent.setAction(action);
                        sendExplicitBroadcast(intent);
                    }
                });
                return true;
            default:
                Log.d(LOG_TAG, "unknown action:" + action);
                return false;
        }
    }

    private boolean sendExplicitBroadcast(Intent intent) {
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> matches = pm.queryBroadcastReceivers(intent, 0);
        boolean sent = false;

        for (ResolveInfo resolveInfo : matches) {
            ComponentName cn =
                    new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,
                            resolveInfo.activityInfo.name);

            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            mContext.sendBroadcast(intent);
            sent = true;
        }
        return sent;
    }
}
