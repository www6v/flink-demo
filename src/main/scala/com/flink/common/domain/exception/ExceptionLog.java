package com.flink.common.domain.exception;

import com.flink.common.domain.common.Head;

public class ExceptionLog extends Head {
    private ExceptionData data;

    public ExceptionData getData() {
        return data;
    }
    public void setData(ExceptionData data) {
        this.data = data;
    }
}