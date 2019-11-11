package com.flink.common.mybatis.entity;

public class RoomStatus {
    private String appId;

    private String roomId;
    private String userId;
    private Long time;
    private Long startTime;
    private Long endTime;
    private Boolean roomState;
    private Integer currentUserAmount;
    private Integer peekUserAmount ;
    private Integer accumulationUserAmount;


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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Boolean getRoomState() {
        return roomState;
    }

    public void setRoomState(Boolean roomState) {
        this.roomState = roomState;
    }

    public Integer getCurrentUserAmount() {
        return currentUserAmount;
    }

    public void setCurrentUserAmount(Integer currentUserAmount) {
        this.currentUserAmount = currentUserAmount;
    }

    public Integer getPeekUserAmount() {
        return peekUserAmount;
    }

    public void setPeekUserAmount(Integer peekUserAmount) {
        this.peekUserAmount = peekUserAmount;
    }

    public Integer getAccumulationUserAmount() {
        return accumulationUserAmount;
    }

    public void setAccumulationUserAmount(Integer accumulationUserAmount) {
        this.accumulationUserAmount = accumulationUserAmount;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
