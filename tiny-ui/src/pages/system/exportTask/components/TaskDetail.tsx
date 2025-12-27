import React from 'react';
import { Drawer, Descriptions, Tag, Button, Space, Typography, Alert, Progress } from 'antd';
import { DownloadOutlined } from '@ant-design/icons';

const { Paragraph } = Typography;

interface TaskDetailProps {
  visible: boolean;
  task: API.ExportTask | null;
  onClose: () => void;
  onDownload: (task: API.ExportTask) => void;
}

const TaskDetail: React.FC<TaskDetailProps> = ({ visible, task, onClose, onDownload }) => {
  if (!task) return null;

  // 状态配置
  const statusConfig: Record<string, { color: string; text: string; percent: number }> = {
    '0': { color: 'default', text: '待处理', percent: 0 },
    '1': { color: 'processing', text: '处理中', percent: 50 },
    '2': { color: 'success', text: '成功', percent: 100 },
    '3': { color: 'error', text: '失败', percent: 100 },
  };

  const status = statusConfig[task.status || '0'];

  // 计算成功率
  const getSuccessRate = () => {
    if (!task.totalCount || task.totalCount === 0) return 0;
    return Math.round(((task.successCount || 0) / task.totalCount) * 100);
  };

  return (
    <Drawer
      title="任务详情"
      placement="right"
      width={600}
      onClose={onClose}
      open={visible}
      extra={
        task.status === '2' && task.fileUrl ? (
          <Button
            type="primary"
            icon={<DownloadOutlined />}
            onClick={() => onDownload(task)}
          >
            下载文件
          </Button>
        ) : null
      }
    >
      {/* 状态展示 */}
      <div style={{ marginBottom: 24, textAlign: 'center' }}>
        <Progress
          type="circle"
          percent={status.percent}
          status={task.status === '3' ? 'exception' : task.status === '1' ? 'active' : undefined}
          format={() => status.text}
        />
      </div>

      {/* 错误信息 */}
      {task.status === '3' && task.errorMsg && (
        <Alert
          message="错误信息"
          description={task.errorMsg}
          type="error"
          showIcon
          style={{ marginBottom: 24 }}
        />
      )}

      {/* 基本信息 */}
      <Descriptions title="基本信息" column={2} bordered size="small">
        <Descriptions.Item label="任务ID">{task.taskId}</Descriptions.Item>
        <Descriptions.Item label="任务名称">{task.taskName}</Descriptions.Item>
        <Descriptions.Item label="任务类型">
          <Tag color={task.taskType === 'export' ? 'blue' : 'green'}>
            {task.taskTypeName || task.taskType}
          </Tag>
        </Descriptions.Item>
        <Descriptions.Item label="业务类型">
          {task.businessTypeName || task.businessType || '-'}
        </Descriptions.Item>
        <Descriptions.Item label="状态">
          <Tag color={status.color}>{status.text}</Tag>
        </Descriptions.Item>
        <Descriptions.Item label="创建人">{task.createBy || '-'}</Descriptions.Item>
      </Descriptions>

      {/* 执行信息 */}
      <Descriptions
        title="执行信息"
        column={2}
        bordered
        size="small"
        style={{ marginTop: 24 }}
      >
        <Descriptions.Item label="开始时间">{task.startTime || '-'}</Descriptions.Item>
        <Descriptions.Item label="结束时间">{task.endTime || '-'}</Descriptions.Item>
        <Descriptions.Item label="耗时">
          {task.duration ? `${task.duration} ms` : '-'}
        </Descriptions.Item>
        <Descriptions.Item label="过期时间">{task.expireTime || '-'}</Descriptions.Item>
      </Descriptions>

      {/* 数据统计 */}
      <Descriptions
        title="数据统计"
        column={2}
        bordered
        size="small"
        style={{ marginTop: 24 }}
      >
        <Descriptions.Item label="总记录数">{task.totalCount ?? '-'}</Descriptions.Item>
        <Descriptions.Item label="成功率">
          {task.totalCount ? (
            <span style={{ color: getSuccessRate() === 100 ? '#52c41a' : '#faad14' }}>
              {getSuccessRate()}%
            </span>
          ) : (
            '-'
          )}
        </Descriptions.Item>
        <Descriptions.Item label="成功数">
          <span style={{ color: '#52c41a' }}>{task.successCount ?? '-'}</span>
        </Descriptions.Item>
        <Descriptions.Item label="失败数">
          <span style={{ color: (task.failCount ?? 0) > 0 ? '#ff4d4f' : undefined }}>
            {task.failCount ?? '-'}
          </span>
        </Descriptions.Item>
      </Descriptions>

      {/* 文件信息 */}
      {task.fileUrl && (
        <Descriptions
          title="文件信息"
          column={1}
          bordered
          size="small"
          style={{ marginTop: 24 }}
        >
          <Descriptions.Item label="文件名">{task.fileName || '-'}</Descriptions.Item>
          <Descriptions.Item label="文件大小">{task.fileSizeDesc || '-'}</Descriptions.Item>
          <Descriptions.Item label="文件路径">
            <Paragraph copyable ellipsis={{ rows: 2 }}>
              {task.fileUrl || '-'}
            </Paragraph>
          </Descriptions.Item>
        </Descriptions>
      )}

      {/* 查询参数 */}
      {task.queryParams && (
        <Descriptions
          title="查询参数"
          column={1}
          bordered
          size="small"
          style={{ marginTop: 24 }}
        >
          <Descriptions.Item label="参数JSON">
            <Paragraph
              copyable
              ellipsis={{ rows: 3, expandable: true }}
              style={{ marginBottom: 0 }}
            >
              {task.queryParams}
            </Paragraph>
          </Descriptions.Item>
        </Descriptions>
      )}

      {/* 备注 */}
      {task.remark && (
        <Descriptions
          title="其他信息"
          column={1}
          bordered
          size="small"
          style={{ marginTop: 24 }}
        >
          <Descriptions.Item label="备注">{task.remark}</Descriptions.Item>
        </Descriptions>
      )}
    </Drawer>
  );
};

export default TaskDetail;
