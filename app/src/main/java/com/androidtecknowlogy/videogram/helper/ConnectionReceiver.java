package com.androidtecknowlogy.videogram.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by AGBOMA franklyn on 7/18/16.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    private final String logTAG = ConnectionReceiver.class.getSimpleName();
    private static boolean value;


    @Override
    public void onReceive(Context context, final Intent intent) {

        int connectionType = Connection.getConnectionState(context);

        if(connectionType == 0){
            Log.e(logTAG, "not connected");
            value = false;
        }
        else {
            Log.e(logTAG, "connected");
            value = true;

        }
    }

    public static boolean isConnected() {
        return value;
    }

}

