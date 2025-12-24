import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { FooterToolbar, PageContainer, ProTable } from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import {
  Button,
  Image,
  Input,
  InputNumber,
  message,
  Modal,
  Popconfirm,
  Progress,
  Space,
  Tag,
  Tooltip,
  Upload,
} from 'antd';
import type { RcFile, UploadProps } from 'antd/es/upload';
import React, { useCallback, useRef, useState } from 'react';
import {
  CopyOutlined,
  DeleteOutlined,
  DownloadOutlined,
  EyeOutlined,
  LinkOutlined,
  UploadOutlined,
} from '@ant-design/icons';
import {
  deleteFile,
  deleteFileBatch,
  getFilePage,
  getFileTempUrl,
  uploadFile,
} from '@/services/ant-design-pro/api';

const FileList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [selectedRowsState, setSelectedRows] = useState<API.FileInfo[]>([]);
  const [uploading, setUploading] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [previewVisible, setPreviewVisible] = useState(false);
  const [previewUrl, setPreviewUrl] = useState('');
  const [tempUrlVisible, setTempUrlVisible] = useState(false);
  const [tempUrlLoading, setTempUrlLoading] = useState(false);
  const [tempUrl, setTempUrl] = useState('');
  const [tempUrlExpireTime, setTempUrlExpireTime] = useState(3600);
  const [currentFileId, setCurrentFileId] = useState<number | null>(null);

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: delRun } = useRequest(deleteFile, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.file.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.file.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteFileBatch, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.file.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.file.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const handleBatchRemove = useCallback(
    async (selectedRows: API.FileInfo[]) => {
      if (!selectedRows?.length) {
        messageApi.warning(
          intl.formatMessage({
            id: 'pages.file.selectRequired',
            defaultMessage: '请选择要删除的文件',
          }),
        );
        return;
      }
      const ids = selectedRows.map((row) => row.fileId!);
      await delBatchRun(ids);
    },
    [delBatchRun, intl, messageApi],
  );

  const isImageFile = (fileType?: string, fileExt?: string): boolean => {
    if (fileType && fileType.startsWith('image/')) return true;
    const imageExts = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg'];
    return !!fileExt && imageExts.includes(fileExt.toLowerCase());
  };

  const getFileTypeLabel = (fileType?: string, fileExt?: string): string => {
    if (fileType && fileType.startsWith('image/')) return 'image';
    if (fileType && fileType.startsWith('video/')) return 'video';
    if (fileType && fileType.startsWith('audio/')) return 'audio';

    const docExts = ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'pdf', 'txt'];
    const archiveExts = ['zip', 'rar', '7z', 'tar', 'gz'];

    if (fileExt) {
      if (docExts.includes(fileExt.toLowerCase())) return 'document';
      if (archiveExts.includes(fileExt.toLowerCase())) return 'archive';
    }
    return 'other';
  };

  const getFileTypeColor = (fileType?: string, fileExt?: string): string => {
    const label = getFileTypeLabel(fileType, fileExt);
    const colorMap: Record<string, string> = {
      image: 'green',
      video: 'blue',
      audio: 'purple',
      document: 'orange',
      archive: 'red',
      other: 'default',
    };
    return colorMap[label] || 'default';
  };

  const handlePreview = useCallback((record: API.FileInfo) => {
    if (record.fileUrl) {
      setPreviewUrl(record.fileUrl);
      setPreviewVisible(true);
    }
  }, []);

  const handleDownload = useCallback((record: API.FileInfo) => {
    if (record.fileUrl) {
      window.open(`/api/storage/file/download/${record.fileId}`, '_blank');
    }
  }, []);

  const handleCopyUrl = useCallback(
    (url?: string) => {
      if (!url) return;

      const copyToClipboard = (text: string) => {
        if (navigator.clipboard && window.isSecureContext) {
          return navigator.clipboard.writeText(text);
        }
        const textArea = document.createElement('textarea');
        textArea.value = text;
        textArea.style.position = 'fixed';
        textArea.style.left = '-9999px';
        document.body.appendChild(textArea);
        textArea.select();
        try {
          document.execCommand('copy');
          return Promise.resolve();
        } catch {
          return Promise.reject();
        } finally {
          document.body.removeChild(textArea);
        }
      };

      copyToClipboard(url)
        .then(() => {
          messageApi.success(
            intl.formatMessage({ id: 'pages.file.copySuccess', defaultMessage: '链接已复制' }),
          );
        })
        .catch(() => {
          messageApi.error('复制失败');
        });
    },
    [messageApi, intl],
  );

  const handleOpenTempUrlModal = useCallback((record: API.FileInfo) => {
    setCurrentFileId(record.fileId || null);
    setTempUrl('');
    setTempUrlExpireTime(3600);
    setTempUrlVisible(true);
  }, []);

  const handleGenerateTempUrl = useCallback(async () => {
    if (!currentFileId) return;
    setTempUrlLoading(true);
    try {
      const res = await getFileTempUrl(currentFileId, tempUrlExpireTime);
      if (res?.code === 200 && res.data) {
        setTempUrl(res.data);
        messageApi.success(
          intl.formatMessage({ id: 'pages.file.tempUrlSuccess', defaultMessage: '临时链接生成成功' }),
        );
      } else {
        messageApi.error(res?.message || '生成失败');
      }
    } catch {
      messageApi.error(
        intl.formatMessage({ id: 'pages.file.tempUrlFailed', defaultMessage: '临时链接生成失败' }),
      );
    } finally {
      setTempUrlLoading(false);
    }
  }, [currentFileId, tempUrlExpireTime, messageApi, intl]);

  const handleUpload = useCallback(
    async (file: RcFile) => {
      setUploading(true);
      setUploadProgress(0);

      try {
        const progressInterval = setInterval(() => {
          setUploadProgress((prev) => {
            if (prev >= 90) {
              clearInterval(progressInterval);
              return prev;
            }
            return prev + 10;
          });
        }, 200);

        const res = await uploadFile(file);

        clearInterval(progressInterval);
        setUploadProgress(100);

        if (res.code === 200) {
          messageApi.success(
            intl.formatMessage({ id: 'pages.file.uploadSuccess', defaultMessage: '上传成功' }),
          );
          actionRef.current?.reload?.();
        } else {
          messageApi.error(res.message || '上传失败');
        }
      } catch {
        messageApi.error(
          intl.formatMessage({ id: 'pages.file.uploadFailed', defaultMessage: '上传失败' }),
        );
      } finally {
        setTimeout(() => {
          setUploading(false);
          setUploadProgress(0);
        }, 500);
      }

      return false;
    },
    [messageApi, intl],
  );

  const uploadProps: UploadProps = {
    beforeUpload: handleUpload,
    showUploadList: false,
    multiple: false,
  };

  const columns: ProColumns<API.FileInfo>[] = [
    {
      title: intl.formatMessage({ id: 'pages.file.fileId', defaultMessage: '文件ID' }),
      dataIndex: 'fileId',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.file.preview', defaultMessage: '预览' }),
      dataIndex: 'preview',
      hideInSearch: true,
      width: 80,
      render: (_, record) =>
        isImageFile(record.fileType, record.fileExt) && record.fileUrl ? (
          <Image
            src={record.fileUrl}
            width={50}
            height={50}
            style={{ objectFit: 'cover', borderRadius: 4, cursor: 'pointer' }}
            preview={false}
            onClick={() => handlePreview(record)}
          />
        ) : (
          <div
            style={{
              width: 50,
              height: 50,
              background: '#f5f5f5',
              borderRadius: 4,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: '#999',
              fontSize: 12,
              textTransform: 'uppercase',
            }}
          >
            {record.fileExt || 'FILE'}
          </div>
        ),
    },
    {
      title: intl.formatMessage({ id: 'pages.file.fileName', defaultMessage: '文件名' }),
      dataIndex: 'originalFilename',
      ellipsis: true,
      width: 200,
      render: (_, record) => (
        <Tooltip title={record.originalFilename}>
          <span>{record.originalFilename}</span>
        </Tooltip>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.file.fileType', defaultMessage: '类型' }),
      dataIndex: 'fileExt',
      hideInSearch: true,
      width: 100,
      render: (_, record) => (
        <Tag color={getFileTypeColor(record.fileType, record.fileExt)}>
          {record.fileExt?.toUpperCase() || '-'}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.file.fileSize', defaultMessage: '大小' }),
      dataIndex: 'fileSizeDesc',
      hideInSearch: true,
      width: 100,
    },
    {
      title: intl.formatMessage({ id: 'pages.file.storageType', defaultMessage: '存储方式' }),
      dataIndex: 'storageType',
      width: 100,
      valueEnum: {
        local: {
          text: intl.formatMessage({ id: 'pages.file.storage.local', defaultMessage: '本地' }),
        },
        aliyun_oss: {
          text: intl.formatMessage({ id: 'pages.file.storage.oss', defaultMessage: 'OSS' }),
        },
      },
      render: (_, record) => (
        <Tag color={record.storageType === 'local' ? 'green' : 'blue'}>
          {record.storageType === 'local' ? '本地' : 'OSS'}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.file.uploadTime', defaultMessage: '上传时间' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      width: 160,
      sorter: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.file.option', defaultMessage: '操作' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 180,
      render: (_, record) => (
        <Space size="small">
          {isImageFile(record.fileType, record.fileExt) && (
            <Tooltip
              title={intl.formatMessage({ id: 'pages.file.preview', defaultMessage: '预览' })}
            >
              <Button
                type="link"
                size="small"
                icon={<EyeOutlined />}
                onClick={() => handlePreview(record)}
              />
            </Tooltip>
          )}
          <Tooltip
            title={intl.formatMessage({ id: 'pages.file.download', defaultMessage: '下载' })}
          >
            <Button
              type="link"
              size="small"
              icon={<DownloadOutlined />}
              onClick={() => handleDownload(record)}
            />
          </Tooltip>
          <Tooltip
            title={intl.formatMessage({ id: 'pages.file.copyUrl', defaultMessage: '复制链接' })}
          >
            <Button
              type="link"
              size="small"
              icon={<CopyOutlined />}
              onClick={() => handleCopyUrl(record.fileUrl)}
            />
          </Tooltip>
          <Tooltip
            title={intl.formatMessage({ id: 'pages.file.tempUrl', defaultMessage: '临时链接' })}
          >
            <Button
              type="link"
              size="small"
              icon={<LinkOutlined />}
              onClick={() => handleOpenTempUrlModal(record)}
            />
          </Tooltip>
          <Popconfirm
            title={intl.formatMessage({
              id: 'pages.file.confirmDelete',
              defaultMessage: '确定删除该文件吗?',
            })}
            onConfirm={() => delRun(record.fileId!)}
          >
            <Tooltip
              title={intl.formatMessage({ id: 'pages.file.delete', defaultMessage: '删除' })}
            >
              <Button type="link" size="small" danger icon={<DeleteOutlined />} />
            </Tooltip>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <PageContainer
      header={{
        title: intl.formatMessage({ id: 'pages.file.title', defaultMessage: '文件管理' }),
      }}
    >
      {contextHolder}
      <ProTable<API.FileInfo, API.FileQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.file.list', defaultMessage: '文件列表' })}
        actionRef={actionRef}
        rowKey="fileId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Upload key="upload" {...uploadProps}>
            <Button type="primary" icon={<UploadOutlined />} loading={uploading}>
              {intl.formatMessage({ id: 'pages.file.upload', defaultMessage: '上传文件' })}
            </Button>
          </Upload>,
        ]}
        request={async (params) => {
          const res = await getFilePage({
            current: params.current,
            size: params.pageSize,
            originalFilename: params.originalFilename,
            fileType: params.fileType,
            storageType: params.storageType,
          });
          return {
            data: res.data?.records || [],
            total: res.data?.total || 0,
            success: res.code === 200,
          };
        }}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
        pagination={{
          defaultPageSize: 10,
          showSizeChanger: true,
          showQuickJumper: true,
        }}
      />

      {uploading && (
        <div style={{ position: 'fixed', bottom: 80, right: 24, width: 300 }}>
          <Progress percent={uploadProgress} status="active" />
        </div>
      )}

      {selectedRowsState?.length > 0 && (
        <FooterToolbar>
          <Button danger loading={delBatchLoading} onClick={() => handleBatchRemove(selectedRowsState)}>
            {intl.formatMessage({ id: 'pages.file.batchDelete', defaultMessage: '批量删除' })}
          </Button>
        </FooterToolbar>
      )}

      <Modal
        open={previewVisible}
        footer={null}
        onCancel={() => setPreviewVisible(false)}
        width={800}
        centered
      >
        <Image src={previewUrl} style={{ width: '100%' }} preview={false} />
      </Modal>

      <Modal
        title={intl.formatMessage({ id: 'pages.file.tempUrlTitle', defaultMessage: '生成临时访问链接' })}
        open={tempUrlVisible}
        onCancel={() => setTempUrlVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setTempUrlVisible(false)}>
            {intl.formatMessage({ id: 'pages.file.close', defaultMessage: '关闭' })}
          </Button>,
          <Button
            key="generate"
            type="primary"
            loading={tempUrlLoading}
            onClick={handleGenerateTempUrl}
          >
            {intl.formatMessage({ id: 'pages.file.generate', defaultMessage: '生成链接' })}
          </Button>,
        ]}
        width={600}
      >
        <div style={{ marginBottom: 16 }}>
          <span style={{ marginRight: 8 }}>
            {intl.formatMessage({ id: 'pages.file.expireTime', defaultMessage: '有效期' })}:
          </span>
          <InputNumber
            min={60}
            max={604800}
            value={tempUrlExpireTime}
            onChange={(value) => setTempUrlExpireTime(value || 3600)}
            addonAfter={intl.formatMessage({ id: 'pages.file.seconds', defaultMessage: '秒' })}
            style={{ width: 200 }}
          />
          <span style={{ marginLeft: 8, color: '#999' }}>
            ({Math.floor(tempUrlExpireTime / 3600)}{intl.formatMessage({ id: 'pages.file.hours', defaultMessage: '小时' })}
            {Math.floor((tempUrlExpireTime % 3600) / 60)}{intl.formatMessage({ id: 'pages.file.minutes', defaultMessage: '分钟' })})
          </span>
        </div>
        {tempUrl && (
          <div>
            <div style={{ marginBottom: 8, fontWeight: 500 }}>
              {intl.formatMessage({ id: 'pages.file.generatedUrl', defaultMessage: '生成的链接' })}:
            </div>
            <Input.TextArea
              value={tempUrl}
              rows={3}
              readOnly
              style={{ marginBottom: 8 }}
            />
            <Button
              type="primary"
              icon={<CopyOutlined />}
              onClick={() => handleCopyUrl(tempUrl)}
            >
              {intl.formatMessage({ id: 'pages.file.copyUrl', defaultMessage: '复制链接' })}
            </Button>
          </div>
        )}
      </Modal>
    </PageContainer>
  );
};

export default FileList;
