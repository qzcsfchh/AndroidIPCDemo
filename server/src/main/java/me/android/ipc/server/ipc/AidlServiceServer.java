package me.android.ipc.server.ipc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import me.android.ipc.data.Data;
import me.android.ipc.IPClInterface;

/**
 * The Aidl service， this service will be started by client.
 * Client should call {@link android.content.Context#bindService(Intent, ServiceConnection, int)} to
 * get connected with Aidl service.
 */
public final class AidlServiceServer extends Service {
    final String TAG = getClass().getSimpleName();
    private static int counter;

    private final IPClInterface.Stub mStub = new IPClInterface.Stub() {
        @Override
        public Bitmap getImage(String uri) throws RemoteException {
            EventBus.getDefault().post(TAG + " getImage() called with: uri = [" + uri + "]" + threadName());
            Drawable icon = getApplicationInfo().loadIcon(getPackageManager());
            return drawable2bitmap(icon);
        }

        @Override
        public Data getData(String id) throws RemoteException {
            EventBus.getDefault().post(TAG + " getData() called with: id = [" + id + "]" + threadName());
            Data data = new Data();
            data.setId(id);
            data.setName(id + "#" + counter++);
            return data;
        }
    };

    private String threadName(){
        return Thread.currentThread().getName();
    }

    @Override
    public IBinder onBind(Intent intent) {
        EventBus.getDefault().post(TAG + " onBind: " + threadName());
        return mStub;
    }


    private Bitmap drawable2bitmap(Drawable icon) {
        if (icon == null) return null;
        int width = icon.getIntrinsicWidth();
        int height = icon.getIntrinsicHeight();
        if (width == 0 || height == 0) return null;
        icon.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        icon.draw(canvas);
        return bitmap;
    }

}
