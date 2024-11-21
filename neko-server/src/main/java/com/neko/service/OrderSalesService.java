package com.neko.service;

import com.neko.entity.OrderSales;
import java.util.List;

public interface OrderSalesService {
    List<OrderSales> listOrders(String customerName, String itemCategory, 
                               String tradePlatform, String salesPerson);
    OrderSales getOrderById(Long id);
    void saveOrder(OrderSales order);
    void updateOrder(OrderSales order);
    void updateOrderStatus(Long id, Integer status);
} 