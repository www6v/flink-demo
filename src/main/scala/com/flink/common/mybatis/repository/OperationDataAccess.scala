package com.flink.common.mybatis.repository

import com.flink.common.mybatis.MybatisUtil
import com.flink.common.mybatis.entity.{Operation, RoomStatus, UserInfo}
import com.flink.common.mybatis.mapper.{OperationMapper, RoomStatusMapper, UserInfoMapper}
import org.apache.ibatis.session.SqlSession
;

object  OperationDataAccess {
  def insertDB(value: (String, String, String, Integer, Long, Integer)): Unit = {
    val operation: Operation = new Operation

    operation.setAppId( value._1)
    operation.setRoomId(value._2)
    operation.setUserId(value._3)
    operation.setStatusType(value._4)
    operation.setTime(value._5)
    operation.setOpertionType(value._6)

    val sqlSession: SqlSession = MybatisUtil.getSqlSession
    val operationMapper: OperationMapper = sqlSession.getMapper(classOf[OperationMapper])

    val i: Int = operationMapper.insertOperation(operation)

    sqlSession.commit
    sqlSession.close
  }
}
