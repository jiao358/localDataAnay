package com.neko.mapper;

import com.neko.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> selectList(@Param("keyword") String keyword);
    SysRole selectById(@Param("id") Long id);
    SysRole selectByCode(@Param("roleCode") String roleCode);
    int insert(SysRole role);
    int update(SysRole role);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
} 