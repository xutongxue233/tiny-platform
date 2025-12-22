import { ProFormText } from '@ant-design/pro-components';
import { Input, Popover, Tabs, Empty } from 'antd';
import * as AntdIcons from '@ant-design/icons';
import type { FC } from 'react';
import { useState, useMemo, useCallback } from 'react';

interface IconSelectProps {
  name: string;
  label: string;
  colProps?: { span: number };
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

const IconSelect: FC<IconSelectProps> = ({ name, label, colProps }) => {
  const [searchValue, setSearchValue] = useState('');
  const [visible, setVisible] = useState(false);

  const filteredCategories = useMemo(() => {
    if (!searchValue) return iconCategories;
    return iconCategories.map((category) => ({
      ...category,
      icons: category.icons.filter((icon) =>
        icon.toLowerCase().includes(searchValue.toLowerCase()),
      ),
    })).filter((category) => category.icons.length > 0);
  }, [searchValue]);

  const renderIconGrid = useCallback(
    (iconNames: string[], onChange: (value: string) => void) => {
      if (iconNames.length === 0) {
        return <Empty description="没有找到图标" />;
      }
      return (
        <div
          style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(8, 1fr)',
            gap: 8,
            maxHeight: 300,
            overflowY: 'auto',
          }}
        >
          {iconNames.map((iconName) => {
            const IconComponent = (AntdIcons as any)[iconName];
            if (!IconComponent) return null;
            return (
              <div
                key={iconName}
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  padding: 8,
                  cursor: 'pointer',
                  borderRadius: 4,
                  transition: 'background-color 0.2s',
                }}
                onClick={() => {
                  onChange(iconName);
                  setVisible(false);
                }}
                onMouseEnter={(e) => {
                  e.currentTarget.style.backgroundColor = '#f0f0f0';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.backgroundColor = 'transparent';
                }}
                title={iconName}
              >
                <IconComponent style={{ fontSize: 24 }} />
              </div>
            );
          })}
        </div>
      );
    },
    [],
  );

  return (
    <ProFormText
      name={name}
      label={label}
      colProps={colProps}
    >
      {({ value, onChange }) => {
        const SelectedIcon = value ? (AntdIcons as any)[value] : null;
        return (
          <Popover
            open={visible}
            onOpenChange={setVisible}
            trigger="click"
            placement="bottomLeft"
            content={
              <div style={{ width: 480 }}>
                <Input
                  placeholder="搜索图标"
                  value={searchValue}
                  onChange={(e) => setSearchValue(e.target.value)}
                  style={{ marginBottom: 12 }}
                  allowClear
                />
                <Tabs
                  size="small"
                  items={filteredCategories.map((category) => ({
                    key: category.key,
                    label: `${category.label} (${category.icons.length})`,
                    children: renderIconGrid(category.icons, onChange),
                  }))}
                />
              </div>
            }
          >
            <Input
              readOnly
              value={value}
              placeholder="点击选择图标"
              prefix={SelectedIcon ? <SelectedIcon /> : null}
              suffix={
                value ? (
                  <AntdIcons.CloseCircleOutlined
                    style={{ cursor: 'pointer', color: '#999' }}
                    onClick={(e) => {
                      e.stopPropagation();
                      onChange('');
                    }}
                  />
                ) : null
              }
              style={{ cursor: 'pointer' }}
            />
          </Popover>
        );
      }}
    </ProFormText>
  );
};

export default IconSelect;
