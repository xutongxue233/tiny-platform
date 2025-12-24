import { Radio, RadioGroupProps } from 'antd';
import React, { useEffect, useState } from 'react';
import { getDictItemsByCode } from '@/services/ant-design-pro/dict';

interface DictRadioProps extends Omit<RadioGroupProps, 'options'> {
  dictCode: string;
  type?: 'default' | 'button';
}

const DictRadio: React.FC<DictRadioProps> = ({ dictCode, type = 'default', ...restProps }) => {
  const [options, setOptions] = useState<{ label: string; value: string }[]>([]);

  useEffect(() => {
    if (!dictCode) return;
    getDictItemsByCode(dictCode).then((items) => {
      setOptions(
        items.map((item) => ({
          label: item.itemLabel || '',
          value: item.itemValue || '',
        })),
      );
    });
  }, [dictCode]);

  if (type === 'button') {
    return (
      <Radio.Group {...restProps}>
        {options.map((option) => (
          <Radio.Button key={option.value} value={option.value}>
            {option.label}
          </Radio.Button>
        ))}
      </Radio.Group>
    );
  }

  return <Radio.Group options={options} {...restProps} />;
};

export default DictRadio;
