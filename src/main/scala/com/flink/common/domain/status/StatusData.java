package com.flink.common.domain.status;

public class StatusData {

   private Integer rtt; ///: int ms, //pub 有效
   private Integer delay; ///:int, //sub 有效

    private Integer cpu;
    private Integer memory;

    private String userid;
    private String streamid;

    private Audio  audio;
    private Video  video;

    public Integer getRtt() {
        return rtt;
    }

    public void setRtt(Integer rtt) {
        this.rtt = rtt;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public String getUserid() {
        if (userid==null || userid.equals("")) {
            return "no" + "Userid";
        }
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStreamid() {
        if (streamid==null || streamid.equals("")) {
            return "no" + "Streamid";
        }
        return streamid;
    }

    public void setStreamid(String streamid) {
        this.streamid = streamid;
    }
}
