package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.dto.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.system.dto.SysDictItemDTO;
import com.tiny.system.dto.SysDictItemQueryDTO;
import com.tiny.system.dto.UpdateStatusDTO;
import com.tiny.system.service.SysDictItemService;
import com.tiny.system.vo.SysDictItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典项控制器
 *
 * 请求示例:
 * POST /api/system/dictItem/page
 * {
 *   "current": 1,
 *   "size": 10,
 *   "dictCode": "sys_user_gender",
 *   "itemLabel": "男",
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
 *         "itemId": 1,
 *         "dictCode": "sys_user_gender",
 *         "itemLabel": "男",
 *         "itemValue": "0",
 *         "itemSort": 1,
 *         "isDefault": "Y",
 *         "status": "0",
 *         "createTime": "2024-01-01 12:00:00"
 *       }
 *     ],
 *     "current": 1,
 *     "size": 10
 *   },
 *   "timestamp": 1704067200000
 * }
 */
@Tag(name = "字典项管理", description = "字典项的增删改查等接口")
@RestController
@RequestMapping("/system/dictItem")
@RequiredArgsConstructor
public class SysDictItemController {

    private final SysDictItemService dictItemService;

    /**
     * 分页查询字典项列表
     *
     * 请求示例:
     * POST /api/system/dictItem/page
     * {
     *   "current": 1,
     *   "size": 10,
     *   "dictCode": "sys_user_gender"
     * }
     */
    @Operation(summary = "分页查询字典项列表")
    @SaCheckPermission("system:dict:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<SysDictItemVO>> page(@Valid @RequestBody SysDictItemQueryDTO queryDTO) {
        PageResult<SysDictItemVO> result = dictItemService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    /**
     * 根据字典编码查询字典项列表
     *
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": [
     *     {
     *       "itemId": 1,
     *       "dictCode": "sys_user_gender",
     *       "itemLabel": "男",
     *       "itemValue": "0",
     *       "itemSort": 1,
     *       "isDefault": "Y",
     *       "status": "0"
     *     }
     *   ],
     *   "timestamp": 1704067200000
     * }
     */
    @Operation(summary = "根据字典编码查询字典项列表")
    @GetMapping("/list/{dictCode}")
    public ResponseResult<List<SysDictItemVO>> listByDictCode(
            @Parameter(description = "字典编码") @PathVariable String dictCode) {
        List<SysDictItemVO> list = dictItemService.listByDictCode(dictCode);
        return ResponseResult.ok(list);
    }

    /**
     * 根据字典编码查询启用状态的字典项列表
     *
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": [
     *     {
     *       "itemId": 1,
     *       "dictCode": "sys_user_gender",
     *       "itemLabel": "男",
     *       "itemValue": "0",
     *       "itemSort": 1
     *     }
     *   ],
     *   "timestamp": 1704067200000
     * }
     */
    @Operation(summary = "根据字典编码查询启用状态的字典项列表")
    @GetMapping("/enabled/{dictCode}")
    public ResponseResult<List<SysDictItemVO>> listEnabledByDictCode(
            @Parameter(description = "字典编码") @PathVariable String dictCode) {
        List<SysDictItemVO> list = dictItemService.listEnabledByDictCode(dictCode);
        return ResponseResult.ok(list);
    }

    /**
     * 查询字典项详情
     *
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "itemId": 1,
     *     "dictCode": "sys_user_gender",
     *     "itemLabel": "男",
     *     "itemValue": "0",
     *     "itemSort": 1,
     *     "cssClass": null,
     *     "listClass": "primary",
     *     "isDefault": "Y",
     *     "status": "0",
     *     "remark": "性别男",
     *     "createTime": "2024-01-01 12:00:00"
     *   },
     *   "timestamp": 1704067200000
     * }
     */
    @Operation(summary = "查询字典项详情")
    @SaCheckPermission("system:dict:query")
    @GetMapping("/{itemId}")
    public ResponseResult<SysDictItemVO> getById(@Parameter(description = "字典项ID") @PathVariable Long itemId) {
        SysDictItemVO dictItem = dictItemService.getDetail(itemId);
        return ResponseResult.ok(dictItem);
    }

    /**
     * 新增字典项
     *
     * 请求示例:
     * POST /api/system/dictItem
     * {
     *   "dictCode": "sys_user_gender",
     *   "itemLabel": "男",
     *   "itemValue": "0",
     *   "itemSort": 1,
     *   "listClass": "primary",
     *   "isDefault": "Y",
     *   "status": "0",
     *   "remark": "性别男"
     * }
     */
    @Operation(summary = "新增字典项")
    @OperationLog(module = "字典管理", type = OperationType.INSERT, desc = "新增字典项")
    @SaCheckPermission("system:dict:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody SysDictItemDTO dto) {
        dictItemService.add(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改字典项
     *
     * 请求示例:
     * PUT /api/system/dictItem
     * {
     *   "itemId": 1,
     *   "dictCode": "sys_user_gender",
     *   "itemLabel": "男",
     *   "itemValue": "0",
     *   "itemSort": 1,
     *   "listClass": "primary",
     *   "isDefault": "Y",
     *   "status": "0",
     *   "remark": "性别男"
     * }
     */
    @Operation(summary = "修改字典项")
    @OperationLog(module = "字典管理", type = OperationType.UPDATE, desc = "修改字典项")
    @SaCheckPermission("system:dict:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody SysDictItemDTO dto) {
        dictItemService.update(dto);
        return ResponseResult.ok();
    }

    /**
     * 删除字典项
     *
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": null,
     *   "timestamp": 1704067200000
     * }
     */
    @Operation(summary = "删除字典项")
    @OperationLog(module = "字典管理", type = OperationType.DELETE, desc = "删除字典项")
    @SaCheckPermission("system:dict:remove")
    @DeleteMapping("/{itemId}")
    public ResponseResult<Void> delete(@Parameter(description = "字典项ID") @PathVariable Long itemId) {
        dictItemService.delete(itemId);
        return ResponseResult.ok();
    }

    /**
     * 批量删除字典项
     *
     * 请求示例:
     * DELETE /api/system/dictItem/batch
     * {
     *   "ids": [1, 2, 3]
     * }
     */
    @Operation(summary = "批量删除字典项")
    @OperationLog(module = "字典管理", type = OperationType.DELETE, desc = "批量删除字典项")
    @SaCheckPermission("system:dict:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        dictItemService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    /**
     * 修改字典项状态
     *
     * 请求示例:
     * PUT /api/system/dictItem/status
     * {
     *   "id": 1,
     *   "status": "1"
     * }
     */
    @Operation(summary = "修改字典项状态")
    @OperationLog(module = "字典管理", type = OperationType.UPDATE, desc = "修改字典项状态")
    @SaCheckPermission("system:dict:edit")
    @PutMapping("/status")
    public ResponseResult<Void> updateStatus(@Valid @RequestBody UpdateStatusDTO dto) {
        dictItemService.updateStatus(dto.getId(), dto.getStatus());
        return ResponseResult.ok();
    }
}
