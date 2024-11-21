package com.neko.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TaobaoOrder {
    private Long id;
    private String itemId;
    private BigDecimal itemPrice;
    private String itemName;
    private String receiverAddress;
    private String receiverName;
    private String receiverNickname;
    private LocalDateTime tradeTime;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 