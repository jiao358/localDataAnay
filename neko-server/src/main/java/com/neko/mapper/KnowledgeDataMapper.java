package com.neko.mapper;

import com.neko.entity.KnowledgeData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface KnowledgeDataMapper {
    List<KnowledgeData> selectList(
        @Param("contentName") String contentName,
        @Param("contentCategory") String contentCategory
    );
    KnowledgeData selectById(@Param("id") Long id);
    int insert(KnowledgeData knowledge);
    int update(KnowledgeData knowledge);
    int delete(@Param("id") Long id);
} 