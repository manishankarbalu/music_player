package com.commonman.manishankar.musicplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Songlist extends AppCompatActivity {
  ListView lv;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);
        lv=(ListView) findViewById(R.id.lvPlaylist);
        final ArrayList<File> mysongs=findsongs(Environment.getExternalStorageDirectory());
        items=new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++){

            items[i]=mysongs.get(i).getName().toString().replace("mp3","").replace("wav","");
        }
        //Arrays.sort(items);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),R.layout.songlayout,R.id.songlay,items);
       lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songlist",mysongs));
            }
        });
    }

    public ArrayList<File> findsongs(File root){
        ArrayList<File>al=new ArrayList<File>();
        File[] files=root.listFiles();
        for(File singlefile : files){
            if(singlefile.isDirectory()){

                    al.addAll(findsongs(singlefile));
            }
            else{
                if(singlefile.getName().endsWith("mp3")||singlefile.getName().endsWith("wav")){

                    al.add(singlefile);
                }
            }
        }
        return al;
    }
}
