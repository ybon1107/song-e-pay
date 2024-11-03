<template>
    <div>
        <div v-if="showModal" class="modal-overlay" @click="handleOverlayClick">
            <div class="modal-content" @click.stop>
                <div class="modal-body">
                    <h2>
                        {{
                            step === 0
                                ? '새 결제 비밀번호 등록'
                                : step === 1
                                  ? '기존 비밀번호 확인'
                                  : step === 2
                                    ? '새 비밀번호 입력'
                                    : '새 비밀번호 확인'
                        }}
                    </h2>

                    <!-- 비밀번호 도트 -->
                    <div class="password-dots">
                        <span
                            v-for="(digit, index) in 6"
                            :key="index"
                            :class="{
                                dot: true,
                                filled: currentPassword.length >= index + 1,
                            }"
                        ></span>
                    </div>

                    <!-- 숫자 키패드 -->
                    <div class="keypad">
                        <button
                            v-for="num in 9"
                            :key="num"
                            @click="enterDigit(num)"
                        >
                            {{ num }}
                        </button>
                        <button @click="clearAll">C</button>
                        <button @click="enterDigit(0)">0</button>
                        <button @click="clearLast">
                            <i class="fas fa-backspace"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';
import settingApi from '@/api/settingApi';
import { useAuthStore } from '@/stores/auth';
// 이벤트 설정
const props = defineProps(['secondPwd']); // 사용하고자 하는 prop을 정의합니다.
const emit = defineEmits(['password-verified', 'close']);
const auth = useAuthStore();

const showModal = ref(true);
const currentPassword = ref('');
const step = ref(props.secondPwd ? 1 : 0);
const newPassword = ref('');
const confirmPassword = ref('');

const verifyPassword = async () => {
    if (!props.secondPwd) {
        // 새 비밀번호 등록
        if (step.value === 0) {
            newPassword.value = currentPassword.value;
            currentPassword.value = '';
            step.value = 3;
        } else if (step.value === 3) {
            if (newPassword.value === currentPassword.value) {
                try {
                    await settingApi.set2ndPassword(
                        newPassword.value,
                        auth.userId
                    );
                    emit('password-verified', newPassword.value);
                    closeModal();
                } catch (error) {
                    console.error('2차 비밀번호 설정 실패:', error);
                    alert(
                        '2차 비밀번호 설정에 실패했습니다. 다시 시도해주세요.'
                    );
                }
            } else {
                alert('비밀번호가 일치하지 않습니다. 다시 입력해주세요.');
                resetInputs();
                step.value = 0;
            }
        }
    } else {
        // 기존 비밀번호 확인 후 변경
        if (step.value === 1) {
            try {
                // 여기에 기존 비밀번호 확인 로직 추가
                const isValid = await settingApi.verify2ndPassword(
                    currentPassword.value,
                    auth.userId
                );
                if (isValid) {
                    currentPassword.value = '';
                    step.value = 2;
                } else {
                    alert(
                        '기존 비밀번호가 일치하지 않습니다. 다시 입력해주세요.'
                    );
                    currentPassword.value = '';
                }
            } catch (error) {
                console.error('비밀번호 확인 실패:', error);
                alert('비밀번호 확인에 실패했습니다. 다시 시도해주세요.');
            }
        } else if (step.value === 2) {
            newPassword.value = currentPassword.value;
            currentPassword.value = '';
            step.value = 3;
        } else if (step.value === 3) {
            if (newPassword.value === currentPassword.value) {
                try {
                    await settingApi.set2ndPassword(
                        newPassword.value,
                        auth.userId
                    );
                    emit('password-verified', newPassword.value);
                    closeModal();
                } catch (error) {
                    console.error('2차 비밀번호 변경 실패:', error);
                    alert(
                        '2차 비밀번호 변경에 실패했습니다. 다시 시도해주세요.'
                    );
                }
            } else {
                alert('비밀번호가 일치하지 않습니다. 다시 입력해주세요.');
                resetInputs();
                step.value = 2;
            }
        }
    }
};

const closeModal = () => {
    showModal.value = false;
    emit('close');
};

const handleOverlayClick = () => {
    closeModal();
};

// 숫자 입력 처리
const enterDigit = (num) => {
    if (currentPassword.value.length < 6) {
        currentPassword.value += num;

        // 비밀번호 길이가 6자리일 때 자동으로 확인
        if (currentPassword.value.length === 6) {
            if (step.value === 0) {
                newPassword.value = currentPassword.value;
                currentPassword.value = '';
                step.value = 3;
            } else if (step.value === 1) {
                verifyPassword();
            } else if (step.value === 2) {
                newPassword.value = currentPassword.value;
                currentPassword.value = '';
                step.value = 3;
            } else {
                confirmPassword.value = currentPassword.value;
                verifyPassword();
            }
        }
    }
};

// 마지막 입력 지우기
const clearLast = () => {
    if (currentPassword.value.length > 0) {
        currentPassword.value = currentPassword.value.slice(0, -1);
    }
};

// 전체 입력 지우기
const clearAll = () => {
    currentPassword.value = '';
};

// resetInputs 함수 정의
const resetInputs = () => {
    currentPassword.value = '';
    newPassword.value = '';
    confirmPassword.value = '';
};
</script>

<style scope>
/* 모달 오버레이 */
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

/* 모달 컨텐츠 */
.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 100px; /* 기본 크기 */
    max-width: 30%; /* 화면에 맞게 줄어들도록 설정 */
    text-align: center;
    z-index: 1001;
}

/* 모달 헤더 */
.modal-header h3 {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 10px;
}

/* 비밀번호 도트 */
.password-dots {
    display: flex;
    justify-content: space-between;
    margin: 20px 0;
}

.dot {
    width: 20px;
    height: 20px;
    border: 2px solid #333;
    border-radius: 50%;
    background-color: transparent;
}

.dot.filled {
    background-color: black;
}

/* 키패드 스타일 */
.keypad {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 10px;
    margin: 20px 0;
    justify-items: center;
    align-items: center;
}

.keypad button {
    width: 60px;
    height: 60px;
    font-size: 24px;
    background-color: #f0f0f0;
    border: none;
    border-radius: 8px;
    cursor: pointer;
}

.keypad button:hover {
    background-color: #ddd;
}

/* 반응형 스타일 */
@media (max-width: 768px) {
    .modal-content {
        width: 90%; /* 작은 화면에서 너비를 90%로 설정 */
    }

    .keypad button {
        width: 50px; /* 버튼 크기 조정 */
        height: 50px; /* 버튼 크기 조정 */
        font-size: 20px; /* 폰트 크기 조정 */
    }
}

@media (max-width: 480px) {
    .modal-content {
        width: 95%; /* 더욱 작은 화면에서 너비를 95%로 설정 */
    }

    .modal-header h3 {
        font-size: 20px; /* 제목 크기 조정 */
    }

    .keypad button {
        width: 40px; /* 버튼 크기 조정 */
        height: 40px; /* 버튼 크기 조정 */
        font-size: 18px; /* 폰트 크기 조정 */
    }
}
</style>
