package com.tiny.export.excel.handler.dict;

import cn.hutool.core.bean.BeanUtil;
import com.tiny.export.dto.dict.SysDictItemExportDTO;
import com.tiny.export.excel.handler.AbstractExcelExportHandler;
import com.tiny.system.dto.SysDictItemQueryDTO;
import com.tiny.system.service.SysDictItemService;
import com.tiny.system.vo.SysDictItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典项导出处理器
 */
@Component
@RequiredArgsConstructor
public class SysDictItemExportHandler extends AbstractExcelExportHandler<SysDictItemExportDTO, SysDictItemQueryDTO> {

    private final SysDictItemService sysDictItemService;

    @Override
    public Class<SysDictItemExportDTO> getDataClass() {
        return SysDictItemExportDTO.class;
    }

    @Override
    public String getSheetName() {
        return "字典数据";
    }

    @Override
    public String getFileName() {
        return "字典数据";
    }

    @Override
    public List<SysDictItemExportDTO> queryDataByPage(SysDictItemQueryDTO params, int page, int size) {
        if (params == null) {
            params = new SysDictItemQueryDTO();
        }
        params.setCurrent((long) page);
        params.setSize((long) size);

        return sysDictItemService.page(params).getRecords().stream()
                .map(this::convertToExportDTO)
                .collect(Collectors.toList());
    }

    private SysDictItemExportDTO convertToExportDTO(SysDictItemVO vo) {
        SysDictItemExportDTO dto = new SysDictItemExportDTO();
        BeanUtil.copyProperties(vo, dto);

        // 是否默认转换
        dto.setIsDefault("Y".equals(vo.getIsDefault()) ? "是" : "否");
        // 状态转换
        dto.setStatus("0".equals(vo.getStatus()) ? "正常" : "停用");

        return dto;
    }
}
