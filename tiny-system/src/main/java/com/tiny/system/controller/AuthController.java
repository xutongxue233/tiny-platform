package com.tiny.system.controller;

import com.tiny.common.core.domain.ResponseResult;
import com.tiny.system.dto.LoginDTO;
import com.tiny.system.service.AuthService;
import com.tiny.system.vo.LoginVO;
import com.tiny.system.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "用户登录、登出、获取用户信息等接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录", description = "根据用户名和密码进行登录")
    @PostMapping("/login")
    public ResponseResult<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return ResponseResult.ok(loginVO);
    }

    @Operation(summary = "用户登出", description = "退出登录状态")
    @PostMapping("/logout")
    public ResponseResult<Void> logout() {
        authService.logout();
        return ResponseResult.ok();
    }

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/getUserInfo")
    public ResponseResult<UserInfoVO> getUserInfo() {
        UserInfoVO userInfo = authService.getUserInfo();
        return ResponseResult.ok(userInfo);
    }
}
