<script setup>
import { ref, computed, watch } from "vue";
import { useAuthStore } from '@/stores/auth';
import settingApi from '@/api/settingApi';

const auth = useAuthStore();
const user = computed(() => auth.user);

// 비밀번호 입력 필드를 ref로 선언
const password = ref('');
const password2 = ref('');

// 실시간 메시지를 보여줄 ref
const passwordMessage = ref('');
const isPasswordMatch = ref(false); // 비밀번호 일치 여부
const isPasswordValid = ref(false); // 비밀번호 유효성 여부

// 비밀번호 비교하는 함수
watch([password, password2], () => {
  // 비밀번호 길이 체크
  isPasswordValid.value = password.value.length >= 8;

  if (password2.value.length === 0) {
    passwordMessage.value = ''; // 비밀번호 확인란이 비어있을 때는 메시지 출력 안 함
  } else if (password.value === password2.value) {
    passwordMessage.value = isPasswordValid.value ? '비밀번호가 일치합니다.' : '비밀번호는 8자리 이상이어야 합니다.';
    isPasswordMatch.value = isPasswordValid.value; // 유효성도 체크
  } else {
    passwordMessage.value = '비밀번호가 일치하지 않습니다.';
    isPasswordMatch.value = false;
  }
});

// 비밀번호 변경 함수
const changePassword = async () => {

    // 전송할 formData 구성
    const formData = {
        password: password.value,
        userNo: user.value.userNo, // 유저 번호 가져오기
    };

    try {
        const response = await settingApi.changePassword(formData);
        console.log('Password change success:', response);
        alert('비밀번호가 성공적으로 변경되었습니다.');
    } catch (error) {
        console.error('Password change failed:', error);
        alert('비밀번호 변경에 실패했습니다.');
    }
};
</script>

<template>
    <div class="password-page page-size">
        <div class="page-size password-container">
            <form class="password-box" @submit.prevent="changePassword">
                <label for="password" class="form-control-label">비밀번호</label>
                <input class="form-control pwd-input" type="password" v-model="password" name="password" id="password" style="margin-bottom: 5px;">

                <label for="password2" class="form-control-label">비밀번호 확인</label>
                <input class="form-control pwd-input" type="password" v-model="password2" name="password" id="password2">
                
                <!-- 비밀번호 일치 여부 메시지 출력 -->
                <div class="password-message" :style="{ color: isPasswordMatch ? 'green' : 'red' }">
                    {{ passwordMessage }}
                </div>
                <a href="/profile"><span type="submit" class="btn btn-back" >뒤로</span></a>
                <button type="submit" class="btn btn-pwd" :disabled="!isPasswordMatch">확인</button> 
            </form>
        </div>
    </div>
</template>
<style>
.password-page {
    background-color: white;
    height: 650px;
    border-radius: 1rem;
    padding: 1rem;
}

.password-container {
    margin-top: 5%;
}

.password-box {
    border: 1px solid black;
    border-radius: 1rem;
    width: 70%;
    margin: 0 auto;
    height: 400px;
    padding: 1rem;
    position: relative;
}

.btn-back {
    position: absolute;
    bottom: 0%;
    left: 5%;
}

.btn-pwd {
    position: absolute;
    bottom: 0%;
    right: 5%;
}

.password-message {
  margin-top: 5px;
  font-size: 12px;
}
</style>