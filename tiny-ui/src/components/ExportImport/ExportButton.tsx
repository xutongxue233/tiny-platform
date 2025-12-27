import React, { useState } from 'react';
import { Button, Dropdown, message, Modal, Progress, Space } from 'antd';
import { DownloadOutlined, ExportOutlined } from '@ant-design/icons';
import type { MenuProps } from 'antd';

export interface ExportButtonProps {
  /** 同步导出函数，返回Blob */
  onExportSync?: () => Promise<Blob>;
  /** 异步导出函数，返回任务ID */
  onExportAsync?: () => Promise<number>;
  /** 导出文件名 */
  fileName?: string;
  /** 是否显示异步导出选项 */
  showAsync?: boolean;
  /** 按钮文字 */
  buttonText?: string;
  /** 权限标识 */
  access?: string;
  /** 是否禁用 */
  disabled?: boolean;
}

const ExportButton: React.FC<ExportButtonProps> = ({
  onExportSync,
  onExportAsync,
  fileName = '导出数据',
  showAsync = true,
  buttonText = '导出',
  disabled = false,
}) => {
  const [loading, setLoading] = useState(false);
  const [asyncModalVisible, setAsyncModalVisible] = useState(false);
  const [taskId, setTaskId] = useState<number | null>(null);

  // 同步导出处理
  const handleSyncExport = async () => {
    if (!onExportSync) {
      message.error('未配置同步导出函数');
      return;
    }

    setLoading(true);
    try {
      const blob = await onExportSync();
      // 创建下载链接
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `${fileName}_${new Date().toISOString().slice(0, 10)}.xlsx`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      message.success('导出成功');
    } catch (error: any) {
      message.error(error.message || '导出失败');
    } finally {
      setLoading(false);
    }
  };

  // 异步导出处理
  const handleAsyncExport = async () => {
    if (!onExportAsync) {
      message.error('未配置异步导出函数');
      return;
    }

    setLoading(true);
    try {
      const id = await onExportAsync();
      setTaskId(id);
      setAsyncModalVisible(true);
      message.success('导出任务已创建，请在导出任务管理中查看进度');
    } catch (error: any) {
      message.error(error.message || '创建导出任务失败');
    } finally {
      setLoading(false);
    }
  };

  // 如果只有同步导出，直接显示按钮
  if (!showAsync || !onExportAsync) {
    return (
      <Button
        type="primary"
        icon={<DownloadOutlined />}
        loading={loading}
        onClick={handleSyncExport}
        disabled={disabled}
      >
        {buttonText}
      </Button>
    );
  }

  // 有异步导出选项时，显示下拉菜单
  const menuItems: MenuProps['items'] = [
    {
      key: 'sync',
      label: '同步导出',
      icon: <DownloadOutlined />,
      onClick: handleSyncExport,
      disabled: !onExportSync,
    },
    {
      key: 'async',
      label: '异步导出',
      icon: <ExportOutlined />,
      onClick: handleAsyncExport,
    },
  ];

  return (
    <>
      <Dropdown menu={{ items: menuItems }} disabled={disabled || loading}>
        <Button type="primary" loading={loading} icon={<DownloadOutlined />}>
          <Space>
            {buttonText}
          </Space>
        </Button>
      </Dropdown>

      <Modal
        title="异步导出任务已创建"
        open={asyncModalVisible}
        onOk={() => setAsyncModalVisible(false)}
        onCancel={() => setAsyncModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setAsyncModalVisible(false)}>
            关闭
          </Button>,
          <Button
            key="view"
            type="primary"
            onClick={() => {
              setAsyncModalVisible(false);
              window.location.href = '/system/exportTask';
            }}
          >
            查看任务
          </Button>,
        ]}
      >
        <div style={{ textAlign: 'center', padding: '20px 0' }}>
          <Progress type="circle" percent={0} status="active" />
          <p style={{ marginTop: 16 }}>
            导出任务已创建，任务ID: {taskId}
          </p>
          <p style={{ color: '#999' }}>
            您可以在"导出任务管理"中查看导出进度和下载文件
          </p>
        </div>
      </Modal>
    </>
  );
};

export default ExportButton;
