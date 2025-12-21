package com.tiny.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
