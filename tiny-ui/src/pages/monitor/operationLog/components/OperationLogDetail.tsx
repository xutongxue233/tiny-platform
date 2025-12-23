import { Descriptions, Modal, Tag, Typography } from 'antd';
import React from 'react';
import { useIntl } from '@umijs/max';

const { Paragraph } = Typography;

interface OperationLogDetailProps {
  open: boolean;
  onClose: () => void;
  record?: API.SysOperationLog;
}

const OperationLogDetail: React.FC<OperationLogDetailProps> = ({
  open,
  onClose,
  record,
}) => {
  const intl = useIntl();

  if (!record) return null;

  const statusMap: Record<string, { color: string; text: string }> = {
    '0': { color: 'success', text: '成功' },
    '1': { color: 'error', text: '失败' },
  };

  const methodColorMap: Record<string, string> = {
    GET: 'green',
    POST: 'blue',
    PUT: 'orange',
    DELETE: 'red',
    PATCH: 'cyan',
  };

  return (
    <Modal
      title={intl.formatMessage({ id: 'pages.operationLog.detail', defaultMessage: '操作日志详情' })}
      open={open}
      onCancel={onClose}
      footer={null}
      width={800}
    >
      <Descriptions column={2} bordered size="small">
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.operationLogId', defaultMessage: '日志ID' })}>
          {record.operationLogId}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.username', defaultMessage: '操作人员' })}>
          {record.username}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.moduleName', defaultMessage: '模块名称' })}>
          {record.moduleName}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.operationType', defaultMessage: '操作类型' })}>
          {record.operationType}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.operationDesc', defaultMessage: '操作描述' })} span={2}>
          {record.operationDesc}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.requestMethod', defaultMessage: '请求方式' })}>
          <Tag color={methodColorMap[record.requestMethod || ''] || 'default'}>
            {record.requestMethod}
          </Tag>
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.status', defaultMessage: '操作状态' })}>
          <Tag color={statusMap[record.status || '']?.color}>
            {statusMap[record.status || '']?.text}
          </Tag>
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.requestUrl', defaultMessage: '请求URL' })} span={2}>
          <Paragraph copyable style={{ marginBottom: 0 }}>{record.requestUrl}</Paragraph>
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.methodName', defaultMessage: '方法名称' })} span={2}>
          <Paragraph copyable style={{ marginBottom: 0 }}>{record.methodName}</Paragraph>
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.ipAddr', defaultMessage: 'IP地址' })}>
          {record.ipAddr}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.operationLocation', defaultMessage: '操作地点' })}>
          {record.operationLocation}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.browser', defaultMessage: '浏览器' })}>
          {record.browser}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.os', defaultMessage: '操作系统' })}>
          {record.os}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.executionTime', defaultMessage: '执行时长' })}>
          {record.executionTime} ms
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.operationTime', defaultMessage: '操作时间' })}>
          {record.operationTime}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.requestParam', defaultMessage: '请求参数' })} span={2}>
          <Paragraph
            copyable
            ellipsis={{ rows: 4, expandable: true, symbol: '展开' }}
            style={{ marginBottom: 0, maxHeight: 200, overflow: 'auto' }}
          >
            <pre style={{ margin: 0, whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}>
              {record.requestParam ? JSON.stringify(JSON.parse(record.requestParam), null, 2) : '-'}
            </pre>
          </Paragraph>
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.responseResult', defaultMessage: '响应结果' })} span={2}>
          <Paragraph
            copyable
            ellipsis={{ rows: 4, expandable: true, symbol: '展开' }}
            style={{ marginBottom: 0, maxHeight: 200, overflow: 'auto' }}
          >
            <pre style={{ margin: 0, whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}>
              {record.responseResult ? JSON.stringify(JSON.parse(record.responseResult), null, 2) : '-'}
            </pre>
          </Paragraph>
        </Descriptions.Item>
        {record.status === '1' && record.errorMsg && (
          <Descriptions.Item label={intl.formatMessage({ id: 'pages.operationLog.errorMsg', defaultMessage: '错误信息' })} span={2}>
            <Paragraph type="danger" style={{ marginBottom: 0 }}>
              {record.errorMsg}
            </Paragraph>
          </Descriptions.Item>
        )}
      </Descriptions>
    </Modal>
  );
};

export default OperationLogDetail;
