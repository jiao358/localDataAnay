package com.neko.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysPermission {
    private Long id;
    private String permissionName;
    private String permissionCode;
    private Integer permissionType;
    private Long parentId;
    private String path;
    private String component;
    private String icon;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 