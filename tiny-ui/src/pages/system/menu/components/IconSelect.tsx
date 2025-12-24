import { ProForm } from '@ant-design/pro-components';
import { Input, Modal, Tabs, Empty } from 'antd';
import { SearchOutlined, CloseCircleOutlined } from '@ant-design/icons';
import * as AntdIcons from '@ant-design/icons';
import type { FC } from 'react';
import { useState, useMemo } from 'react';

interface IconSelectProps {
  name: string;
  label: string;
}

const outlinedIcons = [
  'StepBackwardOutlined', 'StepForwardOutlined', 'FastBackwardOutlined', 'FastForwardOutlined',
  'ShrinkOutlined', 'ArrowsAltOutlined', 'DownOutlined', 'UpOutlined', 'LeftOutlined', 'RightOutlined',
  'CaretUpOutlined', 'CaretDownOutlined', 'CaretLeftOutlined', 'CaretRightOutlined',
  'UpCircleOutlined', 'DownCircleOutlined', 'LeftCircleOutlined', 'RightCircleOutlined',
  'DoubleRightOutlined', 'DoubleLeftOutlined', 'VerticalLeftOutlined', 'VerticalRightOutlined',
  'VerticalAlignTopOutlined', 'VerticalAlignMiddleOutlined', 'VerticalAlignBottomOutlined',
  'ForwardOutlined', 'BackwardOutlined', 'RollbackOutlined', 'EnterOutlined',
  'RetweetOutlined', 'SwapOutlined', 'SwapLeftOutlined', 'SwapRightOutlined',
  'ArrowUpOutlined', 'ArrowDownOutlined', 'ArrowLeftOutlined', 'ArrowRightOutlined',
  'PlayCircleOutlined', 'UpSquareOutlined', 'DownSquareOutlined', 'LeftSquareOutlined', 'RightSquareOutlined',
  'LoginOutlined', 'LogoutOutlined', 'MenuFoldOutlined', 'MenuUnfoldOutlined',
  'BorderBottomOutlined', 'BorderHorizontalOutlined', 'BorderInnerOutlined', 'BorderOuterOutlined',
  'BorderLeftOutlined', 'BorderRightOutlined', 'BorderTopOutlined', 'BorderVerticleOutlined',
  'PicCenterOutlined', 'PicLeftOutlined', 'PicRightOutlined', 'RadiusBottomleftOutlined',
  'RadiusBottomrightOutlined', 'RadiusUpleftOutlined', 'RadiusUprightOutlined', 'FullscreenOutlined',
  'FullscreenExitOutlined',
];

const suggestedIcons = [
  'QuestionOutlined', 'QuestionCircleOutlined', 'PlusOutlined', 'PlusCircleOutlined', 'PauseOutlined',
  'PauseCircleOutlined', 'MinusOutlined', 'MinusCircleOutlined', 'PlusSquareOutlined', 'MinusSquareOutlined',
  'InfoOutlined', 'InfoCircleOutlined', 'ExclamationOutlined', 'ExclamationCircleOutlined',
  'CloseOutlined', 'CloseCircleOutlined', 'CloseSquareOutlined', 'CheckOutlined', 'CheckCircleOutlined',
  'CheckSquareOutlined', 'ClockCircleOutlined', 'WarningOutlined', 'IssuesCloseOutlined',
  'StopOutlined',
];

const editIcons = [
  'EditOutlined', 'FormOutlined', 'CopyOutlined', 'ScissorOutlined', 'DeleteOutlined',
  'SnippetsOutlined', 'DiffOutlined', 'HighlightOutlined', 'AlignCenterOutlined', 'AlignLeftOutlined',
  'AlignRightOutlined', 'BgColorsOutlined', 'BoldOutlined', 'ItalicOutlined', 'UnderlineOutlined',
  'StrikethroughOutlined', 'RedoOutlined', 'UndoOutlined', 'ZoomInOutlined', 'ZoomOutOutlined',
  'FontColorsOutlined', 'FontSizeOutlined', 'LineHeightOutlined', 'DashOutlined',
  'SmallDashOutlined', 'SortAscendingOutlined', 'SortDescendingOutlined', 'DragOutlined',
  'OrderedListOutlined', 'UnorderedListOutlined', 'RadiusSettingOutlined', 'ColumnWidthOutlined',
  'ColumnHeightOutlined',
];

