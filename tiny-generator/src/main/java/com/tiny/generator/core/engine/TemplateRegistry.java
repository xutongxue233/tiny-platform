package com.tiny.generator.core.engine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板注册表
 */
@Component
public class TemplateRegistry {

    private final List<TemplateDefinition> backendTemplates = new ArrayList<>();
    private final List<TemplateDefinition> frontendTemplates = new ArrayList<>();

    public TemplateRegistry() {
        initBackendTemplates();
        initFrontendTemplates();
    }

    /**
     * 初始化后端模板
     */
    private void initBackendTemplates() {
        // Entity
        backendTemplates.add(new TemplateDefinition(
                "backend/java/entity.java.ftl",
                "${className}.java",
                "java/${packagePath}/entity",
                "backend",
                true
        ));

        // Mapper
        backendTemplates.add(new TemplateDefinition(
                "backend/java/mapper.java.ftl",
                "${className}Mapper.java",
                "java/${packagePath}/mapper",
                "backend",
                true
        ));

        // Mapper XML
        backendTemplates.add(new TemplateDefinition(
                "backend/xml/mapper.xml.ftl",
                "${className}Mapper.xml",
                "resources/mapper/${moduleName}",
                "backend",
                true
        ));

        // Service
        backendTemplates.add(new TemplateDefinition(
                "backend/java/service.java.ftl",
                "${className}Service.java",
                "java/${packagePath}/service",
                "backend",
                true
        ));

        // ServiceImpl
        backendTemplates.add(new TemplateDefinition(
                "backend/java/serviceImpl.java.ftl",
                "${className}ServiceImpl.java",
                "java/${packagePath}/service/impl",
                "backend",
                true
        ));

        // Controller
        backendTemplates.add(new TemplateDefinition(
                "backend/java/controller.java.ftl",
                "${className}Controller.java",
                "java/${packagePath}/controller",
                "backend",
                true
        ));

        // DTO
        backendTemplates.add(new TemplateDefinition(
                "backend/java/dto.java.ftl",
                "${className}DTO.java",
                "java/${packagePath}/dto",
                "backend",
                true
        ));

        // Query
        backendTemplates.add(new TemplateDefinition(
                "backend/java/query.java.ftl",
                "${className}Query.java",
                "java/${packagePath}/dto",
                "backend",
                true
        ));

        // VO
        backendTemplates.add(new TemplateDefinition(
                "backend/java/vo.java.ftl",
                "${className}VO.java",
                "java/${packagePath}/vo",
                "backend",
                true
        ));
    }

    /**
     * 初始化前端模板
     */
    private void initFrontendTemplates() {
        // 页面入口
        frontendTemplates.add(new TemplateDefinition(
                "frontend/react-antd/index.tsx.ftl",
                "index.tsx",
                "src/pages/${moduleName}/${businessName}",
                "frontend",
                true
        ));

        // 表单组件
        frontendTemplates.add(new TemplateDefinition(
                "frontend/react-antd/components/Form.tsx.ftl",
                "${className}Form.tsx",
                "src/pages/${moduleName}/${businessName}/components",
                "frontend",
                true
        ));

        // API
        frontendTemplates.add(new TemplateDefinition(
                "frontend/react-antd/api.ts.ftl",
                "${businessName}.ts",
                "src/services/${moduleName}",
                "frontend",
                true
        ));

        // 类型定义
        frontendTemplates.add(new TemplateDefinition(
                "frontend/react-antd/types.d.ts.ftl",
                "${businessName}.d.ts",
                "src/services/${moduleName}",
                "frontend",
                true
        ));
    }

    /**
     * 获取后端模板列表
     */
    public List<TemplateDefinition> getBackendTemplates() {
        return backendTemplates.stream()
                .filter(TemplateDefinition::isEnabled)
                .toList();
    }

    /**
     * 获取前端模板列表
     */
    public List<TemplateDefinition> getFrontendTemplates() {
        return frontendTemplates.stream()
                .filter(TemplateDefinition::isEnabled)
                .toList();
    }

    /**
     * 获取所有模板列表
     */
    public List<TemplateDefinition> getAllTemplates() {
        List<TemplateDefinition> all = new ArrayList<>();
        all.addAll(getBackendTemplates());
        all.addAll(getFrontendTemplates());
        return all;
    }
}
