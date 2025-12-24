import { Modal, Tabs, message, Spin } from 'antd';
import React, { useState } from 'react';
import { previewCode } from '@/services/ant-design-pro/generator';

interface PreviewCodeModalProps {
  tableId: number;
  tableName: string;
}

const PreviewCodeModal: React.FC<PreviewCodeModalProps> = ({ tableId, tableName }) => {
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [codeMap, setCodeMap] = useState<Record<string, string>>({});
  const [messageApi, contextHolder] = message.useMessage();

  const handleOpen = async () => {
    setVisible(true);
    setLoading(true);
    try {
      const res = await previewCode(tableId);
      if (res.code === 200 && res.data) {
        setCodeMap(res.data);
      } else {
        messageApi.error('获取预览代码失败');
      }
    } catch (error) {
      messageApi.error('获取预览代码失败');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setVisible(false);
    setCodeMap({});
  };

  const tabItems = Object.entries(codeMap).map(([fileName, code]) => ({
    key: fileName,
    label: fileName.split('/').pop() || fileName,
    children: (
      <div style={{ maxHeight: '60vh', overflow: 'auto' }}>
        <pre
          style={{
            margin: 0,
            padding: 16,
            backgroundColor: '#1e1e1e',
            color: '#d4d4d4',
            borderRadius: 4,
            fontSize: 13,
            lineHeight: 1.5,
            overflow: 'auto',
            fontFamily: 'Consolas, Monaco, "Courier New", monospace',
          }}
        >
          {code}
        </pre>
      </div>
    ),
  }));

  return (
    <>
      {contextHolder}
      <a onClick={handleOpen}>预览</a>
      <Modal
        title={`代码预览 - ${tableName}`}
        open={visible}
        onCancel={handleClose}
        width={1000}
        footer={null}
        destroyOnClose
      >
        <Spin spinning={loading}>
          {tabItems.length > 0 ? (
            <Tabs
              items={tabItems}
              tabPosition="left"
              style={{ minHeight: 400 }}
            />
          ) : (
            !loading && <div style={{ textAlign: 'center', padding: 40 }}>暂无代码</div>
          )}
        </Spin>
      </Modal>
    </>
  );
};

export default PreviewCodeModal;