const dataIcons = [
  'AreaChartOutlined', 'PieChartOutlined', 'BarChartOutlined', 'DotChartOutlined',
  'LineChartOutlined', 'RadarChartOutlined', 'HeatMapOutlined', 'FallOutlined', 'RiseOutlined',
  'StockOutlined', 'BoxPlotOutlined', 'FundOutlined', 'SlidersOutlined',
];

const brandIcons = [
  'AndroidOutlined', 'AppleOutlined', 'WindowsOutlined', 'IeOutlined', 'ChromeOutlined',
  'GithubOutlined', 'AliwangwangOutlined', 'DingdingOutlined', 'WeiboSquareOutlined',
  'WeiboCircleOutlined', 'TaobaoCircleOutlined', 'Html5Outlined', 'WeiboOutlined',
  'TwitterOutlined', 'WechatOutlined', 'YoutubeOutlined', 'AlipayCircleOutlined',
  'TaobaoOutlined', 'SkypeOutlined', 'QqOutlined', 'MediumWorkmarkOutlined', 'GitlabOutlined',
  'MediumOutlined', 'LinkedinOutlined', 'GooglePlusOutlined', 'DropboxOutlined',
  'FacebookOutlined', 'CodepenOutlined', 'CodeSandboxOutlined', 'AmazonOutlined',
  'GoogleOutlined', 'CodepenCircleOutlined', 'AlipayOutlined', 'AntDesignOutlined',
  'AntCloudOutlined', 'AliyunOutlined', 'ZhihuOutlined', 'SlackOutlined', 'SlackSquareOutlined',
  'BehanceOutlined', 'BehanceSquareOutlined', 'DribbbleOutlined', 'DribbbleSquareOutlined',
  'InstagramOutlined', 'YuqueOutlined', 'AlibabaOutlined', 'YahooOutlined', 'RedditOutlined',
  'SketchOutlined',
];

