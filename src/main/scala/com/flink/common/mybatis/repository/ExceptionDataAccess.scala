package com.flink.common.mybatis.repository

import com.flink.common.mybatis.MybatisUtil
import com.flink.common.mybatis.entity.ExceptionPO
import com.flink.common.mybatis.mapper.{ExceptionMapper, OperationMapper}
import org.apache.ibatis.session.SqlSession
;

object  ExceptionDataAccess {
  def insertDB(value: (String, String, String, Integer, Long, Integer)): Unit = {
    val exception: ExceptionPO = new ExceptionPO

    exception.setAppId(value._1)
    exception.setRoomId(value._2)
    exception.setUserId(value._3)
    exception.setStatusType(value._4)
    exception.setTime(value._5)
    exception.setExceptionType(value._6)

    val sqlSession: SqlSession = MybatisUtil.getSqlSession
    val exceptionMapper: ExceptionMapper = sqlSession.getMapper(classOf[ExceptionMapper])

    val i: Int = exceptionMapper.insertException(exception)

    sqlSession.commit
    sqlSession.close
  }
}
