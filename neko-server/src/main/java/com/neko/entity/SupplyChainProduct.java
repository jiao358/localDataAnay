package com.neko.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("supply_chain_product")
public class SupplyChainProduct {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String productName;
    private String categoryCode;
    private BigDecimal wholesalePrice;
    private BigDecimal limitPrice;
    private String unitCode;
    private Integer quantity;
    private String supplierCompany;
    private String supplierContact;
    private String supplierPhone;
    private String supplierWechat;
    private String productCode;
    private String mainImage;
    
    @TableField(typeHandler = com.neko.config.handler.JsonListTypeHandler.class)
    private List<String> otherImages;
    
    private String materialUrl;
    private String description;
    private LocalDateTime batchTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    private String createBy;
    private String updateBy;
    
    @TableLogic
    private Boolean deleted;
    
    @Version
    private Integer version;
} 