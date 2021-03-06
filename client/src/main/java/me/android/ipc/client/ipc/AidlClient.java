package me.android.ipc.client.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import me.android.ipc.IPCInterface;
import me.android.ipc.data.Data;

public class AidlClient implements ServiceConnection, IPCInterface, LifecycleEventObserver {
    IPCInterface mInterface;
    private final Context mContext;

    public AidlClient(Context context) {
        mContext = context;
    }

    public void bind(LifecycleOwner owner) {
        owner.getLifecycle().addObserver(this);
    }

    public void start() {
        // Intent must be explicit
        Intent intent = new Intent("me.android.ipc.server.aidl");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {//安卓11
            intent.setClassName("me.android.ipc.server", "me.android.ipc.server.ipc.AidlServiceServer");
        } else {
            intent.setPackage("me.android.ipc.server");
        }
        mContext.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    public void stop() {
        mContext.unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mInterface = IPCInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mInterface = null;
    }

    @Override
    public Bitmap getImage(String uri) throws RemoteException {
        return requireInterface().getImage(uri);
    }

    @Override
    public Data getData(String id) throws RemoteException {
        return requireInterface().getData(id);
    }

    @Override
    public IBinder asBinder() {
        return null == mInterface ? null : mInterface.asBinder();
    }

    @NonNull
    private IPCInterface requireInterface() throws RemoteException {
        if (mInterface == null) {
            throw new RemoteException("Service not connected yet, have you call start() ?");
        }
        return mInterface;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            start();
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            stop();
        }
    }
}
