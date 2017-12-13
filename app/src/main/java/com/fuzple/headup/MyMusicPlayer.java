package com.fuzple.headup;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-12-13.
 */

class Mp3Filter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3")); // 확장자가 mp3인지 확인
    }
}

public class MyMusicPlayer extends ListActivity {

    private static final String MEDIA_PATH = new String("/sdcard/"); // 파일 경로 지정
    private List<String> songs = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.music);
            updateSongList();
        } catch (NullPointerException e) {
            Log.v(getString(R.string.app_name), e.getMessage()); // 로그에 에러메시지 기록
        }
    }

    public void updateSongList() {
        File musicfiles = new File(MEDIA_PATH);
        ArrayAdapter<String> musicList = new ArrayAdapter<String>(this,R.layout.music_item,songs);

        if (musicfiles.listFiles( new Mp3Filter()).length > 0) {
            for (File file : musicfiles.listFiles( new Mp3Filter())) {
                songs.add(file.getName()); // mp3파일을 ArrayList에 추가
            }

            setListAdapter(musicList); // ArrayAdapter를 ListView에 바인딩
        }

    }

}