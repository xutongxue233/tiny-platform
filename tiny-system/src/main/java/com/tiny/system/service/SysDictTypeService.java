package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysDictTypeDTO;
import com.tiny.system.dto.SysDictTypeQueryDTO;
import com.tiny.system.entity.SysDictType;
import com.tiny.system.vo.SysDictTypeVO;

import java.util.List;

/**
 * 字典类型服务接口
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 分页查询字典类型列表
     */
    PageResult<SysDictTypeVO> page(SysDictTypeQueryDTO queryDTO);

    /**
     * 查询所有字典类型列表
     */
    List<SysDictTypeVO> listAll();

    /**
     * 查询字典类型详情
     */
    SysDictTypeVO getDetail(Long dictId);

    /**
     * 新增字典类型
     */
    void add(SysDictTypeDTO dto);

    /**
     * 修改字典类型
     */
    void update(SysDictTypeDTO dto);

    /**
     * 删除字典类型
     */
    void delete(Long dictId);

    /**
     * 批量删除字典类型
     */
    void deleteBatch(List<Long> dictIds);

    /**
     * 修改字典类型状态
     */
    void updateStatus(Long dictId, String status);

    /**
     * 根据字典编码查询字典类型
     */
    SysDictType getByDictCode(String dictCode);
}
