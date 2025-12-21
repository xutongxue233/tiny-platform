package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysRoleDTO;
import com.tiny.system.dto.SysRoleQueryDTO;
import com.tiny.system.entity.SysRole;
import com.tiny.system.vo.SysRoleVO;

import java.util.List;

/**
 * 角色管理服务接口
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     */
    PageResult<SysRoleVO> page(SysRoleQueryDTO queryDTO);

    /**
     * 查询所有角色列表（用于下拉选择）
     */
    List<SysRoleVO> listAll();

    /**
     * 查询角色详情
     */
    SysRoleVO getDetail(Long roleId);

    /**
     * 新增角色
     */
    void add(SysRoleDTO dto);

    /**
     * 修改角色
     */
    void update(SysRoleDTO dto);

    /**
     * 删除角色
     */
    void delete(Long roleId);

    /**
     * 批量删除角色
     */
    void deleteBatch(List<Long> roleIds);

    /**
     * 修改角色状态
     */
    void updateStatus(Long roleId, String status);
}
