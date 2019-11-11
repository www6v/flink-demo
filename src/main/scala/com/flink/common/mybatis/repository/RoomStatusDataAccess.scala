package com.flink.common.mybatis.repository

import com.flink.common.entry.Constants
import com.flink.common.mybatis.MybatisUtil
import com.flink.common.mybatis.entity.{UserDetail, UserInfo, RoomStatus}
import com.flink.common.mybatis.mapper.{UserDetailMapper, UserInfoMapper, RoomStatusMapper}
import org.apache.ibatis.session.SqlSession

object  RoomStatusDataAccess {
  def insertUserDetailDB(value: (String, String, String, Integer, Long, String, String, String, String, String, String, Integer, String)) = {
    val statusType = value._3
    if(statusType.equals(Constants.STATUS_TYPE_INIT) ||
      statusType.equals(Constants.STATUS_TYPE_LEAVE) ) {
      val userDetail: UserDetail = new UserDetail
      userDetail.setAppId(value._1)
      userDetail.setUserId(value._2)
      userDetail.setRoomId(value._3)
      userDetail.setStatusType(value._4)
      userDetail.setTime(value._5)

      userDetail.setSdkv(value._6)
      userDetail.setAgent(value._7)
      userDetail.setDevice(value._8)
      userDetail.setSystem(value._9)
      userDetail.setNetwork(value._10)
      userDetail.setCpu(value._11)
      userDetail.setMem(value._12)
      userDetail.setRegion(value._13)

      val sqlSession: SqlSession = MybatisUtil.getSqlSession
      val userDetailMapper: UserDetailMapper = sqlSession.getMapper(classOf[UserDetailMapper])
      val i: Int = userDetailMapper.insertUserDetail(userDetail)

      sqlSession.commit
      sqlSession.close
    }
  }

  def insertDB(value: (String,String,  String, Long, Long, Long, Boolean, Integer,Integer,Integer)): Unit = {
    val roomStatus: RoomStatus = new RoomStatus
    roomStatus.setAppId(value._1)
    roomStatus.setRoomId(value._2)
    roomStatus.setUserId(value._3)
    roomStatus.setTime(value._4)
    roomStatus.setStartTime(value._5)
    roomStatus.setEndTime(value._6)
    roomStatus.setRoomState(value._7)
    roomStatus.setCurrentUserAmount(value._8)
    roomStatus.setPeekUserAmount(value._9)
    roomStatus.setAccumulationUserAmount(value._10)

    val sqlSession: SqlSession = MybatisUtil.getSqlSession
    val rtcBillMapper: RoomStatusMapper = sqlSession.getMapper(classOf[RoomStatusMapper])

//    rtcBillMapper.deleteRoomStatus(roomStatus)  /// 不需要delete
    val i: Int = rtcBillMapper.insertRoomStatus(roomStatus)

    sqlSession.commit
    sqlSession.close
  }

  def insertUserInfoDB(value: (String,String,String, Integer, Long, String, String, String, String, String, String,Integer, String)): Unit = {
    val userInfo: UserInfo = new UserInfo
    userInfo.setAppId(value._1)
    userInfo.setUserId(value._2)
    userInfo.setRoomId(value._3)
    userInfo.setStatusType(value._4)
    userInfo.setTime(value._5)

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
