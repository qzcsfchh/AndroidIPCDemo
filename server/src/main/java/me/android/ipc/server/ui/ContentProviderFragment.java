package me.android.ipc.server.ui;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import me.android.ipc.server.ipc.ContentProviderServer;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ContentProviderFragment extends BaseFragment {
    @Override
    protected String getEventTag() {
        return ContentProviderServer.TAG;
    }
}
