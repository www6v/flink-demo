package com.flink.common.domain.status;

import com.flink.common.domain.common.Head;

public class RtcStatusLog extends Head {
    private StatusData data;

    public String getMethod() {
        return super.getMethod();
    }

    public void setMethod(String method) {
        super.setMethod(method);
    }

    public String getVersion() {
        return super.getVersion();
    }

    public void setVersion(String version) {
        super.setVersion(version);
    }

    public String getRpc_id() {
        return super.getRpc_id();
    }

    public void setRpc_id(String rpc_id) {
        super.setRpc_id(rpc_id);
    }

    public Integer getMtype() {
        return super.getMtype();
    }

    public void setMtype(Integer mtype) {
        super.setMtype(mtype);
    }

    public Integer getType() {
        return super.getType();
    }

    public void setType(Integer type) {
        super.setType(type);
    }

    public Integer getStype() {
        return super.getStype();
    }

    public void setStype(Integer stype) {
        super.setStype(stype);
    }

    public Long getTs() {
        return super.getTs();
    }

    public void setTs(Long ts) {
        super.setTs(ts);
    }

    public String getAid() {
        return super.getAid();
    }

    public void setAid(String aid) {
        super.setAid(aid);
    }

    public String getRid() {
        return super.getRid();
    }

    public void setRid(String rid) {
        super.setRid(rid);
    }

    public String getSid() {
        return super.getSid();
    }

    public void setSid(String sid) {
        super.setSid(sid);
    }

    public String getUid() {
        return super.getUid();
    }

    public void setUid(String uid) {
        super.setUid(uid);
    }

    public StatusData getData() {
        return data;
    }

    public void setData(StatusData data) {
        this.data = data;
    }

    public String getStreamid() {
        return super.getStreamid();
    }

    public void setStreamid(String streamid) {
        super.setStreamid(streamid);
    }
}







