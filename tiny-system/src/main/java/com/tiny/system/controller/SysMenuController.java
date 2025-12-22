package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.domain.ResponseResult;
import com.tiny.system.dto.SysMenuDTO;
import com.tiny.system.dto.SysMenuQueryDTO;
import com.tiny.system.service.SysMenuService;
import com.tiny.system.vo.RouterVO;
import com.tiny.system.vo.SysMenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@Tag(name = "菜单管理", description = "菜单的增删改查等接口")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    @Operation(summary = "获取当前用户路由菜单")
    @GetMapping("/routers")
    public ResponseResult<List<RouterVO>> getRouters() {
        List<RouterVO> routers = menuService.getUserRouters();
        return ResponseResult.ok(routers);
    }

    @Operation(summary = "查询菜单列表")
    @SaCheckPermission("system:menu:list")
    @PostMapping("/list")
    public ResponseResult<List<SysMenuVO>> list(@RequestBody SysMenuQueryDTO queryDTO) {
        List<SysMenuVO> list = menuService.listAll(queryDTO);
        return ResponseResult.ok(list);
    }

    @Operation(summary = "查询菜单树")
    @SaCheckPermission("system:menu:list")
    @PostMapping("/tree")
    public ResponseResult<List<SysMenuVO>> tree(@RequestBody SysMenuQueryDTO queryDTO) {
        List<SysMenuVO> tree = menuService.tree(queryDTO);
        return ResponseResult.ok(tree);
    }

    @Operation(summary = "查询菜单详情")
    @SaCheckPermission("system:menu:query")
    @GetMapping("/detail/{menuId}")
    public ResponseResult<SysMenuVO> getById(@Parameter(description = "菜单ID") @PathVariable Long menuId) {
        SysMenuVO menu = menuService.getDetail(menuId);
        return ResponseResult.ok(menu);
    }

    @Operation(summary = "新增菜单")
    @OperationLog(module = "菜单管理", type = OperationType.INSERT, desc = "新增菜单")
    @SaCheckPermission("system:menu:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody SysMenuDTO dto) {
        menuService.add(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "修改菜单")
    @OperationLog(module = "菜单管理", type = OperationType.UPDATE, desc = "修改菜单")
    @SaCheckPermission("system:menu:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody SysMenuDTO dto) {
        menuService.update(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "删除菜单")
    @OperationLog(module = "菜单管理", type = OperationType.DELETE, desc = "删除菜单")
    @SaCheckPermission("system:menu:remove")
    @DeleteMapping("/{menuId}")
    public ResponseResult<Void> delete(@Parameter(description = "菜单ID") @PathVariable Long menuId) {
        menuService.delete(menuId);
        return ResponseResult.ok();
    }
}