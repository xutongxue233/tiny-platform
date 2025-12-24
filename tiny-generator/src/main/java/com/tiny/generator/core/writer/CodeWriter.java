package com.tiny.generator.core.writer;

import java.util.Map;

/**
 * 代码写入器接口
 */
public interface CodeWriter {

    /**
     * 写入文件到指定路径
     *
     * @param basePath 基础路径
     * @param filePath 相对文件路径
     * @param content  文件内容
     */
    void write(String basePath, String filePath, String content);

    /**
     * 打包为ZIP
     *
     * @param files 文件集合
     * @return ZIP字节数组
     */
    byte[] packageAsZip(Map<String, String> files);
}
