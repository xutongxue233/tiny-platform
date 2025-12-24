import { Select, SelectProps } from 'antd';
import React, { useEffect, useState } from 'react';
import { getDictItemsByCode } from '@/services/ant-design-pro/dict';

interface DictSelectProps extends Omit<SelectProps, 'options'> {
  dictCode: string;
}

const DictSelect: React.FC<DictSelectProps> = ({ dictCode, ...restProps }) => {
  const [options, setOptions] = useState<{ label: string; value: string }[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!dictCode) return;
    setLoading(true);
    getDictItemsByCode(dictCode)
      .then((items) => {
        setOptions(
          items.map((item) => ({
            label: item.itemLabel || '',
            value: item.itemValue || '',
          })),
        );
      })
      .finally(() => {
        setLoading(false);
      });
  }, [dictCode]);

  return <Select loading={loading} options={options} {...restProps} />;
};

export default DictSelect;
