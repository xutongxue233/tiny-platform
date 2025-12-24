package com.tiny.generator.core.engine;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * FreeMarker模板引擎实现
 */
@Slf4j
@Component
public class FreemarkerTemplateEngine implements TemplateEngine {

    private final Configuration configuration;

    public FreemarkerTemplateEngine() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_32);

        // 设置模板加载器
        ClassTemplateLoader classLoader = new ClassTemplateLoader(getClass(), "/templates");
        TemplateLoader[] loaders = new TemplateLoader[]{classLoader};
        MultiTemplateLoader multiLoader = new MultiTemplateLoader(loaders);
        configuration.setTemplateLoader(multiLoader);

        // 设置编码
        configuration.setDefaultEncoding("UTF-8");

        // 设置异常处理
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
    }

    @Override
    public String render(String templatePath, Map<String, Object> variables) {
        try {
            Template template = configuration.getTemplate(templatePath);
            StringWriter writer = new StringWriter();
            template.process(variables, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            log.error("渲染模板失败: {}", templatePath, e);
            throw new RuntimeException("渲染模板失败: " + templatePath, e);
        }
    }

    @Override
    public String renderString(String templateContent, Map<String, Object> variables) {
        try {
            Template template = new Template("stringTemplate", new StringReader(templateContent), configuration);
            StringWriter writer = new StringWriter();
            template.process(variables, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            log.error("渲染模板字符串失败", e);
            throw new RuntimeException("渲染模板字符串失败", e);
        }
    }
}
