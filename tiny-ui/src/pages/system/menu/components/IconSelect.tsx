import { ProForm } from '@ant-design/pro-components';
import { Input, Popover, Tabs, Empty, Tooltip, Spin } from 'antd';
import { CloseCircleFilled, SearchOutlined } from '@ant-design/icons';
import { Icon } from '@iconify/react';
import type { FC } from 'react';
import { useState, useMemo, useCallback, useEffect, useRef } from 'react';
import styles from './IconSelect.module.less';

interface IconSelectProps {
  name: string;
  label: string;
  colProps?: { span?: number; [key: string]: unknown };
}

interface IconSelectInputProps {
  value?: string;
  onChange?: (value: string) => void;
}

// 图标集配置
interface IconSet {
  key: string;
  label: string;
  prefix: string;
  icons: string[];
}

// 预定义的常用图标集
const iconSets: IconSet[] = [
  {
    key: 'ant-design',
    label: 'Ant Design',
    prefix: 'ant-design',
    icons: [
      'home-outlined', 'setting-outlined', 'appstore-outlined', 'user-outlined', 'team-outlined',
      'lock-outlined', 'unlock-outlined', 'key-outlined', 'menu-outlined', 'bell-outlined',
      'search-outlined', 'star-outlined', 'heart-outlined', 'mail-outlined', 'message-outlined',
      'phone-outlined', 'calendar-outlined', 'camera-outlined', 'cloud-outlined', 'download-outlined',
      'upload-outlined', 'file-outlined', 'folder-outlined', 'database-outlined', 'table-outlined',
      'profile-outlined', 'schedule-outlined', 'shopping-outlined', 'shopping-cart-outlined',
      'wallet-outlined', 'bank-outlined', 'credit-card-outlined', 'dollar-outlined',
      'safety-outlined', 'safety-certificate-outlined', 'security-scan-outlined',
      'tool-outlined', 'control-outlined', 'dashboard-outlined', 'code-outlined', 'api-outlined',
      'bug-outlined', 'build-outlined', 'experiment-outlined', 'rocket-outlined',
      'fire-outlined', 'thunderbolt-outlined', 'bulb-outlined', 'flag-outlined', 'crown-outlined',
      'trophy-outlined', 'gift-outlined', 'smile-outlined', 'meh-outlined', 'frown-outlined',
      'like-outlined', 'dislike-outlined', 'eye-outlined', 'eye-invisible-outlined',
      'compass-outlined', 'global-outlined', 'environment-outlined', 'link-outlined',
      'filter-outlined', 'sync-outlined', 'reload-outlined', 'poweroff-outlined',
      'desktop-outlined', 'laptop-outlined', 'mobile-outlined', 'printer-outlined',
      'sound-outlined', 'audio-outlined', 'video-camera-outlined', 'picture-outlined',
      'book-outlined', 'read-outlined', 'history-outlined', 'project-outlined', 'apartment-outlined',
      'branches-outlined', 'partition-outlined', 'cluster-outlined', 'deployment-unit-outlined',
      'pushpin-outlined', 'tag-outlined', 'tags-outlined', 'qrcode-outlined', 'barcode-outlined',
      'scan-outlined', 'wifi-outlined', 'usb-outlined', 'robot-outlined', 'alert-outlined',
      'audit-outlined', 'idcard-outlined', 'contacts-outlined', 'solution-outlined',
      'edit-outlined', 'form-outlined', 'copy-outlined', 'scissor-outlined', 'delete-outlined',
      'plus-outlined', 'minus-outlined', 'check-outlined', 'close-outlined', 'info-outlined',
      'question-outlined', 'exclamation-outlined', 'warning-outlined', 'stop-outlined',
      'left-outlined', 'right-outlined', 'up-outlined', 'down-outlined',
      'fullscreen-outlined', 'fullscreen-exit-outlined', 'login-outlined', 'logout-outlined',
      'area-chart-outlined', 'pie-chart-outlined', 'bar-chart-outlined', 'line-chart-outlined',
      'fund-outlined', 'stock-outlined', 'rise-outlined', 'fall-outlined',
      'github-outlined', 'gitlab-outlined', 'wechat-outlined', 'alipay-outlined',
      'taobao-outlined', 'weibo-outlined', 'qq-outlined', 'dingding-outlined',
      'twitter-outlined', 'facebook-outlined', 'google-outlined', 'linkedin-outlined',
      'youtube-outlined', 'instagram-outlined', 'slack-outlined', 'amazon-outlined',
    ],
  },
  {
    key: 'mdi',
    label: 'Material Design',
    prefix: 'mdi',
    icons: [
      'home', 'account', 'cog', 'bell', 'email', 'phone', 'calendar', 'clock', 'map-marker',
      'magnify', 'plus', 'minus', 'check', 'close', 'pencil', 'delete', 'content-copy',
      'folder', 'file', 'file-document', 'image', 'video', 'music', 'download', 'upload',
      'cloud', 'cloud-upload', 'cloud-download', 'database', 'server', 'lan', 'wifi',
      'lock', 'lock-open', 'key', 'shield', 'shield-check', 'security', 'fingerprint',
      'chart-bar', 'chart-line', 'chart-pie', 'chart-areaspline', 'trending-up', 'trending-down',
      'cart', 'store', 'cash', 'credit-card', 'wallet', 'bank', 'currency-usd',
      'heart', 'star', 'thumb-up', 'thumb-down', 'bookmark', 'flag', 'tag',
      'message', 'comment', 'forum', 'chat', 'send', 'share', 'link',
      'eye', 'eye-off', 'filter', 'sort', 'refresh', 'sync', 'cached',
      'menu', 'dots-vertical', 'dots-horizontal', 'apps', 'view-grid', 'view-list',
      'arrow-left', 'arrow-right', 'arrow-up', 'arrow-down', 'chevron-left', 'chevron-right',
      'play', 'pause', 'stop', 'skip-next', 'skip-previous', 'volume-high', 'volume-off',
      'printer', 'qrcode', 'barcode', 'scan-helper', 'nfc', 'bluetooth', 'usb',
      'monitor', 'laptop', 'cellphone', 'tablet', 'television', 'gamepad-variant',
      'lightbulb', 'flash', 'fire', 'water', 'leaf', 'tree', 'flower',
      'car', 'bus', 'train', 'airplane', 'ship', 'bike', 'walk',
      'food', 'coffee', 'glass-cocktail', 'pizza', 'cake', 'cookie',
      'hospital', 'medical-bag', 'pill', 'needle', 'stethoscope',
      'school', 'book', 'book-open', 'notebook', 'pencil-box', 'ruler',
      'hammer', 'wrench', 'screwdriver', 'saw', 'axe', 'shovel',
      'github', 'gitlab', 'bitbucket', 'docker', 'kubernetes', 'aws',
      'google', 'microsoft', 'apple', 'linux', 'android', 'react',
      'vuejs', 'angular', 'nodejs', 'npm', 'language-python', 'language-java',
    ],
  },
  {
    key: 'tabler',
    label: 'Tabler',
    prefix: 'tabler',
    icons: [
      'home', 'user', 'users', 'settings', 'bell', 'mail', 'phone', 'calendar', 'clock',
      'search', 'plus', 'minus', 'check', 'x', 'edit', 'trash', 'copy', 'clipboard',
      'folder', 'file', 'file-text', 'photo', 'video', 'music', 'download', 'upload',
      'cloud', 'cloud-upload', 'cloud-download', 'database', 'server', 'wifi',
      'lock', 'lock-open', 'key', 'shield', 'shield-check', 'fingerprint',
      'chart-bar', 'chart-line', 'chart-pie', 'chart-area', 'trending-up', 'trending-down',
      'shopping-cart', 'building-store', 'cash', 'credit-card', 'wallet', 'building-bank',
      'heart', 'star', 'thumb-up', 'thumb-down', 'bookmark', 'flag', 'tag',
      'message', 'message-circle', 'messages', 'send', 'share', 'link',
      'eye', 'eye-off', 'filter', 'sort-ascending', 'refresh', 'rotate',
      'menu', 'menu-2', 'dots', 'dots-vertical', 'apps', 'grid-dots', 'list',
      'arrow-left', 'arrow-right', 'arrow-up', 'arrow-down', 'chevron-left', 'chevron-right',
      'player-play', 'player-pause', 'player-stop', 'player-skip-forward', 'volume', 'volume-off',
      'printer', 'qrcode', 'scan', 'bluetooth', 'usb',
      'device-desktop', 'device-laptop', 'device-mobile', 'device-tablet', 'device-tv',
      'bulb', 'bolt', 'flame', 'droplet', 'leaf', 'tree', 'flower',
      'car', 'bus', 'plane', 'ship', 'bike', 'walk',
      'coffee', 'glass', 'pizza', 'cake', 'cookie',
      'first-aid-kit', 'pill', 'stethoscope', 'vaccine',
      'school', 'book', 'book-2', 'notebook', 'pencil', 'ruler',
      'hammer', 'tool', 'settings-2', 'adjustments',
      'brand-github', 'brand-gitlab', 'brand-docker', 'brand-aws',
      'brand-google', 'brand-apple', 'brand-android', 'brand-react',
      'brand-vue', 'brand-angular', 'brand-nodejs', 'brand-npm', 'brand-python', 'brand-java',
      'code', 'terminal', 'bug', 'git-branch', 'git-merge', 'git-pull-request',
      'api', 'webhook', 'package', 'puzzle', 'components', 'layout',
      'dashboard', 'report', 'chart-infographic', 'presentation',
      'world', 'map', 'map-pin', 'compass', 'navigation',
      'alarm', 'hourglass', 'stopwatch', 'timeline',
      'mood-happy', 'mood-sad', 'mood-neutral', 'mood-smile',
    ],
  },
  {
    key: 'lucide',
    label: 'Lucide',
    prefix: 'lucide',
    icons: [
      'home', 'user', 'users', 'settings', 'bell', 'mail', 'phone', 'calendar', 'clock',
      'search', 'plus', 'minus', 'check', 'x', 'edit', 'trash', 'copy', 'clipboard',
      'folder', 'file', 'file-text', 'image', 'video', 'music', 'download', 'upload',
      'cloud', 'cloud-upload', 'cloud-download', 'database', 'server', 'wifi',
      'lock', 'unlock', 'key', 'shield', 'shield-check', 'fingerprint',
      'bar-chart', 'line-chart', 'pie-chart', 'area-chart', 'trending-up', 'trending-down',
      'shopping-cart', 'store', 'banknote', 'credit-card', 'wallet', 'landmark',
      'heart', 'star', 'thumbs-up', 'thumbs-down', 'bookmark', 'flag', 'tag',
      'message-square', 'message-circle', 'messages-square', 'send', 'share', 'link',
      'eye', 'eye-off', 'filter', 'arrow-up-down', 'refresh-cw', 'rotate-cw',
      'menu', 'more-horizontal', 'more-vertical', 'grid', 'list',
      'arrow-left', 'arrow-right', 'arrow-up', 'arrow-down', 'chevron-left', 'chevron-right',
      'play', 'pause', 'square', 'skip-forward', 'volume-2', 'volume-x',
      'printer', 'qr-code', 'scan', 'bluetooth', 'usb',
      'monitor', 'laptop', 'smartphone', 'tablet', 'tv',
      'lightbulb', 'zap', 'flame', 'droplet', 'leaf', 'tree-deciduous', 'flower',
      'car', 'bus', 'plane', 'ship', 'bike', 'footprints',
      'coffee', 'wine', 'pizza', 'cake', 'cookie',
      'activity', 'pill', 'stethoscope', 'syringe',
      'graduation-cap', 'book', 'book-open', 'notebook', 'pencil', 'ruler',
      'hammer', 'wrench', 'settings-2', 'sliders',
      'github', 'gitlab', 'chrome', 'figma',
      'code', 'terminal', 'bug', 'git-branch', 'git-merge', 'git-pull-request',
      'box', 'package', 'puzzle', 'component', 'layout',
      'layout-dashboard', 'file-bar-chart', 'presentation',
      'globe', 'map', 'map-pin', 'compass', 'navigation',
      'alarm-clock', 'hourglass', 'timer', 'calendar-days',
      'smile', 'frown', 'meh', 'laugh',
      'sun', 'moon', 'cloud-sun', 'cloud-rain', 'snowflake',
      'battery', 'battery-charging', 'plug', 'power',
      'camera', 'mic', 'headphones', 'speaker', 'radio',
      'layers', 'square-stack', 'boxes', 'archive',
    ],
  },
  {
    key: 'carbon',
    label: 'Carbon',
    prefix: 'carbon',
    icons: [
      'home', 'user', 'user-multiple', 'settings', 'notification', 'email', 'phone', 'calendar', 'time',
      'search', 'add', 'subtract', 'checkmark', 'close', 'edit', 'trash-can', 'copy', 'paste',
      'folder', 'document', 'document-blank', 'image', 'video', 'music', 'download', 'upload',
      'cloud', 'cloud-upload', 'cloud-download', 'data-base', 'server-dns', 'wifi',
      'locked', 'unlocked', 'password', 'security', 'fingerprint-recognition',
      'chart-bar', 'chart-line', 'chart-pie', 'chart-area', 'growth', 'decline',
      'shopping-cart', 'store', 'currency-dollar', 'purchase', 'wallet', 'bank',
      'favorite', 'star', 'thumbs-up', 'thumbs-down', 'bookmark', 'flag', 'tag',
      'chat', 'forum', 'send', 'share', 'link',
      'view', 'view-off', 'filter', 'arrows-vertical', 'renew', 'rotate',
      'menu', 'overflow-menu-horizontal', 'overflow-menu-vertical', 'grid', 'list',
      'arrow-left', 'arrow-right', 'arrow-up', 'arrow-down', 'chevron-left', 'chevron-right',
      'play', 'pause', 'stop', 'skip-forward', 'volume-up', 'volume-mute',
      'printer', 'qr-code', 'scan', 'bluetooth', 'usb',
      'desktop', 'laptop', 'mobile', 'tablet', 'screen',
      'light', 'flash', 'fire', 'drop', 'tree', 'sprout',
      'car', 'bus', 'airplane', 'sailboat-offshore', 'bicycle', 'pedestrian',
      'cafe', 'drink-01', 'restaurant', 'cake',
      'hospital', 'medication', 'stethoscope',
      'education', 'book', 'notebook', 'pen', 'ruler',
      'tools', 'tool-kit', 'settings-adjust',
      'logo-github', 'logo-gitlab', 'logo-docker',
      'code', 'terminal', 'debug', 'branch', 'git-commit',
      'api', 'webhook', 'package', 'puzzle', 'application',
      'dashboard', 'report', 'presentation-file',
      'earth', 'map', 'location', 'compass', 'navigation',
      'alarm', 'hourglass', 'timer', 'calendar-heat-map',
      'face-satisfied', 'face-dissatisfied', 'face-neutral', 'face-wink',
      'sun', 'moon', 'partly-cloudy', 'rain', 'snow',
      'battery-full', 'battery-charging', 'plug', 'power',
      'camera', 'microphone', 'headphones', 'speaker', 'radio',
      'layers', 'stack', 'box', 'archive',
    ],
  },
];

