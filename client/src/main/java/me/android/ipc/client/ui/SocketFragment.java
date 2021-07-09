package me.android.ipc.client.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;

import me.android.ipc.client.Utils;
import me.android.ipc.client.ipc.SocketClient;
import me.android.ipc.data.Data;

public class SocketFragment extends BaseFragment{
    SocketClient mSocketClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SocketClient().connect(new Utils.BiConsumer<SocketClient, Exception>() {
            @Override
            public void accept(SocketClient socketClient, Exception e) {
                mSocketClient = socketClient;
                if (e != null) {
                    if (mTextView != null) {
                        mTextView.post(() -> mTextView.append(e.getMessage() + "\n"));
                    }
                }
            }
        });
    }

    @Override
    protected void getData() {
        if (mSocketClient != null) {
            mSocketClient.getData(new Utils.BiConsumer<Data, Exception>() {
                @Override
                public void accept(Data data, Exception e) {

                }
            });
        }else {
            mTextView.append("net connected"+"\n");
        }
    }

    @Override
    protected void getImage() {
        if (mSocketClient != null) {
            mSocketClient.getImage(new Utils.BiConsumer<Bitmap, Exception>() {
                @Override
                public void accept(Bitmap data, Exception e) {

                }
            });
        }else {
            mTextView.append("net connected"+"\n");
        }
    }
}
