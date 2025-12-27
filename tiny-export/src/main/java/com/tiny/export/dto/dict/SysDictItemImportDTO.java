package com.tiny.export.dto.dict;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 字典项导入数据模型
 */
@Data
public class SysDictItemImportDTO {

    @ExcelProperty("字典编码")
    @ColumnWidth(20)
    private String dictCode;

    @ExcelProperty("标签")
    @ColumnWidth(20)
    private String itemLabel;

    @ExcelProperty("值")
    @ColumnWidth(15)
    private String itemValue;

    @ExcelProperty("排序")
    @ColumnWidth(8)
    private Integer itemSort;

    @ExcelProperty("样式")
    @ColumnWidth(15)
    private String cssClass;

    @ExcelProperty("回显样式")
    @ColumnWidth(15)
    private String listClass;

    @ExcelProperty("备注")
    @ColumnWidth(30)
    private String remark;
}
