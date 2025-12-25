import { useCallback, useEffect, useState } from 'react';
import { getPublicConfig } from '@/services/ant-design-pro/api';

let cachedConfig: API.PublicConfig | null = null;

export function usePublicConfig() {
  const [config, setConfig] = useState<API.PublicConfig>(cachedConfig || {});
  const [loading, setLoading] = useState(!cachedConfig);

  const fetchConfig = useCallback(async () => {
    if (cachedConfig) {
      setConfig(cachedConfig);
      setLoading(false);
      return;
    }
    setLoading(true);
    try {
      const res = await getPublicConfig();
      if (res.code === 200 && res.data) {
        cachedConfig = res.data;
        setConfig(res.data);
      }
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchConfig();
  }, [fetchConfig]);

  const reload = useCallback(async () => {
    cachedConfig = null;
    setLoading(true);
    try {
      const res = await getPublicConfig();
      if (res.code === 200 && res.data) {
        cachedConfig = res.data;
        setConfig(res.data);
      }
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    appName: config.appName || 'Tiny Platform',
    appVersion: config.appVersion || '1.0.0',
    captchaEnabled: config.captchaEnabled ?? true,
    registerEnabled: config.registerEnabled ?? false,
    loading,
    reload,
  };
}

export function clearPublicConfigCache() {
  cachedConfig = null;
}
