package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.domain.ResponseResult;
import com.tiny.system.dto.SysDeptDTO;
import com.tiny.system.dto.SysDeptQueryDTO;
import com.tiny.system.dto.UpdateStatusDTO;
import com.tiny.system.service.SysDeptService;
import com.tiny.system.vo.SysDeptVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@Tag(name = "部门管理", description = "部门的增删改查等接口")
@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService deptService;

    @Operation(summary = "查询部门树形列表")
    @SaCheckPermission("system:dept:list")
    @GetMapping("/tree")
    public ResponseResult<List<SysDeptVO>> tree(@Valid SysDeptQueryDTO queryDTO) {
        List<SysDeptVO> tree = deptService.tree(queryDTO);
        return ResponseResult.ok(tree);
    }

    @Operation(summary = "查询部门列表（平铺）")
    @SaCheckPermission("system:dept:list")
    @GetMapping("/list")
    public ResponseResult<List<SysDeptVO>> list(@Valid SysDeptQueryDTO queryDTO) {
        List<SysDeptVO> list = deptService.list(queryDTO);
        return ResponseResult.ok(list);
    }

    @Operation(summary = "查询部门下拉树（排除指定部门）")
    @SaCheckPermission(value = {"system:dept:add", "system:dept:edit"}, mode = SaMode.OR)
    @GetMapping("/tree/exclude/{deptId}")
    public ResponseResult<List<SysDeptVO>> treeExclude(@Parameter(description = "部门ID") @PathVariable Long deptId) {
        List<SysDeptVO> tree = deptService.treeExclude(deptId);
        return ResponseResult.ok(tree);
    }

    @Operation(summary = "查询部门下拉树（用于选择）")
    @GetMapping("/tree/select")
    public ResponseResult<List<SysDeptVO>> treeSelect() {
        List<SysDeptVO> tree = deptService.tree(null);
        return ResponseResult.ok(tree);
    }

    @Operation(summary = "查询部门详情")
    @SaCheckPermission("system:dept:query")
    @GetMapping("/{deptId}")
    public ResponseResult<SysDeptVO> getById(@Parameter(description = "部门ID") @PathVariable Long deptId) {
        SysDeptVO dept = deptService.getDetail(deptId);
        return ResponseResult.ok(dept);
    }

    @Operation(summary = "新增部门")
    @OperationLog(module = "部门管理", type = OperationType.INSERT, desc = "新增部门")
    @SaCheckPermission("system:dept:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody SysDeptDTO dto) {
        deptService.add(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "修改部门")
    @OperationLog(module = "部门管理", type = OperationType.UPDATE, desc = "修改部门")
    @SaCheckPermission("system:dept:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody SysDeptDTO dto) {
        deptService.update(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "删除部门")
    @OperationLog(module = "部门管理", type = OperationType.DELETE, desc = "删除部门")
    @SaCheckPermission("system:dept:remove")
    @DeleteMapping("/{deptId}")
    public ResponseResult<Void> delete(@Parameter(description = "部门ID") @PathVariable Long deptId) {
        deptService.delete(deptId);
        return ResponseResult.ok();
    }

    @Operation(summary = "修改部门状态")
    @OperationLog(module = "部门管理", type = OperationType.UPDATE, desc = "修改部门状态")
    @SaCheckPermission("system:dept:edit")
    @PutMapping("/status")
    public ResponseResult<Void> updateStatus(@Valid @RequestBody UpdateStatusDTO dto) {
        deptService.updateStatus(dto.getId(), dto.getStatus());
        return ResponseResult.ok();
    }
}
