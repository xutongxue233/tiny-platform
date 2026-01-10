import { Select, SelectProps } from 'antd';
import React from 'react';
import { useDict } from '@/hooks/useDict';

interface DictSelectProps extends Omit<SelectProps, 'options'> {
  dictCode: string;
}

/**
 * 字典选择器组件
 * 使用 useDict hook 自动获取字典数据并缓存
 */
const DictSelect: React.FC<DictSelectProps> = ({ dictCode, ...restProps }) => {
  const { options, loading } = useDict(dictCode);

  return <Select loading={loading} options={options} {...restProps} />;
};

export default DictSelect;
