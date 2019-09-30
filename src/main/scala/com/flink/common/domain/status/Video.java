package com.flink.common.domain.status;

public class Video {

    private Integer br; ///:  int bps,
    private Integer lostpre; ///: int  0 – 100,
    private Integer frt; ///: int
    private Integer w; ///: int
    private Integer h; ///: int

    private String mime; /// :string "vp8"  "vp9" "h264"


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

    public Integer getFrt() {
        return frt;
    }

    public void setFrt(Integer frt) {
        this.frt = frt;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }
}

