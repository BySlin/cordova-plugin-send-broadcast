package com.byslin.cordova.plugin;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class SendBroadcast extends CordovaPlugin {

    private static final String LOG_TAG = "SendBroadcast";

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "sendAction":
                String actionName = args.getString(0);
                cordova.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = new Intent();
                        intent.setAction(actionName);
                        sendExplicitBroadcast(intent);
                    }
                });
                return true;
            default:
                Log.d(LOG_TAG, "unknown action:" + action);
                return false;
        }
    }

    private void sendExplicitBroadcast(Intent intent) {
        PackageManager pm = cordova.getContext().getPackageManager();
        List<ResolveInfo> matches = pm.queryBroadcastReceivers(intent, 0);

        for (ResolveInfo resolveInfo : matches) {
            ComponentName cn =
                    new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,
                            resolveInfo.activityInfo.name);

            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            cordova.getContext().sendBroadcast(intent);
        }
    }
}
