package com.tiny.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.constant.CommonConstants;
import com.tiny.common.exception.BusinessException;
import com.tiny.core.utils.LoginUserUtil;
import com.tiny.system.dto.SysMenuDTO;
import com.tiny.system.dto.SysMenuQueryDTO;
import com.tiny.system.entity.SysMenu;
import com.tiny.system.entity.SysRoleMenu;
import com.tiny.system.entity.SysUser;
import com.tiny.system.mapper.SysMenuMapper;
import com.tiny.system.mapper.SysRoleMenuMapper;
import com.tiny.system.mapper.SysUserMapper;
import com.tiny.system.service.SysMenuService;
import com.tiny.system.vo.RouterVO;
import com.tiny.system.vo.SysMenuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单管理服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysUserMapper userMapper;

    @Override
    public List<SysMenuVO> listAll(SysMenuQueryDTO queryDTO) {
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getMenuName()), SysMenu::getMenuName, queryDTO.getMenuName())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysMenu::getStatus, queryDTO.getStatus())
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getSort);

        List<SysMenu> menus = this.list(wrapper);

        return menus.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysMenuVO> tree(SysMenuQueryDTO queryDTO) {
        List<SysMenuVO> menus = listAll(queryDTO);
        return buildTree(menus, 0L);
    }

    @Override
    public SysMenuVO getDetail(Long menuId) {
        SysMenu menu = this.getById(menuId);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }
        return toVO(menu);
    }

    @Override
    public void add(SysMenuDTO dto) {
        // 检查菜单名称是否已存在（同一父级下）
        if (checkMenuNameExists(dto.getMenuName(), dto.getParentId(), null)) {
            throw new BusinessException("同一父级下菜单名称已存在");
        }

        SysMenu menu = BeanUtil.copyProperties(dto, SysMenu.class, "parentId", "sort", "menuType", "visible", "status");
        menu.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        menu.setSort(dto.getSort() != null ? dto.getSort() : 0);
        menu.setMenuType(StrUtil.isBlank(dto.getMenuType()) ? "M" : dto.getMenuType());
        menu.setVisible(StrUtil.isBlank(dto.getVisible()) ? "0" : dto.getVisible());
        menu.setStatus(StrUtil.isBlank(dto.getStatus()) ? CommonConstants.STATUS_NORMAL : dto.getStatus());

        this.save(menu);
    }

    @Override
    public void update(SysMenuDTO dto) {
        SysMenu menu = this.getById(dto.getMenuId());
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }

        // 检查菜单名称是否已存在（同一父级下）
        if (checkMenuNameExists(dto.getMenuName(), dto.getParentId(), dto.getMenuId())) {
            throw new BusinessException("同一父级下菜单名称已存在");
        }

        // 不能将父菜单设置为自己
        if (dto.getMenuId().equals(dto.getParentId())) {
            throw new BusinessException("父菜单不能选择自己");
        }

        // 不能将父菜单设置为自己的子菜单
        if (dto.getParentId() != null && dto.getParentId() != 0) {
            List<Long> childIds = getAllChildIds(dto.getMenuId());
            if (childIds.contains(dto.getParentId())) {
                throw new BusinessException("父菜单不能选择自己的子菜单");
            }
        }

        BeanUtil.copyProperties(dto, menu, "menuId", "parentId");
        menu.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);

        this.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long menuId) {
        SysMenu menu = this.getById(menuId);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }

        // 检查是否有子菜单
        Long childCount = this.count(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getParentId, menuId)
        );
        if (childCount > 0) {
            throw new BusinessException("存在子菜单，无法删除");
        }

        // 检查菜单是否已分配给角色
        Long roleCount = roleMenuMapper.selectCount(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getMenuId, menuId)
        );
        if (roleCount > 0) {
            throw new BusinessException("该菜单已分配给角色，无法删除");
        }

        this.removeById(menuId);
    }

    /**
     * 构建菜单树
     */
    private List<SysMenuVO> buildTree(List<SysMenuVO> menus, Long parentId) {
        List<SysMenuVO> tree = new ArrayList<>();
        for (SysMenuVO menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                List<SysMenuVO> children = buildTree(menus, menu.getMenuId());
                menu.setChildren(children);
                tree.add(menu);
            }
        }
        return tree;
    }

    /**
     * 获取所有子菜单ID
     */
    private List<Long> getAllChildIds(Long menuId) {
        List<Long> childIds = new ArrayList<>();
        List<SysMenu> children = this.list(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getParentId, menuId)
        );
        for (SysMenu child : children) {
            childIds.add(child.getMenuId());
            childIds.addAll(getAllChildIds(child.getMenuId()));
        }
        return childIds;
    }

    /**
     * 检查菜单名称是否存在（同一父级下）
     */
    private boolean checkMenuNameExists(String menuName, Long parentId, Long excludeMenuId) {
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysMenu::getMenuName, menuName);
        wrapper.eq(SysMenu::getParentId, parentId != null ? parentId : 0L);
        if (excludeMenuId != null) {
            wrapper.ne(SysMenu::getMenuId, excludeMenuId);
        }
        return this.count(wrapper) > 0;
    }

    /**
     * 实体转VO
     */
    private SysMenuVO toVO(SysMenu menu) {
        return menu.toVO();
    }

    @Override
    public List<RouterVO> getUserRouters() {
        Long userId = LoginUserUtil.getUserId();
        List<SysMenu> menus;

        // 检查是否为超级管理员
        SysUser user = userMapper.selectById(userId);
        if (user != null && Integer.valueOf(1).equals(user.getSuperAdmin())) {
            // 超级管理员获取所有菜单
            menus = this.list(Wrappers.<SysMenu>lambdaQuery()
                    .in(SysMenu::getMenuType, "M", "C")
                    .eq(SysMenu::getStatus, CommonConstants.STATUS_NORMAL)
                    .orderByAsc(SysMenu::getParentId)
                    .orderByAsc(SysMenu::getSort));
        } else {
            // 普通用户根据角色权限获取菜单
            menus = baseMapper.selectMenusByUserId(userId);
        }

        // 转换为RouterVO
        List<RouterVO> routerList = menus.stream()
                .map(menu -> menuToRouter(menu, menu.getParentId()))
                .collect(Collectors.toList());

        return buildRouterTree(routerList, 0L);
    }

    /**
     * 菜单实体转路由VO
     */
    private RouterVO menuToRouter(SysMenu menu, Long parentId) {
        RouterVO router = new RouterVO();
        router.setMenuId(menu.getMenuId());
        router.setParentId(parentId);
        router.setName(menu.getMenuName());
        router.setPath(menu.getPath());
        router.setComponent(menu.getComponent());
        router.setIcon(menu.getIcon());
        router.setIsFrame(menu.getIsFrame());
        router.setIsCache(menu.getIsCache());
        router.setLink(menu.getLink());
        router.setTarget(menu.getTarget());
        router.setHideInMenu(menu.getVisible());
        router.setBadge(menu.getBadge());
        router.setBadgeColor(menu.getBadgeColor());
        router.setSort(menu.getSort());
        router.setPerms(menu.getPerms());
        return router;
    }

    /**
     * 构建路由树
     */
    private List<RouterVO> buildRouterTree(List<RouterVO> routers, Long parentId) {
        List<RouterVO> tree = new ArrayList<>();
        for (RouterVO router : routers) {
            if (parentId.equals(router.getParentId())) {
                List<RouterVO> children = buildRouterTree(routers, router.getMenuId());
                if (!children.isEmpty()) {
                    router.setChildren(children);
                }
                tree.add(router);
            }
        }
        return tree;
    }
}
