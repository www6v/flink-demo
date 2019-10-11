package com.flink.common.mybatis.mapper;

import com.flink.common.mybatis.entity.ExceptionPO;

public interface ExceptionMapper {
    int insertException(ExceptionPO exception);
}
