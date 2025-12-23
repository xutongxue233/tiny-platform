package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.domain.ResponseResult;
import com.tiny.common.core.model.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.ResetPasswordDTO;
import com.tiny.system.dto.SysUserDTO;
import com.tiny.system.dto.SysUserQueryDTO;
import com.tiny.system.dto.UpdateStatusDTO;
import com.tiny.system.service.SysRoleService;
import com.tiny.system.service.SysUserService;
import com.tiny.system.vo.SysRoleVO;
import com.tiny.system.vo.SysUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理", description = "用户的增删改查等接口")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;
    private final SysRoleService roleService;

    @Operation(summary = "分页查询用户列表")
    @SaCheckPermission("system:user:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<SysUserVO>> page(@RequestBody SysUserQueryDTO queryDTO) {
        PageResult<SysUserVO> result = userService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    @Operation(summary = "查询用户详情")
    @SaCheckPermission("system:user:query")
    @GetMapping("/{userId}")
    public ResponseResult<SysUserVO> getById(@Parameter(description = "用户ID") @PathVariable Long userId) {
        SysUserVO user = userService.getDetail(userId);
        return ResponseResult.ok(user);
    }

    @Operation(summary = "新增用户")
    @OperationLog(module = "用户管理", type = OperationType.INSERT, desc = "新增用户")
    @SaCheckPermission("system:user:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody SysUserDTO dto) {
        userService.add(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "修改用户")
    @OperationLog(module = "用户管理", type = OperationType.UPDATE, desc = "修改用户")
    @SaCheckPermission("system:user:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody SysUserDTO dto) {
        userService.update(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "删除用户")
    @OperationLog(module = "用户管理", type = OperationType.DELETE, desc = "删除用户")
    @SaCheckPermission("system:user:remove")
    @DeleteMapping("/{userId}")
    public ResponseResult<Void> delete(@Parameter(description = "用户ID") @PathVariable Long userId) {
        userService.delete(userId);
        return ResponseResult.ok();
    }

    @Operation(summary = "批量删除用户")
    @OperationLog(module = "用户管理", type = OperationType.DELETE, desc = "批量删除用户")
    @SaCheckPermission("system:user:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        userService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    @Operation(summary = "重置密码")
    @OperationLog(module = "用户管理", type = OperationType.UPDATE, desc = "重置密码")
    @SaCheckPermission("system:user:resetPwd")
    @PutMapping("/resetPassword")
    public ResponseResult<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(dto.getUserId(), dto.getNewPassword());
        return ResponseResult.ok();
    }

    @Operation(summary = "修改用户状态")
    @OperationLog(module = "用户管理", type = OperationType.UPDATE, desc = "修改用户状态")
    @SaCheckPermission("system:user:edit")
    @PutMapping("/status")
    public ResponseResult<Void> updateStatus(@Valid @RequestBody UpdateStatusDTO dto) {
        userService.updateStatus(dto.getId(), dto.getStatus());
        return ResponseResult.ok();
    }

    @Operation(summary = "获取角色列表")
    @GetMapping("/roles")
    public ResponseResult<List<SysRoleVO>> getRoles() {
        List<SysRoleVO> list = roleService.listAll();
        return ResponseResult.ok(list);
    }

}