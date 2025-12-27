package com.tiny.export.vo;

import com.tiny.export.dto.ImportResultDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 导入结果VO
 */
@Data
@Schema(description = "导入结果")
public class ImportResultVO {

    @Schema(description = "总记录数")
    private Integer totalCount;

    @Schema(description = "成功记录数")
    private Integer successCount;

    @Schema(description = "失败记录数")
    private Integer failCount;

    @Schema(description = "错误信息列表")
    private List<String> errorMessages;

    /**
     * 从DTO转换为VO
     */
    public static ImportResultVO from(ImportResultDTO dto) {
        ImportResultVO vo = new ImportResultVO();
        vo.setTotalCount(dto.getTotalCount());
        vo.setSuccessCount(dto.getSuccessCount());
        vo.setFailCount(dto.getFailCount());
        vo.setErrorMessages(dto.getErrorMessages());
        return vo;
    }
}
