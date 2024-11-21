package com.neko.controller;

import com.neko.entity.OrderSales;
import com.neko.service.OrderSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class OrderSalesController {
    
    @Autowired
    private OrderSalesService orderService;

    @GetMapping
    public ResponseEntity<?> list(
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String itemCategory,
        @RequestParam(required = false) String tradePlatform,
        @RequestParam(required = false) String salesPerson
    ) {
        try {
            List<OrderSales> orders = orderService.listOrders(customerName, itemCategory, 
                                                            tradePlatform, salesPerson);
            Map<String, Object> response = new HashMap<>();
            response.put("records", orders);
            response.put("total", orders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            OrderSales order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody OrderSales order) {
        try {
            orderService.saveOrder(order);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody OrderSales order) {
        try {
            order.setId(id);
            orderService.updateOrder(order);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 