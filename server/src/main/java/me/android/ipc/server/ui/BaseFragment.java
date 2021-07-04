package me.android.ipc.server.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseFragment extends Fragment {

    private TextView mTextView;
    private final List<String> mHistory = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(requireContext());
        scrollView.setFillViewport(true);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTextView = new TextView(requireContext());
        mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollView.addView(mTextView);
        return scrollView;
    }

    @Subscribe(sticky = true)
    public void onEvent(String msg){
        final String tag = getEventTag();
        if (msg.contains(tag)) {
            msg = msg.replace(tag, "");
            if (mTextView != null) {
                if (!mHistory.isEmpty()) {
                    Iterator<String> iterator = mHistory.iterator();
                    while (iterator.hasNext()) {
                        mTextView.append(iterator.next() + "\n");
                        iterator.remove();
                    }
                }
                mTextView.append(msg + "\n");
            } else {
                mHistory.add(msg);
            }
        }
    }

    protected abstract String getEventTag();
}
