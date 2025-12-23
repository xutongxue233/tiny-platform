package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.system.dto.SysMenuDTO;
import com.tiny.system.dto.SysMenuQueryDTO;
import com.tiny.system.entity.SysMenu;
import com.tiny.system.vo.RouterVO;
import com.tiny.system.vo.SysMenuVO;

import java.util.List;

/**
 * 菜单管理服务接口
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 查询菜单列表
     */
    List<SysMenuVO> listAll(SysMenuQueryDTO queryDTO);

    /**
     * 查询菜单树
     */
    List<SysMenuVO> tree(SysMenuQueryDTO queryDTO);

    /**
     * 查询菜单详情
     */
    SysMenuVO getDetail(Long menuId);

    /**
     * 新增菜单
     */
    void add(SysMenuDTO dto);

    /**
     * 修改菜单
     */
    void update(SysMenuDTO dto);

    /**
     * 删除菜单
     */
    void delete(Long menuId);

    /**
     * 获取当前用户的路由菜单树
     */
    List<RouterVO> getUserRouters();
}
