package com.tiny.export.excel.listener.user;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.tiny.export.dto.user.SysUserImportDTO;
import com.tiny.export.excel.listener.AbstractImportListener;
import com.tiny.export.excel.validator.ImportValidator;
import com.tiny.export.excel.validator.user.SysUserImportValidator;
import com.tiny.system.dto.SysUserDTO;
import com.tiny.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户导入监听器
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class SysUserImportListener extends AbstractImportListener<SysUserImportDTO> {

    private final SysUserService sysUserService;
    private final SysUserImportValidator validator;

    /**
     * 默认密码
     */
    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    public Class<SysUserImportDTO> getDataClass() {
        return SysUserImportDTO.class;
    }

    @Override
    protected ImportValidator<SysUserImportDTO> getValidator() {
        return validator;
    }

    @Override
    protected void doSave(List<SysUserImportDTO> dataList) {
        for (SysUserImportDTO data : dataList) {
            SysUserDTO userDTO = new SysUserDTO();
            userDTO.setUsername(data.getUsername());
            userDTO.setRealName(data.getRealName());
            userDTO.setPhone(data.getPhone());
            userDTO.setEmail(data.getEmail());
            userDTO.setGender(convertGender(data.getGender()));
            userDTO.setDeptId(data.getDeptId());
            userDTO.setRemark(data.getRemark());
            userDTO.setStatus("0"); // 默认正常状态

            // 设置密码
            String password = StrUtil.isNotBlank(data.getPassword())
                    ? data.getPassword() : DEFAULT_PASSWORD;
            userDTO.setPassword(SecureUtil.md5(password));

            sysUserService.add(userDTO);
        }
    }

    private String convertGender(String gender) {
        if (gender == null) {
            return "2";
        }
        return switch (gender.trim()) {
            case "男" -> "0";
            case "女" -> "1";
            default -> "2";
        };
    }
}
