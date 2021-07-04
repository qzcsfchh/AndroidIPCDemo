package me.android.ipc.server.ipc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import me.android.ipc.data.Data;
import me.android.ipc.server.Utils;

/**
 * When it comes to {@link BroadcastReceiver} in IPC occasion, both client/server should be static registered and exported.
 */
public final class BroadcastReceiverServer extends BroadcastReceiver {
    public static final String TAG = "BroadcastReceiverServer";
    private static final String ACTION_FEEDBACK = "me.android.ipc.server.broadcast.feedback";
    private static int counter;

    @Override
    public void onReceive(Context context, Intent intent) {
        String call = intent.getStringExtra("call");
        String aPackage = intent.getPackage();
        Log.d(TAG, " onReceive: call = " + call+", package = "+aPackage);
        EventBus.getDefault().post(TAG + " onReceive: call = " + call+", package = "+aPackage);
        if (call != null) {
            switch (call) {
                case "getData":
                    String param = intent.getStringExtra("param");
                    Data data = new Data();
                    data.setId(param);
                    data.setName(param + "#" + counter++);
                    context.sendBroadcast(new Intent(ACTION_FEEDBACK)
                            .putExtra("return", data)
                            .putExtra("call", "getData")
                            .putExtra("callId", intent.getStringExtra("callId"))
                            .setClassName(intent.getStringExtra("clientPackage"),intent.getStringExtra("clientClass"))
                    );
                    break;
                case "getImage":
                    Bitmap bitmap = Utils.loadBitmap(context);
                    context.sendBroadcast(new Intent(ACTION_FEEDBACK)
                            .putExtra("return", bitmap)
                            .putExtra("call", "getImage")
                            .putExtra("callId", intent.getStringExtra("callId"))
                            .setClassName(intent.getStringExtra("clientPackage"),intent.getStringExtra("clientClass"))
                   );
                    break;
                default:
                    break;
            }
        }
    }
}
