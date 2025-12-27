package com.tiny.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.enums.StatusEnum;
import com.tiny.common.exception.BusinessException;
import com.tiny.system.dto.SysRoleDTO;
import com.tiny.system.dto.SysRoleQueryDTO;
import com.tiny.system.entity.SysRole;
import com.tiny.system.entity.SysRoleDept;
import com.tiny.system.entity.SysRoleMenu;
import com.tiny.system.entity.SysUserRole;
import com.tiny.system.mapper.SysRoleDeptMapper;
import com.tiny.system.mapper.SysRoleMapper;
import com.tiny.system.mapper.SysRoleMenuMapper;
import com.tiny.system.mapper.SysUserRoleMapper;
import com.tiny.system.service.SysMenuService;
import com.tiny.system.service.SysRoleService;
import com.tiny.system.vo.SysRoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色管理服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysRoleDeptMapper roleDeptMapper;
    private final SysUserRoleMapper userRoleMapper;
    @Lazy
    private final SysMenuService menuService;

    @Override
    public PageResult<SysRoleVO> page(SysRoleQueryDTO queryDTO) {
        Page<SysRole> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getRoleName()), SysRole::getRoleName, queryDTO.getRoleName())
                .like(StrUtil.isNotBlank(queryDTO.getRoleKey()), SysRole::getRoleKey, queryDTO.getRoleKey())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysRole::getStatus, queryDTO.getStatus())
                .orderByAsc(SysRole::getSort);

        Page<SysRole> result = baseMapper.selectPage(page, wrapper);
        return PageResult.of(result, SysRole::toVO);
    }

    @Override
    public List<SysRoleVO> listAll() {
        List<SysRole> roles = this.list(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getStatus, StatusEnum.NORMAL.getCode())
                .orderByAsc(SysRole::getSort)
        );

        return roles.stream()
                .map(SysRole::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public SysRoleVO getDetail(Long roleId) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return toVOWithRelations(role);
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
        role.setStatus(StrUtil.isBlank(dto.getStatus()) ? StatusEnum.NORMAL.getCode() : dto.getStatus());

        this.save(role);

        // 保存角色菜单关联
        if (CollUtil.isNotEmpty(dto.getMenuIds())) {
            saveRoleMenus(role.getRoleId(), dto.getMenuIds());
        }

        // 保存角色部门关联（自定义数据权限）
        if (CollUtil.isNotEmpty(dto.getDeptIds())) {
            saveRoleDepts(role.getRoleId(), dto.getDeptIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleDTO dto) {
        SysRole role = this.getById(dto.getRoleId());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 合并检查角色名称和标识是否存在（优化：2次查询合并为1次）
        int existResult = checkRoleExists(dto.getRoleName(), dto.getRoleKey(), dto.getRoleId());
        if (existResult == 1) {
            throw new BusinessException("角色名称已存在");
        }
        if (existResult == 2) {
            throw new BusinessException("角色标识已存在");
        }

        BeanUtil.copyProperties(dto, role, "roleId");

        this.updateById(role);

        // 差异化更新角色菜单关联（优化：只处理变化的数据）
        updateRoleMenusDiff(dto.getRoleId(), dto.getMenuIds());

        // 差异化更新角色部门关联（优化：只处理变化的数据）
        updateRoleDeptsDiff(dto.getRoleId(), dto.getDeptIds());

        // 清除该角色关联用户的菜单缓存
        clearRoleRelatedUserMenuCache(dto.getRoleId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long roleId) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 检查角色是否已分配给用户
        Long userCount = userRoleMapper.selectCount(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getRoleId, roleId)
        );
        if (userCount > 0) {
            throw new BusinessException("该角色已分配给用户，无法删除");
        }

        // 先删除关联表，再删除主表（修复删除顺序）
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId));

        roleDeptMapper.delete(Wrappers.<SysRoleDept>lambdaQuery()
                .eq(SysRoleDept::getRoleId, roleId));

        this.removeById(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }

        // 检查角色是否已分配给用户
        Long userCount = userRoleMapper.selectCount(Wrappers.<SysUserRole>lambdaQuery()
                .in(SysUserRole::getRoleId, roleIds)
        );
        if (userCount > 0) {
            throw new BusinessException("选中的角色已分配给用户，无法删除");
        }

        // 先删除关联表，再删除主表（修复删除顺序）
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery()
                .in(SysRoleMenu::getRoleId, roleIds));

        roleDeptMapper.delete(Wrappers.<SysRoleDept>lambdaQuery()
                .in(SysRoleDept::getRoleId, roleIds));

        this.removeByIds(roleIds);
    }

    @Override
    public void updateStatus(Long roleId, String status) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
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
     * 批量检查角色名称和标识是否存在（合并查询优化）
     * @return 0-不存在冲突, 1-角色名称冲突, 2-角色标识冲突
     */
    private int checkRoleExists(String roleName, String roleKey, Long excludeRoleId) {
        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();
        wrapper.and(w -> w.eq(SysRole::getRoleName, roleName).or().eq(SysRole::getRoleKey, roleKey));
        if (excludeRoleId != null) {
            wrapper.ne(SysRole::getRoleId, excludeRoleId);
        }
        List<SysRole> existingRoles = this.list(wrapper);
        for (SysRole role : existingRoles) {
            if (roleName.equals(role.getRoleName())) {
                return 1;
            }
            if (roleKey.equals(role.getRoleKey())) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * 保存角色菜单关联（批量插入）
     */
    private void saveRoleMenus(Long roleId, List<Long> menuIds) {
        List<SysRoleMenu> roleMenus = menuIds.stream()
                .map(menuId -> {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menuId);
                    return roleMenu;
                })
                .collect(Collectors.toList());
        Db.saveBatch(roleMenus);
    }

    /**
     * 保存角色部门关联（批量插入）
     */
    private void saveRoleDepts(Long roleId, List<Long> deptIds) {
        List<SysRoleDept> roleDepts = deptIds.stream()
                .map(deptId -> {
                    SysRoleDept roleDept = new SysRoleDept();
                    roleDept.setRoleId(roleId);
                    roleDept.setDeptId(deptId);
                    return roleDept;
                })
                .collect(Collectors.toList());
        Db.saveBatch(roleDepts);
    }

    /**
     * 差异化更新角色菜单关联（只删除移除的，只添加新增的）
     */
    private void updateRoleMenusDiff(Long roleId, List<Long> newMenuIds) {
        // 查询当前已有的菜单ID
        Set<Long> existingMenuIds = roleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId)
                .select(SysRoleMenu::getMenuId)
        ).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());

        // 处理空列表情况
        if (CollUtil.isEmpty(newMenuIds)) {
            if (CollUtil.isNotEmpty(existingMenuIds)) {
                roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));
            }
            return;
        }

        Set<Long> newMenuIdSet = new HashSet<>(newMenuIds);

        // 计算需要删除的（存在于旧数据但不在新数据中）
        List<Long> toDelete = existingMenuIds.stream()
                .filter(id -> !newMenuIdSet.contains(id))
                .collect(Collectors.toList());

        // 计算需要新增的（存在于新数据但不在旧数据中）
        List<Long> toAdd = newMenuIds.stream()
                .filter(id -> !existingMenuIds.contains(id))
                .collect(Collectors.toList());

        // 批量删除
        if (CollUtil.isNotEmpty(toDelete)) {
            roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery()
                    .eq(SysRoleMenu::getRoleId, roleId)
                    .in(SysRoleMenu::getMenuId, toDelete));
        }

        // 批量新增
        if (CollUtil.isNotEmpty(toAdd)) {
            saveRoleMenus(roleId, toAdd);
        }
    }

    /**
     * 差异化更新角色部门关联（只删除移除的，只添加新增的）
     */
    private void updateRoleDeptsDiff(Long roleId, List<Long> newDeptIds) {
        // 查询当前已有的部门ID
        Set<Long> existingDeptIds = roleDeptMapper.selectList(Wrappers.<SysRoleDept>lambdaQuery()
                .eq(SysRoleDept::getRoleId, roleId)
                .select(SysRoleDept::getDeptId)
        ).stream().map(SysRoleDept::getDeptId).collect(Collectors.toSet());

        // 处理空列表情况
        if (CollUtil.isEmpty(newDeptIds)) {
            if (CollUtil.isNotEmpty(existingDeptIds)) {
                roleDeptMapper.delete(Wrappers.<SysRoleDept>lambdaQuery().eq(SysRoleDept::getRoleId, roleId));
            }
            return;
        }

        Set<Long> newDeptIdSet = new HashSet<>(newDeptIds);

        // 计算需要删除的
        List<Long> toDelete = existingDeptIds.stream()
                .filter(id -> !newDeptIdSet.contains(id))
                .collect(Collectors.toList());

        // 计算需要新增的
        List<Long> toAdd = newDeptIds.stream()
                .filter(id -> !existingDeptIds.contains(id))
                .collect(Collectors.toList());

        // 批量删除
        if (CollUtil.isNotEmpty(toDelete)) {
            roleDeptMapper.delete(Wrappers.<SysRoleDept>lambdaQuery()
                    .eq(SysRoleDept::getRoleId, roleId)
                    .in(SysRoleDept::getDeptId, toDelete));
        }

        // 批量新增
        if (CollUtil.isNotEmpty(toAdd)) {
            saveRoleDepts(roleId, toAdd);
        }
    }

    /**
     * 实体转VO（包含关联数据，用于详情查询）
     */
    private SysRoleVO toVOWithRelations(SysRole role) {
        SysRoleVO vo = role.toVO();

        // 查询角色菜单
        List<Long> menuIds = roleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getRoleId, role.getRoleId())
        ).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        vo.setMenuIds(menuIds);

        // 查询角色部门
        List<Long> deptIds = roleDeptMapper.selectList(Wrappers.<SysRoleDept>lambdaQuery()
                .eq(SysRoleDept::getRoleId, role.getRoleId())
        ).stream().map(SysRoleDept::getDeptId).collect(Collectors.toList());

        vo.setDeptIds(deptIds);

        return vo;
    }

    /**
     * 清除该角色关联用户的菜单缓存
     */
    private void clearRoleRelatedUserMenuCache(Long roleId) {
        // 查询该角色的所有用户
        List<Long> userIds = userRoleMapper.selectList(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getRoleId, roleId)
        ).stream().map(SysUserRole::getUserId).collect(Collectors.toList());

        // 清除这些用户的菜单缓存
        for (Long userId : userIds) {
            menuService.clearUserMenuCache(userId);
        }
    }
}
