package com.flink.common.domain.status;


public class Audio {
    private Integer br; ///:  int bps,
    private Integer lostpre; ///: int  0 – 100,
    private Integer vol; ///: 0 – 100,
    private String mime; ///:string "opus"  " vp8"  " h264"


    public Integer getBr() {
        return br;
    }

    public void setBr(Integer br) {
        this.br = br;
    }

    public Integer getLostpre() {
        return lostpre;
    }

    public void setLostpre(Integer lostpre) {
        this.lostpre = lostpre;
    }

    public Integer getVol() {
        return vol;
    }

    public void setVol(Integer vol) {
        this.vol = vol;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }
}
