package com.flink.common.mybatis.repository

import com.flink.common.entry.Constants
import com.flink.common.mybatis.MybatisUtil
import com.flink.common.mybatis.entity.{UserDetail, UserInfo, RoomStatus}
import com.flink.common.mybatis.mapper.{UserDetailMapper, UserInfoMapper, RoomStatusMapper}
import org.apache.ibatis.session.SqlSession

object  RoomStatusDataAccess {
  def insertUserDetailDB(value: (String, String, Integer, Long, String, String, String, String, String, String, Integer, String)) = {
    val statusType = value._3
    if(statusType.equals(Constants.STATUS_TYPE_INIT ||
      Constants.STATUS_TYPE_LEAVE)) {
      val userDetail: UserDetail = new UserDetail
      userDetail.setUserId(value._1)
      userDetail.setRoomId(value._2)
      userDetail.setStatusType(value._3)
      userDetail.setTime(value._4)

      userDetail.setSdkv(value._5)
      userDetail.setAgent(value._6)
      userDetail.setDevice(value._7)
      userDetail.setSystem(value._8)
      userDetail.setNetwork(value._9)
      userDetail.setCpu(value._10)
      userDetail.setMem(value._11)
      userDetail.setRegion(value._12)

      val sqlSession: SqlSession = MybatisUtil.getSqlSession
      val userDetailMapper: UserDetailMapper = sqlSession.getMapper(classOf[UserDetailMapper])
      val i: Int = userDetailMapper.insertUserDetail(userDetail)

      sqlSession.commit
      sqlSession.close
    }
  }

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

  def insertUserInfoDB(value: (String,String, Integer, Long, String, String, String, String, String, String,Integer, String)): Unit = {
    val userInfo: UserInfo = new UserInfo
    userInfo.setUserId(value._1)
    userInfo.setRoomId(value._2)
    userInfo.setStatusType(value._3)
    userInfo.setTime(value._4)

//    userInfo.setSdkv(value._5)
//    userInfo.setAgent(value._6)
//    userInfo.setDevice(value._7)
//    userInfo.setSystem(value._8)
//    userInfo.setNetwork(value._9)
//    userInfo.setCpu(value._10)
//    userInfo.setMem(value._11)
//    userInfo.setRegion(value._12)

    val sqlSession: SqlSession = MybatisUtil.getSqlSession
    val userInfoMapper: UserInfoMapper = sqlSession.getMapper(classOf[UserInfoMapper])

    val i: Int = userInfoMapper.insertUserInfo(userInfo)

    sqlSession.commit
    sqlSession.close
  }
}
