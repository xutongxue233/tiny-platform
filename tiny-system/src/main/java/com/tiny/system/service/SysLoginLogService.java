package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysLoginLogQueryDTO;
import com.tiny.system.entity.SysLoginLog;
import com.tiny.system.vo.SysLoginLogVO;

import java.util.List;

/**
 * 登录日志Service
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 分页查询登录日志
     */
    PageResult<SysLoginLogVO> page(SysLoginLogQueryDTO queryDTO);

    /**
     * 记录登录日志
     *
     * @param username  用户名
     * @param userId    用户ID
     * @param loginType 登录类型
     * @param status    状态
     * @param errorMsg  错误消息
     * @param ipAddr    IP地址
     * @param userAgent User-Agent
     */
    void recordLoginLog(String username, Long userId, String loginType, String status, String errorMsg,
                        String ipAddr, String userAgent);

    /**
     * 批量删除登录日志
     */
    void deleteBatch(List<Long> loginLogIds);

    /**
     * 清空登录日志
     */
    void clean();
}
