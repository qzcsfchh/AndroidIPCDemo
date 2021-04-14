package me.android.ipc.client.ipc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

public class BroadcastReceiverClient extends BroadcastReceiver implements LifecycleEventObserver {
    final Context mHost;

    public BroadcastReceiverClient(Context host) {
        mHost = host;
    }

    public void start(){
//        mHost.registerReceiver(this,new )
    }

    public void stop(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

    }
}
