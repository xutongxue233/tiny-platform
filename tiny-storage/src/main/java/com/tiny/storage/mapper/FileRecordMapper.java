package com.tiny.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.storage.entity.FileRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件记录Mapper
 */
@Mapper
public interface FileRecordMapper extends BaseMapper<FileRecord> {
}