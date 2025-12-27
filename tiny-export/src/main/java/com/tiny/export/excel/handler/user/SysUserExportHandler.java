package com.tiny.export.excel.handler.user;

import cn.hutool.core.bean.BeanUtil;
import com.tiny.export.dto.user.SysUserExportDTO;
import com.tiny.export.excel.handler.AbstractExcelExportHandler;
import com.tiny.system.dto.SysUserQueryDTO;
import com.tiny.system.service.SysUserService;
import com.tiny.system.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户导出处理器
 */
@Component
@RequiredArgsConstructor
public class SysUserExportHandler extends AbstractExcelExportHandler<SysUserExportDTO, SysUserQueryDTO> {

    private final SysUserService sysUserService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Class<SysUserExportDTO> getDataClass() {
        return SysUserExportDTO.class;
    }

    @Override
    public String getSheetName() {
        return "用户列表";
    }

    @Override
    public String getFileName() {
        return "用户数据";
    }

    @Override
    public List<SysUserExportDTO> queryDataByPage(SysUserQueryDTO params, int page, int size) {
        if (params == null) {
            params = new SysUserQueryDTO();
        }
        params.setCurrent((long) page);
        params.setSize((long) size);

        return sysUserService.page(params).getRecords().stream()
                .map(this::convertToExportDTO)
                .collect(Collectors.toList());
    }

    private SysUserExportDTO convertToExportDTO(SysUserVO vo) {
        SysUserExportDTO dto = new SysUserExportDTO();
        BeanUtil.copyProperties(vo, dto);

        // 性别转换
        dto.setGender(convertGender(vo.getGender()));
        // 状态转换
        dto.setStatus("0".equals(vo.getStatus()) ? "正常" : "停用");
        // 时间格式化
        if (vo.getCreateTime() != null) {
            dto.setCreateTime(vo.getCreateTime().format(FORMATTER));
        }

        return dto;
    }

    private String convertGender(String gender) {
        if (gender == null) {
            return "未知";
        }
        return switch (gender) {
            case "0" -> "男";
            case "1" -> "女";
            default -> "未知";
        };
    }
}
