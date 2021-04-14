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

public class AidlFragment extends Fragment {
    private AidlClient mAidlClient;
    private TextView mTextView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAidlClient = new AidlClient(context);
        mAidlClient.bind(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout line = new LinearLayout(requireContext());
        line.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(line);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.weight = 1;
        Button button = new Button(requireContext());
        button.setLayoutParams(llp);
        button.setText("getImage");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        line.addView(button);
        button = new Button(requireContext());
        button.setLayoutParams(llp);
        button.setText("getData");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        line.addView(button);
        ScrollView scrollView = new ScrollView(requireContext());
        scrollView.setFillViewport(true);
        linearLayout.addView(scrollView);
        mTextView = new TextView(requireContext());
        mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollView.addView(mTextView);
        return linearLayout;
    }

    private void getData() {
        try {
            Data data = mAidlClient.getData(UUID.randomUUID().toString());
            mTextView.append(data + "\n");
        } catch (RemoteException e) {
            mTextView.append(e.getMessage()+"\n");
        }
    }

    private void getImage(){
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
