package com.flink.common.domain;

public class RtcClinetLog {

    private String method; ///: "logup",
    private String version; ///: 1.0,
    private String rpc_id; ///: xxxx,
    private Integer mtype; ///: 1 cam 2 screen,
    private Integer type; /// log分类 取值 1 2 3, 1 通话开始 2 通话状态 3 通话结束
    private Integer stype; ///: 1 pub 1 sub,
    private Long ts; ///:uint32 time sec 1970,
    private String aid; ///: string,
    private String rid; ///: string,
    private String sid; ///: string,
    private String uid;  ///: string

    private String streamid; ///: string,

   private Data data;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRpc_id() {
        return rpc_id;
    }

    public void setRpc_id(String rpc_id) {
        this.rpc_id = rpc_id;
    }

    public Integer getMtype() {
        return mtype;
    }

    public void setMtype(Integer mtype) {
        this.mtype = mtype;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStype() {
        return stype;
    }

    public void setStype(Integer stype) {
        this.stype = stype;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getStreamid() {
        return streamid;
    }

    public void setStreamid(String streamid) {
        this.streamid = streamid;
    }
}







