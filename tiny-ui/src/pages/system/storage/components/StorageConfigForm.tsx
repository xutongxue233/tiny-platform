import { useIntl } from '@umijs/max';
import {
  Button,
  Col,
  Form,
  Input,
  message,
  Modal,
  Radio,
  Row,
  Select,
  Switch,
} from 'antd';
import React, { useCallback, useEffect, useState } from 'react';
import {
  addStorageConfig,
  getStorageTypes,
  testStorageConnection,
  updateStorageConfig,
} from '@/services/ant-design-pro/api';
interface StorageConfigFormProps {
  trigger: React.ReactNode;
  values?: API.StorageConfigVO;
  onOk?: () => void;
}
// 默认存储类型列表，当接口获取失败时使用
const DEFAULT_STORAGE_TYPES: API.StorageTypeOption[] = [
  { code: 'local', desc: '本地存储' },
  { code: 'aliyun_oss', desc: '阿里云OSS' },
  { code: 'minio', desc: 'MinIO' },
  { code: 'aws_s3', desc: 'AWS S3' },
];
const StorageConfigForm: React.FC<StorageConfigFormProps> = ({ trigger, values, onOk }) => {
  const [open, setOpen] = useState(false);
  const [form] = Form.useForm();
  const [storageType, setStorageType] = useState<string>('local');
  const [storageTypes, setStorageTypes] = useState<API.StorageTypeOption[]>(DEFAULT_STORAGE_TYPES);
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();
  // 获取存储类型列表
  useEffect(() => {
    const fetchStorageTypes = async () => {
      try {
        const res = await getStorageTypes();
        if (res?.code === 200 && res?.data && res.data.length > 0) {
          setStorageTypes(res.data);
        }
      } catch (e) {
        console.error('获取存储类型失败:', e);
      }
    };
    fetchStorageTypes();
  }, []);
  const [submitLoading, setSubmitLoading] = useState(false);
  const submitRun = useCallback(
    async (data: API.StorageConfigDTO) => {
      setSubmitLoading(true);
      try {
        const res = data.configId
          ? await updateStorageConfig(data)
          : await addStorageConfig(data);
        if (res?.code === 200) {
          messageApi.success(
            data.configId
              ? intl.formatMessage({ id: 'pages.storageConfig.updateSuccess', defaultMessage: '更新成功' })
              : intl.formatMessage({ id: 'pages.storageConfig.addSuccess', defaultMessage: '新增成功' }),
          );
          setOpen(false);
          onOk?.();
        } else {
          messageApi.error(res?.message || '操作失败');
        }
      } catch {
        messageApi.error(
          data.configId
            ? intl.formatMessage({ id: 'pages.storageConfig.updateFailed', defaultMessage: '更新失败' })
            : intl.formatMessage({ id: 'pages.storageConfig.addFailed', defaultMessage: '新增失败' }),
        );
      } finally {
        setSubmitLoading(false);
      }
    },
    [messageApi, intl, onOk],
  );
  const [testLoading, setTestLoading] = useState(false);
  const testRun = useCallback(
    async (data: API.StorageConfigDTO) => {
      setTestLoading(true);
      try {
        const res = await testStorageConnection(data);
        if (res?.code === 200 && res?.data) {
          messageApi.success(
            intl.formatMessage({ id: 'pages.storageConfig.testSuccess', defaultMessage: '连接成功' }),
          );
        } else {
          messageApi.error(res?.message ||
            intl.formatMessage({ id: 'pages.storageConfig.testFailed', defaultMessage: '连接失败' }),
          );
        }
      } catch {
        messageApi.error(
          intl.formatMessage({ id: 'pages.storageConfig.testFailed', defaultMessage: '连接失败' }),
        );
      } finally {
        setTestLoading(false);
      }
    },
    [messageApi, intl],
  );
  useEffect(() => {
    if (open && values) {
      setStorageType(values.storageType || 'local');
      form.setFieldsValue({
        ...values,
        useHttps: values.useHttps === '1',
      });
    } else if (open && !values) {
      setStorageType('local');
      form.resetFields();
      form.setFieldsValue({
        storageType: 'local',
        status: '0',
        useHttps: false,
      });
    }
  }, [open, values, form]);
  const handleStorageTypeChange = useCallback((value: string) => {
    setStorageType(value);
  }, []);
  const handleSubmit = useCallback(async () => {
    try {
      const formValues = await form.validateFields();
      const data: API.StorageConfigDTO = {
        ...formValues,
        configId: values?.configId,
        useHttps: formValues.useHttps ? '1' : '0',
      };
      await submitRun(data);
    } catch {
      // validation failed
    }
  }, [form, values, submitRun]);
  const handleTestConnection = useCallback(async () => {
    try {
      const formValues = await form.validateFields();
      const data: API.StorageConfigDTO = {
        ...formValues,
        useHttps: formValues.useHttps ? '1' : '0',
      };
      await testRun(data);
    } catch {
      messageApi.warning('请先填写完整的配置信息');
    }
  }, [form, testRun, messageApi]);
  return (
    <>
      {contextHolder}
      <span onClick={() => setOpen(true)}>{trigger}</span>
      <Modal
        title={
          values?.configId
            ? intl.formatMessage({ id: 'pages.storageConfig.editTitle', defaultMessage: '编辑存储配置' })
            : intl.formatMessage({ id: 'pages.storageConfig.addTitle', defaultMessage: '新增存储配置' })
        }
        open={open}
        onCancel={() => setOpen(false)}
        onOk={handleSubmit}
        confirmLoading={submitLoading}
        width={700}
        okText={intl.formatMessage({ id: 'pages.storageConfig.save', defaultMessage: '保存' })}
        cancelText={intl.formatMessage({ id: 'pages.storageConfig.cancel', defaultMessage: '取消' })}
        footer={(_, { OkBtn, CancelBtn }) => (
          <>
            <CancelBtn />
            {storageType !== 'local' && (
              <Button
                loading={testLoading}
                onClick={handleTestConnection}
              >
                测试连接
              </Button>
            )}
            <OkBtn />
          </>
        )}
      >
        <Form
          form={form}
          layout="vertical"
          initialValues={{
            storageType: 'local',
            status: '0',
            useHttps: false,
          }}
        >
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                name="configName"
                label={intl.formatMessage({ id: 'pages.storageConfig.configName', defaultMessage: '配置名称' })}
                rules={[{ required: true, message: '请输入配置名称' }]}
              >
                <Input placeholder="请输入配置名称" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                name="storageType"
                label={intl.formatMessage({ id: 'pages.storageConfig.storageType', defaultMessage: '存储类型' })}
                rules={[{ required: true, message: '请选择存储类型' }]}
              >
                <Select onChange={handleStorageTypeChange} disabled={!!values?.configId}>
                  {storageTypes.map((item) => (
                    <Select.Option key={item.code} value={item.code}>
                      {item.desc}
                    </Select.Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                name="status"
                label={intl.formatMessage({ id: 'pages.storageConfig.status', defaultMessage: '状态' })}
              >
                <Radio.Group>
                  <Radio value="0">正常</Radio>
                  <Radio value="1">停用</Radio>
                </Radio.Group>
              </Form.Item>
            </Col>
          </Row>
          {storageType === 'local' && (
            <>
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item
                    name="localPath"
                    label={intl.formatMessage({ id: 'pages.storageConfig.localPath', defaultMessage: '存储路径' })}
                    rules={[{ required: true, message: '请输入存储路径' }]}
                    tooltip="文件存储的本地目录路径"
                  >
                    <Input placeholder="例如: /data/files 或 D:\files" />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item
                    name="localUrlPrefix"
                    label={intl.formatMessage({ id: 'pages.storageConfig.localUrlPrefix', defaultMessage: '访问前缀' })}
                    tooltip="文件访问的URL前缀"
                  >
                    <Input placeholder="例如: http://localhost:8081/api/storage/file" />
                  </Form.Item>
                </Col>
              </Row>
            </>
          )}
          {storageType !== 'local' && (
            <>
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item
                    name="endpoint"
                    label="Endpoint"
                    rules={[{ required: true, message: '请输入Endpoint' }]}
                    tooltip="存储服务访问域名"
                  >
                    <Input placeholder="例如: oss-cn-hangzhou.aliyuncs.com" />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item
                    name="bucketName"
                    label="Bucket"
                    rules={[{ required: true, message: '请输入Bucket名称' }]}
                  >
                    <Input placeholder="请输入存储桶名称" />
                  </Form.Item>
                </Col>
              </Row>
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item
                    name="accessKeyId"
                    label="Access Key ID"
                    rules={[{ required: true, message: '请输入AccessKey' }]}
                  >
                    <Input placeholder="请输入Access Key ID" />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item
                    name="accessKeySecret"
                    label="Access Key Secret"
                    rules={[{ required: true, message: '请输入SecretKey' }]}
                  >
                    <Input.Password placeholder="请输入Access Key Secret" />
                  </Form.Item>
                </Col>
              </Row>
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item
                    name="region"
                    label="Region"
                    tooltip="存储服务所在区域"
                    rules={[
                      {
                        required: storageType === 'tencent_cos',
                        message: '腾讯云COS必须填写Region',
                      },
                    ]}
                  >
                    <Input placeholder="例如: ap-guangzhou" />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item name="domain" label="自定义域名" tooltip="用于访问文件的自定义域名">
                    <Input placeholder="例如: https://cdn.example.com" />
                  </Form.Item>
                </Col>
              </Row>
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item name="useHttps" label="使用HTTPS" valuePropName="checked">
                    <Switch checkedChildren="是" unCheckedChildren="否" />
                  </Form.Item>
                </Col>
              </Row>
            </>
          )}
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={2} placeholder="请输入备注" />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};
export default StorageConfigForm;
