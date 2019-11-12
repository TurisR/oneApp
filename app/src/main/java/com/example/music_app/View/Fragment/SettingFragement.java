package com.example.music_app.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.music_app.R;
import com.example.music_app.View.Activity.ScanActivity;

import java.util.zip.Inflater;

public class SettingFragement extends Fragment {

    private View view;
    private Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragement_setting, null);
        btn=(Button) view.findViewById(R.id.scan_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(getActivity(), ScanActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "success2", Toast.LENGTH_LONG).show();
            }
        });

    }
}
