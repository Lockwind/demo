package com.example.demo.dao;

import com.example.demo.domain.buoy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface Buoydao {
    //根据是时间显示最新的一条数据
    List<buoy> findByNewTime(@Param("time") LocalDateTime oldtime);
}
