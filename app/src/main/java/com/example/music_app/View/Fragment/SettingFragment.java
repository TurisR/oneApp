package com.example.music_app.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.R;
import com.example.music_app.View.Activity.ScanActivity;
import com.example.music_app.View.Activity.SetTimeClockActivity;

public class SettingFragment extends Fragment {

    private View view;
    private RelativeLayout btn;
    private RelativeLayout time_clock;
    private Switch aSwitch;
    private TextView clock_time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragement_setting, null);
        btn=(RelativeLayout) view.findViewById(R.id.scan_btn);
        clock_time = view.findViewById(R.id.clock_time);
        time_clock = (RelativeLayout) view.findViewById(R.id.time_clock);
        aSwitch = view.findViewById(R.id.sensor_switch_btn);

        if(AppConstant.getInstance().getClockTime() == null){
            clock_time.setVisibility(View.INVISIBLE);
        }else {
            clock_time.setText(AppConstant.getInstance().getClockTime());
        }

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
            }
        });
        time_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SetTimeClockActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    if (!AppConstant.getInstance().getSensorState())
                    {
                        AppConstant.getInstance().getPlayerUtil(getActivity()).openSensor();
                        aSwitch.setChecked(true);
                        Toast.makeText(getActivity(), "开启摇摇切歌", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    if (AppConstant.getInstance().getSensorState())
                    {
                        AppConstant.getInstance().getPlayerUtil(getActivity()).closeSensor();
                        aSwitch.setChecked(false);
                        Toast.makeText(getActivity(), "关闭摇摇切歌", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppConstant.getInstance().getClockTime() == null){
            clock_time.setVisibility(View.INVISIBLE);
//            Toast.makeText(getActivity(),"set1"+AppConstant.getInstance().getClockTime(),Toast.LENGTH_SHORT).show();
        }else {
            clock_time.setVisibility(View.VISIBLE);
            clock_time.setText(AppConstant.getInstance().getClockTime());
//            Toast.makeText(getActivity(),"set2"+AppConstant.getInstance().getClockTime(),Toast.LENGTH_SHORT).show();
        }
//        Toast.makeText(getActivity(),"set"+AppConstant.getInstance().getClockTime(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
                Log.e("刷新","setFragment");
            }
        }
}



