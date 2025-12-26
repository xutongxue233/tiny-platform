import { ModalForm, ProFormRadio, ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { useRequest } from '@umijs/max';
import { Button, message } from 'antd';
import React, { FC, ReactElement, useEffect, useMemo, useState } from 'react';
import { addNotice, updateNotice } from '@/services/ant-design-pro/notice';
import { uploadFile } from '@/services/ant-design-pro/api';
import { useDicts } from '@/hooks/useDict';
import { Editor } from '@bytemd/react';
import type { BytemdPlugin } from 'bytemd';
import gfm from '@bytemd/plugin-gfm';
import highlight from '@bytemd/plugin-highlight';
import zhHans from 'bytemd/locales/zh_Hans.json';
import 'bytemd/dist/index.css';
import 'highlight.js/styles/github.css';

const plugins: BytemdPlugin[] = [gfm(), highlight()];

interface NoticeFormProps {
  trigger?: ReactElement;
  values?: API.SysNotice;
  onOk?: () => void;
}

const NoticeForm: FC<NoticeFormProps> = (props) => {
  const { trigger, values, onOk } = props;
  const isEdit = !!values?.noticeId;
  const [open, setOpen] = useState(false);
  const [content, setContent] = useState('');
  const [messageApi, contextHolder] = message.useMessage();

  const { getOptions } = useDicts(['sys_notice_type', 'sys_common_status']);

  const noticeTypeOptions = getOptions('sys_notice_type');
  const statusOptions = getOptions('sys_common_status');

  // 图片上传处理
  const handleUploadImages = async (files: File[]) => {
    const results: { url: string; alt: string; title: string }[] = [];
    for (const file of files) {
      try {
        const res = await uploadFile(file);
        if (res.code === 200 && res.data?.url) {
          results.push({
            url: res.data.url,
            alt: res.data.originalFilename || file.name,
            title: res.data.originalFilename || file.name,
          });
        } else {
          messageApi.error(`上传失败: ${file.name}`);
        }
      } catch (error) {
        messageApi.error(`上传失败: ${file.name}`);
      }
    }
    return results;
  };

  useEffect(() => {
    if (open && values?.noticeContent) {
      setContent(values.noticeContent);
    } else if (!open) {
      setContent('');
    }
  }, [open, values?.noticeContent]);

  const { run: addRun, loading: addLoading } = useRequest(addNotice, {
    manual: true,
    onSuccess: () => {
      setOpen(false);
      setContent('');
      messageApi.success('新增成功');
      onOk?.();
    },
    onError: () => {
      messageApi.error('新增失败');
    },
  });

  const { run: updateRun, loading: updateLoading } = useRequest(updateNotice, {
    manual: true,
    onSuccess: () => {
      setOpen(false);
      setContent('');
      messageApi.success('修改成功');
      onOk?.();
    },
    onError: () => {
      messageApi.error('修改失败');
    },
  });

  const handleFinish = async (formValues: API.SysNoticeDTO) => {
    const submitData = {
      ...formValues,
      noticeContent: content,
    };

    if (isEdit) {
      submitData.noticeId = values?.noticeId;
      await updateRun(submitData);
    } else {
      await addRun(submitData);
    }
    return true;
  };

  const triggerDom = useMemo(() => {
    if (trigger) {
      return React.cloneElement(trigger, {
        onClick: () => setOpen(true),
      });
    }
    return (
      <Button type="primary" onClick={() => setOpen(true)}>
        新增
      </Button>
    );
  }, [trigger]);

  return (
    <>
      {contextHolder}
      {triggerDom}
      <ModalForm<API.SysNoticeDTO>
        title={isEdit ? '编辑公告' : '新增公告'}
        open={open}
        onOpenChange={setOpen}
        width={900}
        grid
        rowProps={{ gutter: [16, 0] }}
        initialValues={{
          noticeType: '1',
          isTop: '0',
          status: '0',
          ...values,
        }}
        modalProps={{
          destroyOnHidden: true,
          maskClosable: false,
        }}
        submitter={{
          submitButtonProps: {
            loading: addLoading || updateLoading,
          },
        }}
        onFinish={handleFinish}
      >
        <ProFormText
          name="noticeTitle"
          label="公告标题"
          colProps={{ span: 24 }}
          rules={[
            { required: true, message: '请输入公告标题' },
            { max: 100, message: '公告标题长度不能超过100个字符' },
          ]}
          placeholder="请输入公告标题"
        />
        <ProFormRadio.Group
          name="noticeType"
          label="公告类型"
          colProps={{ span: 8 }}
          rules={[{ required: true, message: '请选择公告类型' }]}
          options={noticeTypeOptions}
        />
        <ProFormRadio.Group
          name="isTop"
          label="是否置顶"
          colProps={{ span: 8 }}
          options={[
            { label: '否', value: '0' },
            { label: '是', value: '1' },
          ]}
        />
        <ProFormRadio.Group
          name="status"
          label="状态"
          colProps={{ span: 8 }}
          options={statusOptions}
        />
        <div style={{ marginBottom: 24 }}>
          <label style={{ display: 'block', marginBottom: 8 }}>
            公告内容
          </label>
          <Editor
            value={content}
            plugins={plugins}
            locale={zhHans}
            onChange={(v) => setContent(v)}
            uploadImages={handleUploadImages}
            placeholder="请输入公告内容，支持 Markdown 格式"
            editorConfig={{
              autofocus: false,
            }}
          />
          <style>{`
            .bytemd-toolbar-right [bytemd-tippy-path="4"],
            .bytemd-toolbar-right [bytemd-tippy-path="5"] {
              display: none !important;
            }
          `}</style>
        </div>
        <ProFormTextArea
          name="remark"
          label="备注"
          colProps={{ span: 24 }}
          fieldProps={{
            rows: 3,
            maxLength: 500,
            showCount: true,
          }}
          placeholder="请输入备注"
        />
      </ModalForm>
    </>
  );
};

export default NoticeForm;
