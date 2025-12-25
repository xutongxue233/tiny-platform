import { ProForm } from '@ant-design/pro-components';
import { Input, Popover, Tabs, Empty } from 'antd';
import { CloseCircleOutlined } from '@ant-design/icons';
import Icon, * as AntdIcons from '@ant-design/icons';
import type { FC } from 'react';
import { useState, useMemo } from 'react';

interface IconSelectProps {
  name: string;
  label: string;
  colProps?: Record<string, unknown>;
}

interface IconSelectInputProps {
  value?: string;
  onChange?: (value: string) => void;
}

// 图标分类数据
const iconData: Record<string, string[]> = {
  Direction: [
    'StepBackwardOutlined', 'StepForwardOutlined', 'FastBackwardOutlined', 'FastForwardOutlined',
    'ShrinkOutlined', 'ArrowsAltOutlined', 'DownOutlined', 'UpOutlined', 'LeftOutlined', 'RightOutlined',
    'CaretUpOutlined', 'CaretDownOutlined', 'CaretLeftOutlined', 'CaretRightOutlined',
    'UpCircleOutlined', 'DownCircleOutlined', 'LeftCircleOutlined', 'RightCircleOutlined',
    'DoubleRightOutlined', 'DoubleLeftOutlined', 'VerticalLeftOutlined', 'VerticalRightOutlined',
    'VerticalAlignTopOutlined', 'VerticalAlignMiddleOutlined', 'VerticalAlignBottomOutlined',
    'ForwardOutlined', 'BackwardOutlined', 'RollbackOutlined', 'EnterOutlined',
    'RetweetOutlined', 'SwapOutlined', 'SwapLeftOutlined', 'SwapRightOutlined',
    'ArrowUpOutlined', 'ArrowDownOutlined', 'ArrowLeftOutlined', 'ArrowRightOutlined',
    'LoginOutlined', 'LogoutOutlined', 'MenuFoldOutlined', 'MenuUnfoldOutlined',
    'FullscreenOutlined', 'FullscreenExitOutlined',
  ],
  Suggestion: [
    'QuestionOutlined', 'QuestionCircleOutlined', 'PlusOutlined', 'PlusCircleOutlined',
    'PauseOutlined', 'PauseCircleOutlined', 'MinusOutlined', 'MinusCircleOutlined',
    'PlusSquareOutlined', 'MinusSquareOutlined', 'InfoOutlined', 'InfoCircleOutlined',
    'ExclamationOutlined', 'ExclamationCircleOutlined', 'CloseOutlined', 'CloseCircleOutlined',
    'CloseSquareOutlined', 'CheckOutlined', 'CheckCircleOutlined', 'CheckSquareOutlined',
    'ClockCircleOutlined', 'WarningOutlined', 'IssuesCloseOutlined', 'StopOutlined',
  ],
  Editor: [
    'EditOutlined', 'FormOutlined', 'CopyOutlined', 'ScissorOutlined', 'DeleteOutlined',
    'SnippetsOutlined', 'DiffOutlined', 'HighlightOutlined', 'AlignCenterOutlined',
    'AlignLeftOutlined', 'AlignRightOutlined', 'BgColorsOutlined', 'BoldOutlined',
    'ItalicOutlined', 'UnderlineOutlined', 'StrikethroughOutlined', 'RedoOutlined',
    'UndoOutlined', 'ZoomInOutlined', 'ZoomOutOutlined', 'FontColorsOutlined',
    'FontSizeOutlined', 'LineHeightOutlined', 'SortAscendingOutlined', 'SortDescendingOutlined',
    'DragOutlined', 'OrderedListOutlined', 'UnorderedListOutlined',
  ],
  Data: [
    'AreaChartOutlined', 'PieChartOutlined', 'BarChartOutlined', 'DotChartOutlined',
    'LineChartOutlined', 'RadarChartOutlined', 'HeatMapOutlined', 'FallOutlined',
    'RiseOutlined', 'StockOutlined', 'BoxPlotOutlined', 'FundOutlined', 'SlidersOutlined',
  ],
  Common: [
    'HomeOutlined', 'SettingOutlined', 'AppstoreOutlined', 'UserOutlined', 'TeamOutlined',
    'LockOutlined', 'UnlockOutlined', 'KeyOutlined', 'MenuOutlined', 'BellOutlined',
    'SearchOutlined', 'StarOutlined', 'HeartOutlined', 'MailOutlined', 'MessageOutlined',
    'PhoneOutlined', 'CalendarOutlined', 'CameraOutlined', 'CloudOutlined', 'DownloadOutlined',
    'UploadOutlined', 'FileOutlined', 'FolderOutlined', 'DatabaseOutlined', 'TableOutlined',
    'ProfileOutlined', 'ScheduleOutlined', 'ShoppingOutlined', 'ShoppingCartOutlined',
    'WalletOutlined', 'BankOutlined', 'CreditCardOutlined', 'DollarOutlined',
    'SafetyOutlined', 'SafetyCertificateOutlined', 'SecurityScanOutlined',
    'ToolOutlined', 'ControlOutlined', 'DashboardOutlined', 'CodeOutlined', 'ApiOutlined',
    'BugOutlined', 'BuildOutlined', 'ExperimentOutlined', 'RocketOutlined',
    'FireOutlined', 'ThunderboltOutlined', 'BulbOutlined', 'FlagOutlined', 'CrownOutlined',
    'TrophyOutlined', 'GiftOutlined', 'SmileOutlined', 'MehOutlined', 'FrownOutlined',
    'LikeOutlined', 'DislikeOutlined', 'EyeOutlined', 'EyeInvisibleOutlined',
    'CompassOutlined', 'GlobalOutlined', 'EnvironmentOutlined', 'LinkOutlined',
    'FilterOutlined', 'SyncOutlined', 'ReloadOutlined', 'PoweroffOutlined',
    'DesktopOutlined', 'LaptopOutlined', 'MobileOutlined', 'PrinterOutlined',
    'SoundOutlined', 'AudioOutlined', 'VideoCameraOutlined', 'PictureOutlined',
    'BookOutlined', 'ReadOutlined', 'HistoryOutlined', 'ProjectOutlined', 'ApartmentOutlined',
    'BranchesOutlined', 'PartitionOutlined', 'ClusterOutlined', 'DeploymentUnitOutlined',
    'PushpinOutlined', 'TagOutlined', 'TagsOutlined', 'QrcodeOutlined', 'BarcodeOutlined',
    'ScanOutlined', 'WifiOutlined', 'UsbOutlined', 'RobotOutlined', 'AlertOutlined',
    'AuditOutlined', 'IdcardOutlined', 'ContactsOutlined', 'SolutionOutlined',
    'InsuranceOutlined', 'ReconciliationOutlined', 'CarryOutOutlined', 'FileProtectOutlined',
    'AccountBookOutlined', 'MoneyCollectOutlined', 'PropertySafetyOutlined',
    'TransactionOutlined', 'RedEnvelopeOutlined', 'CoffeeOutlined', 'MedicineBoxOutlined',
    'RestOutlined', 'SkinOutlined', 'CarOutlined', 'ShopOutlined',
  ],
  Brand: [
    'AndroidOutlined', 'AppleOutlined', 'WindowsOutlined', 'ChromeOutlined',
    'GithubOutlined', 'GitlabOutlined', 'Html5Outlined', 'WechatOutlined',
    'AlipayOutlined', 'TaobaoOutlined', 'WeiboOutlined', 'QqOutlined',
    'DingdingOutlined', 'TwitterOutlined', 'FacebookOutlined', 'GoogleOutlined',
    'LinkedinOutlined', 'YoutubeOutlined', 'InstagramOutlined', 'SkypeOutlined',
    'SlackOutlined', 'AmazonOutlined', 'DropboxOutlined', 'RedditOutlined',
    'CodeSandboxOutlined', 'CodepenOutlined', 'AntDesignOutlined', 'AliyunOutlined',
    'ZhihuOutlined', 'BehanceOutlined', 'DribbbleOutlined', 'SketchOutlined', 'YuqueOutlined',
  ],
};

