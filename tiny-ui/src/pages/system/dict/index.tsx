import type { ActionType } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { useAccess, useIntl } from '@umijs/max';
import { Card, Col, message, Row } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import { clearDictCache } from '@/services/ant-design-pro/dict';
import DictTypeList from './components/DictTypeList';
import DictItemTable from './components/DictItemTable';
import DictTypeForm from './components/DictTypeForm';

const DictManagement: React.FC = () => {
  const [selectedDictType, setSelectedDictType] = useState<API.SysDictType | undefined>();
  const dictTypeListRef = useRef<{ reload: () => void }>(null);
  const dictItemTableRef = useRef<ActionType | null>(null);

  const intl = useIntl();
  const access = useAccess();
  const [messageApi, contextHolder] = message.useMessage();

  const handleDictTypeSelect = useCallback((dictType?: API.SysDictType) => {
    setSelectedDictType(dictType);
  }, []);

  const handleDictTypeChange = useCallback(() => {
    dictTypeListRef.current?.reload();
    if (selectedDictType?.dictCode) {
      clearDictCache(selectedDictType.dictCode);
    }
  }, [selectedDictType]);

  const handleDictItemChange = useCallback(() => {
    dictItemTableRef.current?.reload();
    if (selectedDictType?.dictCode) {
      clearDictCache(selectedDictType.dictCode);
    }
  }, [selectedDictType]);

  return (
    <PageContainer>
      {contextHolder}
      <Row gutter={16}>
        <Col span={6}>
          <Card
            title={intl.formatMessage({ id: 'pages.dict.typeList', defaultMessage: '字典类型' })}
            bordered={false}
            extra={
              access.hasPermission('system:dict:add') && (
                <DictTypeForm onOk={handleDictTypeChange} />
              )
            }
          >
            <DictTypeList
              ref={dictTypeListRef}
              selectedDictType={selectedDictType}
              onSelect={handleDictTypeSelect}
              onStatusChange={handleDictTypeChange}
            />
          </Card>
        </Col>
        <Col span={18}>
          <Card
            title={
              selectedDictType
                ? `${intl.formatMessage({ id: 'pages.dict.itemList', defaultMessage: '字典项' })} - ${selectedDictType.dictName}`
                : intl.formatMessage({ id: 'pages.dict.itemList', defaultMessage: '字典项' })
            }
            bordered={false}
          >
            <DictItemTable
              ref={dictItemTableRef}
              dictCode={selectedDictType?.dictCode}
              onChange={handleDictItemChange}
            />
          </Card>
        </Col>
      </Row>
    </PageContainer>
  );
};

export default DictManagement;
