package com.tiny.system.service;

import com.tiny.system.dto.LoginDTO;
import com.tiny.system.dto.RegisterDTO;
import com.tiny.system.vo.LoginVO;
import com.tiny.system.vo.UserInfoVO;

/**
 * 登录服务接口
 */
public interface AuthService {

    /**
     * 登录
     *
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 登出
     */
    void logout();

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    UserInfoVO getUserInfo();

    /**
     * 用户注册
     *
     * @param registerDTO 注册参数
     */
    void register(RegisterDTO registerDTO);
}