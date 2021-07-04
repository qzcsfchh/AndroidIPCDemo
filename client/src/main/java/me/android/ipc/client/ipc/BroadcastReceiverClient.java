package me.android.ipc.client.ipc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.android.ipc.data.Data;

public class BroadcastReceiverClient extends BroadcastReceiver {
    private static final String TAG = "BroadcastReceiverClient";
    static final Map<String, Consumer<?>> mConsumers = new ConcurrentHashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String call = intent.getStringExtra("call");
        String callId = intent.getStringExtra("callId");
        Log.d(TAG, " onReceive: call = " + call+", callId = "+callId);
        if (call != null&&callId!=null) {
            switch (call) {
                case "getData":
                    Data data = intent.getParcelableExtra("return");
                    Consumer<Data> consumer = (Consumer<Data>) mConsumers.remove(callId);
                    if (consumer != null) {
                        consumer.accept(data);
                    }
                    break;
                case "getImage":
                    Bitmap bitmap = intent.getParcelableExtra("return");
                    Consumer<Bitmap> consumer2 = (Consumer<Bitmap>) mConsumers.remove(callId);
                    if (consumer2 != null) {
                        consumer2.accept(bitmap);
                    }
                    break;
                default:
                    break;
            }
        }
    }


    public void getData(Context context,Consumer<Data> consumer){
        String id = UUID.randomUUID().toString();
        context.sendBroadcast(new Intent("me.android.ipc.server.broadcast")
                        .putExtra("call", "getData")
                        .putExtra("callId", id)
                        .putExtra("param", id)
                        .putExtra("clientPackage", context.getPackageName())
                        .putExtra("clientClass", getClass().getName())
                .setClassName("me.android.ipc.server","me.android.ipc.server.ipc.BroadcastReceiverServer")
                );
        mConsumers.put(id, consumer);
    }

    public void getImage(Context context,Consumer<Bitmap> consumer){
        String id = UUID.randomUUID().toString();
        context.sendBroadcast(new Intent("me.android.ipc.server.broadcast")
                        .putExtra("call", "getImage")
                        .putExtra("callId", id)
                        .putExtra("clientPackage", context.getPackageName())
                        .putExtra("clientClass", getClass().getName())
                        .setClassName("me.android.ipc.server","me.android.ipc.server.ipc.BroadcastReceiverServer")
                );
        mConsumers.put(id, consumer);
    }
}
