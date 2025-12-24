import { useAccess, useIntl, useRequest } from '@umijs/max';
import { List, message, Modal, Popconfirm, Space, Tag, Typography } from 'antd';
import React, { forwardRef, useCallback, useEffect, useImperativeHandle, useState } from 'react';
import {
  changeDictTypeStatus,
  deleteDictType,
  getDictTypeList,
} from '@/services/ant-design-pro/dict';
import DictTypeForm from './DictTypeForm';
import { DeleteOutlined, EditOutlined } from '@ant-design/icons';

interface DictTypeListProps {
  selectedDictType?: API.SysDictType;
  onSelect: (dictType?: API.SysDictType) => void;
  onStatusChange: () => void;
}

const DictTypeList = forwardRef<{ reload: () => void }, DictTypeListProps>(
  ({ selectedDictType, onSelect, onStatusChange }, ref) => {
    const [dictTypeList, setDictTypeList] = useState<API.SysDictType[]>([]);
    const intl = useIntl();
    const access = useAccess();
    const [messageApi, contextHolder] = message.useMessage();

    const { run: fetchList, loading } = useRequest(getDictTypeList, {
      manual: true,
      onSuccess: (res) => {
        if (res.code === 200 && res.data) {
          setDictTypeList(res.data);
          if (res.data.length > 0 && !selectedDictType) {
            onSelect(res.data[0]);
          }
        }
      },
    });

    const { run: deleteRun } = useRequest(deleteDictType, {
      manual: true,
      onSuccess: () => {
        messageApi.success(
          intl.formatMessage({ id: 'pages.dict.deleteSuccess', defaultMessage: '删除成功' }),
        );
        fetchList();
        onStatusChange();
        if (selectedDictType) {
          onSelect(undefined);
        }
      },
      onError: () => {
        messageApi.error(
          intl.formatMessage({ id: 'pages.dict.deleteFailed', defaultMessage: '删除失败' }),
        );
      },
    });

    const { run: changeStatusRun } = useRequest(changeDictTypeStatus, {
      manual: true,
      onSuccess: () => {
        messageApi.success(
          intl.formatMessage({ id: 'pages.dict.statusChangeSuccess', defaultMessage: '状态修改成功' }),
        );
        fetchList();
        onStatusChange();
      },
      onError: () => {
        messageApi.error(
          intl.formatMessage({ id: 'pages.dict.statusChangeFailed', defaultMessage: '状态修改失败' }),
        );
      },
    });

    useEffect(() => {
      fetchList();
    }, [fetchList]);

    useImperativeHandle(ref, () => ({
      reload: fetchList,
    }));

    const handleStatusChange = useCallback(
      (checked: boolean, record: API.SysDictType, e: React.MouseEvent) => {
        e.stopPropagation();
        Modal.confirm({
          title: intl.formatMessage({ id: 'pages.dict.confirmStatusChange', defaultMessage: '确认修改状态' }),
          content: intl.formatMessage(
            { id: 'pages.dict.statusChangeContent', defaultMessage: '确定要{action}字典类型"{name}"吗？' },
            { action: checked ? '启用' : '禁用', name: record.dictName },
          ),
          onOk: () => {
            changeStatusRun({ id: record.dictId!, status: checked ? '0' : '1' });
          },
        });
      },
      [changeStatusRun, intl],
    );

    const handleDelete = useCallback(
      (dictId: number, e?: React.MouseEvent) => {
        e?.stopPropagation();
        deleteRun(dictId);
      },
      [deleteRun],
    );

    return (
      <>
        {contextHolder}
        <List
          loading={loading}
          dataSource={dictTypeList}
          renderItem={(item) => (
            <List.Item
              key={item.dictId}
              onClick={() => onSelect(item)}
              style={{
                cursor: 'pointer',
                backgroundColor: selectedDictType?.dictId === item.dictId ? '#e6f7ff' : undefined,
                padding: '12px',
                borderRadius: '4px',
                marginBottom: '4px',
              }}
              actions={[
                access.hasPermission('system:dict:edit') && (
                  <DictTypeForm
                    key="edit"
                    values={item}
                    trigger={<EditOutlined onClick={(e) => e.stopPropagation()} />}
                    onOk={() => {
                      fetchList();
                      onStatusChange();
                    }}
                  />
                ),
                access.hasPermission('system:dict:remove') && (
                  <Popconfirm
                    key="delete"
                    title={intl.formatMessage({ id: 'pages.dict.confirmDelete', defaultMessage: '确定删除该字典类型吗？' })}
                    onConfirm={(e) => handleDelete(item.dictId!, e as React.MouseEvent)}
                    onCancel={(e) => e?.stopPropagation()}
                  >
                    <DeleteOutlined
                      style={{ color: '#ff4d4f' }}
                      onClick={(e) => e.stopPropagation()}
                    />
                  </Popconfirm>
                ),
              ].filter(Boolean)}
            >
              <List.Item.Meta
                title={
                  <Space>
                    <Typography.Text strong>{item.dictName}</Typography.Text>
                    <Tag color={item.status === '0' ? 'success' : 'error'}>
                      {item.status === '0' ? '正常' : '停用'}
                    </Tag>
                  </Space>
                }
                description={
                  <Typography.Text type="secondary" style={{ fontSize: '12px' }}>
                    {item.dictCode}
                  </Typography.Text>
                }
              />
            </List.Item>
          )}
        />
      </>
    );
  },
);

export default DictTypeList;
