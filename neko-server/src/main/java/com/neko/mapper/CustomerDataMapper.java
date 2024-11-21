package com.neko.mapper;

import com.neko.entity.CustomerData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CustomerDataMapper {
    List<CustomerData> selectList(
        @Param("customerName") String customerName,
        @Param("channel") String channel,
        @Param("contact") String contact,
        @Param("valueLevel") String valueLevel,
        @Param("minCommunication") Integer minCommunication,
        @Param("maxCommunication") Integer maxCommunication
    );
    CustomerData selectById(@Param("id") Long id);
    int insert(CustomerData customer);
    int update(CustomerData customer);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int incrementCommunicationCount(@Param("id") Long id);
} 