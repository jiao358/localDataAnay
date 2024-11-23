package com.neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neko.entity.MultiPlatformData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MultiPlatformDataMapper extends BaseMapper<MultiPlatformData> {
} 