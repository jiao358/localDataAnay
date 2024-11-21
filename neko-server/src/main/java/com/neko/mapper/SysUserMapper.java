package com.neko.mapper;

import com.neko.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper {
    List<SysUser> selectList(@Param("keyword") String keyword);
    SysUser selectById(@Param("id") Long id);
    SysUser findByUsername(@Param("username") String username);
    int insert(SysUser user);
    int update(SysUser user);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
} 