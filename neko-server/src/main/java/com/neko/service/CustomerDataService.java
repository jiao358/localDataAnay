package com.neko.service;

import com.neko.entity.CustomerData;
import java.util.List;

public interface CustomerDataService {
    List<CustomerData> listCustomers(
        String customerName, 
        String channel, 
        String contact, 
        String valueLevel,
        Integer minCommunication, 
        Integer maxCommunication
    );
    CustomerData getCustomerById(Long id);
    void saveCustomer(CustomerData customer);
    void updateCustomer(CustomerData customer);
    void updateCustomerStatus(Long id, Integer status);
    void incrementCommunicationCount(Long id);
} 