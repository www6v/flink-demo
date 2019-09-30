package com.flink.common.domain.operation;


import com.flink.common.domain.common.Head;

public class OpertionLog  extends Head {
    private OpertionData data;

    public OpertionData getData() {
        return data;
    }
    public void setData(OpertionData data) {
        this.data = data;
    }
}
