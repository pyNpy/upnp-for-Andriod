package kl.example.com.imageslector;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kl.dlna.MediaType;
import com.example.kl.dlna.MyBroadcastReceiver;
import com.example.kl.dlna.TVOperator;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kl on 2019/1/12.
 */

public class MusicPlayer implements View.OnClickListener {
    private Context mContext;

    private TextView mcurrentTime;
    private TextView mtotalTime;
    private SeekBar mseekBar;
    private ImageButton mPlay;
    private ImageButton mPause;
    private ImageButton mReset;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String maudiopath = "";
    private static Handler mHandler = new Handler();
    public boolean mStatus = false;//是否完成第一次点击play按钮

    //仅更新用户
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (MusicPlayer.this.mediaPlayer != null) {
                if (MusicPlayer.this.mcurrentTime != null)
                    setTime(MusicPlayer.this.mcurrentTime, mediaPlayer.getCurrentPosition());
                if (MusicPlayer.this.mseekBar != null)
                    MusicPlayer.this.mseekBar.setProgress(mediaPlayer.getCurrentPosition());

                MusicPlayer.this.mHandler.postDelayed(MusicPlayer.this.runnable, 200L);
            }
        }
    };

    public MusicPlayer() {
    }

    public void Create(Context c, TextView currentTime, TextView totalTime,
                       SeekBar seekbar,
                       ImageButton play, ImageButton pause, ImageButton reset) {
        this.mContext =c;
        this.mcurrentTime = currentTime;
        this.mtotalTime = totalTime;
        this.mseekBar = seekbar;
        this.mPlay = play;
        this.mPause = pause;
        this.mReset = reset;

    }

    public void Destroy() {
        if (this.mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(runnable);
            this.mStatus=false;
        }
    }

    public void init(final String audiopath) {
        this.mStatus = false;
        this.mHandler.removeCallbacks(runnable);
        try {
            if (this.mediaPlayer !=null) {
                this.mediaPlayer.release();
                this.mediaPlayer = new MediaPlayer();
            }
            this.maudiopath = audiopath;
            this.mediaPlayer.setDataSource(this.maudiopath);
            this.mediaPlayer.setLooping(false);
            this.mediaPlayer.prepare();
            if(this.mseekBar!=null){
                this.mseekBar.setProgress(0);
                this.mseekBar.setMax(this.mediaPlayer.getDuration());
            }
            setTime(this.mcurrentTime, 0L);
            setTime(this.mtotalTime, this.mediaPlayer.getDuration());
            this.mPlay.setOnClickListener(this);
            this.mPause.setOnClickListener(this);
            this.mReset.setOnClickListener(this);
            this.mseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
//                        MusicPlayer.this.mseekBar.setProgress(progress);
                        MusicPlayer.this.mediaPlayer.seekTo(progress);
                        setTime(MusicPlayer.this.mcurrentTime, MusicPlayer.this.mediaPlayer.getCurrentPosition());

                        TVOperator operator=new TVOperator(MediaType.Audio,audiopath);
                        operator.setCast2tv(false);
                        operator.setSeek(true);
                        String time=TVOperator.getTime((long) MusicPlayer.this.mseekBar.getProgress() );
                        operator.setSeektotime( time );
                        Intent intentaudio= new Intent();
                        intentaudio.setClass(mContext, MyBroadcastReceiver.class);
                        intentaudio.setAction("Device2TV");
                        intentaudio.putExtra(TVOperator.getTag(), operator);
                        MusicPlayer.this.mContext.sendBroadcast(intentaudio );
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        if(this.mediaPlayer!=null)
        {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer=null;
        }
        this.mseekBar.setProgress(0);
        this.mseekBar.setMax(100);
        setTime(this.mcurrentTime,0L);
        setTime(this.mtotalTime,0L);
        this.mStatus=false;
        this.mHandler.removeCallbacks(runnable);
    }



    public void play() {
        if (this.mediaPlayer != null) {
            this.mseekBar.setProgress(this.mediaPlayer.getCurrentPosition());
            setTime(this.mcurrentTime, this.mediaPlayer.getCurrentPosition());
        }
        if (!this.mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (this.mediaPlayer != null) {
            this.mseekBar.setProgress(this.mediaPlayer.getCurrentPosition());
            setTime(this.mcurrentTime, this.mediaPlayer.getCurrentPosition());
            if (this.mediaPlayer.isPlaying()) {
                this.mediaPlayer.pause();
            }
        }
    }

    public void reset() {
        try {
            this.mediaPlayer.stop();
            this.mediaPlayer.reset();
            this.mediaPlayer.setDataSource(maudiopath);
            this.mediaPlayer.prepare();
            this.mediaPlayer.seekTo(0);
            //重置
            setTime(this.mcurrentTime, 0L);
            this.mseekBar.setProgress(0);
            //停止更新seekBar
            this.mHandler.removeCallbacks(runnable);
            this.mStatus = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置textview 00:00::00
     **/
    private void setTime(TextView view, long value) {
        String temp="";
        long time=Long.valueOf(value);
        long hours= time/1000/60/60;
        if(hours<10){
            temp="0"+hours;
        }else{
            temp+=hours;
        }
        long min=time/60000;
        if(min<10){
            temp=":0"+min;
        }else {
            temp+=":"+min;
        }
        long second=(time/1000)%60;
        if(second<10){
            temp+=":0"+second;
        }else {
            temp+=":"+second;
        }
        if (view != null)
            view.setText(temp);

    }

    public String getAudioPath()
    {
        return this.maudiopath;
    }
    @Override
    public void onClick(View v) {
        int id= v.getId();
        if(id== R.id.musicplayer_play) {
            this.play();
            if(this.mStatus==false){
                this.mStatus=true;
                this.mHandler.postDelayed(runnable,100L);
            }
            TVOperator operator = new TVOperator(MediaType.Audio,maudiopath );
            operator.setCast2tv(false);
            operator.setPlay(true);
            Intent intentaudio= new Intent();
            intentaudio.setClass(mContext, MyBroadcastReceiver.class);
            intentaudio.setAction("Device2TV");
            intentaudio.putExtra(TVOperator.getTag(), operator);
            MusicPlayer.this.mContext.sendBroadcast(intentaudio );
        }
        if(id==R.id.musicplayer_pause){
            this.pause();
            TVOperator operator = new TVOperator(MediaType.Audio,maudiopath );
            operator.setCast2tv(false);
            operator.setPause(true);
            Intent intentaudio= new Intent();
            intentaudio.setClass(mContext, MyBroadcastReceiver.class);
            intentaudio.setAction("Device2TV");
            intentaudio.putExtra(TVOperator.getTag(), operator);
            MusicPlayer.this.mContext.sendBroadcast(intentaudio );
        }
        if(id==R.id.musicplayer_reset){
            this.reset();
            TVOperator operator = new TVOperator(MediaType.Audio,maudiopath );
            operator.setCast2tv(false);
            operator.setPause(true);
            operator.setSeek(true);
            operator.setSeektotime("00:00:00");
            Intent intentaudio= new Intent();
            intentaudio.setClass(mContext, MyBroadcastReceiver.class);
            intentaudio.setAction("Device2TV");
            intentaudio.putExtra(TVOperator.getTag(),operator);
            MusicPlayer.this.mContext.sendBroadcast(intentaudio );


        }
    }
}
