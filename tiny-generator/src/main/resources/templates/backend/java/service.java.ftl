package ${packageName}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ${packageName}.dto.${className}DTO;
import ${packageName}.dto.${className}Query;
import ${packageName}.entity.${className};
import ${packageName}.vo.${className}VO;

import java.util.List;

/**
 * ${functionName!tableComment} Service接口
 *
 * @author ${author}
 * @date ${datetime}
 */
public interface ${className}Service extends IService<${className}> {

    /**
     * 查询${functionName!tableComment}列表
     */
    List<${className}VO> selectList(${className}Query query);

    /**
     * 根据ID查询${functionName!tableComment}
     */
    ${className}VO selectById(${pkColumn.javaType} ${pkColumn.javaField});

    /**
     * 新增${functionName!tableComment}
     */
    void insert(${className}DTO dto);

    /**
     * 修改${functionName!tableComment}
     */
    void update(${className}DTO dto);

    /**
     * 批量删除${functionName!tableComment}
     */
    void deleteByIds(List<${pkColumn.javaType}> ${pkColumn.javaField}s);
}
