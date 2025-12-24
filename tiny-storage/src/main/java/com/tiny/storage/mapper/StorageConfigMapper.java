package com.tiny.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.storage.entity.StorageConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 存储配置Mapper
 */
@Mapper
public interface StorageConfigMapper extends BaseMapper<StorageConfig> {
}