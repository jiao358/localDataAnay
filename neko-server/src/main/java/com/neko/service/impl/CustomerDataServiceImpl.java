package com.neko.service.impl;

import com.neko.entity.CustomerData;
import com.neko.mapper.CustomerDataMapper;
import com.neko.service.CustomerDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerDataServiceImpl implements CustomerDataService {

    @Autowired
    private CustomerDataMapper customerMapper;

    @Override
    public List<CustomerData> listCustomers(
        String customerName, 
        String channel, 
        String contact, 
        String valueLevel,
        Integer minCommunication,  // 添加最少沟通次数参数
        Integer maxCommunication   // 添加最多沟通次数参数
    ) {
        return customerMapper.selectList(customerName, channel, contact, valueLevel, 
                                      minCommunication, maxCommunication);
    }

    @Override
    public CustomerData getCustomerById(Long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public void saveCustomer(CustomerData customer) {
        customerMapper.insert(customer);
    }

    @Override
    public void updateCustomer(CustomerData customer) {
        customerMapper.update(customer);
    }

    @Override
    public void updateCustomerStatus(Long id, Integer status) {
        customerMapper.updateStatus(id, status);
    }

    @Override
    public void incrementCommunicationCount(Long id) {
        customerMapper.incrementCommunicationCount(id);
    }
} 