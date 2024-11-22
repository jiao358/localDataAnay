package com.neko.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neko.common.response.Result;
import com.neko.entity.SupplyChainProduct;
import com.neko.service.SupplyChainProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/system/supply-chain")
public class SupplyChainProductController {

    @Resource
    private SupplyChainProductService supplyChainProductService;

    @GetMapping
    public Result<IPage<SupplyChainProduct>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            SupplyChainProduct query) {
        return Result.success(supplyChainProductService.queryPage(new Page<>(current, size), query));
    }

    @GetMapping("/{id}")
    public Result<SupplyChainProduct> getById(@PathVariable Long id) {
        return Result.success(supplyChainProductService.getById(id));
    }

    @PostMapping
    public Result<Boolean> create(@Valid @RequestBody SupplyChainProduct product) {
        return Result.success(supplyChainProductService.save(product));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @Valid @RequestBody SupplyChainProduct product) {
        product.setId(id);
        return Result.success(supplyChainProductService.updateById(product));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(supplyChainProductService.removeById(id));
    }

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, String> result = supplyChainProductService.uploadImage(file);
            return Result.success(result);
        } catch (Exception e) {
            log.error("File upload failed", e);
            return Result.error(500, "文件上传失败");
        }
    }
} 