package com.tiny.export.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.tiny.export.excel.validator.ImportValidator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象导入监听器
 */
@Slf4j
@Getter
public abstract class AbstractImportListener<T> implements ExcelImportListener<T> {

    private final List<T> dataList = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();
    private int totalCount = 0;
    private int successCount = 0;
    private int failCount = 0;

    /**
     * 批量保存大小
     */
    private static final int BATCH_SIZE = 100;

    /**
     * 最大错误信息数量
     */
    private static final int MAX_ERROR_COUNT = 100;

    @Override
    public void invoke(T data, AnalysisContext context) {
        totalCount++;
        int rowNum = context.readRowHolder().getRowIndex() + 1;

        // 数据校验
        ImportValidator<T> validator = getValidator();
        if (validator != null) {
            List<String> errors = validator.validate(data, rowNum);
            if (!errors.isEmpty()) {
                failCount++;
                if (errorMessages.size() < MAX_ERROR_COUNT) {
                    errorMessages.addAll(errors);
                }
                return;
            }
        }

        dataList.add(data);

        // 批量保存
        if (dataList.size() >= BATCH_SIZE) {
            saveBatch();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 保存剩余数据
        if (!dataList.isEmpty()) {
            saveBatch();
        }
        log.info("导入完成: 总数={}, 成功={}, 失败={}", totalCount, successCount, failCount);
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("导入异常, 行号: {}", context.readRowHolder().getRowIndex(), exception);
        failCount++;
        if (errorMessages.size() < MAX_ERROR_COUNT) {
            errorMessages.add("第" + (context.readRowHolder().getRowIndex() + 1) + "行解析异常: " + exception.getMessage());
        }
    }

    private void saveBatch() {
        try {
            doSave(dataList);
            successCount += dataList.size();
        } catch (Exception e) {
            failCount += dataList.size();
            if (errorMessages.size() < MAX_ERROR_COUNT) {
                errorMessages.add("批量保存失败: " + e.getMessage());
            }
            log.error("批量保存失败", e);
        } finally {
            dataList.clear();
        }
    }

    /**
     * 获取数据校验器
     */
    protected abstract ImportValidator<T> getValidator();

    /**
     * 保存数据
     */
    protected abstract void doSave(List<T> dataList);
}
