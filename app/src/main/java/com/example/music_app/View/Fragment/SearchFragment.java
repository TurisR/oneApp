package com.example.music_app.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.music_app.Presenter.AppConstant;
import com.example.music_app.R;
import com.example.music_app.View.Adapter.ShowListAdapter;
import com.example.music_app.mould.Model.bean.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:搜索歌曲的listAdapter
 * @author:JiangJiaHui
 * @createDate: 2019/11/10
 * @Modified By：
 * @version: 1.0
 */
public class SearchFragment extends Fragment {

    private View view;
    private EditText edit_text;
    private TextView text_search;
    private ListView search_result_list;
    private LinearLayout search_history_layout;
    private ListView search_history_list;
    private ShowListAdapter resultAdpter,historyAdpter;
    private List<Song> searchResult=new ArrayList<>();
    private String mSearch;
    private List<Song> mSongList;
    private int mIntIndex;
    private LinearLayout search_layout;
    private InputMethodManager mImm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        edit_text=view.findViewById(R.id.edit_text);
        text_search=view.findViewById(R.id.text_search);
        search_result_list=view.findViewById(R.id.search_result_list);
        search_history_layout=view.findViewById(R.id.search_history_layout);
        search_history_list=view.findViewById(R.id.search_history_list);
        search_layout=view.findViewById(R.id.search_layout);
       // resultAdpter=new ShowListAdapter(getActivity(),);
        historyAdpter=new ShowListAdapter(getActivity(), AppConstant.DataType.RECENT_SEARCH);
        search_history_list.setAdapter(historyAdpter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initEvent() {

        search_history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song=(Song)AppConstant.getInstance().getList(AppConstant.DataType.RECENT_SEARCH).get(position);
                AppConstant.getInstance().getPlayerUtil(getActivity()).play(song);
                AppConstant.getInstance().addCurrentSongList(song);
                //AppConstant.getInstance().setCurrentSongList(AppConstant.getInstance().getList(AppConstant.DataType.RECENT_SEARCH).get(position));
            }
        });

        mSongList = AppConstant.getInstance().getLocalSongList();
        mIntIndex = -1;

            text_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearch = edit_text.getText().toString();
                    search_layout.requestFocus();
                    if(!TextUtils.isEmpty(mSearch)){
                        Toast.makeText(getActivity(),"搜索值为"+ mSearch,Toast.LENGTH_LONG).show();
                        search();
                    }else {
                        Toast.makeText(getActivity(),"搜索值为空"+ mSearch,Toast.LENGTH_LONG).show();
                        search_result_list.setVisibility(View.GONE);
                        search_history_layout.setVisibility(View.VISIBLE);
                        historyAdpter=new ShowListAdapter(getActivity(), AppConstant.DataType.RECENT_SEARCH);
                        search_history_list.setAdapter(historyAdpter);
                    }
                    if (null !=  mImm && view != null) {
                        mImm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                });
    }



    private void search() {
        searchResult.clear();
        for (Song song: mSongList){
            mIntIndex = song.getTitle().indexOf(mSearch);
            if(mIntIndex != -1&&!AppConstant.getInstance().isExist(searchResult,song)){
                searchResult.add(song);
            }
            mIntIndex = song.getSinger().indexOf(mSearch);
            if(mIntIndex != -1&&!AppConstant.getInstance().isExist(searchResult,song)){
                searchResult.add(song);
            }
        }

        if(searchResult.size()==0){
            Toast.makeText(getActivity(),"没有找到先关歌曲",Toast.LENGTH_LONG).show();
        }else {
            resultAdpter=new ShowListAdapter(getActivity(),searchResult);
            search_result_list.setVisibility(View.VISIBLE);
            search_history_layout.setVisibility(View.GONE);
            search_result_list.setAdapter(resultAdpter);
        }

        search_result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppConstant.getInstance().getPlayerUtil(getActivity()).play(searchResult.get(position));
                AppConstant.getInstance().addListSong(AppConstant.DataType.RECENT_SEARCH,searchResult.get(position));
                edit_text.setText("");

                search_result_list.setVisibility(View.GONE);
                search_history_layout.setVisibility(View.VISIBLE);
                historyAdpter=new ShowListAdapter(getActivity(), AppConstant.DataType.RECENT_SEARCH);
                search_history_list.setAdapter(historyAdpter);
                AppConstant.getInstance().setCurrentSongList(searchResult.get(position));
            }
        });
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (!isVisibleToUser) {

            if (search_layout != null) {
                if (null !=  mImm && view != null) {
                     mImm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                search_layout.requestFocus();
                edit_text.setText("");
                search_result_list.setVisibility(View.GONE);
                search_history_layout.setVisibility(View.VISIBLE);
                historyAdpter = new ShowListAdapter(getActivity(), AppConstant.DataType.RECENT_SEARCH);
                search_history_list.setAdapter(historyAdpter);
                Log.e("刷新", "searchFragment");
            }
        }

    }

}
