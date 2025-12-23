package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.system.dto.SysDeptDTO;
import com.tiny.system.dto.SysDeptQueryDTO;
import com.tiny.system.entity.SysDept;
import com.tiny.system.vo.SysDeptVO;

import java.util.List;

/**
 * 部门管理服务接口
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 查询部门列表（树形）
     */
    List<SysDeptVO> tree(SysDeptQueryDTO queryDTO);

    /**
     * 查询部门列表（平铺）
     */
    List<SysDeptVO> list(SysDeptQueryDTO queryDTO);

    /**
     * 查询部门详情
     */
    SysDeptVO getDetail(Long deptId);

    /**
     * 新增部门
     */
    void add(SysDeptDTO dto);

    /**
     * 修改部门
     */
    void update(SysDeptDTO dto);

    /**
     * 删除部门
     */
    void delete(Long deptId);

    /**
     * 修改部门状态
     */
    void updateStatus(Long deptId, String status);

    /**
     * 获取子部门ID列表（包含自身）
     */
    List<Long> getChildDeptIds(Long deptId);

    /**
     * 查询部门下拉树（排除指定部门及其子部门）
     */
    List<SysDeptVO> treeExclude(Long excludeDeptId);
}
