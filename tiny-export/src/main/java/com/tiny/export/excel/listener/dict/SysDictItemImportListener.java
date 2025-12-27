package com.tiny.export.excel.listener.dict;

import com.tiny.export.dto.dict.SysDictItemImportDTO;
import com.tiny.export.excel.listener.AbstractImportListener;
import com.tiny.export.excel.validator.ImportValidator;
import com.tiny.export.excel.validator.dict.SysDictItemImportValidator;
import com.tiny.system.dto.SysDictItemDTO;
import com.tiny.system.service.SysDictItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字典项导入监听器
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class SysDictItemImportListener extends AbstractImportListener<SysDictItemImportDTO> {

    private final SysDictItemService sysDictItemService;
    private final SysDictItemImportValidator validator;

    @Override
    public Class<SysDictItemImportDTO> getDataClass() {
        return SysDictItemImportDTO.class;
    }

    @Override
    protected ImportValidator<SysDictItemImportDTO> getValidator() {
        return validator;
    }

    @Override
    protected void doSave(List<SysDictItemImportDTO> dataList) {
        for (SysDictItemImportDTO data : dataList) {
            SysDictItemDTO dto = new SysDictItemDTO();
            dto.setDictCode(data.getDictCode());
            dto.setItemLabel(data.getItemLabel());
            dto.setItemValue(data.getItemValue());
            dto.setItemSort(data.getItemSort() != null ? data.getItemSort() : 0);
            dto.setCssClass(data.getCssClass());
            dto.setListClass(data.getListClass());
            dto.setRemark(data.getRemark());
            dto.setIsDefault("N");
            dto.setStatus("0"); // 默认正常状态

            sysDictItemService.add(dto);
        }
    }
}
