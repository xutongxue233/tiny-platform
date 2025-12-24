package com.tiny.system.controller;

import com.tiny.core.web.ResponseResult;
import com.tiny.system.service.SysDictItemService;
import com.tiny.system.vo.SysDictItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典数据公共接口控制器
 *
 * 提供字典数据的公共查询接口，供业务模块使用
 *
 * 请求示例:
 * GET /api/dict/sys_user_gender
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
 *     },
 *     {
 *       "itemId": 2,
 *       "dictCode": "sys_user_gender",
 *       "itemLabel": "女",
 *       "itemValue": "1",
 *       "itemSort": 2
 *     }
 *   ],
 *   "timestamp": 1704067200000
 * }
 */
@Tag(name = "字典数据", description = "字典数据公共查询接口")
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictItemService dictItemService;

    /**
     * 根据字典编码查询字典项列表
     *
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": [
     *     {
     *       "itemLabel": "男",
     *       "itemValue": "0",
     *       "itemSort": 1
     *     }
     *   ],
     *   "timestamp": 1704067200000
     * }
     */
    @Operation(summary = "根据字典编码查询字典项列表")
    @GetMapping("/{dictCode}")
    public ResponseResult<List<SysDictItemVO>> getByDictCode(
            @Parameter(description = "字典编码") @PathVariable String dictCode) {
        List<SysDictItemVO> list = dictItemService.listEnabledByDictCode(dictCode);
        return ResponseResult.ok(list);
    }

    /**
     * 批量查询多个字典编码的字典项列表
     *
     * 请求示例:
     * GET /api/dict/batch?dictCodes=sys_user_gender,sys_common_status
     *
     * 响应示例:
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "sys_user_gender": [
     *       { "itemLabel": "男", "itemValue": "0" }
     *     ],
     *     "sys_common_status": [
     *       { "itemLabel": "正常", "itemValue": "0" }
     *     ]
     *   },
     *   "timestamp": 1704067200000
     * }
     */
    @Operation(summary = "批量查询多个字典编码的字典项列表")
    @GetMapping("/batch")
    public ResponseResult<Map<String, List<SysDictItemVO>>> getBatchByDictCodes(
            @Parameter(description = "字典编码列表，逗号分隔") @RequestParam String dictCodes) {
        String[] codes = dictCodes.split(",");
        Map<String, List<SysDictItemVO>> result = Arrays.stream(codes)
                .map(String::trim)
                .collect(Collectors.toMap(
                        code -> code,
                        code -> dictItemService.listEnabledByDictCode(code)
                ));
        return ResponseResult.ok(result);
    }
}
