package com.commonman.manishankar.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
    ArrayList mySongs;
    int position;
    static MediaPlayer mp;
    Uri u;
    SeekBar sb;
    Button btpause,ff,fb,prev,next;
    Thread updateseekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btpause=(Button)findViewById(R.id.btnpause);
        ff=(Button)findViewById(R.id.btnff);
        fb=(Button)findViewById(R.id.btnfb);
        prev=(Button)findViewById(R.id.btnpre);
        next=(Button)findViewById(R.id.btnnxt);
        btpause.setOnClickListener(this);
        ff.setOnClickListener(this);
        fb.setOnClickListener(this);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        if(mp!=null){
            mp.stop();
            mp.release();
        }
        sb=(SeekBar) findViewById(R.id.seekBar);
        updateseekbar=new Thread(){
            @Override
            public void run() {
                int totalDuration=mp.getDuration();
                int currentposition=0;
                sb.setMax(totalDuration);
                while(currentposition<totalDuration) {
                    try {
                        sleep(500);
                        currentposition = mp.getCurrentPosition();
                        sb.setProgress(currentposition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };
        Intent i=getIntent();
        Bundle b=i.getExtras();
        mySongs=(ArrayList)b.getParcelableArrayList("songlist");
        position=b.getInt("pos", 0);
        u=Uri.parse(mySongs.get(position).toString());
        mp=MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        updateseekbar.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){

            case R.id.btnpause:
                      if(mp.isPlaying()){
                          mp.pause();
                          btpause.setText(">");
                      }else {
                          mp.start();
                          btpause.setText("||");
                            }
                  break;
            case R.id.btnff:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btnfb:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btnnxt:
                mp.stop();
                mp.release();
                position=(position+1)% mySongs.size();
                u=Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                break;
            case R.id.btnpre:
                mp.stop();
                mp.release();
                position=(position-1<0)?mySongs.size()-1:position-1;
                u=Uri.parse(mySongs.get((position)).toString());
                mp=MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                break;




        }
    }
}
