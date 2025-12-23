import { getDeptTreeSelect } from '@/services/ant-design-pro/api';
import { useRequest } from '@umijs/max';
import { Input, Tree, Spin, Card } from 'antd';
import type { Key } from 'react';
import React, { useCallback, useEffect, useMemo, useState } from 'react';

const { Search } = Input;

interface DeptTreeProps {
  onSelect?: (deptId?: number) => void;
}

interface TreeNode {
  title: string;
  key: number;
  children?: TreeNode[];
}

const DeptTree: React.FC<DeptTreeProps> = ({ onSelect }) => {
  const [expandedKeys, setExpandedKeys] = useState<Key[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<Key[]>([]);
  const [searchValue, setSearchValue] = useState('');
  const [autoExpandParent, setAutoExpandParent] = useState(true);

  const { data, loading } = useRequest(getDeptTreeSelect, {
    formatResult: (res) => res.data || [],
  });

  const convertToTreeData = useCallback((depts: API.SysDept[]): TreeNode[] => {
    return depts.map((dept) => ({
      title: dept.deptName || '',
      key: dept.deptId!,
      children: dept.children ? convertToTreeData(dept.children) : undefined,
    }));
  }, []);

  const treeData = useMemo(() => {
    return data ? convertToTreeData(data) : [];
  }, [data, convertToTreeData]);

  const getAllKeys = useCallback((nodes: TreeNode[]): Key[] => {
    let keys: Key[] = [];
    nodes.forEach((node) => {
      keys.push(node.key);
      if (node.children) {
        keys = keys.concat(getAllKeys(node.children));
      }
    });
    return keys;
  }, []);

  useEffect(() => {
    if (treeData.length > 0) {
      setExpandedKeys(getAllKeys(treeData));
    }
  }, [treeData, getAllKeys]);

  const getParentKey = useCallback((key: Key, tree: TreeNode[]): Key | undefined => {
    let parentKey: Key | undefined;
    for (let i = 0; i < tree.length; i++) {
      const node = tree[i];
      if (node.children) {
        if (node.children.some((item) => item.key === key)) {
          parentKey = node.key;
        } else {
          const found = getParentKey(key, node.children);
          if (found) {
            parentKey = found;
          }
        }
      }
    }
    return parentKey;
  }, []);

  const dataList = useMemo(() => {
    const list: { key: Key; title: string }[] = [];
    const generateList = (nodes: TreeNode[]) => {
      for (let i = 0; i < nodes.length; i++) {
        const node = nodes[i];
        list.push({ key: node.key, title: node.title });
        if (node.children) {
          generateList(node.children);
        }
      }
    };
    generateList(treeData);
    return list;
  }, [treeData]);

  const handleSearch = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const { value } = e.target;
      if (!value) {
        setSearchValue('');
        setExpandedKeys(getAllKeys(treeData));
        setAutoExpandParent(false);
        return;
      }
      const newExpandedKeys = dataList
        .map((item) => {
          if (item.title.indexOf(value) > -1) {
            return getParentKey(item.key, treeData);
          }
          return null;
        })
        .filter((item, i, self): item is Key => !!(item && self.indexOf(item) === i));
      setExpandedKeys(newExpandedKeys);
      setSearchValue(value);
      setAutoExpandParent(true);
    },
    [dataList, getParentKey, treeData, getAllKeys],
  );

  const handleExpand = useCallback((keys: Key[]) => {
    setExpandedKeys(keys);
    setAutoExpandParent(false);
  }, []);

  const handleSelect = useCallback(
    (keys: Key[]) => {
      setSelectedKeys(keys);
      if (keys.length > 0) {
        onSelect?.(Number(keys[0]));
      } else {
        onSelect?.(undefined);
      }
    },
    [onSelect],
  );

  const filterTreeNode = useCallback(
    (node: TreeNode): TreeNode | null => {
      const title = node.title;
      const matchSearch = title.indexOf(searchValue) > -1;

      if (node.children) {
        const filteredChildren = node.children
          .map(filterTreeNode)
          .filter((child): child is TreeNode => child !== null);

        if (filteredChildren.length > 0 || matchSearch) {
          return {
            ...node,
            children: filteredChildren.length > 0 ? filteredChildren : undefined,
          };
        }
      }

      return matchSearch ? node : null;
    },
    [searchValue],
  );

  const filteredTreeData = useMemo(() => {
    if (!searchValue) {
      return treeData;
    }
    return treeData
      .map(filterTreeNode)
      .filter((node): node is TreeNode => node !== null);
  }, [treeData, searchValue, filterTreeNode]);

  const titleRender = useCallback(
    (nodeData: TreeNode) => {
      const title = nodeData.title;
      const index = title.indexOf(searchValue);
      const beforeStr = title.substring(0, index);
      const afterStr = title.slice(index + searchValue.length);
      const titleNode =
        index > -1 ? (
          <span>
            {beforeStr}
            <span style={{ color: '#f50' }}>{searchValue}</span>
            {afterStr}
          </span>
        ) : (
          <span>{title}</span>
        );
      return titleNode;
    },
    [searchValue],
  );

  if (loading) {
    return (
      <Card title="部门" size="small" style={{ height: '100%' }}>
        <div style={{ display: 'flex', justifyContent: 'center', padding: 24 }}>
          <Spin />
        </div>
      </Card>
    );
  }

  return (
    <Card title="部门" size="small" style={{ height: '100%' }}>
      <Search
        style={{ marginBottom: 8 }}
        placeholder="搜索部门"
        onChange={handleSearch}
        allowClear
      />
      <div style={{ maxHeight: 'calc(100vh - 280px)', overflow: 'auto' }}>
        <Tree
          showLine={{ showLeafIcon: false }}
          expandedKeys={expandedKeys}
          autoExpandParent={autoExpandParent}
          onExpand={handleExpand}
          selectedKeys={selectedKeys}
          onSelect={handleSelect}
          treeData={filteredTreeData}
          titleRender={titleRender}
        />
      </div>
    </Card>
  );
};

export default DeptTree;
