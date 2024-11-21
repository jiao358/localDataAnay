package com.neko.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysDictType {
    private Long id;
    private String dictName;
    private String dictCode;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 