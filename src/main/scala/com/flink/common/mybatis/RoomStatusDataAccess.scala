package com.flink.common.mybatis

import org.apache.ibatis.session.SqlSession


object  RoomStatusDataAccess {
  def insertDB(value: (String,  String, Long, Long, Long, Boolean, Integer,Integer,Integer)): Unit = {
    val roomStatus: RoomStatus = new RoomStatus
    roomStatus.setRoomId(value._1)
    roomStatus.setUserId(value._2)
    roomStatus.setTime(value._3)
    roomStatus.setStartTime(value._4)
    roomStatus.setEndTime(value._5)
    roomStatus.setRoomState(value._6)
    roomStatus.setCurrentUserAmount(value._7)
    roomStatus.setPeekUserAmount(value._8)
    roomStatus.setAccumulationUserAmount(value._9)

    val sqlSession: SqlSession = MybatisUtil.getSqlSession
    val rtcBillMapper: RoomStatusMapper = sqlSession.getMapper(classOf[RoomStatusMapper])

//    rtcBillMapper.deleteRoomStatus(roomStatus)  /// 不需要delete
    val i: Int = rtcBillMapper.insertRoomStatus(roomStatus)

    sqlSession.commit
    sqlSession.close
  }
}
