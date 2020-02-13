package com.myscript.iink.demo.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.myscript.iink.demo.utils.HttpHelper;
import com.myscript.iink.demo.utils.RequestPackage;

import java.io.IOException;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD= "myServicePayload";
    public static final String REQUEST_PACKAGE = "requestPackage";
    public static final String EMAIL_CONFIRM = "confirmemail";

    public MyService() {
        super("MyService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(REQUEST_PACKAGE);

        String response;

        try {
            response = HttpHelper.dowloadUrl(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);

        messageIntent.putExtra(MY_SERVICE_PAYLOAD, response);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
        Log.i(TAG, "Downloaded Contents Right now  ");
        Log.i(TAG, response);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}


