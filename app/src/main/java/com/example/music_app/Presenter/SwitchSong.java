package com.example.music_app.Presenter;

import com.example.music_app.mould.Model.bean.Song;

import java.util.List;

public class SwitchSong {

    private Song mSong;
    private int position;
    private List<Song> mSongList;
    private int mode;
    private int size;

    public SwitchSong() {
        position=AppConstant.getInstance().getPosition();
        mSongList=AppConstant.getInstance().getCurrentSongList();
        mSong=AppConstant.getInstance().getPlayingSong();
        mode=AppConstant.getInstance().getMode();
        size=mSongList.size();
    }

    public Song getNextSong(boolean bl){
        //true 下一首 false 上一首
        switch (mode){
            case 0: //单曲播放
                break;
            case 1: //循环播放
                if(bl){
                    position++;
                    if(position > size - 1) {	//变为第一首的位置继续播放
                        position = 0;
                    }
                }else{
                    position--;
                    if(position<0){
                        position=size-1;
                    }
                }
                mSong=mSongList.get(position);

                //  play(0);
                break;
            case 2://循序播放
                if(bl){
                    if (position < size-1){
                        position++;
                    } else {
                        position = 0;
                    }
                }else{
                    if (position > 0) {
                        position--;
                    } else {
                        position = size - 1;
                    }
                }
                mSong=mSongList.get(position);
                break;
            case 3://随机播放
                position = getRandomIndex(size - 1);
                mSong=mSongList.get(position);
                //  play(0);
                break;

        }
        return mSong;
    }

    private int getRandomIndex(int end) {
        int index = (int) (Math.random() * end);
        return index;
    }

}
