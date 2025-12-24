import { useCallback, useEffect, useMemo, useState } from 'react';
import { getDictItemsByCode, clearDictCache } from '@/services/ant-design-pro/dict';

export interface DictItem {
  label: string;
  value: string;
  listClass?: string;
  cssClass?: string;
}

export interface DictValueEnumItem {
  text: string;
  status?: 'Success' | 'Error' | 'Processing' | 'Warning' | 'Default';
  color?: string;
}

const LIST_CLASS_TO_STATUS: Record<string, DictValueEnumItem['status']> = {
  success: 'Success',
  error: 'Error',
  processing: 'Processing',
  warning: 'Warning',
  default: 'Default',
};

export function useDict(dictCode: string) {
  const [dictItems, setDictItems] = useState<DictItem[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchDict = useCallback(async () => {
    if (!dictCode) {
      setLoading(false);
      return;
    }
    setLoading(true);
    try {
      const items = await getDictItemsByCode(dictCode);
      setDictItems(
        items.map((item) => ({
          label: item.itemLabel || '',
          value: item.itemValue || '',
          listClass: item.listClass,
          cssClass: item.cssClass,
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
    (value: string | undefined | null) => {
      if (value === undefined || value === null) return '';
      const item = dictItems.find((i) => i.value === value);
      return item?.label || value;
    },
    [dictItems],
  );

  const options = useMemo(
    () =>
      dictItems.map((item) => ({
        label: item.label,
        value: item.value,
      })),
    [dictItems],
  );

  return {
    dictItems,
    options,
    loading,
    getLabel,
    reload: fetchDict,
  };
}

export function useDictValueEnum(dictCode: string) {
  const [valueEnum, setValueEnum] = useState<Record<string, DictValueEnumItem>>({});
  const [loading, setLoading] = useState(false);

  const fetchValueEnum = useCallback(async () => {
    if (!dictCode) return;
    setLoading(true);
    try {
      const items = await getDictItemsByCode(dictCode);
      const result: Record<string, DictValueEnumItem> = {};
      items.forEach((item) => {
        if (item.itemValue) {
          const enumItem: DictValueEnumItem = {
            text: item.itemLabel || '',
          };
          if (item.listClass) {
            enumItem.status = LIST_CLASS_TO_STATUS[item.listClass];
          }
          if (item.cssClass) {
            enumItem.color = item.cssClass;
          }
          result[item.itemValue] = enumItem;
        }
      });
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

export function useDicts(dictCodes: string[]) {
  const [dictMap, setDictMap] = useState<Record<string, DictItem[]>>({});
  const [loading, setLoading] = useState(true);

  const dictCodesKey = dictCodes.join(',');

  useEffect(() => {
    if (!dictCodes.length) {
      setLoading(false);
      return;
    }
    setLoading(true);
    Promise.all(
      dictCodes.map(async (code) => {
        const items = await getDictItemsByCode(code);
        return {
          code,
          items: items.map((item) => ({
            label: item.itemLabel || '',
            value: item.itemValue || '',
            listClass: item.listClass,
            cssClass: item.cssClass,
          })),
        };
      }),
    )
      .then((results) => {
        const map: Record<string, DictItem[]> = {};
        results.forEach(({ code, items }) => {
          map[code] = items;
        });
        setDictMap(map);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [dictCodesKey]);

  const getOptions = useCallback(
    (dictCode: string) => {
      const items = dictMap[dictCode] || [];
      return items.map((item) => ({
        label: item.label,
        value: item.value,
      }));
    },
    [dictMap],
  );

  const getLabel = useCallback(
    (dictCode: string, value: string | undefined | null) => {
      if (value === undefined || value === null) return '';
      const items = dictMap[dictCode] || [];
      const item = items.find((i) => i.value === value);
      return item?.label || value;
    },
    [dictMap],
  );

  const getValueEnum = useCallback(
    (dictCode: string) => {
      const items = dictMap[dictCode] || [];
      const result: Record<string, DictValueEnumItem> = {};
      items.forEach((item) => {
        const enumItem: DictValueEnumItem = {
          text: item.label,
        };
        if (item.listClass) {
          enumItem.status = LIST_CLASS_TO_STATUS[item.listClass];
        }
        if (item.cssClass) {
          enumItem.color = item.cssClass;
        }
        result[item.value] = enumItem;
      });
      return result;
    },
    [dictMap],
  );

  const reload = useCallback(() => {
    if (!dictCodes.length) return;
    setLoading(true);
    dictCodes.forEach((code) => {
      clearDictCache(code);
    });
    Promise.all(
      dictCodes.map(async (code) => {
        const items = await getDictItemsByCode(code);
        return {
          code,
          items: items.map((item) => ({
            label: item.itemLabel || '',
            value: item.itemValue || '',
            listClass: item.listClass,
            cssClass: item.cssClass,
          })),
        };
      }),
    )
      .then((results) => {
        const map: Record<string, DictItem[]> = {};
        results.forEach(({ code, items }) => {
          map[code] = items;
        });
        setDictMap(map);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [dictCodesKey]);

  return {
    dictMap,
    loading,
    getOptions,
    getLabel,
    getValueEnum,
    reload,
  };
}
