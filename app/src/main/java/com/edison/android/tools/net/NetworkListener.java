package com.edison.android.tools.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;

public abstract class NetworkListener extends BroadcastReceiver {

    private boolean mConnected;

    @Override
    public final void onReceive(Context context, Intent intent) {
        boolean connected = !(intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY , false));
        if (mConnected != connected) {
            connected = new VerifyConnection(context).connected();
            setConnected(connected);
        }
    }

    public boolean connected() {
        return mConnected;
    }

    private void setConnected(boolean connected) {
        boolean oldState = mConnected;
        mConnected = connected;
        if (!oldState && connected) {
            onConnected();
        } else if (oldState && !connected) {
            onConnectionFailure();
        }
    }

    public abstract void onConnected();

    public abstract void onConnectionFailure();

}
