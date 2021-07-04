package me.android.ipc.client.ipc;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.UUID;

import me.android.ipc.IPCInterface;
import me.android.ipc.data.Data;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ContentProviderClient {
    private final IPCInterface mStub;

    public ContentProviderClient(Context context) {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://me.android.ipc.server.contentprovider"),
                null, null, null, null);
        if (cursor != null) {
            mStub = IPCInterface.Stub.asInterface(cursor.getExtras().getBinder("binder"));
            cursor.close();
        }else {
            mStub = null;
        }
    }

    public Data getData() throws RemoteException {
        String id = UUID.randomUUID().toString();
        return requireInterface().getData(id);
    }

    public Bitmap getImage() throws RemoteException {
        String id = UUID.randomUUID().toString();
        return requireInterface().getImage(id);
    }


    @NonNull
    private IPCInterface requireInterface() throws RemoteException {
        if (mStub == null) {
            throw new RemoteException("Service not connected yet, have you call start() ?");
        }
        return mStub;
    }
}
