package com.flink.common.mybatis.repository

import com.flink.common.mybatis.MybatisUtil
import com.flink.common.mybatis.entity.{Operation, RoomStatus, UserInfo}
import com.flink.common.mybatis.mapper.{OperationMapper, RoomStatusMapper, UserInfoMapper}
import org.apache.ibatis.session.SqlSession
;

object  OperationDataAccess {
  def insertDB(value: (String, String, Integer, Long, Integer)): Unit = {
    val operation: Operation = new Operation
    operation.setRoomId(value._1)
    operation.setUserId(value._2)
    operation.setStatusType(value._3)
    operation.setTime(value._4)
    operation.setOpertionType(value._5)

    val sqlSession: SqlSession = MybatisUtil.getSqlSession
    val operationMapper: OperationMapper = sqlSession.getMapper(classOf[OperationMapper])

    val i: Int = operationMapper.insertOperation(operation)

    sqlSession.commit
    sqlSession.close
  }
}
