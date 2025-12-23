import { getRoleMenuTree } from '@/services/ant-design-pro/api';
import { Spin, Tree } from 'antd';
import type { DataNode, TreeProps } from 'antd/es/tree';
import type { FC } from 'react';
import { useCallback, useEffect, useMemo, useState } from 'react';

interface MenuTreeSelectProps {
  value?: number[];
  onChange?: (value: number[]) => void;
}

const MenuTreeSelect: FC<MenuTreeSelectProps> = ({ value, onChange }) => {
  const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState(true);
  const [menuData, setMenuData] = useState<API.SysMenu[]>([]);
  const [loading, setLoading] = useState(true);

  // 加载菜单数据
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const res = await getRoleMenuTree();
        if (res.code === 200 && res.data) {
          setMenuData(res.data);
        }
      } catch (err) {
        console.error('Failed to fetch menu tree:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  // 数据加载完成后展开所有节点
  useEffect(() => {
    if (menuData && menuData.length > 0 && expandedKeys.length === 0) {
      const keys: React.Key[] = [];
      const collectKeys = (menus: API.SysMenu[]) => {
        menus?.forEach((menu) => {
          if (menu.menuId) {
            keys.push(menu.menuId);
          }
          if (menu.children && menu.children.length > 0) {
            collectKeys(menu.children);
          }
        });
      };
      collectKeys(menuData);
      setExpandedKeys(keys);
    }
  }, [menuData]);

  // 构建菜单映射关系
  const menuMap = useMemo(() => {
    const map = new Map<number, { parentId: number; childIds: number[] }>();
    const buildMap = (menus: API.SysMenu[], parentId: number = 0) => {
      menus?.forEach((menu) => {
        const childIds: number[] = [];
        if (menu.children && menu.children.length > 0) {
          menu.children.forEach((child) => {
            if (child.menuId) childIds.push(child.menuId);
          });
          buildMap(menu.children, menu.menuId!);
        }
        if (menu.menuId) {
          map.set(menu.menuId, { parentId, childIds });
        }
      });
    };
    buildMap(menuData);
    return map;
  }, [menuData]);

  // 获取所有子节点ID（递归）
  const getAllChildIds = useCallback((menuId: number): number[] => {
    const result: number[] = [];
    const item = menuMap.get(menuId);
    if (item && item.childIds.length > 0) {
      item.childIds.forEach((childId) => {
        result.push(childId);
        result.push(...getAllChildIds(childId));
      });
    }
    return result;
  }, [menuMap]);

  // 获取所有父节点ID（递归）
  const getAllParentIds = useCallback((menuId: number): number[] => {
    const result: number[] = [];
    const item = menuMap.get(menuId);
    if (item && item.parentId !== 0) {
      result.push(item.parentId);
      result.push(...getAllParentIds(item.parentId));
    }
    return result;
  }, [menuMap]);

  const treeData = useMemo(() => {
    const convertToTreeData = (menus: API.SysMenu[]): DataNode[] => {
      return menus.map((menu) => ({
        key: menu.menuId!,
        title: menu.menuName,
        children: menu.children && menu.children.length > 0
          ? convertToTreeData(menu.children)
          : undefined,
      }));
    };
    return menuData ? convertToTreeData(menuData) : [];
  }, [menuData]);

  const onExpand: TreeProps['onExpand'] = useCallback((expandedKeysValue) => {
    setExpandedKeys(expandedKeysValue);
    setAutoExpandParent(false);
  }, []);

  const onCheck: TreeProps['onCheck'] = useCallback(
    (checked: React.Key[] | { checked: React.Key[]; halfChecked: React.Key[] }, info: any) => {
      const currentChecked = value || [];
      const nodeKey = info.node.key as number;
      const isChecking = info.checked;

      let newChecked: number[];

      if (isChecking) {
        // 选中：添加当前节点 + 所有子节点 + 所有父节点
        const childIds = getAllChildIds(nodeKey);
        const parentIds = getAllParentIds(nodeKey);
        const toAdd = [nodeKey, ...childIds, ...parentIds];
        newChecked = [...new Set([...currentChecked, ...toAdd])];
      } else {
        // 取消选中：只移除当前节点（不影响子节点和父节点）
        newChecked = currentChecked.filter((id) => id !== nodeKey);
      }

      onChange?.(newChecked);
    },
    [value, onChange, getAllChildIds, getAllParentIds],
  );

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '20px' }}>
        <Spin />
      </div>
    );
  }

  if (!treeData || treeData.length === 0) {
    return <div style={{ color: '#999', padding: '8px 0' }}>暂无菜单数据</div>;
  }

  return (
    <div style={{ border: '1px solid #d9d9d9', borderRadius: 6, padding: 8, maxHeight: 300, overflow: 'auto' }}>
      <Tree
        checkable
        checkStrictly
        expandedKeys={expandedKeys}
        autoExpandParent={autoExpandParent}
        checkedKeys={{ checked: value || [], halfChecked: [] }}
        onExpand={onExpand}
        onCheck={onCheck}
        treeData={treeData}
      />
    </div>
  );
};

export default MenuTreeSelect;
