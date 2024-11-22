package com.neko.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neko.entity.SupplyChainProduct;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface SupplyChainProductService extends IService<SupplyChainProduct> {
    IPage<SupplyChainProduct> queryPage(Page<SupplyChainProduct> page, SupplyChainProduct query);
    
    Map<String, String> uploadImage(MultipartFile file) throws Exception;
} 