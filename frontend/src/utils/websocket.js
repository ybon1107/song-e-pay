import { Client } from '@stomp/stompjs';
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const stompClient = ref(null);
const isConnected = ref(false);

export function useWebSocket() {
    const authStore = useAuthStore();
    const connect = (onMessageReceived) => {
        const wsUrl = '/ws';

        stompClient.value = new Client({
            brokerURL: wsUrl,
            connectHeaders: {
                Authorization: `Bearer ${authStore.getToken()}`
            },

            onConnect: () => {
                console.log('WebSocket 연결 성공');
                console.log('현재 사용자 ID:', authStore.userId);
                isConnected.value = true;

                stompClient.value.subscribe(`/queue/notifications/user/${authStore.userId}`, (message) => {
                    const newNotification = JSON.parse(message.body);
                    console.log('---------Received message:', newNotification);
                    onMessageReceived(newNotification);
                });
            },
            onDisconnect: () => {
                console.log('WebSocket 연결 해제');
                isConnected.value = false;
            },

            onError: (error) => {
                console.error('WebSocket 연결 오류:', error);
            },
            
        });

        stompClient.value.activate();
    };

    const disconnect = () => {
        if (stompClient.value) {
            stompClient.value.deactivate();
        }
    };

    return {
        connect,
        disconnect,
        isConnected,
    };
}