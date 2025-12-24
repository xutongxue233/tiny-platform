package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.core.web.ResponseResult;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.OnlineUserQueryDTO;
import com.tiny.system.service.OnlineUserService;
import com.tiny.system.vo.OnlineUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 在线用户控制器
 */
@Tag(name = "在线用户管理", description = "在线用户查看、强退、封禁等接口")
@RestController
@RequestMapping("/monitor/online")
@RequiredArgsConstructor
public class OnlineUserController {

    private final OnlineUserService onlineUserService;

    @Operation(summary = "分页查询在线用户")
    @SaCheckPermission("monitor:online:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<OnlineUserVO>> page(@Valid @RequestBody OnlineUserQueryDTO queryDTO) {
        PageResult<OnlineUserVO> result = onlineUserService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    @Operation(summary = "强制退出用户")
    @SaCheckPermission("monitor:online:kickout")
    @DeleteMapping("/kickout/{tokenId}")
    public ResponseResult<Void> kickout(@Parameter(description = "会话Token") @PathVariable String tokenId) {
        onlineUserService.kickout(tokenId);
        return ResponseResult.ok();
    }

    @Operation(summary = "强制退出用户（根据用户ID踢出所有会话）")
    @SaCheckPermission("monitor:online:kickout")
    @DeleteMapping("/kickoutByUserId/{userId}")
    public ResponseResult<Void> kickoutByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        onlineUserService.kickoutByUserId(userId);
        return ResponseResult.ok();
    }

    @Operation(summary = "封禁用户账号")
    @SaCheckPermission("monitor:online:disable")
    @PostMapping("/disable/{userId}")
    public ResponseResult<Void> disableUser(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "封禁时长（秒），-1表示永久封禁") @RequestParam(defaultValue = "-1") long disableTime) {
        onlineUserService.disableUser(userId, disableTime);
        return ResponseResult.ok();
    }

    @Operation(summary = "解除用户封禁")
    @SaCheckPermission("monitor:online:disable")
    @PostMapping("/untieDisable/{userId}")
    public ResponseResult<Void> untieDisable(@Parameter(description = "用户ID") @PathVariable Long userId) {
        onlineUserService.untieDisable(userId);
        return ResponseResult.ok();
    }

    @Operation(summary = "检查用户是否被封禁")
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/isDisabled/{userId}")
    public ResponseResult<Boolean> isDisabled(@Parameter(description = "用户ID") @PathVariable Long userId) {
        boolean disabled = onlineUserService.isDisabled(userId);
        return ResponseResult.ok(disabled);
    }

    @Operation(summary = "获取用户封禁剩余时间")
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/disableTime/{userId}")
    public ResponseResult<Long> getDisableTime(@Parameter(description = "用户ID") @PathVariable Long userId) {
        long disableTime = onlineUserService.getDisableTime(userId);
        return ResponseResult.ok(disableTime);
    }
}
