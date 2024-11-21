package com.neko.service.impl;

import com.neko.entity.OrderSales;
import com.neko.mapper.OrderSalesMapper;
import com.neko.service.OrderSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderSalesServiceImpl implements OrderSalesService {

    @Autowired
    private OrderSalesMapper orderMapper;

    @Override
    public List<OrderSales> listOrders(String customerName, String itemCategory, 
                                     String tradePlatform, String salesPerson) {
        return orderMapper.selectList(customerName, itemCategory, tradePlatform, salesPerson);
    }

    @Override
    public OrderSales getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public void saveOrder(OrderSales order) {
        orderMapper.insert(order);
    }

    @Override
    public void updateOrder(OrderSales order) {
        orderMapper.update(order);
    }

    @Override
    public void updateOrderStatus(Long id, Integer status) {
        orderMapper.updateStatus(id, status);
    }
} 