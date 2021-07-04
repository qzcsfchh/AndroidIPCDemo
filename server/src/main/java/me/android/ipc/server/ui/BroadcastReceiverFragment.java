package me.android.ipc.server.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import me.android.ipc.server.ipc.BroadcastReceiverServer;

public class BroadcastReceiverFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getEventTag() {
        return BroadcastReceiverServer.TAG;
    }
}
