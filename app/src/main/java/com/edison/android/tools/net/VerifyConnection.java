package com.edison.android.tools.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class VerifyConnection {

    private ConnectivityManager mManager;

    public VerifyConnection(Context context) {
        mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean connected() {
        boolean connected = false;
        if (mManager != null) {
            NetworkInfo activeNetwork = mManager.getActiveNetworkInfo();
            connected = (activeNetwork != null && activeNetwork.isConnected());
        }
        return connected;
    }
}
