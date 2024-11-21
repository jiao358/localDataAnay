package com.neko.service.impl;

import com.neko.entity.TaobaoOrder;
import com.neko.mapper.TaobaoOrderMapper;
import com.neko.service.TaobaoOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TaobaoOrderServiceImpl implements TaobaoOrderService {

    @Autowired
    private TaobaoOrderMapper orderMapper;

    @Override
    public List<TaobaoOrder> listOrders(String itemId, String phone, String receiverName, 
                                      BigDecimal minPrice, BigDecimal maxPrice) {
        return orderMapper.selectList(itemId, phone, receiverName, minPrice, maxPrice);
    }

    @Override
    public TaobaoOrder getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public void saveOrder(TaobaoOrder order) {
        orderMapper.insert(order);
    }

    @Override
    public void updateOrder(TaobaoOrder order) {
        orderMapper.update(order);
    }

    @Override
    public void updateOrderStatus(Long id, Integer status) {
        orderMapper.updateStatus(id, status);
    }
} 