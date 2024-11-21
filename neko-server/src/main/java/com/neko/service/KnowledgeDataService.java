package com.neko.service;

import com.neko.entity.KnowledgeData;
import java.util.List;

public interface KnowledgeDataService {
    List<KnowledgeData> listKnowledge(String contentName, String contentCategory);
    KnowledgeData getKnowledgeById(Long id);
    void saveKnowledge(KnowledgeData knowledge);
    void updateKnowledge(KnowledgeData knowledge);
    void deleteKnowledge(Long id);
    String uploadImage(byte[] imageData, String originalFilename);
} 