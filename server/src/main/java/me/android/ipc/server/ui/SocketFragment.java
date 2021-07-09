package me.android.ipc.server.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import me.android.ipc.server.ipc.SocketServer;

public class SocketFragment extends BaseFragment{
    @Override
    protected String getEventTag() {
        return SocketServer.TAG;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().startService(new Intent(requireContext(), SocketServer.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().stopService(new Intent(requireContext(), SocketServer.class));
    }
}
