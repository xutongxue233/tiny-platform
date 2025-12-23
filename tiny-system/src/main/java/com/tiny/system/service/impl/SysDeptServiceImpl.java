package com.tiny.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.enums.StatusEnum;
import com.tiny.common.exception.BusinessException;
import com.tiny.system.dto.SysDeptDTO;
import com.tiny.system.dto.SysDeptQueryDTO;
import com.tiny.system.entity.SysDept;
import com.tiny.system.entity.SysUser;
import com.tiny.system.mapper.SysDeptMapper;
import com.tiny.system.mapper.SysUserMapper;
import com.tiny.system.service.SysDeptService;
import com.tiny.system.vo.SysDeptVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysUserMapper userMapper;

    @Override
    public List<SysDeptVO> tree(SysDeptQueryDTO queryDTO) {
        List<SysDept> depts = listDepts(queryDTO);
        return buildTree(depts);
    }

    @Override
    public List<SysDeptVO> list(SysDeptQueryDTO queryDTO) {
        List<SysDept> depts = listDepts(queryDTO);
        return depts.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public SysDeptVO getDetail(Long deptId) {
        SysDept dept = this.getById(deptId);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }
        SysDeptVO vo = toVO(dept);
        // 设置父部门名称
        if (dept.getParentId() != null && dept.getParentId() > 0) {
            SysDept parent = this.getById(dept.getParentId());
            if (parent != null) {
                vo.setParentName(parent.getDeptName());
            }
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysDeptDTO dto) {
        // 检查部门名称是否已存在
        if (checkDeptNameExists(dto.getDeptName(), dto.getParentId(), null)) {
            throw new BusinessException("部门名称已存在");
        }

        SysDept dept = BeanUtil.copyProperties(dto, SysDept.class, "sort", "status");
        dept.setSort(dto.getSort() != null ? dto.getSort() : 0);
        dept.setStatus(StrUtil.isBlank(dto.getStatus()) ? StatusEnum.NORMAL.getCode() : dto.getStatus());
        dept.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);

        // 设置祖级列表
        if (dept.getParentId() == 0L) {
            dept.setAncestors("0");
        } else {
            SysDept parent = this.getById(dept.getParentId());
            if (parent == null) {
                throw new BusinessException("父部门不存在");
            }
            if (StatusEnum.DISABLE.getCode().equals(parent.getStatus())) {
                throw new BusinessException("父部门已停用，不允许新增子部门");
            }
            dept.setAncestors(parent.getAncestors() + "," + parent.getDeptId());
        }

        this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDeptDTO dto) {
        SysDept dept = this.getById(dto.getDeptId());
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        // 检查部门名称是否已存在
        if (checkDeptNameExists(dto.getDeptName(), dto.getParentId(), dto.getDeptId())) {
            throw new BusinessException("部门名称已存在");
        }

        // 不能将自己设置为自己的父部门
        if (dto.getDeptId().equals(dto.getParentId())) {
            throw new BusinessException("上级部门不能是自己");
        }

        // 不能将自己的子部门设置为自己的父部门
        if (dto.getParentId() != null && dto.getParentId() != 0) {
            List<Long> childIds = getChildDeptIds(dto.getDeptId());
            if (childIds.contains(dto.getParentId())) {
                throw new BusinessException("上级部门不能是自己的子部门");
            }
        }

        String oldAncestors = dept.getAncestors();
        Long newParentId = dto.getParentId() != null ? dto.getParentId() : 0L;
        String newAncestors;

        if (newParentId == 0L) {
            newAncestors = "0";
        } else {
            SysDept parent = this.getById(newParentId);
            if (parent == null) {
                throw new BusinessException("父部门不存在");
            }
            newAncestors = parent.getAncestors() + "," + parent.getDeptId();
        }

        BeanUtil.copyProperties(dto, dept, "deptId", "ancestors");
        dept.setParentId(newParentId);
        dept.setAncestors(newAncestors);

        // 如果祖级列表变化，需要更新所有子部门的祖级列表
        if (!oldAncestors.equals(newAncestors)) {
            updateChildAncestors(dept.getDeptId(), oldAncestors, newAncestors);
        }

        this.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long deptId) {
        SysDept dept = this.getById(deptId);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        // 检查是否有子部门
        Long childCount = this.count(Wrappers.<SysDept>lambdaQuery()
                .eq(SysDept::getParentId, deptId));
        if (childCount > 0) {
            throw new BusinessException("存在子部门，不允许删除");
        }

        // 检查是否有用户
        Long userCount = userMapper.selectCount(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getDeptId, deptId));
        if (userCount > 0) {
            throw new BusinessException("部门下存在用户，不允许删除");
        }

        this.removeById(deptId);
    }

    @Override
    public void updateStatus(Long deptId, String status) {
        SysDept dept = this.getById(deptId);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }

        // 如果是停用，需要检查子部门状态
        if (StatusEnum.DISABLE.getCode().equals(status)) {
            Long normalChildCount = this.count(Wrappers.<SysDept>lambdaQuery()
                    .eq(SysDept::getParentId, deptId)
                    .eq(SysDept::getStatus, StatusEnum.NORMAL.getCode()));
            if (normalChildCount > 0) {
                throw new BusinessException("该部门包含未停用的子部门");
            }
        } else {
            // 如果是启用，需要检查父部门状态
            if (dept.getParentId() != null && dept.getParentId() > 0) {
                SysDept parent = this.getById(dept.getParentId());
                if (parent != null && StatusEnum.DISABLE.getCode().equals(parent.getStatus())) {
                    throw new BusinessException("父部门已停用，不允许启用");
                }
            }
        }

        dept.setStatus(status);
        this.updateById(dept);
    }

    @Override
    public List<Long> getChildDeptIds(Long deptId) {
        List<Long> result = new ArrayList<>();
        result.add(deptId);

        List<SysDept> children = this.list(Wrappers.<SysDept>lambdaQuery()
                .likeRight(SysDept::getAncestors, deptId));
        for (SysDept child : children) {
            result.add(child.getDeptId());
        }

        return result;
    }

    @Override
    public List<SysDeptVO> treeExclude(Long excludeDeptId) {
        List<SysDept> depts = this.list(Wrappers.<SysDept>lambdaQuery()
                .orderByAsc(SysDept::getSort));

        // 过滤掉指定部门及其子部门
        if (excludeDeptId != null) {
            List<Long> excludeIds = getChildDeptIds(excludeDeptId);
            depts = depts.stream()
                    .filter(d -> !excludeIds.contains(d.getDeptId()))
                    .collect(Collectors.toList());
        }

        return buildTree(depts);
    }

    /**
     * 查询部门列表
     */
    private List<SysDept> listDepts(SysDeptQueryDTO queryDTO) {
        LambdaQueryWrapper<SysDept> wrapper = Wrappers.lambdaQuery();
        if (queryDTO != null) {
            wrapper.like(StrUtil.isNotBlank(queryDTO.getDeptName()), SysDept::getDeptName, queryDTO.getDeptName())
                    .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysDept::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByAsc(SysDept::getSort);
        return this.list(wrapper);
    }

    /**
     * 构建部门树
     */
    private List<SysDeptVO> buildTree(List<SysDept> depts) {
        List<SysDeptVO> voList = depts.stream().map(this::toVO).collect(Collectors.toList());
        List<SysDeptVO> tree = new ArrayList<>();

        for (SysDeptVO vo : voList) {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                tree.add(vo);
            }
            for (SysDeptVO child : voList) {
                if (vo.getDeptId().equals(child.getParentId())) {
                    if (vo.getChildren() == null) {
                        vo.setChildren(new ArrayList<>());
                    }
                    vo.getChildren().add(child);
                }
            }
        }

        return tree;
    }

    /**
     * 检查部门名称是否存在
     */
    private boolean checkDeptNameExists(String deptName, Long parentId, Long excludeDeptId) {
        LambdaQueryWrapper<SysDept> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysDept::getDeptName, deptName)
                .eq(SysDept::getParentId, parentId != null ? parentId : 0L);
        if (excludeDeptId != null) {
            wrapper.ne(SysDept::getDeptId, excludeDeptId);
        }
        return this.count(wrapper) > 0;
    }

    /**
     * 更新子部门的祖级列表
     */
    private void updateChildAncestors(Long deptId, String oldAncestors, String newAncestors) {
        List<SysDept> children = this.list(Wrappers.<SysDept>lambdaQuery()
                .likeRight(SysDept::getAncestors, oldAncestors + "," + deptId));

        for (SysDept child : children) {
            String childAncestors = child.getAncestors();
            child.setAncestors(childAncestors.replaceFirst(oldAncestors, newAncestors));
            this.updateById(child);
        }
    }

    /**
     * 实体转VO
     */
    private SysDeptVO toVO(SysDept dept) {
        return dept.toVO();
    }
}
