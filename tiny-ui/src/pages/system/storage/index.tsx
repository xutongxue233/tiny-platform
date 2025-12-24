import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import {
  Badge,
  Button,
  message,
  Popconfirm,
  Space,
  Tag,
} from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import {
  deleteStorageConfig,
  getStorageConfigPage,
  setDefaultStorageConfig,
  getStorageTypes,
} from '@/services/ant-design-pro/api';
import StorageConfigForm from './components/StorageConfigForm';

const StorageConfigList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [storageTypes, setStorageTypes] = useState<Record<string, string>>({});

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  useRequest(getStorageTypes, {
    onSuccess: (data) => {
      if (Array.isArray(data)) {
        const typeMap: Record<string, string> = {};
        data.forEach((item) => {
          typeMap[item.code] = item.desc;
        });
        setStorageTypes(typeMap);
      }
    },
  });

  const { run: delRun } = useRequest(deleteStorageConfig, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.storageConfig.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.storageConfig.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const { run: setDefaultRun } = useRequest(setDefaultStorageConfig, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.storageConfig.setDefaultSuccess', defaultMessage: '设置成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.storageConfig.setDefaultFailed', defaultMessage: '设置失败' }),
      );
    },
  });

  const handleSetDefault = useCallback(
    (configId: number) => {
      setDefaultRun(configId);
    },
    [setDefaultRun],
  );

  const columns: ProColumns<API.StorageConfigVO>[] = [
    {
      title: intl.formatMessage({ id: 'pages.storageConfig.configName', defaultMessage: '配置名称' }),
      dataIndex: 'configName',
      width: 150,
      render: (_, record) => (
        <Space>
          <span>{record.configName}</span>
          {record.isDefault === '1' && (
            <Tag color="blue">
              {intl.formatMessage({ id: 'pages.storageConfig.default', defaultMessage: '默认' })}
            </Tag>
          )}
        </Space>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.storageConfig.storageType', defaultMessage: '存储类型' }),
      dataIndex: 'storageType',
      width: 120,
      valueEnum: {
        local: { text: '本地存储' },
        aliyun_oss: { text: '阿里云OSS' },
        minio: { text: 'MinIO' },
        aws_s3: { text: 'AWS S3' },
      },
      render: (_, record) => (
        <Tag color={record.storageType === 'local' ? 'green' : 'blue'}>
          {record.storageTypeDesc || storageTypes[record.storageType || ''] || record.storageType}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.storageConfig.endpoint', defaultMessage: '服务地址' }),
      dataIndex: 'endpoint',
      hideInSearch: true,
      width: 200,
      ellipsis: true,
      render: (_, record) => {
        if (record.storageType === 'local') {
          return record.localPath || '-';
        }
        return record.endpoint || '-';
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.storageConfig.bucket', defaultMessage: '存储桶/路径' }),
      dataIndex: 'bucketName',
      hideInSearch: true,
      width: 150,
      ellipsis: true,
      render: (_, record) => {
        if (record.storageType === 'local') {
          return record.localUrlPrefix || '-';
        }
        return record.bucketName || '-';
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.storageConfig.status', defaultMessage: '状态' }),
      dataIndex: 'status',
      width: 80,
      valueEnum: {
        '0': { text: '正常', status: 'Success' },
        '1': { text: '停用', status: 'Error' },
      },
      render: (_, record) => (
        <Badge
          status={record.status === '0' ? 'success' : 'error'}
          text={record.status === '0' ? '正常' : '停用'}
        />
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.storageConfig.createTime', defaultMessage: '创建时间' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      width: 160,
    },
    {
      title: intl.formatMessage({ id: 'pages.storageConfig.option', defaultMessage: '操作' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 200,
      render: (_, record) => (
        <Space size="small">
          <StorageConfigForm
            key="edit"
            trigger={
              <a>{intl.formatMessage({ id: 'pages.storageConfig.edit', defaultMessage: '编辑' })}</a>
            }
            values={record}
            onOk={() => actionRef.current?.reload?.()}
          />
          {record.isDefault !== '1' && (
            <Popconfirm
              title={intl.formatMessage({
                id: 'pages.storageConfig.confirmSetDefault',
                defaultMessage: '确定设为默认配置吗?',
              })}
              onConfirm={() => handleSetDefault(record.configId!)}
            >
              <a>
                {intl.formatMessage({ id: 'pages.storageConfig.setDefault', defaultMessage: '设为默认' })}
              </a>
            </Popconfirm>
          )}
          {record.isDefault !== '1' && (
            <Popconfirm
              title={intl.formatMessage({
                id: 'pages.storageConfig.confirmDelete',
                defaultMessage: '确定删除该配置吗?',
              })}
              onConfirm={() => delRun(record.configId!)}
            >
              <a style={{ color: '#ff4d4f' }}>
                {intl.formatMessage({ id: 'pages.storageConfig.delete', defaultMessage: '删除' })}
              </a>
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  return (
    <PageContainer
      header={{
        title: intl.formatMessage({ id: 'pages.storageConfig.title', defaultMessage: '存储配置' }),
      }}
    >
      {contextHolder}

      <ProTable<API.StorageConfigVO, API.StorageConfigQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.storageConfig.list', defaultMessage: '配置列表' })}
        actionRef={actionRef}
        rowKey="configId"
        search={{
          labelWidth: 100,
        }}
        toolBarRender={() => [
          <StorageConfigForm
            key="add"
            trigger={
              <Button type="primary" icon={<PlusOutlined />}>
                {intl.formatMessage({ id: 'pages.storageConfig.add', defaultMessage: '新增配置' })}
              </Button>
            }
            onOk={() => actionRef.current?.reload?.()}
          />,
        ]}
        request={async (params) => {
          const res = await getStorageConfigPage({
            current: params.current,
            size: params.pageSize,
            configName: params.configName,
            storageType: params.storageType,
            status: params.status,
          });
          return {
            data: res.data?.records || [],
            total: res.data?.total || 0,
            success: res.code === 200,
          };
        }}
        columns={columns}
        pagination={{
          defaultPageSize: 10,
          showSizeChanger: true,
          showQuickJumper: true,
        }}
      />
    </PageContainer>
  );
};

export default StorageConfigList;
