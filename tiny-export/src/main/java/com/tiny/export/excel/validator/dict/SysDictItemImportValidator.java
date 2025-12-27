package com.tiny.export.excel.validator.dict;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tiny.export.dto.dict.SysDictItemImportDTO;
import com.tiny.export.excel.validator.ImportValidator;
import com.tiny.system.entity.SysDictType;
import com.tiny.system.service.SysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典项导入校验器
 */
@Component
@RequiredArgsConstructor
public class SysDictItemImportValidator implements ImportValidator<SysDictItemImportDTO> {

    private final SysDictTypeService sysDictTypeService;

    @Override
    public List<String> validate(SysDictItemImportDTO data, int rowNum) {
        List<String> errors = new ArrayList<>();

        // 字典编码校验
        if (StrUtil.isBlank(data.getDictCode())) {
            errors.add(String.format("第%d行: 字典编码不能为空", rowNum));
        } else if (!existsDictCode(data.getDictCode())) {
            errors.add(String.format("第%d行: 字典编码'%s'不存在", rowNum, data.getDictCode()));
        }

        // 标签校验
        if (StrUtil.isBlank(data.getItemLabel())) {
            errors.add(String.format("第%d行: 标签不能为空", rowNum));
        } else if (data.getItemLabel().length() > 100) {
            errors.add(String.format("第%d行: 标签长度不能超过100个字符", rowNum));
        }

        // 值校验
        if (StrUtil.isBlank(data.getItemValue())) {
            errors.add(String.format("第%d行: 值不能为空", rowNum));
        } else if (data.getItemValue().length() > 100) {
            errors.add(String.format("第%d行: 值长度不能超过100个字符", rowNum));
        }

        return errors;
    }

    private boolean existsDictCode(String dictCode) {
        return sysDictTypeService.count(
                new LambdaQueryWrapper<SysDictType>()
                        .eq(SysDictType::getDictCode, dictCode)
        ) > 0;
    }
}
