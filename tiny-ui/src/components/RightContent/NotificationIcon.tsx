import { BellOutlined } from '@ant-design/icons';
import { history, useIntl, useRequest } from '@umijs/max';
import { Badge, Button, Empty, List, message, Popover, Space, Spin, Tag, Typography } from 'antd';
import { createStyles } from 'antd-style';
import React, { useCallback, useEffect, useState } from 'react';
import {
  getUnreadMessageCount,
  getUserMessagePage,
  markAllMessageRead,
  markMessageRead,
} from '@/services/ant-design-pro/message';
import { useMessageNotification } from '@/hooks/useWebSocket';

const { Text } = Typography;

const useStyles = createStyles(({ token }) => ({
  iconButton: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: '48px',
    height: '48px',
    cursor: 'pointer',
    borderRadius: token.borderRadius,
    overflow: 'visible',
    '&:hover': {
      backgroundColor: token.colorBgTextHover,
    },
  },
  popoverContent: {
    width: '360px',
    maxHeight: '400px',
  },
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '12px 16px',
    borderBottom: `1px solid ${token.colorBorderSecondary}`,
  },
  listItem: {
    cursor: 'pointer',
    padding: '12px 16px !important',
    '&:hover': {
      backgroundColor: token.colorBgTextHover,
    },
  },
  unread: {
    backgroundColor: token.colorPrimaryBg,
  },
  messageTitle: {
    fontWeight: 500,
    marginBottom: '4px',
  },
  messageContent: {
    color: token.colorTextSecondary,
    fontSize: '12px',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
  },
  messageTime: {
    color: token.colorTextTertiary,
    fontSize: '12px',
    marginTop: '4px',
  },
  footer: {
    display: 'flex',
    justifyContent: 'center',
    padding: '8px 16px',
    borderTop: `1px solid ${token.colorBorderSecondary}`,
  },
  emptyWrapper: {
    padding: '24px',
  },
}));

const NotificationIcon: React.FC = () => {
  const { styles } = useStyles();
  const intl = useIntl();
  const [open, setOpen] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  // 获取未读消息数量
  const {
    data: unreadCount,
    refresh: refreshUnreadCount,
    loading: loadingCount,
  } = useRequest(getUnreadMessageCount, {
    formatResult: (res) => res.data || 0,
  });

  // 确保 count 是数字
  const badgeCount = typeof unreadCount === 'number' ? unreadCount : 0;

  // 获取最近消息列表
  const {
    data: messages,
    loading: loadingMessages,
    run: fetchMessages,
  } = useRequest(
    () => getUserMessagePage({
      current: 1,
      size: 5,
      isRead: '0',
    }),
    {
      manual: true,
      formatResult: (res) => res.data?.records || [],
    },
  );

  // 监听 WebSocket 消息
  const handleWebSocketMessage = useCallback(
    (wsMessage: any) => {
      if (wsMessage.type === 'SITE_MESSAGE') {
        // 刷新未读数，铃铛图标上的徽章会自动更新
        refreshUnreadCount();
        // 如果下拉菜单已打开，同时刷新消息列表
        if (open) {
          fetchMessages();
        }
      }
    },
    [refreshUnreadCount, fetchMessages, open],
  );

  useMessageNotification(handleWebSocketMessage);

  // 打开弹窗时加载消息列表
  useEffect(() => {
    if (open) {
      fetchMessages();
    }
  }, [open, fetchMessages]);

  // 标记消息已读
  const handleMarkRead = async (recipientId: number) => {
    try {
      await markMessageRead(recipientId);
      refreshUnreadCount();
      fetchMessages();
    } catch (e) {
      messageApi.error(intl.formatMessage({ id: 'pages.message.center.markReadFailed' }));
    }
  };

  // 全部标记已读
  const handleMarkAllRead = async () => {
    try {
      await markAllMessageRead();
      refreshUnreadCount();
      fetchMessages();
      messageApi.success(intl.formatMessage({ id: 'pages.message.center.markAllReadSuccess' }));
    } catch (e) {
      messageApi.error(intl.formatMessage({ id: 'pages.message.center.markAllReadFailed' }));
    }
  };

  // 跳转到消息中心
  const handleViewAll = () => {
    setOpen(false);
    history.push('/message/center');
  };

  // 点击消息项
  const handleClickMessage = (item: API.UserMessage) => {
    if (item.isRead === '0' && item.recipientId) {
      handleMarkRead(item.recipientId);
    }
    setOpen(false);
    history.push('/message/center');
  };

  const priorityConfig: Record<number, { color: string; text: string }> = {
    0: { color: 'default', text: intl.formatMessage({ id: 'pages.message.list.priority.normal' }) },
    1: { color: 'warning', text: intl.formatMessage({ id: 'pages.message.list.priority.important' }) },
    2: { color: 'error', text: intl.formatMessage({ id: 'pages.message.list.priority.urgent' }) },
  };

  const content = (
    <div className={styles.popoverContent}>
      <div className={styles.header}>
        <Text strong>{intl.formatMessage({ id: 'component.globalHeader.message' })}</Text>
        {badgeCount > 0 && (
          <Button type="link" size="small" onClick={handleMarkAllRead}>
            {intl.formatMessage({ id: 'component.globalHeader.message.markAllRead' })}
          </Button>
        )}
      </div>

      {loadingMessages ? (
        <div style={{ textAlign: 'center', padding: '24px' }}>
          <Spin />
        </div>
      ) : messages && messages.length > 0 ? (
        <List
          dataSource={messages}
          renderItem={(item: API.UserMessage) => (
            <List.Item
              className={`${styles.listItem} ${item.isRead === '0' ? styles.unread : ''}`}
              onClick={() => handleClickMessage(item)}
            >
              <div style={{ width: '100%' }}>
                <div className={styles.messageTitle}>
                  <Space>
                    {item.isRead === '0' && <Badge status="processing" />}
                    <span>{item.title}</span>
                    {item.priority !== undefined && item.priority > 0 && (
                      <Tag color={priorityConfig[item.priority]?.color} style={{ marginLeft: 4 }}>
                        {priorityConfig[item.priority]?.text}
                      </Tag>
                    )}
                  </Space>
                </div>
                <div className={styles.messageContent}>{item.content}</div>
                <div className={styles.messageTime}>{item.createTime}</div>
              </div>
            </List.Item>
          )}
        />
      ) : (
        <div className={styles.emptyWrapper}>
          <Empty
            image={Empty.PRESENTED_IMAGE_SIMPLE}
            description={intl.formatMessage({ id: 'component.globalHeader.message.empty' })}
          />
        </div>
      )}

      <div className={styles.footer}>
        <Button type="link" onClick={handleViewAll}>
          {intl.formatMessage({ id: 'component.noticeIcon.view-more' })}
        </Button>
      </div>
    </div>
  );

  return (
    <>
      {contextHolder}
      <Popover
        content={content}
        trigger="click"
        open={open}
        onOpenChange={setOpen}
        placement="bottomRight"
        arrow={false}
      >
        <div className={styles.iconButton}>
          <Badge count={badgeCount} overflowCount={99} size="small" offset={[2, -2]}>
            <BellOutlined style={{ fontSize: '18px' }} />
          </Badge>
        </div>
      </Popover>
    </>
  );
};

export default NotificationIcon;
