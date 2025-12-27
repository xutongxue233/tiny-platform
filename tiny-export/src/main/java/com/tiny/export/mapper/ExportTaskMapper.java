package com.tiny.export.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.export.entity.ExportTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 导出任务Mapper
 */
@Mapper
public interface ExportTaskMapper extends BaseMapper<ExportTask> {
}
