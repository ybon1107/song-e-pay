import { Client } from '@stomp/stompjs';
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const stompClient = ref(null);
const isConnected = ref(false);

export function useWebSocket() {
    const authStore = useAuthStore();
    const connect = (onMessageReceived) => {
        const wsUrl = import.meta.env.VITE_WEBSOCKET_URL;

        stompClient.value = new Client({
            brokerURL: wsUrl,
            onConnect: () => {
                console.log('WebSocket 연결 성공');
                isConnected.value = true;
                stompClient.value.subscribe(`/user/${authStore.userId}/topic/notifications`, (message) => {
                    const newNotification = JSON.parse(message.body);
                    onMessageReceived(newNotification);
                });
            },
            onDisconnect: () => {
                console.log('WebSocket 연결 해제');
                isConnected.value = false;
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