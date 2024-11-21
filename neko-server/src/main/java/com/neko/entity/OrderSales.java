package com.neko.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderSales {
    private Long id;
    private String customerName;
    private String customerGender;
    private Integer customerAge;
    private String itemCategory;
    private String itemName;
    private BigDecimal itemPrice;
    private Integer itemQuantity;
    private String itemSku;
    private String deliveryAddress;
    private String contactPhone;
    private String tradePlatform;
    private String salesPerson;
    private LocalDateTime orderTime;
    private LocalDateTime deliveryTime;
    private String trackingNumber;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 