package com.neko.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysDictData {
    private Long id;
    private String dictCode;
    private String dictLabel;
    private String dictValue;
    private Integer sortOrder;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 