const IconSelectInput: FC<IconSelectInputProps> = ({ value, onChange }) => {
  const [open, setOpen] = useState(false);

  // 渲染图标
  const renderIcon = (iconName: string) => {
    const IconComponent = (AntdIcons as Record<string, unknown>)[iconName];
    if (!IconComponent) return null;
    return <Icon component={IconComponent as React.ForwardRefExoticComponent<unknown>} />;
  };

  const handleSelect = (iconName: string) => {
    onChange?.(iconName);
    setOpen(false);
  };

  const handleClear = (e: React.MouseEvent) => {
    e.stopPropagation();
    onChange?.('');
  };

  // 渲染图标网格
  const renderIconGrid = (icons: string[]) => {
    if (icons.length === 0) {
      return <Empty description="No icons" image={Empty.PRESENTED_IMAGE_SIMPLE} />;
    }

    return (
      <div
        style={{
          display: 'flex',
          flexWrap: 'wrap',
          maxHeight: 280,
          overflowY: 'auto',
        }}
      >
        {icons.map((iconName) => {
          const isSelected = value === iconName;
          return (
            <div
              key={iconName}
              title={iconName}
              onClick={() => handleSelect(iconName)}
              style={{
                width: 40,
                height: 40,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                cursor: 'pointer',
                fontSize: 20,
                borderRadius: 4,
                border: isSelected ? '1px solid #1677ff' : '1px solid transparent',
                backgroundColor: isSelected ? '#e6f4ff' : 'transparent',
                color: isSelected ? '#1677ff' : '#666',
                transition: 'all 0.2s',
              }}
              onMouseEnter={(e) => {
                if (!isSelected) {
                  e.currentTarget.style.backgroundColor = '#f5f5f5';
                }
              }}
              onMouseLeave={(e) => {
                if (!isSelected) {
                  e.currentTarget.style.backgroundColor = 'transparent';
                }
              }}
            >
              {renderIcon(iconName)}
            </div>
          );
        })}
      </div>
    );
  };

  // 标签页配置
  const tabItems = useMemo(
    () =>
      Object.entries(iconData).map(([key, icons]) => ({
        key,
        label: key,
        children: renderIconGrid(icons),
      })),
    [value],
  );

  const content = (
    <div style={{ width: 400 }}>
      <Tabs size="small" items={tabItems} tabBarStyle={{ marginBottom: 8 }} />
    </div>
  );

  return (
    <Popover
      content={content}
      trigger="click"
      open={open}
      onOpenChange={setOpen}
      placement="bottomLeft"
      arrow={false}
    >
      <span style={{ display: 'block', cursor: 'pointer' }}>
        <Input
          readOnly
          value={value}
          placeholder="Select icon"
          prefix={value ? renderIcon(value) : null}
          suffix={
            value ? (
              <CloseCircleOutlined
                style={{ color: '#999', cursor: 'pointer' }}
                onClick={handleClear}
              />
            ) : null
          }
          style={{ cursor: 'pointer' }}
        />
      </span>
    </Popover>
  );
};

const IconSelect: FC<IconSelectProps> = ({ name, label, colProps }) => {
  return (
    <ProForm.Item name={name} label={label} colProps={colProps}>
      <IconSelectInput />
    </ProForm.Item>
  );
};

export default IconSelect;
