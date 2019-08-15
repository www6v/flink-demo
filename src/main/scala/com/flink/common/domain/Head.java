package com.flink.common.domain;

/**
 * Created by www6v on 2019/8/15.
 */
public class Head {
    String method; ///: "logup",
    String version; ///: 1.0,
    String rpc_id; ///: xxxx,
    Integer mtype; ///: 1 cam 2 screen,
    Integer type; /// log分类 取值 1 2 3, 1 通话开始 2 通话状态 3 通话结束
    Integer stype; ///: 1 pub 1 sub,
    Long ts; ///:uint32 time sec 1970,
    String aid; ///: string,
    String rid; ///: string,
    String sid; ///: string,
    String uid;  ///: string
    String streamid; ///: string,


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

    public String getStreamid() {
        return streamid;
    }

    public void setStreamid(String streamid) {
        this.streamid = streamid;
    }
}
