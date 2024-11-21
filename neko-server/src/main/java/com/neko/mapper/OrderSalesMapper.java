package com.neko.mapper;

import com.neko.entity.OrderSales;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderSalesMapper {
    List<OrderSales> selectList(
        @Param("customerName") String customerName,
        @Param("itemCategory") String itemCategory,
        @Param("tradePlatform") String tradePlatform,
        @Param("salesPerson") String salesPerson
    );
    OrderSales selectById(@Param("id") Long id);
    int insert(OrderSales order);
    int update(OrderSales order);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
} 