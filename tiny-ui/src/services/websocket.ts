import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { TokenUtil } from './ant-design-pro/api';

export type WebSocketMessageHandler = (message: WebSocketMessage) => void;

export interface WebSocketMessage {
  type: string;
  title?: string;
  content?: string;
  data?: any;
  timestamp?: string;
}

class WebSocketClient {
  private client: Client | null = null;
  private subscriptions: Map<string, StompSubscription> = new Map();
  private messageHandlers: Map<string, Set<WebSocketMessageHandler>> = new Map();
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 5;
  private reconnectDelay = 3000;
  private connected = false;

  /**
   * 连接 WebSocket
   */
  connect(): Promise<void> {
    return new Promise((resolve, reject) => {
      const token = TokenUtil.getToken();
      if (!token) {
        reject(new Error('未登录，无法连接WebSocket'));
        return;
      }

      if (this.client && this.connected) {
        resolve();
        return;
      }

      const wsUrl = `${window.location.protocol === 'https:' ? 'https:' : 'http:'}//${window.location.host}/api/ws`;

      this.client = new Client({
        webSocketFactory: () => new SockJS(wsUrl, null, {
          transports: ['xhr-streaming', 'xhr-polling'],
        }),
        connectHeaders: {
          Authorization: `Bearer ${token}`,
        },
        debug: () => {},
        reconnectDelay: this.reconnectDelay,
        heartbeatIncoming: 30000,
        heartbeatOutgoing: 30000,
        onConnect: () => {
          this.connected = true;
          this.reconnectAttempts = 0;
          resolve();
        },
        onDisconnect: () => {
          this.connected = false;
        },
        onStompError: (frame) => {
          console.error('[WebSocket] STOMP错误:', frame.headers['message']);
          this.connected = false;
          reject(new Error(frame.headers['message']));
        },
        onWebSocketClose: () => {
          this.connected = false;
        },
      });

      this.client.activate();
    });
  }

  /**
   * 断开连接
   */
  disconnect(): void {
    if (this.client) {
      this.subscriptions.forEach((sub) => sub.unsubscribe());
      this.subscriptions.clear();
      this.messageHandlers.clear();

      this.client.deactivate();
      this.client = null;
      this.connected = false;
    }
  }

  /**
   * 订阅用户消息
   */
  subscribeUserMessage(handler: WebSocketMessageHandler): void {
    this.subscribe('/user/queue/message', handler);
  }

  /**
   * 订阅广播消息
   */
  subscribeBroadcast(handler: WebSocketMessageHandler): void {
    this.subscribe('/topic/broadcast', handler);
  }

  /**
   * 订阅指定目的地
   */
  subscribe(destination: string, handler: WebSocketMessageHandler): void {
    if (!this.client || !this.connected) {
      return;
    }

    if (!this.messageHandlers.has(destination)) {
      this.messageHandlers.set(destination, new Set());
    }
    this.messageHandlers.get(destination)!.add(handler);

    if (this.subscriptions.has(destination)) {
      return;
    }

    const subscription = this.client.subscribe(destination, (message: IMessage) => {
      try {
        const body = JSON.parse(message.body) as WebSocketMessage;
        const handlers = this.messageHandlers.get(destination);
        if (handlers) {
          handlers.forEach((h) => h(body));
        }
      } catch (e) {
        console.error('[WebSocket] 解析消息失败:', e);
      }
    });

    this.subscriptions.set(destination, subscription);
  }

  /**
   * 取消订阅
   */
  unsubscribe(destination: string, handler?: WebSocketMessageHandler): void {
    if (handler) {
      const handlers = this.messageHandlers.get(destination);
      if (handlers) {
        handlers.delete(handler);
        if (handlers.size === 0) {
          const subscription = this.subscriptions.get(destination);
          if (subscription) {
            subscription.unsubscribe();
            this.subscriptions.delete(destination);
          }
          this.messageHandlers.delete(destination);
        }
      }
    } else {
      const subscription = this.subscriptions.get(destination);
      if (subscription) {
        subscription.unsubscribe();
        this.subscriptions.delete(destination);
      }
      this.messageHandlers.delete(destination);
    }
  }

  /**
   * 发送消息
   */
  send(destination: string, body: any): void {
    if (!this.client || !this.connected) {
      return;
    }

    this.client.publish({
      destination,
      body: JSON.stringify(body),
    });
  }

  /**
   * 检查是否已连接
   */
  isConnected(): boolean {
    return this.connected;
  }
}

export const websocketClient = new WebSocketClient();
