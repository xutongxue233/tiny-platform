package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysDictItemDTO;
import com.tiny.system.dto.SysDictItemQueryDTO;
import com.tiny.system.entity.SysDictItem;
import com.tiny.system.vo.SysDictItemVO;

import java.util.List;

/**
 * 字典项服务接口
 */
public interface SysDictItemService extends IService<SysDictItem> {

    /**
     * 分页查询字典项列表
     */
    PageResult<SysDictItemVO> page(SysDictItemQueryDTO queryDTO);

    /**
     * 根据字典编码查询字典项列表
     */
    List<SysDictItemVO> listByDictCode(String dictCode);

    /**
     * 根据字典编码查询启用状态的字典项列表
     */
    List<SysDictItemVO> listEnabledByDictCode(String dictCode);

    /**
     * 查询字典项详情
     */
    SysDictItemVO getDetail(Long itemId);

    /**
     * 新增字典项
     */
    void add(SysDictItemDTO dto);

    /**
     * 修改字典项
     */
    void update(SysDictItemDTO dto);

    /**
     * 删除字典项
     */
    void delete(Long itemId);

    /**
     * 批量删除字典项
     */
    void deleteBatch(List<Long> itemIds);

    /**
     * 修改字典项状态
     */
    void updateStatus(Long itemId, String status);

    /**
     * 根据字典编码删除所有字典项
     */
    void deleteByDictCode(String dictCode);

    /**
     * 根据字典编码统计字典项数量
     */
    long countByDictCode(String dictCode);
}
