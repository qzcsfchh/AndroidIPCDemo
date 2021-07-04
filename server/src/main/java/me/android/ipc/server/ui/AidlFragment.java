package me.android.ipc.server.ui;

import me.android.ipc.server.ipc.AidlServiceServer;

public class AidlFragment extends BaseFragment {

    @Override
    protected String getEventTag() {
        return AidlServiceServer.TAG;
    }
}
