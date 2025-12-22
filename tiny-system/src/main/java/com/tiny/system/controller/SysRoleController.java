package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.domain.ResponseResult;
import com.tiny.common.core.model.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysRoleDTO;
import com.tiny.system.dto.SysRoleQueryDTO;
import com.tiny.system.dto.UpdateStatusDTO;
import com.tiny.system.service.SysRoleService;
import com.tiny.system.vo.SysRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@Tag(name = "角色管理", description = "角色的增删改查等接口")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @Operation(summary = "分页查询角色列表")
    @SaCheckPermission("system:role:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<SysRoleVO>> page(@RequestBody SysRoleQueryDTO queryDTO) {
        PageResult<SysRoleVO> result = roleService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    @Operation(summary = "查询所有角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/list")
    public ResponseResult<List<SysRoleVO>> list() {
        List<SysRoleVO> list = roleService.listAll();
        return ResponseResult.ok(list);
    }

    @Operation(summary = "查询角色详情")
    @SaCheckPermission("system:role:query")
    @GetMapping("/{roleId}")
    public ResponseResult<SysRoleVO> getById(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        SysRoleVO role = roleService.getDetail(roleId);
        return ResponseResult.ok(role);
    }

    @Operation(summary = "新增角色")
    @OperationLog(module = "角色管理", type = OperationType.INSERT, desc = "新增角色")
    @SaCheckPermission("system:role:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody SysRoleDTO dto) {
        roleService.add(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "修改角色")
    @OperationLog(module = "角色管理", type = OperationType.UPDATE, desc = "修改角色")
    @SaCheckPermission("system:role:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody SysRoleDTO dto) {
        roleService.update(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "删除角色")
    @OperationLog(module = "角色管理", type = OperationType.DELETE, desc = "删除角色")
    @SaCheckPermission("system:role:remove")
    @DeleteMapping("/{roleId}")
    public ResponseResult<Void> delete(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        roleService.delete(roleId);
        return ResponseResult.ok();
    }

    @Operation(summary = "批量删除角色")
    @OperationLog(module = "角色管理", type = OperationType.DELETE, desc = "批量删除角色")
    @SaCheckPermission("system:role:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        roleService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    @Operation(summary = "修改角色状态")
    @OperationLog(module = "角色管理", type = OperationType.UPDATE, desc = "修改角色状态")
    @SaCheckPermission("system:role:edit")
    @PutMapping("/status")
    public ResponseResult<Void> updateStatus(@Valid @RequestBody UpdateStatusDTO dto) {
        roleService.updateStatus(dto.getId(), dto.getStatus());
        return ResponseResult.ok();
    }
}