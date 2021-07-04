package me.android.ipc.client.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import me.android.ipc.client.ipc.ContentProviderClient;
import me.android.ipc.data.Data;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ContentProviderFragment extends BaseFragment{
    private ContentProviderClient mClient;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mClient = new ContentProviderClient(context);
    }

    @Override
    protected void getData() {
        try {
            Data data = mClient.getData();
            mTextView.append(data + "\n");
        } catch (RemoteException e) {
            mTextView.append(e.getMessage()+"\n");
        }
    }

    @Override
    protected void getImage() {
        try {
            Bitmap image = mClient.getImage();
            if (image == null) {
                mTextView.append("null"+"\n");
            }else {
                SpannableString ss = new SpannableString("ss");
                ImageSpan imageSpan = new ImageSpan(requireContext(), image, ImageSpan.ALIGN_BASELINE);
                ss.setSpan(imageSpan,0,2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                mTextView.append(ss);
                mTextView.append("\n");
            }
        } catch (RemoteException e) {
            mTextView.append(e.getMessage()+"\n");
        }
    }
}
