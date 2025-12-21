package com.tiny.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.constant.CommonConstants;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.system.dto.SysRoleDTO;
import com.tiny.system.dto.SysRoleQueryDTO;
import com.tiny.system.entity.SysRole;
import com.tiny.system.entity.SysRoleMenu;
import com.tiny.system.entity.SysUserRole;
import com.tiny.system.mapper.SysRoleMapper;
import com.tiny.system.mapper.SysRoleMenuMapper;
import com.tiny.system.mapper.SysUserRoleMapper;
import com.tiny.system.service.SysRoleService;
import com.tiny.system.vo.SysRoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysUserRoleMapper userRoleMapper;

    @Override
    public PageResult<SysRoleVO> page(SysRoleQueryDTO queryDTO) {
        Page<SysRole> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getRoleName()), SysRole::getRoleName, queryDTO.getRoleName())
                .like(StrUtil.isNotBlank(queryDTO.getRoleKey()), SysRole::getRoleKey, queryDTO.getRoleKey())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysRole::getStatus, queryDTO.getStatus())
                .orderByAsc(SysRole::getSort);

        Page<SysRole> result = baseMapper.selectPage(page, wrapper);

        List<SysRoleVO> voList = result.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return new PageResult<>(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public List<SysRoleVO> listAll() {
        List<SysRole> roles = this.list(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getStatus, CommonConstants.STATUS_NORMAL)
                .orderByAsc(SysRole::getSort)
        );

        return roles.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public SysRoleVO getDetail(Long roleId) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return toVO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysRoleDTO dto) {
        // 检查角色名称是否已存在
        if (checkRoleNameExists(dto.getRoleName(), null)) {
            throw new BusinessException("角色名称已存在");
        }

        // 检查角色标识是否已存在
        if (checkRoleKeyExists(dto.getRoleKey(), null)) {
            throw new BusinessException("角色标识已存在");
        }

        SysRole role = BeanUtil.copyProperties(dto, SysRole.class, "sort", "dataScope", "status");
        role.setSort(dto.getSort() != null ? dto.getSort() : 0);
        role.setDataScope(StrUtil.isBlank(dto.getDataScope()) ? "1" : dto.getDataScope());
        role.setStatus(StrUtil.isBlank(dto.getStatus()) ? CommonConstants.STATUS_NORMAL : dto.getStatus());

        this.save(role);

        // 保存角色菜单关联
        if (CollUtil.isNotEmpty(dto.getMenuIds())) {
            saveRoleMenus(role.getRoleId(), dto.getMenuIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleDTO dto) {
        SysRole role = this.getById(dto.getRoleId());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 不允许修改admin角色的角色标识
        if ("admin".equals(role.getRoleKey()) && !"admin".equals(dto.getRoleKey())) {
            throw new BusinessException("不允许修改超级管理员角色标识");
        }

        // 检查角色名称是否已存在
        if (checkRoleNameExists(dto.getRoleName(), dto.getRoleId())) {
            throw new BusinessException("角色名称已存在");
        }

        // 检查角色标识是否已存在
        if (checkRoleKeyExists(dto.getRoleKey(), dto.getRoleId())) {
            throw new BusinessException("角色标识已存在");
        }

        BeanUtil.copyProperties(dto, role, "roleId");

        this.updateById(role);

        // 更新角色菜单关联
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getRoleId, dto.getRoleId()));
        if (CollUtil.isNotEmpty(dto.getMenuIds())) {
            saveRoleMenus(dto.getRoleId(), dto.getMenuIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long roleId) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 不允许删除admin角色
        if ("admin".equals(role.getRoleKey())) {
            throw new BusinessException("不允许删除超级管理员角色");
        }

        // 检查角色是否已分配给用户
        Long userCount = userRoleMapper.selectCount(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getRoleId, roleId)
        );
        if (userCount > 0) {
            throw new BusinessException("该角色已分配给用户，无法删除");
        }

        this.removeById(roleId);

        // 删除角色菜单关联
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }

        // 检查是否包含admin角色
        List<SysRole> roles = this.listByIds(roleIds);
        for (SysRole role : roles) {
            if ("admin".equals(role.getRoleKey())) {
                throw new BusinessException("不允许删除超级管理员角色");
            }
        }

        // 检查角色是否已分配给用户
        Long userCount = userRoleMapper.selectCount(Wrappers.<SysUserRole>lambdaQuery()
                .in(SysUserRole::getRoleId, roleIds)
        );
        if (userCount > 0) {
            throw new BusinessException("选中的角色已分配给用户，无法删除");
        }

        this.removeByIds(roleIds);

        // 删除角色菜单关联
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery()
                .in(SysRoleMenu::getRoleId, roleIds));
    }

    @Override
    public void updateStatus(Long roleId, String status) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 不允许停用admin角色
        if ("admin".equals(role.getRoleKey()) && CommonConstants.STATUS_DISABLE.equals(status)) {
            throw new BusinessException("不允许停用超级管理员角色");
        }

        role.setStatus(status);
        this.updateById(role);
    }

    /**
     * 检查角色名称是否存在
     */
    private boolean checkRoleNameExists(String roleName, Long excludeRoleId) {
        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRole::getRoleName, roleName);
        if (excludeRoleId != null) {
            wrapper.ne(SysRole::getRoleId, excludeRoleId);
        }
        return this.count(wrapper) > 0;
    }

    /**
     * 检查角色标识是否存在
     */
    private boolean checkRoleKeyExists(String roleKey, Long excludeRoleId) {
        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRole::getRoleKey, roleKey);
        if (excludeRoleId != null) {
            wrapper.ne(SysRole::getRoleId, excludeRoleId);
        }
        return this.count(wrapper) > 0;
    }

    /**
     * 保存角色菜单关联
     */
    private void saveRoleMenus(Long roleId, List<Long> menuIds) {
        for (Long menuId : menuIds) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    /**
     * 实体转VO（包含关联数据）
     */
    private SysRoleVO toVO(SysRole role) {
        SysRoleVO vo = role.toVO();

        // 查询角色菜单
        List<Long> menuIds = roleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getRoleId, role.getRoleId())
        ).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        vo.setMenuIds(menuIds);

        return vo;
    }
}
