package com.flink.common.mybatis.mapper;

import com.flink.common.mybatis.entity.RoomStatus;

public interface RoomStatusMapper {
//    RoomStatus selectBill(int i);
    int insertRoomStatus(RoomStatus rb);
    int deleteRoomStatus(RoomStatus rb);
}
