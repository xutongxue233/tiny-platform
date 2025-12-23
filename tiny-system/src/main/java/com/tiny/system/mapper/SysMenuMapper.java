package com.tiny.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单Mapper
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户ID查询菜单列表
     */
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID列表查询菜单列表
     */
    List<SysMenu> selectMenusByRoleIds(@Param("roleIds") List<Long> roleIds);
}
