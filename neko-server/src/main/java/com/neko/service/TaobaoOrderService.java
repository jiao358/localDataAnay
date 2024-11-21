package com.neko.service;

import com.neko.entity.TaobaoOrder;
import java.math.BigDecimal;
import java.util.List;

public interface TaobaoOrderService {
    List<TaobaoOrder> listOrders(String itemId, String phone, String receiverName, 
                                BigDecimal minPrice, BigDecimal maxPrice);
    TaobaoOrder getOrderById(Long id);
    void saveOrder(TaobaoOrder order);
    void updateOrder(TaobaoOrder order);
    void updateOrderStatus(Long id, Integer status);
} 