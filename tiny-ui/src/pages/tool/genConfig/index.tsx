import { PageContainer, ProCard, ProForm, ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { message, Spin } from 'antd';
import React, { useEffect, useState } from 'react';
import { getGenConfigList, batchUpdateGenConfig, type GenConfig } from '@/services/ant-design-pro/generator';

const GenConfigPage: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [configs, setConfigs] = useState<GenConfig[]>([]);
  const [messageApi, contextHolder] = message.useMessage();

  useEffect(() => {
    loadConfigs();
  }, []);

  const loadConfigs = async () => {
    setLoading(true);
    try {
      const res = await getGenConfigList();
      if (res.code === 200 && res.data) {
        setConfigs(res.data);
      }
    } catch (error) {
      messageApi.error('获取配置失败');
    } finally {
      setLoading(false);
    }
  };

  const getConfigValue = (key: string): string => {
    const config = configs.find((c) => c.configKey === key);
    return config?.configValue || '';
  };

  const handleSubmit = async (values: Record<string, string>) => {
    setSaving(true);
    try {
      const updateConfigs: GenConfig[] = Object.entries(values).map(([key, value]) => {
        const existing = configs.find((c) => c.configKey === key);
        return {
          configId: existing?.configId,
          configKey: key,
          configValue: value,
        };
      });
      await batchUpdateGenConfig(updateConfigs);
      messageApi.success('保存成功');
      loadConfigs();
    } catch (error) {
      messageApi.error('保存失败');
    } finally {
      setSaving(false);
    }
  };

  const initialValues = configs.reduce((acc, config) => {
    if (config.configKey) {
      acc[config.configKey] = config.configValue || '';
    }
    return acc;
  }, {} as Record<string, string>);

  return (
    <PageContainer title="生成配置">
      {contextHolder}
      <Spin spinning={loading}>
        {configs.length > 0 && (
          <ProForm
            initialValues={initialValues}
            onFinish={handleSubmit}
            submitter={{
              submitButtonProps: {
                loading: saving,
              },
              searchConfig: {
                submitText: '保存配置',
              },
              resetButtonProps: {
                style: { display: 'none' },
              },
            }}
          >
            <ProCard title="基本配置" collapsible bordered style={{ marginBottom: 16 }}>
              <ProForm.Group>
                <ProFormText
                  name="gen.author"
                  label="默认作者"
                  width="md"
                  tooltip="生成代码的默认作者"
                  rules={[{ required: true, message: '请输入默认作者' }]}
                />
                <ProFormText
                  name="gen.packageName"
                  label="默认包名"
                  width="md"
                  tooltip="生成代码的默认包路径"
                  rules={[{ required: true, message: '请输入默认包名' }]}
                />
              </ProForm.Group>
              <ProFormTextArea
                name="gen.tablePrefixes"
                label="表前缀"
                width="xl"
                tooltip="多个前缀用逗号分隔，导入表时会自动去除前缀"
                placeholder="例如：sys_,gen_,tb_"
                fieldProps={{ rows: 2 }}
              />
            </ProCard>

            <ProCard title="路径配置" collapsible bordered style={{ marginBottom: 16 }}>
              <ProFormText
                name="gen.backendPath"
                label="后端代码路径"
                width="xl"
                tooltip="后端代码生成的目标路径"
                placeholder="例如：D:/project/tiny-platform"
              />
              <ProFormText
                name="gen.frontendPath"
                label="前端代码路径"
                width="xl"
                tooltip="前端代码生成的目标路径"
                placeholder="例如：D:/project/tiny-platform/tiny-ui"
              />
            </ProCard>

            <ProCard title="高级配置" collapsible bordered defaultCollapsed style={{ marginBottom: 24 }}>
              <ProFormText
                name="gen.removePrefix"
                label="是否去除前缀"
                width="md"
                tooltip="true/false，生成类名时是否去除表前缀"
                placeholder="true"
              />
              <ProFormTextArea
                name="gen.excludeColumns"
                label="排除字段"
                width="xl"
                tooltip="生成时排除的字段，多个用逗号分隔"
                placeholder="例如：create_by,create_time,update_by,update_time"
                fieldProps={{ rows: 2 }}
              />
            </ProCard>
          </ProForm>
        )}
      </Spin>
    </PageContainer>
  );
};

export default GenConfigPage;
