package com.neko.mapper;

import com.neko.entity.TaobaoOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TaobaoOrderMapper {
    List<TaobaoOrder> selectList(
        @Param("itemId") String itemId,
        @Param("phone") String phone,
        @Param("receiverName") String receiverName,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
    );
    TaobaoOrder selectById(@Param("id") Long id);
    int insert(TaobaoOrder order);
    int update(TaobaoOrder order);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
} 