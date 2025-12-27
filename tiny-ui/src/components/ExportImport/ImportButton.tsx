import React, { useState } from 'react';
import { Button, Modal, Upload, message, Alert, Table, Space, Typography } from 'antd';
import { UploadOutlined, DownloadOutlined, InboxOutlined } from '@ant-design/icons';
import type { UploadProps } from 'antd';

const { Dragger } = Upload;
const { Text } = Typography;

export interface ImportButtonProps {
  /** 导入函数，返回导入结果 */
  onImport: (file: File) => Promise<API.ImportResult>;
  /** 模板下载URL */
  templateUrl?: string;
  /** 下载模板函数(用于需要鉴权的情况) */
  onDownloadTemplate?: () => void;
  /** 按钮文字 */
  buttonText?: string;
  /** 导入成功后的回调 */
  onSuccess?: () => void;
  /** 接受的文件类型 */
  accept?: string;
  /** 最大文件大小(MB) */
  maxSize?: number;
  /** 是否禁用 */
  disabled?: boolean;
}

const ImportButton: React.FC<ImportButtonProps> = ({
  onImport,
  templateUrl,
  onDownloadTemplate,
  buttonText = '导入',
  onSuccess,
  accept = '.xlsx,.xls',
  maxSize = 10,
  disabled = false,
}) => {
  const [modalVisible, setModalVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [importResult, setImportResult] = useState<API.ImportResult | null>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  // 打开导入弹窗
  const openModal = () => {
    setModalVisible(true);
    setImportResult(null);
    setSelectedFile(null);
  };

  // 关闭弹窗
  const closeModal = () => {
    setModalVisible(false);
    setImportResult(null);
    setSelectedFile(null);
  };

  // 下载模板
  const handleDownloadTemplate = () => {
    if (onDownloadTemplate) {
      onDownloadTemplate();
    } else if (templateUrl) {
      window.open(templateUrl, '_blank');
    } else {
      message.warning('未配置模板下载');
    }
  };

  // 文件上传前校验
  const beforeUpload: UploadProps['beforeUpload'] = (file) => {
    const isValidType = accept
      .split(',')
      .some((type) => file.name.toLowerCase().endsWith(type.trim()));
    if (!isValidType) {
      message.error(`只能上传 ${accept} 格式的文件`);
      return false;
    }

    const isValidSize = file.size / 1024 / 1024 < maxSize;
    if (!isValidSize) {
      message.error(`文件大小不能超过 ${maxSize}MB`);
      return false;
    }

    setSelectedFile(file);
    return false; // 阻止自动上传
  };

  // 执行导入
  const handleImport = async () => {
    if (!selectedFile) {
      message.warning('请先选择要导入的文件');
      return;
    }

    setLoading(true);
    try {
      const result = await onImport(selectedFile);
      setImportResult(result);

      if (result.failCount === 0) {
        message.success(`导入成功，共导入 ${result.successCount} 条数据`);
        if (onSuccess) {
          onSuccess();
        }
      } else {
        message.warning(`导入完成，成功 ${result.successCount} 条，失败 ${result.failCount} 条`);
      }
    } catch (error: any) {
      message.error(error.message || '导入失败');
    } finally {
      setLoading(false);
    }
  };

  // 错误信息列配置
  const errorColumns = [
    {
      title: '行号',
      dataIndex: 'row',
      key: 'row',
      width: 80,
    },
    {
      title: '错误信息',
      dataIndex: 'message',
      key: 'message',
    },
  ];

  // 解析错误信息
  const parseErrorMessages = (messages?: string[]) => {
    if (!messages || messages.length === 0) return [];
    return messages.map((msg, index) => {
      const match = msg.match(/^第(\d+)行[：:](.*)/);
      if (match) {
        return { key: index, row: match[1], message: match[2] };
      }
      return { key: index, row: '-', message: msg };
    });
  };

  return (
    <>
      <Button icon={<UploadOutlined />} onClick={openModal} disabled={disabled}>
        {buttonText}
      </Button>

      <Modal
        title="数据导入"
        open={modalVisible}
        onCancel={closeModal}
        width={600}
        footer={
          importResult ? (
            <Button onClick={closeModal}>关闭</Button>
          ) : (
            <Space>
              <Button onClick={closeModal}>取消</Button>
              <Button
                type="primary"
                onClick={handleImport}
                loading={loading}
                disabled={!selectedFile}
              >
                开始导入
              </Button>
            </Space>
          )
        }
      >
        {!importResult ? (
          <div>
            {/* 模板下载提示 */}
            <Alert
              message="导入说明"
              description={
                <div>
                  <p>1. 请先下载导入模板，按照模板格式填写数据</p>
                  <p>2. 支持 .xlsx 和 .xls 格式，文件大小不超过 {maxSize}MB</p>
                  <p>3. 导入数据将进行校验，校验失败的数据不会被导入</p>
                </div>
              }
              type="info"
              showIcon
              style={{ marginBottom: 16 }}
              action={
                (templateUrl || onDownloadTemplate) && (
                  <Button
                    size="small"
                    icon={<DownloadOutlined />}
                    onClick={handleDownloadTemplate}
                  >
                    下载模板
                  </Button>
                )
              }
            />

            {/* 文件上传区域 */}
            <Dragger
              accept={accept}
              beforeUpload={beforeUpload}
              maxCount={1}
              fileList={selectedFile ? [selectedFile as any] : []}
              onRemove={() => setSelectedFile(null)}
            >
              <p className="ant-upload-drag-icon">
                <InboxOutlined />
              </p>
              <p className="ant-upload-text">点击或拖拽文件到此区域上传</p>
              <p className="ant-upload-hint">
                支持 {accept} 格式，文件大小不超过 {maxSize}MB
              </p>
            </Dragger>
          </div>
        ) : (
          <div>
            {/* 导入结果展示 */}
            <Alert
              message="导入结果"
              description={
                <Space direction="vertical" style={{ width: '100%' }}>
                  <Text>总记录数: {importResult.totalCount}</Text>
                  <Text type="success">成功: {importResult.successCount}</Text>
                  {(importResult.failCount ?? 0) > 0 && (
                    <Text type="danger">失败: {importResult.failCount}</Text>
                  )}
                </Space>
              }
              type={importResult.failCount === 0 ? 'success' : 'warning'}
              showIcon
              style={{ marginBottom: 16 }}
            />

            {/* 错误信息列表 */}
            {importResult.errorMessages && importResult.errorMessages.length > 0 && (
              <div>
                <Text strong style={{ marginBottom: 8, display: 'block' }}>
                  错误详情:
                </Text>
                <Table
                  columns={errorColumns}
                  dataSource={parseErrorMessages(importResult.errorMessages)}
                  size="small"
                  pagination={{ pageSize: 5 }}
                  scroll={{ y: 200 }}
                />
              </div>
            )}
          </div>
        )}
      </Modal>
    </>
  );
};

export default ImportButton;
