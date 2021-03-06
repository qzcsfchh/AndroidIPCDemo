package me.android.ipc.client.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

import me.android.ipc.client.ipc.AidlClient;
import me.android.ipc.data.Data;

public class AidlFragment extends BaseFragment {
    private AidlClient mAidlClient;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAidlClient = new AidlClient(context);
        mAidlClient.bind(this);
    }

    @Override
    protected void getData() {
        try {
            Data data = mAidlClient.getData(UUID.randomUUID().toString());
            mTextView.append(data + "\n");
        } catch (RemoteException e) {
            mTextView.append(e.getMessage()+"\n");
        }
    }

    @Override
    protected void getImage(){
        try {
            Bitmap image = mAidlClient.getImage(UUID.randomUUID().toString());
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
