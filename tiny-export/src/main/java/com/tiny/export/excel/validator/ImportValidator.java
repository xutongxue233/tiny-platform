package com.tiny.export.excel.validator;

import java.util.List;

/**
 * 导入数据校验器接口
 *
 * @param <T> 数据类型
 */
public interface ImportValidator<T> {

    /**
     * 校验数据
     *
     * @param data   数据对象
     * @param rowNum 行号
     * @return 错误信息列表，为空表示校验通过
     */
    List<String> validate(T data, int rowNum);
}
