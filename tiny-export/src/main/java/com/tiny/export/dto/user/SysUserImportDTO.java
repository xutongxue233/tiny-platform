package com.tiny.export.dto.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 用户导入数据模型
 */
@Data
public class SysUserImportDTO {

    @ExcelProperty("用户名")
    @ColumnWidth(15)
    private String username;

    @ExcelProperty("姓名")
    @ColumnWidth(12)
    private String realName;

    @ExcelProperty("手机号")
    @ColumnWidth(15)
    private String phone;

    @ExcelProperty("邮箱")
    @ColumnWidth(25)
    private String email;

    @ExcelProperty("性别")
    @ColumnWidth(8)
    private String gender;

    @ExcelProperty("部门ID")
    @ColumnWidth(10)
    private Long deptId;

    @ExcelProperty("初始密码")
    @ColumnWidth(15)
    private String password;

    @ExcelProperty("备注")
    @ColumnWidth(30)
    private String remark;
}
