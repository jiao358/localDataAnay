package com.neko.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerData {
    private Long id;
    private String customerName;
    private String channel;
    private String contact;
    private String address;
    private Integer communicationCount;
    private String lastIntention;
    private String valueLevel;
    private String itemCategory;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 