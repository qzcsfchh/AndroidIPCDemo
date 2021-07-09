package me.android.ipc.server.ipc;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import me.android.ipc.data.Data;
import me.android.ipc.server.Utils;

public class SocketServer extends IntentService {
    public static final String TAG = "SocketServer";
    private volatile boolean isServiceDestroyed;
    ServerSocket mServerSocket;
    private final AtomicInteger mCounter = new AtomicInteger();

    public SocketServer() {
        super("IPCSocketServer");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent() called with: intent = [" + Utils.dumpBundle(intent == null ? null : intent.getExtras()) + "]");
        try {
            mServerSocket = new ServerSocket(10001);
        } catch (IOException e) {
            EventBus.getDefault().post(TAG + Log.getStackTraceString(e));
            return;
        }
        while (!isServiceDestroyed) {
            try {
                EventBus.getDefault().post(TAG + "server start listening...");
                final Socket client = mServerSocket.accept();
                final String clientAddress = client.getLocalAddress().getHostAddress();
                EventBus.getDefault().post(TAG + clientAddress+"is online.");
                // start new thread for each client
                new Thread(){
                    @Override
                    public void run() {
                        BufferedReader br = null;
                        BufferedOutputStream bw = null;

                        try {

                            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            bw = new BufferedOutputStream(client.getOutputStream());
                            while (!isServiceDestroyed) {
                                String line = br.readLine();
                                if (line == null) {
                                    EventBus.getDefault().post(TAG + clientAddress + " is offline.");
                                    return;
                                }
                                EventBus.getDefault().post(TAG + " receive call: " + line);
                                // parse incoming call and give corresponding response.
                                Uri uri = Uri.parse(line);
                                String path = uri.getPath();
                                String param = uri.getQueryParameter("param");
                                String res = null;
                                Utils.XData xData = new Utils.XData();
                                if (TextUtils.isEmpty(path)) {
                                    res = "unknown call " + line + " from " + clientAddress + "\n";
                                    xData.setCall("unknown");
                                    xData.setData(res.getBytes());
                                } else {
                                    switch (path) {
                                        case "getData":
                                            xData.setCall("getData");
                                            Data data = new Data();
                                            data.setId(param);
                                            data.setName(param + "#" + mCounter.getAndDecrement());
                                            xData.setData(Utils.parcelable2Bytes(data));
                                            break;
                                        case "getImage":
                                            Bitmap bitmap = Utils.loadBitmap(getApplicationContext());
                                            xData.setCall("getImage");
                                            xData.setData(Utils.bitmap2Bytes(bitmap));
                                            break;
                                        default:
                                            xData.setCall(path);
                                            res = "call " + path + " from " + clientAddress + " is unsupported." + "\n";
                                            xData.setData(res.getBytes());
                                            break;
                                    }
                                }
                                bw.write(Utils.parcelable2Bytes(xData));
                                bw.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Utils.close(br, bw, client);
                    }
                }.start();
            } catch (IOException e) {
                EventBus.getDefault().post(TAG + Log.getStackTraceString(e));
            }

        }
    }

    @Override
    public void onDestroy() {
        isServiceDestroyed = true;
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }




}
