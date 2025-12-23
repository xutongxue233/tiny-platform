import { getMenuTree } from '@/services/ant-design-pro/api';
import { useRequest } from '@umijs/max';
import { Spin, Tree } from 'antd';
import type { DataNode, TreeProps } from 'antd/es/tree';
import type { FC } from 'react';
import { useCallback, useEffect, useMemo, useState } from 'react';

interface MenuTreeSelectProps {
  value?: number[];
  onChange?: (value: number[]) => void;
}

const MenuTreeSelect: FC<MenuTreeSelectProps> = ({ value = [], onChange }) => {
  const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
  const [checkedKeys, setCheckedKeys] = useState<React.Key[]>(value);
  const [autoExpandParent, setAutoExpandParent] = useState(true);

  const { data: menuData, loading } = useRequest(
    async () => {
      const res = await getMenuTree({});
      return res.data || [];
    },
    {
      onSuccess: (data) => {
        const keys: React.Key[] = [];
        const collectKeys = (menus: API.SysMenu[]) => {
          menus.forEach((menu) => {
            if (menu.menuId) {
              keys.push(menu.menuId);
            }
            if (menu.children && menu.children.length > 0) {
              collectKeys(menu.children);
            }
          });
        };
        collectKeys(data);
        setExpandedKeys(keys);
      },
    },
  );

  useEffect(() => {
    setCheckedKeys(value);
  }, [value]);

  const treeData = useMemo(() => {
    const convertToTreeData = (menus: API.SysMenu[]): DataNode[] => {
      return menus.map((menu) => ({
        key: menu.menuId!,
        title: menu.menuName,
        children: menu.children ? convertToTreeData(menu.children) : undefined,
      }));
    };
    return menuData ? convertToTreeData(menuData) : [];
  }, [menuData]);

  const onExpand: TreeProps['onExpand'] = (expandedKeysValue) => {
    setExpandedKeys(expandedKeysValue);
    setAutoExpandParent(false);
  };

  const onCheck: TreeProps['onCheck'] = useCallback(
    (checked: React.Key[] | { checked: React.Key[]; halfChecked: React.Key[] }) => {
      const keys = Array.isArray(checked) ? checked : checked.checked;
      setCheckedKeys(keys);
      onChange?.(keys as number[]);
    },
    [onChange],
  );

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '20px' }}>
        <Spin />
      </div>
    );
  }

  return (
    <Tree
      checkable
      checkStrictly
      expandedKeys={expandedKeys}
      autoExpandParent={autoExpandParent}
      checkedKeys={checkedKeys}
      onExpand={onExpand}
      onCheck={onCheck}
      treeData={treeData}
      style={{ maxHeight: 300, overflow: 'auto' }}
    />
  );
};

export default MenuTreeSelect;
