import { Tag } from 'antd';
import React, { useEffect, useState } from 'react';
import { getDictItemsByCode } from '@/services/ant-design-pro/dict';

interface DictTagProps {
  dictCode: string;
  value: string;
  colorMap?: Record<string, string>;
}

const DictTag: React.FC<DictTagProps> = ({ dictCode, value, colorMap }) => {
  const [label, setLabel] = useState<string>(value);
  const [listClass, setListClass] = useState<string | undefined>();

  useEffect(() => {
    if (!dictCode || !value) return;
    getDictItemsByCode(dictCode).then((items) => {
      const item = items.find((i) => i.itemValue === value);
      if (item) {
        setLabel(item.itemLabel || value);
        setListClass(item.listClass || undefined);
      }
    });
  }, [dictCode, value]);

  const getColor = () => {
    if (colorMap && colorMap[value]) {
      return colorMap[value];
    }
    if (listClass) {
      const colorMapping: Record<string, string> = {
        primary: 'blue',
        success: 'green',
        warning: 'orange',
        danger: 'red',
        info: 'cyan',
        default: 'default',
      };
      return colorMapping[listClass] || listClass;
    }
    return 'default';
  };

  return <Tag color={getColor()}>{label}</Tag>;
};

export default DictTag;
