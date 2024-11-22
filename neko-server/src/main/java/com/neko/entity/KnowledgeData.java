package com.neko.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class KnowledgeData implements Serializable {
    private Integer status;
    private String processImage;
    // ... 其他字段
    private Long id;
    private String contentName;
    private String contentCategory;
    private String contentUrl;
    private String contentSummary;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

} 