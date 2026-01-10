package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.dto.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.system.dto.SysDictTypeDTO;
import com.tiny.system.dto.SysDictTypeQueryDTO;
import com.tiny.system.dto.UpdateStatusDTO;
import com.tiny.system.service.SysDictTypeService;
import com.tiny.system.vo.SysDictTypeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典类型控制器
 *
 * 请求示例:
 * POST /api/system/dictType/page
 * {
 *   "current": 1,
 *   "size": 10,
 *   "dictName": "性别",
 *   "dictCode": "sys_user_gender",
 *   "status": "0"
 * }
 *
 * 响应示例:
 * {
 *   "code": 200,
 *   "message": "操作成功",
 *   "data": {
 *     "total": 3,
 *     "records": [
 *       {
 *         "dictId": 1,
 *         "dictName": "用户性别",
 *         "dictCode": "sys_user_gender",
 *         "status": "0",
 *         "remark": "用户性别列表",
 *         "createTime": "2024-01-01 12:00:00"
 *       }
 *     ],
 *     "current": 1,
 *     "size": 10
 *   },
 *   "timestamp": 1704067200000
 * }
 */
@Tag(name = "字典类型管理", description = "字典类型的增删改查等接口")
@RestController
@RequestMapping("/system/dictType")
@RequiredArgsConstructor
public class SysDictTypeController {

    private final SysDictTypeService dictTypeService;

    /**
     * 分页查询字典类型列表
     *
     * 请求示例:
     * POST /api/system/dictType/page
     * {
     *   "current": 1,
     *   "size": 10,
     *   "dictName": "性别"
     * }
     */
    @Operation(summary = "分页查询字典类型列表")
    @SaCheckPermission("system:dict:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<SysDictTypeVO>> page(@Valid @RequestBody SysDictTypeQueryDTO queryDTO) {
        PageResult<SysDictTypeVO> result = dictTypeService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    /**
     * 查询所有字典类型列表
     *
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": [
     *     {
     *       "dictId": 1,
     *       "dictName": "用户性别",
     *       "dictCode": "sys_user_gender",
     *       "status": "0"
     *     }
     *   ],
     *   "timestamp": 1704067200000
     * }
     */
    @Operation(summary = "查询所有字典类型列表")
    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    public ResponseResult<List<SysDictTypeVO>> list() {
        List<SysDictTypeVO> list = dictTypeService.listAll();
        return ResponseResult.ok(list);
    }

    /**
     * 查询字典类型详情
     *
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "dictId": 1,
     *     "dictName": "用户性别",
     *     "dictCode": "sys_user_gender",
     *     "status": "0",
     *     "remark": "用户性别列表",
     *     "createTime": "2024-01-01 12:00:00"
     *   },
     *   "timestamp": 1704067200000
     * }
     */
    @Operation(summary = "查询字典类型详情")
    @SaCheckPermission("system:dict:query")
    @GetMapping("/{dictId}")
    public ResponseResult<SysDictTypeVO> getById(@Parameter(description = "字典类型ID") @PathVariable Long dictId) {
        SysDictTypeVO dictType = dictTypeService.getDetail(dictId);
        return ResponseResult.ok(dictType);
    }

    /**
     * 新增字典类型
     *
     * 请求示例:
     * POST /api/system/dictType
     * {
     *   "dictName": "用户性别",
     *   "dictCode": "sys_user_gender",
     *   "status": "0",
     *   "remark": "用户性别列表"
     * }
     */
    @Operation(summary = "新增字典类型")
    @OperationLog(module = "字典管理", type = OperationType.INSERT, desc = "新增字典类型")
    @SaCheckPermission("system:dict:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody SysDictTypeDTO dto) {
        dictTypeService.add(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改字典类型
     *
     * 请求示例:
     * PUT /api/system/dictType
     * {
     *   "dictId": 1,
     *   "dictName": "用户性别",
     *   "dictCode": "sys_user_gender",
     *   "status": "0",
     *   "remark": "用户性别列表"
     * }
     */
    @Operation(summary = "修改字典类型")
    @OperationLog(module = "字典管理", type = OperationType.UPDATE, desc = "修改字典类型")
    @SaCheckPermission("system:dict:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody SysDictTypeDTO dto) {
        dictTypeService.update(dto);
        return ResponseResult.ok();
    }

    /**
     * 删除字典类型
     *
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": null,
     *   "timestamp": 1704067200000
     * }
     */
    @Operation(summary = "删除字典类型")
    @OperationLog(module = "字典管理", type = OperationType.DELETE, desc = "删除字典类型")
    @SaCheckPermission("system:dict:remove")
    @DeleteMapping("/{dictId}")
    public ResponseResult<Void> delete(@Parameter(description = "字典类型ID") @PathVariable Long dictId) {
        dictTypeService.delete(dictId);
        return ResponseResult.ok();
    }

    /**
     * 批量删除字典类型
     *
     * 请求示例:
     * DELETE /api/system/dictType/batch
     * {
     *   "ids": [1, 2, 3]
     * }
     */
    @Operation(summary = "批量删除字典类型")
    @OperationLog(module = "字典管理", type = OperationType.DELETE, desc = "批量删除字典类型")
    @SaCheckPermission("system:dict:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        dictTypeService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    /**
     * 修改字典类型状态
     *
     * 请求示例:
     * PUT /api/system/dictType/status
     * {
     *   "id": 1,
     *   "status": "1"
     * }
     */
    @Operation(summary = "修改字典类型状态")
    @OperationLog(module = "字典管理", type = OperationType.UPDATE, desc = "修改字典类型状态")
    @SaCheckPermission("system:dict:edit")
    @PutMapping("/status")
    public ResponseResult<Void> updateStatus(@Valid @RequestBody UpdateStatusDTO dto) {
        dictTypeService.updateStatus(dto.getId(), dto.getStatus());
        return ResponseResult.ok();
    }
}