// 获取完整图标名称
const getFullIconName = (prefix: string, icon: string) => `${prefix}:${icon}`;

// 获取所有图标（用于搜索）
const getAllIcons = () => {
  const result: { name: string; prefix: string; icon: string }[] = [];
  iconSets.forEach((set) => {
    set.icons.forEach((icon) => {
      result.push({
        name: getFullIconName(set.prefix, icon),
        prefix: set.prefix,
        icon,
      });
    });
  });
  return result;
};

const allIcons = getAllIcons();

const IconSelectInput: FC<IconSelectInputProps> = ({ value, onChange }) => {
  const [open, setOpen] = useState(false);
  const [searchValue, setSearchValue] = useState('');
  const [activeTab, setActiveTab] = useState('ant-design');
  const [loading, setLoading] = useState(false);
  const searchInputRef = useRef<HTMLInputElement>(null);

  // 打开时聚焦搜索框
  useEffect(() => {
    if (open) {
      setTimeout(() => {
        searchInputRef.current?.focus();
      }, 100);
    }
  }, [open]);

  // 搜索过滤图标
  const filteredIcons = useMemo(() => {
    if (!searchValue.trim()) return null;
    const keyword = searchValue.toLowerCase();
    return allIcons.filter((item) => item.icon.toLowerCase().includes(keyword));
  }, [searchValue]);

  // 选择图标
  const handleSelect = useCallback(
    (iconName: string) => {
      onChange?.(iconName);
      setOpen(false);
      setSearchValue('');
    },
    [onChange],
  );

  // 清除选择
  const handleClear = useCallback(
    (e: React.MouseEvent) => {
      e.stopPropagation();
      e.preventDefault();
      onChange?.('');
    },
    [onChange],
  );

  // 渲染单个图标
  const renderIconItem = useCallback(
    (iconName: string, displayName: string) => {
      const isSelected = value === iconName;
      return (
        <Tooltip key={iconName} title={displayName} mouseEnterDelay={0.3}>
          <div
            className={`${styles.iconItem} ${isSelected ? styles.iconItemSelected : ''}`}
            onClick={() => handleSelect(iconName)}
          >
            <Icon icon={iconName} />
          </div>
        </Tooltip>
      );
    },
    [value, handleSelect],
  );

  // 渲染图标网格
  const renderIconGrid = useCallback(
    (icons: { name: string; icon: string }[]) => {
      if (icons.length === 0) {
        return <Empty description="未找到图标" image={Empty.PRESENTED_IMAGE_SIMPLE} />;
      }

      return (
        <div className={styles.iconGrid}>
          {icons.map((item) => renderIconItem(item.name, item.icon))}
        </div>
      );
    },
    [renderIconItem],
  );

  // 渲染图标集内容
  const renderIconSetContent = useCallback(
    (iconSet: IconSet) => {
      const icons = iconSet.icons.map((icon) => ({
        name: getFullIconName(iconSet.prefix, icon),
        icon,
      }));
      return renderIconGrid(icons);
    },
    [renderIconGrid],
  );

  // 标签页配置
  const tabItems = useMemo(
    () =>
      iconSets.map((iconSet) => ({
        key: iconSet.key,
        label: iconSet.label,
        children: renderIconSetContent(iconSet),
      })),
    [renderIconSetContent],
  );

  // 弹出框内容
  const popoverContent = (
    <div className={styles.iconSelectPopover}>
      <div className={styles.searchWrapper}>
        <Input
          ref={searchInputRef as any}
          placeholder="搜索图标名称..."
          prefix={<SearchOutlined style={{ color: '#bfbfbf' }} />}
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
          allowClear
        />
      </div>
      <Spin spinning={loading}>
        <div className={styles.iconContent}>
          {filteredIcons ? (
            <div className={styles.searchResult}>
              <div className={styles.searchResultTitle}>
                搜索结果 ({filteredIcons.length})
              </div>
              {renderIconGrid(filteredIcons.map((item) => ({ name: item.name, icon: item.icon })))}
            </div>
          ) : (
            <Tabs
              size="small"
              activeKey={activeTab}
              onChange={setActiveTab}
              items={tabItems}
              tabBarStyle={{ marginBottom: 8 }}
            />
          )}
        </div>
      </Spin>
    </div>
  );

  // 渲染已选中的图标
  const renderSelectedIcon = () => {
    if (!value) return null;
    return (
      <span className={styles.selectedIcon}>
        <Icon icon={value} />
      </span>
    );
  };

  return (
    <Popover
      content={popoverContent}
      trigger="click"
      open={open}
      onOpenChange={(visible) => {
        setOpen(visible);
        if (!visible) {
          setSearchValue('');
        }
      }}
      placement="bottomLeft"
      arrow={false}
      overlayClassName={styles.iconSelectOverlay}
    >
      <div className={styles.iconSelectTrigger}>
        <Input
          readOnly
          value={value}
          placeholder="请选择图标"
          prefix={renderSelectedIcon()}
          suffix={
            value ? (
              <CloseCircleFilled className={styles.clearIcon} onClick={handleClear} />
            ) : null
          }
        />
      </div>
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
