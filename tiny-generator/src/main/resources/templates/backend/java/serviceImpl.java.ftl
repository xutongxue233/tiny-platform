package ${packageName}.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.dto.${className}DTO;
import ${packageName}.dto.${className}Query;
import ${packageName}.entity.${className};
import ${packageName}.mapper.${className}Mapper;
import ${packageName}.service.${className}Service;
import ${packageName}.vo.${className}VO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${functionName!tableComment} Service实现
 *
 * @author ${author}
 * @date ${datetime}
 */
@Service
@RequiredArgsConstructor
public class ${className}ServiceImpl extends ServiceImpl<${className}Mapper, ${className}> implements ${className}Service {

    @Override
    public List<${className}VO> selectList(${className}Query query) {
        LambdaQueryWrapper<${className}> wrapper = new LambdaQueryWrapper<>();
<#list queryColumns as column>
<#if column.queryType == "EQ">
        wrapper.eq(<#if column.javaType == "String">StrUtil.isNotBlank(query.get${column.capJavaField}())<#else>query.get${column.capJavaField}() != null</#if>, ${className}::get${column.capJavaField}, query.get${column.capJavaField}());
<#elseif column.queryType == "LIKE">
        wrapper.like(StrUtil.isNotBlank(query.get${column.capJavaField}()), ${className}::get${column.capJavaField}, query.get${column.capJavaField}());
<#elseif column.queryType == "BETWEEN">
        wrapper.ge(query.getBegin${column.capJavaField}() != null, ${className}::get${column.capJavaField}, query.getBegin${column.capJavaField}());
        wrapper.le(query.getEnd${column.capJavaField}() != null, ${className}::get${column.capJavaField}, query.getEnd${column.capJavaField}());
</#if>
</#list>
        wrapper.orderByDesc(${className}::getCreateTime);

        List<${className}> list = list(wrapper);
        return BeanUtil.copyToList(list, ${className}VO.class);
    }

    @Override
    public ${className}VO selectById(${pkColumn.javaType} ${pkColumn.javaField}) {
        ${className} entity = getById(${pkColumn.javaField});
        return BeanUtil.copyProperties(entity, ${className}VO.class);
    }

    @Override
    public void insert(${className}DTO dto) {
        ${className} entity = BeanUtil.copyProperties(dto, ${className}.class);
        save(entity);
    }

    @Override
    public void update(${className}DTO dto) {
        ${className} entity = BeanUtil.copyProperties(dto, ${className}.class);
        updateById(entity);
    }

    @Override
    public void deleteByIds(List<${pkColumn.javaType}> ${pkColumn.javaField}s) {
        removeByIds(${pkColumn.javaField}s);
    }
}
