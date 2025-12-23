import type { ActionType, ProColumns } from '@ant-design/pro-components';
import {
  PageContainer,
  ProTable,
} from '@ant-design/pro-components';
import { useIntl, useRequest, useModel } from '@umijs/max';
import { Button, message, Modal, Popconfirm, InputNumber, Space, Tooltip } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import {
  getOnlineUserPage,
  kickoutOnlineUser,
  disableOnlineUser,
  untieDisableUser,
  checkUserDisabled,
} from '@/services/ant-design-pro/api';
import dayjs from 'dayjs';

const OnlineUserList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [disableModalVisible, setDisableModalVisible] = useState(false);
  const [disableUserId, setDisableUserId] = useState<number | null>(null);
  const [disableTime, setDisableTime] = useState<number>(-1);
  const { initialState } = useModel('@@initialState');
  const currentUserId = initialState?.currentUser?.userId;

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: kickoutRun, loading: kickoutLoading } = useRequest(kickoutOnlineUser, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.onlineUser.kickoutSuccess', defaultMessage: '强退成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.onlineUser.kickoutFailed', defaultMessage: '强退失败' }),
      );
    },
  });

  const { run: disableRun, loading: disableLoading } = useRequest(
    (userId: number, time: number) => disableOnlineUser(userId, time),
    {
      manual: true,
      onSuccess: () => {
        setDisableModalVisible(false);
        setDisableUserId(null);
        setDisableTime(-1);
        actionRef.current?.reload?.();
        messageApi.success(
          intl.formatMessage({ id: 'pages.onlineUser.disableSuccess', defaultMessage: '封禁成功' }),
        );
      },
      onError: () => {
        messageApi.error(
          intl.formatMessage({ id: 'pages.onlineUser.disableFailed', defaultMessage: '封禁失败' }),
        );
      },
    },
  );

  const { run: untieRun, loading: untieLoading } = useRequest(untieDisableUser, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.onlineUser.untieSuccess', defaultMessage: '解封成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.onlineUser.untieFailed', defaultMessage: '解封失败' }),
      );
    },
  });

  const handleKickout = useCallback(
    async (tokenId: string) => {
      await kickoutRun(tokenId);
    },
    [kickoutRun],
  );

  const handleOpenDisableModal = useCallback((userId: number) => {
    setDisableUserId(userId);
    setDisableTime(-1);
    setDisableModalVisible(true);
  }, []);

  const handleDisable = useCallback(() => {
    if (disableUserId) {
      disableRun(disableUserId, disableTime);
    }
  }, [disableRun, disableUserId, disableTime]);

  const handleUntie = useCallback(
    async (userId: number) => {
      await untieRun(userId);
    },
    [untieRun],
  );

  const formatDuration = (seconds: number): string => {
    if (seconds < 0) return '-';
    if (seconds < 60) return `${seconds}秒`;
    if (seconds < 3600) return `${Math.floor(seconds / 60)}分钟`;
    if (seconds < 86400) return `${Math.floor(seconds / 3600)}小时`;
    return `${Math.floor(seconds / 86400)}天`;
  };

  const columns: ProColumns<API.OnlineUser>[] = [
    {
      title: intl.formatMessage({ id: 'pages.onlineUser.userId', defaultMessage: '用户ID' }),
      dataIndex: 'userId',
      hideInSearch: true,
      width: 80,
    },
    {
      title: intl.formatMessage({ id: 'pages.onlineUser.username', defaultMessage: '用户名' }),
      dataIndex: 'username',
      width: 120,
    },
    {
      title: intl.formatMessage({ id: 'pages.onlineUser.realName', defaultMessage: '真实姓名' }),
      dataIndex: 'realName',
      hideInSearch: true,
      width: 120,
    },
    {
      title: intl.formatMessage({ id: 'pages.onlineUser.deptName', defaultMessage: '部门' }),
      dataIndex: 'deptName',
      hideInSearch: true,
      width: 150,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.onlineUser.ipAddr', defaultMessage: 'IP地址' }),
      dataIndex: 'ipAddr',
      width: 140,
      render: () => '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.onlineUser.loginTime', defaultMessage: '登录时间' }),
      dataIndex: 'loginTime',
      hideInSearch: true,
      width: 170,
      render: (_, record) =>
        record.loginTime ? dayjs(record.loginTime).format('YYYY-MM-DD HH:mm:ss') : '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.onlineUser.lastAccessTime', defaultMessage: '最后访问' }),
      dataIndex: 'lastAccessTime',
      hideInSearch: true,
      width: 170,
      render: (_, record) =>
        record.lastAccessTime ? dayjs(record.lastAccessTime).format('YYYY-MM-DD HH:mm:ss') : '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.onlineUser.tokenTimeout', defaultMessage: '剩余有效期' }),
      dataIndex: 'tokenTimeout',
      hideInSearch: true,
      width: 100,
      render: (_, record) => formatDuration(record.tokenTimeout || 0),
    },
    {
      title: intl.formatMessage({ id: 'pages.common.operation', defaultMessage: '操作' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 200,
      fixed: 'right',
      render: (_, record) => {
        const isSelf = record.userId === currentUserId;
        return (
          <Space size="small">
            <Tooltip title={isSelf ? '不能强退自己' : ''}>
              <Popconfirm
                title={intl.formatMessage({
                  id: 'pages.onlineUser.confirmKickout',
                  defaultMessage: '确定要强制退出该用户吗？',
                })}
                onConfirm={() => handleKickout(record.tokenId!)}
                disabled={isSelf}
              >
                <Button
                  type="link"
                  size="small"
                  danger
                  disabled={isSelf || kickoutLoading}
                >
                  {intl.formatMessage({ id: 'pages.onlineUser.kickout', defaultMessage: '强退' })}
                </Button>
              </Popconfirm>
            </Tooltip>
            <Tooltip title={isSelf ? '不能封禁自己' : ''}>
              <Button
                type="link"
                size="small"
                danger
                disabled={isSelf}
                onClick={() => handleOpenDisableModal(record.userId!)}
              >
                {intl.formatMessage({ id: 'pages.onlineUser.disable', defaultMessage: '封禁' })}
              </Button>
            </Tooltip>
          </Space>
        );
      },
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.OnlineUser, API.OnlineUserQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.onlineUser.title', defaultMessage: '在线用户' })}
        actionRef={actionRef}
        rowKey="tokenId"
        search={{
          labelWidth: 120,
        }}
        request={async (params) => {
          const res = await getOnlineUserPage({
            current: params.current,
            size: params.pageSize,
            username: params.username,
            ipAddr: params.ipAddr,
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
        scroll={{ x: 1200 }}
      />

      <Modal
        title={intl.formatMessage({ id: 'pages.onlineUser.disableTitle', defaultMessage: '封禁用户' })}
        open={disableModalVisible}
        onOk={handleDisable}
        onCancel={() => {
          setDisableModalVisible(false);
          setDisableUserId(null);
          setDisableTime(-1);
        }}
        confirmLoading={disableLoading}
      >
        <div style={{ marginBottom: 16 }}>
          {intl.formatMessage({
            id: 'pages.onlineUser.disableTimeLabel',
            defaultMessage: '封禁时长（秒）：',
          })}
        </div>
        <InputNumber
          style={{ width: '100%' }}
          min={-1}
          value={disableTime}
          onChange={(value) => setDisableTime(value || -1)}
          placeholder={intl.formatMessage({
            id: 'pages.onlineUser.disableTimePlaceholder',
            defaultMessage: '-1表示永久封禁',
          })}
          addonAfter={
            <span style={{ color: '#999' }}>
              {disableTime === -1
                ? intl.formatMessage({ id: 'pages.onlineUser.permanent', defaultMessage: '永久' })
                : formatDuration(disableTime)}
            </span>
          }
        />
        <div style={{ marginTop: 8, color: '#999', fontSize: 12 }}>
          {intl.formatMessage({
            id: 'pages.onlineUser.disableTimeHint',
            defaultMessage: '提示：输入-1表示永久封禁，其他正整数表示封禁秒数',
          })}
        </div>
      </Modal>
    </PageContainer>
  );
};

export default OnlineUserList;