const applicationIcons = [
  'AccountBookOutlined', 'AimOutlined', 'AlertOutlined', 'ApartmentOutlined', 'ApiOutlined',
  'AppstoreAddOutlined', 'AppstoreOutlined', 'AudioOutlined', 'AudioMutedOutlined', 'AuditOutlined',
  'BankOutlined', 'BarcodeOutlined', 'BarsOutlined', 'BellOutlined', 'BlockOutlined',
  'BookOutlined', 'BorderOutlined', 'BorderlessTableOutlined', 'BranchesOutlined', 'BugOutlined',
  'BuildOutlined', 'BulbOutlined', 'CalculatorOutlined', 'CalendarOutlined', 'CameraOutlined',
  'CarOutlined', 'CarryOutOutlined', 'CiCircleOutlined', 'CiOutlined', 'ClearOutlined',
  'CloudDownloadOutlined', 'CloudOutlined', 'CloudServerOutlined', 'CloudSyncOutlined',
  'CloudUploadOutlined', 'ClusterOutlined', 'CodeOutlined', 'CoffeeOutlined', 'CommentOutlined',
  'CompassOutlined', 'CompressOutlined', 'ConsoleSqlOutlined', 'ContactsOutlined',
  'ContainerOutlined', 'ControlOutlined', 'CopyrightOutlined', 'CreditCardOutlined',
  'CrownOutlined', 'CustomerServiceOutlined', 'DashboardOutlined', 'DatabaseOutlined',
  'DeleteColumnOutlined', 'DeleteRowOutlined', 'DeliveredProcedureOutlined', 'DeploymentUnitOutlined',
  'DesktopOutlined', 'DisconnectOutlined', 'DislikeOutlined', 'DollarOutlined',
  'DownloadOutlined', 'EllipsisOutlined', 'EnvironmentOutlined', 'EuroCircleOutlined',
  'EuroOutlined', 'ExceptionOutlined', 'ExpandAltOutlined', 'ExpandOutlined', 'ExperimentOutlined',
  'ExportOutlined', 'EyeOutlined', 'EyeInvisibleOutlined', 'FieldBinaryOutlined',
  'FieldNumberOutlined', 'FieldStringOutlined', 'FieldTimeOutlined', 'FileAddOutlined',
  'FileDoneOutlined', 'FileExcelOutlined', 'FileExclamationOutlined', 'FileOutlined',
  'FileGifOutlined', 'FileImageOutlined', 'FileJpgOutlined', 'FileMarkdownOutlined',
  'FilePdfOutlined', 'FilePptOutlined', 'FileProtectOutlined', 'FileSearchOutlined',
  'FileSyncOutlined', 'FileTextOutlined', 'FileUnknownOutlined', 'FileWordOutlined',
  'FileZipOutlined', 'FilterOutlined', 'FireOutlined', 'FlagOutlined', 'FolderAddOutlined',
  'FolderOutlined', 'FolderOpenOutlined', 'FolderViewOutlined', 'ForkOutlined',
  'FormatPainterOutlined', 'FrownOutlined', 'FunctionOutlined', 'FundProjectionScreenOutlined',
  'FundViewOutlined', 'FunnelPlotOutlined', 'GatewayOutlined', 'GifOutlined', 'GiftOutlined',
  'GlobalOutlined', 'GoldOutlined', 'GroupOutlined', 'HddOutlined', 'HeartOutlined',
  'HistoryOutlined', 'HolderOutlined', 'HomeOutlined', 'HourglassOutlined', 'IdcardOutlined',
  'ImportOutlined', 'InboxOutlined', 'InsertRowAboveOutlined', 'InsertRowBelowOutlined',
  'InsertRowLeftOutlined', 'InsertRowRightOutlined', 'InsuranceOutlined', 'InteractionOutlined',
  'KeyOutlined', 'LaptopOutlined', 'LayoutOutlined', 'LikeOutlined', 'LineOutlined',
  'LinkOutlined', 'Loading3QuartersOutlined', 'LoadingOutlined', 'LockOutlined',
  'MacCommandOutlined', 'MailOutlined', 'ManOutlined', 'MedicineBoxOutlined', 'MehOutlined',
  'MenuOutlined', 'MergeCellsOutlined', 'MessageOutlined', 'MobileOutlined', 'MoneyCollectOutlined',
  'MonitorOutlined', 'MoreOutlined', 'NodeCollapseOutlined', 'NodeExpandOutlined',
  'NodeIndexOutlined', 'NotificationOutlined', 'NumberOutlined', 'OneToOneOutlined',
  'PaperClipOutlined', 'PartitionOutlined', 'PayCircleOutlined', 'PercentageOutlined',
  'PhoneOutlined', 'PictureOutlined', 'PlaySquareOutlined', 'PoundCircleOutlined', 'PoundOutlined',
  'PoweroffOutlined', 'PrinterOutlined', 'ProfileOutlined', 'ProjectOutlined', 'PropertySafetyOutlined',
  'PullRequestOutlined', 'PushpinOutlined', 'QrcodeOutlined', 'ReadOutlined', 'ReconciliationOutlined',
  'RedEnvelopeOutlined', 'ReloadOutlined', 'RestOutlined', 'RobotOutlined', 'RocketOutlined',
  'RotateLeftOutlined', 'RotateRightOutlined', 'SafetyCertificateOutlined', 'SafetyOutlined',
  'SaveOutlined', 'ScanOutlined', 'ScheduleOutlined', 'SearchOutlined', 'SecurityScanOutlined',
  'SelectOutlined', 'SendOutlined', 'SettingOutlined', 'ShakeOutlined', 'ShareAltOutlined',
  'ShopOutlined', 'ShoppingCartOutlined', 'ShoppingOutlined', 'SisternodeOutlined',
  'SkinOutlined', 'SmileOutlined', 'SolutionOutlined', 'SoundOutlined', 'SplitCellsOutlined',
  'StarOutlined', 'SubnodeOutlined', 'SwitcherOutlined', 'SyncOutlined', 'TableOutlined',
  'TabletOutlined', 'TagOutlined', 'TagsOutlined', 'TeamOutlined', 'ThunderboltOutlined',
  'ToTopOutlined', 'ToolOutlined', 'TrademarkCircleOutlined', 'TrademarkOutlined',
  'TransactionOutlined', 'TranslationOutlined', 'TrophyOutlined', 'UngroupOutlined',
  'UnlockOutlined', 'UploadOutlined', 'UsbOutlined', 'UserAddOutlined', 'UserDeleteOutlined',
  'UserOutlined', 'UserSwitchOutlined', 'UsergroupAddOutlined', 'UsergroupDeleteOutlined',
  'VerifiedOutlined', 'VideoCameraAddOutlined', 'VideoCameraOutlined', 'WalletOutlined',
  'WhatsAppOutlined', 'WifiOutlined', 'WomanOutlined',
];

