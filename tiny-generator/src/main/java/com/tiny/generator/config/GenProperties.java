package com.tiny.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 代码生成器配置属性
 */
@Data
@ConfigurationProperties(prefix = "tiny.generator")
public class GenProperties {

    /**
     * 作者
     */
    private String author = "generator";

    /**
     * 默认包路径
     */
    private String packageName = "com.tiny";

    /**
     * 表前缀列表
     */
    private List<String> tablePrefixes = List.of("sys_", "gen_");

    /**
     * 是否去除表前缀
     */
    private boolean removePrefix = true;

    /**
     * 后端模板路径
     */
    private String backendTemplatePath = "templates/backend";

    /**
     * 前端模板路径
     */
    private String frontendTemplatePath = "templates/frontend/react-antd";

    /**
     * 默认生成路径
     */
    private String genPath = "";
}
