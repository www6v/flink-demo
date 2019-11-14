package com.flink.common.domain.common;


import java.util.Date;

public class Head {
    protected String method;
    protected String version;
    protected String rpc_id;
    protected Integer mtype; ///: 1 cam 2 screen,
    protected Integer type; /// log分类  1 通话开始 2 通话状态 3 通话结束
    protected Integer stype; ///: 1 pub 1 sub,
    protected Long ts;
    protected String aid;
    protected String rid;
    protected String sid;
    protected String uid;
    protected String streamid;

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
        return new Date().getTime();
        // return ts;
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
