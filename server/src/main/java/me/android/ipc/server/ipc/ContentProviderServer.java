package me.android.ipc.server.ipc;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.renderscript.ScriptGroup;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;

import me.android.ipc.IPCInterface;
import me.android.ipc.data.Data;
import me.android.ipc.server.Utils;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public final class ContentProviderServer extends ContentProvider {
    public static final String TAG = "ContentProviderServer";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: uri = "+uri);
        return new BinderCursor(getContext());
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private static class BinderCursor extends MatrixCursor {
        private final Context mContext;
        private final Bundle mBundle = new Bundle();

        public BinderCursor(Context context) {
            super(new String[]{"service"});
            mContext = context;
            mBundle.putBinder("binder", mStub);
        }

        @Override
        public Bundle getExtras() {
            return mBundle;
        }

        private static int counter;

        private final IPCInterface.Stub mStub = new IPCInterface.Stub() {
            @Override
            public Bitmap getImage(String uri) throws RemoteException {
                EventBus.getDefault().post(TAG + " getImage() called with: uri = [" + uri + "]" + threadName());
                return Utils.loadBitmap(mContext);
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

    }

}
