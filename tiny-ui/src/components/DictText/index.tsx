import React, { useEffect, useState } from 'react';
import { getDictItemsByCode } from '@/services/ant-design-pro/dict';

interface DictTextProps {
  dictCode: string;
  value: string;
  defaultText?: string;
}

const DictText: React.FC<DictTextProps> = ({ dictCode, value, defaultText = '-' }) => {
  const [label, setLabel] = useState<string>(defaultText);

  useEffect(() => {
    if (!dictCode || value === undefined || value === null || value === '') {
      setLabel(defaultText);
      return;
    }
    getDictItemsByCode(dictCode).then((items) => {
      const item = items.find((i) => i.itemValue === value);
      setLabel(item?.itemLabel || defaultText);
    });
  }, [dictCode, value, defaultText]);

  return <span>{label}</span>;
};

export default DictText;
