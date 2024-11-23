package com.neko.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neko.entity.KnowledgeData;
import java.util.List;

public interface KnowledgeDataService {
    IPage<KnowledgeData> listKnowledge(Page<KnowledgeData> page, String contentName, String contentCategory, String contentSummary);
    KnowledgeData getKnowledgeById(Long id);
    void saveKnowledge(KnowledgeData knowledge);
    void updateKnowledge(KnowledgeData knowledge);
    void deleteKnowledge(Long id);
    String uploadImage(byte[] imageData, String originalFilename);
} 