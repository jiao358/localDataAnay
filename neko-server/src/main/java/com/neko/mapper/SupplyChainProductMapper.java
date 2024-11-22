package com.neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neko.entity.SupplyChainProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SupplyChainProductMapper extends BaseMapper<SupplyChainProduct> {
    IPage<SupplyChainProduct> queryByCondition(Page<SupplyChainProduct> page, @Param("query") SupplyChainProduct query);
} 