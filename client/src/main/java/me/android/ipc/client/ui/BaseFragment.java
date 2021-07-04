package me.android.ipc.client.ui;

import android.os.Bundle;
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

public abstract class BaseFragment extends Fragment {

    protected TextView mTextView;

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

    protected abstract void getData();

    protected abstract void getImage();
}
