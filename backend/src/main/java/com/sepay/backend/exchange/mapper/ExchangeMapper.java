package com.sepay.backend.exchange.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.sepay.backend.exchange.dto.ExchangeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeMapper {

    List<ExchangeDTO> selectExchange(@Param("baseCode") Integer baseCode, @Param("targetCode") Integer targetCode);

    int updateExchange(ExchangeDTO rate);
}