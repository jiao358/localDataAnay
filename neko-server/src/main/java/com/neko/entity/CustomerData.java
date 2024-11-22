package com.neko.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("customer_data")
public class CustomerData {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String customerName;
    private String customerPhone;
    private String customerWechat;
    private String customerAddress;
    private String remark;
    
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