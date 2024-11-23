package com.neko.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neko.entity.KnowledgeData;
import com.neko.mapper.KnowledgeDataMapper;
import com.neko.service.KnowledgeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class KnowledgeDataServiceImpl implements KnowledgeDataService {

    @Autowired
    private KnowledgeDataMapper knowledgeMapper;

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    private static final String URL_PREFIX = "/uploads/";

    @Override
    public IPage<KnowledgeData> listKnowledge(Page<KnowledgeData> page, String contentName, String contentCategory) {
        LambdaQueryWrapper<KnowledgeData> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(contentName)) {
            queryWrapper.like(KnowledgeData::getContentName, contentName);
        }
        if (StringUtils.hasText(contentCategory)) {
            queryWrapper.eq(KnowledgeData::getContentCategory, contentCategory);
        }
        queryWrapper.orderByDesc(KnowledgeData::getCreateTime);
        return knowledgeMapper.selectPage(page, queryWrapper);
    }

    @Override
    public KnowledgeData getKnowledgeById(Long id) {
        return knowledgeMapper.selectById(id);
    }

    @Override
    @Transactional
    public void saveKnowledge(KnowledgeData knowledge) {
        knowledge.setStatus(1);
        knowledgeMapper.insert(knowledge);
    }

    @Override
    @Transactional
    public void updateKnowledge(KnowledgeData knowledge) {
        knowledgeMapper.updateById(knowledge);
    }

    @Override
    @Transactional
    public void deleteKnowledge(Long id) {
        // 获取知识点信息
        KnowledgeData knowledge = knowledgeMapper.selectById(id);
        if (knowledge != null && StringUtils.hasText(knowledge.getProcessImage())) {
            // 删除关联的图片文件
            try {
                String imagePath = knowledge.getProcessImage();
                if (imagePath.startsWith("/uploads/")) {
                    imagePath = uploadPath + imagePath.substring(8);
                    Files.deleteIfExists(Paths.get(imagePath));
                }
            } catch (Exception e) {
                // 记录错误但不影响删除操作
                e.printStackTrace();
            }
        }
        knowledgeMapper.deleteById(id);
    }

    @Override
    public String uploadImage(byte[] imageData, String originalFilename) {
        try {
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成新的文件名
            String extension = StringUtils.getFilenameExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + "." + extension;

            // 保存文件
            Path filePath = Paths.get(uploadPath, newFilename);
            Files.write(filePath, imageData);

            // 返回访问URL
            return URL_PREFIX + newFilename;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }
} 