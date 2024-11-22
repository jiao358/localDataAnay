package com.neko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neko.entity.SupplyChainProduct;
import com.neko.mapper.SupplyChainProductMapper;
import com.neko.service.SupplyChainProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class SupplyChainProductServiceImpl extends ServiceImpl<SupplyChainProductMapper, SupplyChainProduct> implements SupplyChainProductService {
    
    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public IPage<SupplyChainProduct> queryPage(Page<SupplyChainProduct> page, SupplyChainProduct query) {
        LambdaQueryWrapper<SupplyChainProduct> wrapper = new LambdaQueryWrapper<>();
        
        if (query != null) {
            if (StringUtils.hasText(query.getProductName())) {
                wrapper.like(SupplyChainProduct::getProductName, query.getProductName());
            }
            if (StringUtils.hasText(query.getCategoryCode())) {
                wrapper.eq(SupplyChainProduct::getCategoryCode, query.getCategoryCode());
            }
            if (StringUtils.hasText(query.getSupplierCompany())) {
                wrapper.like(SupplyChainProduct::getSupplierCompany, query.getSupplierCompany());
            }
        }
        
        wrapper.orderByDesc(SupplyChainProduct::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Map<String, String> uploadImage(MultipartFile file) throws Exception {
        String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        String filePath = "supply-chain/" + fileName;
        
        // 确保目录存在
        Files.createDirectories(Paths.get(uploadPath, "supply-chain"));
        
        // 保存文件
        Files.copy(file.getInputStream(), Paths.get(uploadPath, filePath));
        
        Map<String, String> result = new HashMap<>();
        result.put("url", filePath);
        return result;
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SupplyChainProduct product) {
        if (product.getProductCode() == null) {
            product.setProductCode(generateProductCode());
        }
        return super.save(product);
    }
    
    private String generateProductCode() {
        return "SP" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }
} 