import { getDeptTreeSelect } from '@/services/ant-design-pro/api';
import { useRequest } from '@umijs/max';
import { Tree, Spin } from 'antd';
import type { Key } from 'react';
import React, { useCallback, useEffect, useState } from 'react';

interface DeptTreeSelectProps {
  value?: number[];
  onChange?: (value: number[]) => void;
}

const DeptTreeSelect: React.FC<DeptTreeSelectProps> = ({ value, onChange }) => {
  const [expandedKeys, setExpandedKeys] = useState<Key[]>([]);
  const [checkedKeys, setCheckedKeys] = useState<Key[]>([]);

  const { data, loading } = useRequest(getDeptTreeSelect, {
    formatResult: (res) => res.data || [],
  });

  useEffect(() => {
    if (value) {
      setCheckedKeys(value.map((id) => id));
    }
  }, [value]);

  const convertToTreeData = useCallback((depts: API.SysDept[]): any[] => {
    return depts.map((dept) => ({
      title: dept.deptName,
      key: dept.deptId,
      children: dept.children ? convertToTreeData(dept.children) : undefined,
    }));
  }, []);

  const treeData = data ? convertToTreeData(data) : [];

  const handleCheck = useCallback(
    (checked: Key[] | { checked: Key[]; halfChecked: Key[] }) => {
      const checkedList = Array.isArray(checked) ? checked : checked.checked;
      setCheckedKeys(checkedList);
      onChange?.(checkedList.map((key) => Number(key)));
    },
    [onChange],
  );

  const handleExpand = useCallback((keys: Key[]) => {
    setExpandedKeys(keys);
  }, []);

  if (loading) {
    return <Spin size="small" />;
  }

  return (
    <Tree
      checkable
      expandedKeys={expandedKeys}
      onExpand={handleExpand}
      checkedKeys={checkedKeys}
      onCheck={handleCheck}
      treeData={treeData}
      style={{
        border: '1px solid #d9d9d9',
        borderRadius: 6,
        padding: 8,
        maxHeight: 300,
        overflow: 'auto',
      }}
    />
  );
};

export default DeptTreeSelect;
