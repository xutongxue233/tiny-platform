package com.tiny.generator.core.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 默认代码写入器实现
 */
@Slf4j
@Component
public class DefaultCodeWriter implements CodeWriter {

    @Override
    public void write(String basePath, String filePath, String content) {
        try {
            Path fullPath = Paths.get(basePath, filePath);
            // 创建父目录
            Files.createDirectories(fullPath.getParent());
            // 写入文件
            Files.writeString(fullPath, content, StandardCharsets.UTF_8);
            log.info("生成文件: {}", fullPath);
        } catch (IOException e) {
            log.error("写入文件失败: {}/{}", basePath, filePath, e);
            throw new RuntimeException("写入文件失败", e);
        }
    }

    @Override
    public byte[] packageAsZip(Map<String, String> files) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (Map.Entry<String, String> entry : files.entrySet()) {
                String filePath = entry.getKey();
                String content = entry.getValue();

                // 创建ZIP条目
                ZipEntry zipEntry = new ZipEntry(filePath);
                zos.putNextEntry(zipEntry);
                zos.write(content.getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }

            zos.finish();
            return baos.toByteArray();

        } catch (IOException e) {
            log.error("打包ZIP失败", e);
            throw new RuntimeException("打包ZIP失败", e);
        }
    }
}
