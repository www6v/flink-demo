package com.flink.common.mybatis.entity;

public class ExceptionPO {
    private String roomId;
    private String userId;
    private Integer statusType;
    private Long time;
    private Integer exceptionType;


    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatusType() {
        return statusType;
    }
    public void setStatusType(Integer statusType) {
        this.statusType = statusType;
    }

    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getExceptionType() {
        return exceptionType;
    }
    public void setExceptionType(Integer exceptionType) {
        this.exceptionType = exceptionType;
    }
}
