package com.tonnfccard.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.util.Log;

import com.tonnfccard.api.callback.NfcCallback;


import static com.tonnfccard.api.TonWalletApi.EXCEPTION_HELPER;
import static com.tonnfccard.api.TonWalletApi.JSON_HELPER;
import static com.tonnfccard.api.utils.ResponsesConstants.DONE_MSG;
import static com.tonnfccard.api.utils.ResponsesConstants.ERROR_MSG_NO_CONTEXT;
import static com.tonnfccard.api.utils.ResponsesConstants.FALSE_MSG;
import static com.tonnfccard.api.utils.ResponsesConstants.TRUE_MSG;

public class NfcApi {
    private static final String TAG = "NfcApi";

    private Context activity;

    public NfcApi(Context activity) {
        this.activity = activity;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }


    public void openNfcSettings(final NfcCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String json = openNfcSettingsAndGetJson();
                    resolveJson(json, callback);
                    Log.d(TAG, "openNfcSettings response : " + json);
                } catch (Exception e) {
                    EXCEPTION_HELPER.handleException(e, callback, TAG);
                }
            }
        }).start();
    }

    public String openNfcSettingsAndGetJson() throws Exception  {
        if (activity == null) throw new Exception(ERROR_MSG_NO_CONTEXT);
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        return JSON_HELPER.createResponseJson(DONE_MSG);
    }

    public void checkIfNfcEnabled(final NfcCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String json = checkIfNfcEnabledAndGetJson();
                    resolveJson(json, callback);
                    Log.d(TAG, "checkIfNfcEnabled response : " + json);
                } catch (Exception e) {
                    EXCEPTION_HELPER.handleException(e, callback, TAG);
                }
            }
        }).start();
    }

    public String checkIfNfcEnabledAndGetJson() throws Exception {
        if (activity == null) throw new Exception(ERROR_MSG_NO_CONTEXT);
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        boolean res = nfcAdapter.isEnabled();
        return JSON_HELPER.createResponseJson(res ? TRUE_MSG : FALSE_MSG);
    }

    public void checkIfNfcSupported(final NfcCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String json = checkIfNfcSupportedAndGetJson();
                    resolveJson(json, callback);
                    Log.d(TAG, "checkIsNfcSupported response : " + json);
                } catch (Exception e) {
                    EXCEPTION_HELPER.handleException(e, callback, TAG);
                }
            }
        }).start();
    }

    public String checkIfNfcSupportedAndGetJson() throws Exception {
        if (activity == null) throw new Exception(ERROR_MSG_NO_CONTEXT);
        boolean res = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
        return JSON_HELPER.createResponseJson(res ? TRUE_MSG : FALSE_MSG);
    }

    void resolveJson(String json, NfcCallback callback){
        callback.getResolve().resolve(json);
        Log.d(TAG, "json = " + json);
    }
}
