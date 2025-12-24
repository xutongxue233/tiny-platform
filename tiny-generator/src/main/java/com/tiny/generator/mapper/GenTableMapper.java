package com.tiny.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.generator.entity.GenTable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 代码生成表Mapper
 */
@Mapper
public interface GenTableMapper extends BaseMapper<GenTable> {

    /**
     * 根据表名查询（忽略逻辑删除）
     */
    @Select("SELECT * FROM gen_table WHERE table_name = #{tableName} LIMIT 1")
    GenTable selectByTableNameIgnoreDeleted(String tableName);

    /**
     * 根据表名物理删除已逻辑删除的记录
     */
    @Delete("DELETE FROM gen_table WHERE table_name = #{tableName} AND del_flag = '1'")
    int physicalDeleteByTableName(String tableName);
}
