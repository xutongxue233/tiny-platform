package ${packageName}.controller;

import ${packageName}.dto.${className}DTO;
import ${packageName}.dto.${className}Query;
import ${packageName}.service.${className}Service;
import ${packageName}.vo.${className}VO;
import com.tiny.core.web.ResponseResult;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ${functionName!tableComment} Controller
 *
 * @author ${author}
 * @date ${datetime}
 */
@Tag(name = "${functionName!tableComment}")
@RestController
@RequestMapping("/${businessName}")
@RequiredArgsConstructor
public class ${className}Controller {

    private final ${className}Service ${classname}Service;

    @Operation(summary = "查询${functionName!tableComment}列表")
    @SaCheckPermission("${permissionPrefix}:list")
    @GetMapping("/list")
    public ResponseResult<List<${className}VO>> list(${className}Query query) {
        return ResponseResult.ok(${classname}Service.selectList(query));
    }

    @Operation(summary = "获取${functionName!tableComment}详情")
    @SaCheckPermission("${permissionPrefix}:query")
    @GetMapping("/{${pkColumn.javaField}}")
    public ResponseResult<${className}VO> getInfo(@PathVariable ${pkColumn.javaType} ${pkColumn.javaField}) {
        return ResponseResult.ok(${classname}Service.selectById(${pkColumn.javaField}));
    }

    @Operation(summary = "新增${functionName!tableComment}")
    @SaCheckPermission("${permissionPrefix}:add")
    @OperationLog(module = "${functionName!tableComment}", type = OperationType.INSERT)
    @PostMapping
    public ResponseResult<Void> add(@Validated @RequestBody ${className}DTO dto) {
        ${classname}Service.insert(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "修改${functionName!tableComment}")
    @SaCheckPermission("${permissionPrefix}:edit")
    @OperationLog(module = "${functionName!tableComment}", type = OperationType.UPDATE)
    @PutMapping
    public ResponseResult<Void> edit(@Validated @RequestBody ${className}DTO dto) {
        ${classname}Service.update(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "删除${functionName!tableComment}")
    @SaCheckPermission("${permissionPrefix}:remove")
    @OperationLog(module = "${functionName!tableComment}", type = OperationType.DELETE)
    @DeleteMapping("/{${pkColumn.javaField}s}")
    public ResponseResult<Void> remove(@PathVariable List<${pkColumn.javaType}> ${pkColumn.javaField}s) {
        ${classname}Service.deleteByIds(${pkColumn.javaField}s);
        return ResponseResult.ok();
    }
}
