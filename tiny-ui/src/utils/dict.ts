import { getDictItemsByCode, getDictValueEnum, clearDictCache } from '@/services/ant-design-pro/dict';

export { getDictItemsByCode, getDictValueEnum, clearDictCache };

/**
 * 根据字典编码和值获取标签
 */
export async function getDictLabel(dictCode: string, value: string | number): Promise<string> {
  if (value === undefined || value === null || value === '') {
    return '';
  }
  const items = await getDictItemsByCode(dictCode);
  const item = items.find((i) => i.itemValue === String(value));
  return item?.itemLabel || String(value);
}

/**
 * 根据字典编码和标签获取值
 */
export async function getDictValue(dictCode: string, label: string): Promise<string> {
  if (!label) {
    return '';
  }
  const items = await getDictItemsByCode(dictCode);
  const item = items.find((i) => i.itemLabel === label);
  return item?.itemValue || label;
}

/**
 * 获取字典选项列表
 */
export async function getDictOptions(dictCode: string): Promise<{ label: string; value: string }[]> {
  const items = await getDictItemsByCode(dictCode);
  return items.map((item) => ({
    label: item.itemLabel || '',
    value: item.itemValue || '',
  }));
}

/**
 * 获取默认字典项
 */
export async function getDictDefault(dictCode: string): Promise<API.SysDictItem | undefined> {
  const items = await getDictItemsByCode(dictCode);
  return items.find((item) => item.isDefault === 'Y');
}
