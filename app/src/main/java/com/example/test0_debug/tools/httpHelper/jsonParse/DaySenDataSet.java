package com.example.test0_debug.tools.httpHelper.jsonParse;

import java.io.Serializable;
import java.util.List;

/**
 * 每日一句
 * */
public class DaySenDataSet implements Serializable {

    /**
     * errno : 0
     * errmsg : success
     * sid : 3742
     * title : 2020-04-24
     * content : The mankind’s courage and resolution will be witnessed and remembered by stars.
     * note : 人类的勇气和坚毅必将被镌刻在星空之下。
     * translation : 新版每日一句
     * picture : https://v-sq.ks3-cn-beijing.ksyun.com/image/213b880e693178c1a9bb01c5e60ad208.jpg
     * picture2 : https://v-sq.ks3-cn-beijing.ksyun.com/image/31918a2a573cb047957ce94efd74edae.jpg
     * picture3 : https://v-sq.ks3-cn-beijing.ksyun.com/image/6f5b2174bda612bb1cf949e020f88bb3.jpg
     * caption : 词霸每日一句
     * tts : https://v-sq.ks3-cn-beijing.ksyun.com/audio/cc51b82981fe3dccebb5cac20c8a9efa.mp3
     * tts_size :
     * s_pv : 79959
     * sp_pv : 0
     * love : 2172
     * s_content :
     * s_link :
     * period : 0
     * loveFlag : 0
     * tags :
     * keep : 0
     * comment_count : 1444
     * last_title : 2020-04-23
     * next_title : 2020-04-25
     * week_info : [{"week":"Mon","date":"2020-04-20","flag":"show"},{"week":"Tue","date":"2020-04-21","flag":"show"},{"week":"Wen","date":"2020-04-22","flag":"show"},{"week":"Thu","date":"2020-04-23","flag":"show"},{"week":"Fri","date":"2020-04-24","flag":"cur"},{"week":"Sat","date":"2020-04-25","flag":"show"},{"week":"Sun","date":"2020-04-26","flag":"show"}]
     */

    private int errno;
    private String errmsg;
    private int sid;
    private String title;
    private String content;
    private String note;
    private String translation;
    private String picture;
    private String picture2;
    private String picture3;
    private String caption;
    private String tts;
    private String tts_size;
    private int s_pv;
    private int sp_pv;
    private int love;
    private String s_content;
    private String s_link;
    private int period;
    private int loveFlag;
    private String tags;
    private int keep;
    private int comment_count;
    private String last_title;
    private String next_title;
    private List<WeekInfoBean> week_info;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getTts_size() {
        return tts_size;
    }

    public void setTts_size(String tts_size) {
        this.tts_size = tts_size;
    }

    public int getS_pv() {
        return s_pv;
    }

    public void setS_pv(int s_pv) {
        this.s_pv = s_pv;
    }

    public int getSp_pv() {
        return sp_pv;
    }

    public void setSp_pv(int sp_pv) {
        this.sp_pv = sp_pv;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public String getS_content() {
        return s_content;
    }

    public void setS_content(String s_content) {
        this.s_content = s_content;
    }

    public String getS_link() {
        return s_link;
    }

    public void setS_link(String s_link) {
        this.s_link = s_link;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getLoveFlag() {
        return loveFlag;
    }

    public void setLoveFlag(int loveFlag) {
        this.loveFlag = loveFlag;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getKeep() {
        return keep;
    }

    public void setKeep(int keep) {
        this.keep = keep;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getLast_title() {
        return last_title;
    }

    public void setLast_title(String last_title) {
        this.last_title = last_title;
    }

    public String getNext_title() {
        return next_title;
    }

    public void setNext_title(String next_title) {
        this.next_title = next_title;
    }

    public List<WeekInfoBean> getWeek_info() {
        return week_info;
    }

    public void setWeek_info(List<WeekInfoBean> week_info) {
        this.week_info = week_info;
    }

    public static class WeekInfoBean implements Serializable {
        /**
         * week : Mon
         * date : 2020-04-20
         * flag : show
         */

        private String week;
        private String date;
        private String flag;

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }
}
