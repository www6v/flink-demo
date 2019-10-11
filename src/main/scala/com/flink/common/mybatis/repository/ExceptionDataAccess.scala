package com.flink.common.mybatis.repository

import com.flink.common.mybatis.MybatisUtil
import com.flink.common.mybatis.entity.ExceptionPO
import com.flink.common.mybatis.mapper.{ExceptionMapper, OperationMapper}
import org.apache.ibatis.session.SqlSession
;

object  ExceptionDataAccess {
  def insertDB(value: (String, String, Integer, Long, Integer)): Unit = {
    val exception: ExceptionPO = new ExceptionPO
    exception.setRoomId(value._1)
    exception.setUserId(value._2)
    exception.setStatusType(value._3)
    exception.setTime(value._4)
    exception.setExceptionType(value._5)


    val sqlSession: SqlSession = MybatisUtil.getSqlSession
    val exceptionMapper: ExceptionMapper = sqlSession.getMapper(classOf[ExceptionMapper])

    val i: Int = exceptionMapper.insertException(exception)

    sqlSession.commit
    sqlSession.close
  }
}
