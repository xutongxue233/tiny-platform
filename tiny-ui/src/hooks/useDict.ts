import { useCallback, useEffect, useState } from 'react';
import { getDictItemsByCode, getDictValueEnum } from '@/services/ant-design-pro/dict';

export interface DictItem {
  label: string;
  value: string;
}

export function useDict(dictCode: string) {
  const [dictItems, setDictItems] = useState<DictItem[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchDict = useCallback(async () => {
    if (!dictCode) return;
    setLoading(true);
    try {
      const items = await getDictItemsByCode(dictCode);
      setDictItems(
        items.map((item) => ({
          label: item.itemLabel || '',
          value: item.itemValue || '',
        })),
      );
    } finally {
      setLoading(false);
    }
  }, [dictCode]);

  useEffect(() => {
    fetchDict();
  }, [fetchDict]);

  const getLabel = useCallback(
    (value: string) => {
      const item = dictItems.find((i) => i.value === value);
      return item?.label || value;
    },
    [dictItems],
  );

  return {
    dictItems,
    loading,
    getLabel,
    reload: fetchDict,
  };
}

export function useDictValueEnum(dictCode: string) {
  const [valueEnum, setValueEnum] = useState<Record<string, { text: string }>>({});
  const [loading, setLoading] = useState(false);

  const fetchValueEnum = useCallback(async () => {
    if (!dictCode) return;
    setLoading(true);
    try {
      const result = await getDictValueEnum(dictCode);
      setValueEnum(result);
    } finally {
      setLoading(false);
    }
  }, [dictCode]);

  useEffect(() => {
    fetchValueEnum();
  }, [fetchValueEnum]);

  return {
    valueEnum,
    loading,
    reload: fetchValueEnum,
  };
}
