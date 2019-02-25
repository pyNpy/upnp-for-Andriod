package com.example.kl.dlna;

import android.service.autofill.FillEventHistory;

import java.io.Serializable;

/**
 * Created by kl on 2019/1/15.
 */

/**
 * 1. picture: mediatype; mediapath
 * 2. audio: mediatype; mediapath ;play ;pause ;seektotime;
 * 3.video: mediatype; mediapath ;play ;pause ;seektotime;
 *
 *
 * */

public class TVOperator implements Serializable{
    final  private  static  String TAG=TVOperator.class.getSimpleName();

    private MediaType mediatype;
    private String meidapath;
    private boolean  cast2tv;

    private boolean play;
    private boolean pause;
    private boolean seek;
    /**
     *  formate as:00:02:30
     * **/
    private String  seektotime;//audio video "00:10"

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    private boolean stop;
    public TVOperator(MediaType mediatype, String meidapath) {
        this.mediatype = mediatype;
        this.meidapath = meidapath;
        this.cast2tv=false;
        this.play =false;
        this.pause=false;
        this.stop=false;
        this.seek=false;
        this.seektotime="00:00:00";
    }
    public static String getTag()
    {
        return TAG;
    }
    public MediaType getMediatype() {
        return mediatype;
    }

    public void setMediatype(MediaType mediatype) {
        this.mediatype = mediatype;
    }

    public String getMeidapath() {
        return meidapath;
    }

    public boolean isCast2tv() {
        return cast2tv;
    }

    public void setCast2tv(boolean cast2tv) {
        this.cast2tv = cast2tv;
    }

    public void setMeidapath(String meidapath) {
        this.meidapath = meidapath;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isSeek() {
        return seek;
    }

    public void setSeek(boolean seek) {
        this.seek = seek;
    }

    public String getSeektotime() {
        return seektotime;
    }

    public void setSeektotime(String seektotime) {
        this.seektotime = seektotime;
    }

    @Override
    public String toString() {
        return "TVOperator{" +
                "mediatype='" + mediatype + '\'' +
                ", meidapath='" + meidapath + '\'' +
                ", play=" + play +
                ", pause=" + pause +
                ", seek=" + seek +
                ", seektotime='" + seektotime + '\'' +
                '}';
    }

    public static  String getTime(long value)
    {
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
        return temp;

    }




}
