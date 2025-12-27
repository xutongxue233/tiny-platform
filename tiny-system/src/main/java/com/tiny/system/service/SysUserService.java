package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysUserDTO;
import com.tiny.system.dto.SysUserQueryDTO;
import com.tiny.system.entity.SysUser;
import com.tiny.system.vo.SysUserVO;

import java.util.List;

/**
 * 用户管理服务接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     */
    PageResult<SysUserVO> page(SysUserQueryDTO queryDTO);

    /**
     * 查询用户详情
     */
    SysUserVO getDetail(Long userId);

    /**
     * 新增用户
     */
    void add(SysUserDTO dto);

    /**
     * 修改用户
     */
    void update(SysUserDTO dto);

    /**
     * 删除用户
     */
    void delete(Long userId);

    /**
     * 批量删除用户
     */
    void deleteBatch(List<Long> userIds);

    /**
     * 重置密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 修改用户状态
     */
    void updateStatus(Long userId, String status);

    /**
     * 封禁用户
     *
     * @param userId      用户ID
     * @param disableTime 封禁时长（秒），-1表示永久封禁
     */
    void disableUser(Long userId, long disableTime);

    /**
     * 解除封禁
     */
    void untieDisable(Long userId);

    /**
     * 获取所有正常状态的用户简单列表
     */
    List<SysUserVO> listAllSimple();

}
