package com.neko.controller;

import com.neko.entity.CustomerData;
import com.neko.service.CustomerDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerDataController {
    
    @Autowired
    private CustomerDataService customerService;

    @GetMapping
    public ResponseEntity<?> list(
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String channel,
        @RequestParam(required = false) String contact,
        @RequestParam(required = false) String valueLevel,
        @RequestParam(required = false) Integer minCommunication,
        @RequestParam(required = false) Integer maxCommunication
    ) {
        try {
            List<CustomerData> customers = customerService.listCustomers(
                customerName, channel, contact, valueLevel, 
                minCommunication, maxCommunication
            );
            Map<String, Object> response = new HashMap<>();
            response.put("records", customers);
            response.put("total", customers.size());
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
            CustomerData customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CustomerData customer) {
        try {
            customerService.saveCustomer(customer);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CustomerData customer) {
        try {
            customer.setId(id);
            customerService.updateCustomer(customer);
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
            customerService.updateCustomerStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}/communication")
    public ResponseEntity<?> incrementCommunication(@PathVariable Long id) {
        try {
            customerService.incrementCommunicationCount(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 