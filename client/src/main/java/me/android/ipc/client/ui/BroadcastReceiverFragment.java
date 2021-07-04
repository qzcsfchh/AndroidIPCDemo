package me.android.ipc.client.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import me.android.ipc.client.ipc.BroadcastReceiverClient;
import me.android.ipc.data.Data;

public class BroadcastReceiverFragment extends BaseFragment{
    private BroadcastReceiverClient mClient;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mClient = new BroadcastReceiverClient();
    }

    @Override
    protected void getData() {
        mClient.getData(requireContext(),new Consumer<Data>() {
            @Override
            public void accept(Data data) {
                mTextView.append(data + "\n");
            }
        });
    }

    @Override
    protected void getImage() {
        mClient.getImage(requireContext(), new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) {
                if (bitmap == null) {
                    mTextView.append("null"+"\n");
                }else {
                    SpannableString ss = new SpannableString("ss");
                    ImageSpan imageSpan = new ImageSpan(requireContext(), bitmap, ImageSpan.ALIGN_BASELINE);
                    ss.setSpan(imageSpan,0,2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    mTextView.append(ss);
                    mTextView.append("\n");
                }
            }
        });
    }
}