const iconCategories = [
  { key: 'direction', label: '方向', icons: outlinedIcons },
  { key: 'suggestion', label: '提示', icons: suggestedIcons },
  { key: 'edit', label: '编辑', icons: editIcons },
  { key: 'data', label: '数据', icons: dataIcons },
  { key: 'brand', label: '品牌', icons: brandIcons },
  { key: 'application', label: '应用', icons: applicationIcons },
];

const IconSelect: FC<IconSelectProps> = ({ name, label }) => {
  return (
    <ProForm.Item
      name={name}
      label={label}
    >
      <IconSelectInput />
    </ProForm.Item>
  );
};

interface IconSelectInputProps {
  value?: string;
  onChange?: (value: string) => void;
}

const IconSelectInput: FC<IconSelectInputProps> = ({ value, onChange }) => {
  const [open, setOpen] = useState(false);
  const [searchValue, setSearchValue] = useState('');

  const filteredCategories = useMemo(() => {
    if (!searchValue) return iconCategories;
    return iconCategories.map((category) => ({
      ...category,
      icons: category.icons.filter((icon) =>
        icon.toLowerCase().includes(searchValue.toLowerCase()),
      ),
    })).filter((category) => category.icons.length > 0);
  }, [searchValue]);

  const handleSelect = (iconName: string) => {
    onChange?.(iconName);
    setOpen(false);
    setSearchValue('');
  };

  const handleClear = (e: React.MouseEvent) => {
    e.stopPropagation();
    onChange?.('');
  };

  const handleModalClose = () => {
    setOpen(false);
    setSearchValue('');
  };

  const SelectedIcon = value ? (AntdIcons as any)[value] : null;

  const renderIconGrid = (iconNames: string[]) => {
    if (iconNames.length === 0) {
      return <Empty description="没有找到图标" />;
    }
    return (
      <div className="grid grid-cols-8 gap-2 max-h-80 overflow-y-auto p-1">
        {iconNames.map((iconName) => {
          const IconComponent = (AntdIcons as any)[iconName];
          if (!IconComponent) return null;
          const isSelected = value === iconName;
          return (
            <div
              key={iconName}
              className={`flex flex-col items-center justify-center p-2 cursor-pointer rounded transition-all hover:bg-gray-100 ${
                isSelected ? 'bg-blue-50 ring-2 ring-blue-500' : ''
              }`}
              onClick={() => handleSelect(iconName)}
              title={iconName}
            >
              <IconComponent className={`text-2xl ${isSelected ? 'text-blue-500' : ''}`} />
            </div>
          );
        })}
      </div>
    );
  };

  return (
    <>
      <Input
        readOnly
        value={value}
        placeholder="点击选择图标"
        onClick={() => setOpen(true)}
        prefix={SelectedIcon ? <SelectedIcon /> : null}
        suffix={
          value ? (
            <CloseCircleOutlined
              className="cursor-pointer text-gray-400 hover:text-gray-600"
              onClick={handleClear}
            />
          ) : null
        }
        className="cursor-pointer"
      />
      <Modal
        title="选择图标"
        open={open}
        onCancel={handleModalClose}
        footer={null}
        width={600}
        destroyOnClose
      >
        <Input
          placeholder="搜索图标名称"
          prefix={<SearchOutlined />}
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
          allowClear
          className="mb-4"
        />
        <Tabs
          size="small"
          items={filteredCategories.map((category) => ({
            key: category.key,
            label: `${category.label} (${category.icons.length})`,
            children: renderIconGrid(category.icons),
          }))}
        />
      </Modal>
    </>
  );
};

export default IconSelect;
