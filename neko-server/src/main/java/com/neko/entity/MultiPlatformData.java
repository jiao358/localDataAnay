package com.neko.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("multi_platform_data")
public class MultiPlatformData {
    private Long id;
    private String accountName;
    private String accountPassword;
    private String securityInfo;
    private String paymentPassword;
    private String platformType;
    private String platformUrl;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 非数据库字段，用于前端显示
    @TableField(exist = false)
    private String platformTypeName;

    public String getPlatformTypeName() {
        // 从字典服务获取平台类型名称
        return platformType;
    }
} 