package com.tiny.export.excel.validator.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tiny.export.dto.user.SysUserImportDTO;
import com.tiny.export.excel.validator.ImportValidator;
import com.tiny.system.entity.SysUser;
import com.tiny.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 用户导入校验器
 */
@Component
@RequiredArgsConstructor
public class SysUserImportValidator implements ImportValidator<SysUserImportDTO> {

    private final SysUserService sysUserService;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{1,19}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @Override
    public List<String> validate(SysUserImportDTO data, int rowNum) {
        List<String> errors = new ArrayList<>();

        // 用户名校验
        if (StrUtil.isBlank(data.getUsername())) {
            errors.add(String.format("第%d行: 用户名不能为空", rowNum));
        } else if (!USERNAME_PATTERN.matcher(data.getUsername()).matches()) {
            errors.add(String.format("第%d行: 用户名格式不正确(必须以字母开头,2-20位字母数字下划线)", rowNum));
        } else if (existsByUsername(data.getUsername())) {
            errors.add(String.format("第%d行: 用户名'%s'已存在", rowNum, data.getUsername()));
        }

        // 姓名校验
        if (StrUtil.isBlank(data.getRealName())) {
            errors.add(String.format("第%d行: 姓名不能为空", rowNum));
        }

        // 手机号校验
        if (StrUtil.isNotBlank(data.getPhone()) && !PHONE_PATTERN.matcher(data.getPhone()).matches()) {
            errors.add(String.format("第%d行: 手机号格式不正确", rowNum));
        }

        // 邮箱校验
        if (StrUtil.isNotBlank(data.getEmail()) && !EMAIL_PATTERN.matcher(data.getEmail()).matches()) {
            errors.add(String.format("第%d行: 邮箱格式不正确", rowNum));
        }

        return errors;
    }

    private boolean existsByUsername(String username) {
        return sysUserService.count(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
        ) > 0;
    }
}
