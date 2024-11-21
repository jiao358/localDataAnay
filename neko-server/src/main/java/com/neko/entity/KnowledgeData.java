package com.neko.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class KnowledgeData {
    private Long id;
    private String contentName;
    private String contentCategory;
    private String contentUrl;
    private String contentSummary;
    private String processImage;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 