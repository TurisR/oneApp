package com.example.music_app.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_app.R;


/**
 * Created by yifeng on 16/8/3.
 *
 */
public class PersonalListFragment extends Fragment {

    private static final String EXTRA_CONTENT = "content";

    public static PersonalListFragment newInstance(String content){
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_CONTENT, content);
        PersonalListFragment personalListFragment = new PersonalListFragment();
        personalListFragment.setArguments(arguments);
        return personalListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_personal_list, null);
        ((TextView)contentView.findViewById(R.id.tv_content)).setText("个人歌单");
        return contentView;
    }
}
