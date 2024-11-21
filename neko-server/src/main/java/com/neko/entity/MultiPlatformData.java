package com.neko.entity;

import com.neko.constant.PlatformTypeDict;
import lombok.Data;
import java.time.LocalDateTime;

@Data
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
    private String platformTypeName;

    public String getPlatformTypeName() {
        return PlatformTypeDict.getTypeName(this.platformType);
    }
} 