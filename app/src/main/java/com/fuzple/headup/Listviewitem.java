package com.fuzple.headup;

/**
 * Created by user on 2017-12-21.
 */

public class Listviewitem {
    int im_image,im;          //am,pm 경로
    boolean sc;        // switch 상태
    String ap,time, day, memo;              // 알람 정보

    public void setTime(String str) {
        this.time = str;
    }
    public void setDay(String str) {
        day = str;
    }
    public void setMemo(String str) {memo = str;}
    public void setSwitch(boolean sc) {this.sc = sc;}
    public void setap(String ap) {this.ap = ap;}
    public void setimimage(int num)
    {
        im_image = num;
    }
    public void setim(int im){this.im = im;}

    public String getTime() {
        return time;
    }
    public String getDay() {
        return day;
    }
    public String getMemo() {return memo;}
    public boolean getSwitch() {return sc;}
    public String getAp()
    {
        return ap;
    }
    public int getimimage()
    {
        return im_image;
    }
    public int getim(){return im;}
}
