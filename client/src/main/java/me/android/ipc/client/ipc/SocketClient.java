package me.android.ipc.client.ipc;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

import me.android.ipc.client.Utils;
import me.android.ipc.data.Data;

public class SocketClient extends HandlerThread implements LifecycleEventObserver {
    private static final String TAG = "SocketClient";
    private Socket mClientSocket;
    private Handler mHandler;
    private BufferedInputStream bis;
    private BufferedWriter bw;
    private final Queue<Utils.BiConsumer<Data, Exception>> mDataQueue = new LinkedBlockingDeque<>();
    private final Queue<Utils.BiConsumer<Bitmap, Exception>> mImageQueue = new LinkedBlockingDeque<>();
    Utils.BiConsumer<SocketClient,Exception> mConnectConsumer;
    public SocketClient() {
        super(TAG);
        start();
    }


    public void connect(Utils.BiConsumer<SocketClient,Exception> consumer){
        if (mClientSocket != null) {
            Log.d(TAG, "connect abort: already connected.");
            return;
        }
        mConnectConsumer = consumer;
    }

    public void getData(Utils.BiConsumer<Data,Exception> consumer){
        if (bw == null||bis==null) {
            consumer.accept(null,new Exception("Server not connected."));
        }else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        bw.write("custom://host/getData?param="+ UUID.randomUUID().toString());
                        mDataQueue.add(consumer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    public void getImage(Utils.BiConsumer<Bitmap,Exception> consumer){
        if (bw == null||bis==null) {
            consumer.accept(null,new Exception("Server not connected."));
        }else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        bw.write("custom://host/getImage");
                        mImageQueue.add(consumer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    public void closeConnection(){
        Utils.close(bis,bw,mClientSocket);
        bis = null;
        bw = null;
        mClientSocket = null;
    }



    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(getLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                while (socket == null) {
                    try {
                        socket = new Socket("localhost", 10001);
                        mClientSocket = socket;
                        bis = new BufferedInputStream(socket.getInputStream());
                        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        if (mConnectConsumer != null) {
                            mConnectConsumer.accept(SocketClient.this,null);
                        }

                        // start listen to server.
                        Log.d(TAG, "start listen to server");
                        new Thread(){
                            @Override
                            public void run() {
                                while (SocketClient.this.isAlive()) {

                                    ByteArrayOutputStream baos = null;
                                    try {
                                        int available = bis.available();
                                        if (available <= 0) continue;
                                        baos = new ByteArrayOutputStream();
                                        byte[] buf = new byte[1024];
                                        int len = -1;
                                        while ((len = bis.read(buf)) != -1) {
                                            baos.write(buf, 0, len);
                                        }
                                        Utils.XData xData = Utils.bytes2Parcelable(baos.toByteArray(), Utils.XData.CREATOR);
                                        String call = xData.getCall();
                                        Log.d(TAG, "from server: " + call);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Utils.close(baos);
                                }
                            }
                        }.start();
                    } catch (IOException e) {
                        if (mConnectConsumer != null) {
                            mConnectConsumer.accept(null, e);
                        }
                        SystemClock.sleep(1000);
                    }
                }
            }
        });
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event== Lifecycle.Event.ON_DESTROY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                quitSafely();
            }else {
                quit();
            }
            closeConnection();
        }
    }
}
