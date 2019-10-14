package com.flink.common.domain.joinLeave;

import com.flink.common.domain.common.Data;

public class InitData extends Data {
    private String  region;  /// 區域

    private String  sdkv;  /// sdk 版本号
    private String agent;/// sdk 类型
    private String device;/// 设备类型类型
    private String system;/// 系统描述
    private String network;/// 网络类型
    private String cpu;/// cpu 描述
    private Integer mem;/// 内存大小


    public String getSdkv() {
        return sdkv;
    }

    public void setSdkv(String sdkv) {
        this.sdkv = sdkv;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public Integer getMem() {
        return mem;
    }

    public void setMem(Integer mem) {
        this.mem = mem;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}