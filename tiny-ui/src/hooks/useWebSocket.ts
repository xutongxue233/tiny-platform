import { useCallback, useEffect, useRef, useState } from 'react';
import { useModel } from '@umijs/max';
import { websocketClient, WebSocketMessage, WebSocketMessageHandler } from '@/services/websocket';

/**
 * WebSocket 连接 Hook
 */
export function useWebSocket() {
  const { initialState } = useModel('@@initialState');
  const [connected, setConnected] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const connectingRef = useRef(false);

  // 连接 WebSocket
  const connect = useCallback(async () => {
    if (connectingRef.current || websocketClient.isConnected()) {
      return;
    }

    connectingRef.current = true;
    setError(null);

    try {
      await websocketClient.connect();
      setConnected(true);
    } catch (e: any) {
      setError(e.message || '连接失败');
      setConnected(false);
    } finally {
      connectingRef.current = false;
    }
  }, []);

  // 断开连接
  const disconnect = useCallback(() => {
    websocketClient.disconnect();
    setConnected(false);
  }, []);

  // 订阅用户消息
  const subscribeUserMessage = useCallback((handler: WebSocketMessageHandler) => {
    websocketClient.subscribeUserMessage(handler);
  }, []);

  // 取消订阅
  const unsubscribe = useCallback((destination: string, handler?: WebSocketMessageHandler) => {
    websocketClient.unsubscribe(destination, handler);
  }, []);

  // 用户登录后自动连接，登出后断开
  useEffect(() => {
    if (initialState?.currentUser) {
      connect();
    } else {
      disconnect();
    }

    return () => {
      disconnect();
    };
  }, [initialState?.currentUser, connect, disconnect]);

  return {
    connected,
    error,
    connect,
    disconnect,
    subscribeUserMessage,
    unsubscribe,
  };
}

/**
 * 消息通知 Hook
 * 订阅用户消息并提供回调
 */
export function useMessageNotification(onMessage?: (message: WebSocketMessage) => void) {
  const { connected, subscribeUserMessage, unsubscribe } = useWebSocket();
  const [latestMessage, setLatestMessage] = useState<WebSocketMessage | null>(null);
  const handlerRef = useRef<WebSocketMessageHandler | null>(null);

  useEffect(() => {
    if (!connected) {
      return;
    }

    // 创建消息处理器
    const handler: WebSocketMessageHandler = (message) => {
      setLatestMessage(message);
      onMessage?.(message);
    };

    handlerRef.current = handler;

    // 订阅用户消息
    subscribeUserMessage(handler);

    return () => {
      if (handlerRef.current) {
        unsubscribe('/user/queue/message', handlerRef.current);
      }
    };
  }, [connected, onMessage, subscribeUserMessage, unsubscribe]);

  return {
    connected,
    latestMessage,
  };
}
