package com.neko.mapper;

import com.neko.entity.SysRole;
import com.neko.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserRoleMapper {
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
    int deleteByUserId(@Param("userId") Long userId);
    int batchInsert(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
} 