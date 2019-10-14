package com.flink.common.mybatis.repository

import com.flink.common.mybatis.MybatisUtil
import com.flink.common.mybatis.entity.{UserInfo, RoomStatus}
import com.flink.common.mybatis.mapper.{UserInfoMapper, RoomStatusMapper}
import org.apache.ibatis.session.SqlSession
;

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

  def insertUserDB(value: (String,String, Integer, Long, String, String, String, String, String, String,Integer, String)): Unit = {
    val userInfo: UserInfo = new UserInfo
    userInfo.setUserId(value._1)
    userInfo.setRoomId(value._2)
    userInfo.setStatusType(value._3)
    userInfo.setTime(value._4)

    userInfo.setSdkv(value._5)
    userInfo.setAgent(value._6)
    userInfo.setDevice(value._7)
    userInfo.setSystem(value._8)
    userInfo.setNetwork(value._9)
    userInfo.setCpu(value._10)
    userInfo.setMem(value._11)
    userInfo.setRegion(value._12)

    val sqlSession: SqlSession = MybatisUtil.getSqlSession
    val userInfoMapper: UserInfoMapper = sqlSession.getMapper(classOf[UserInfoMapper])

    val i: Int = userInfoMapper.insertUserInfo(userInfo)

    sqlSession.commit
    sqlSession.close
  }
}
