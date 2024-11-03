<script setup>
import { ref, computed, watch } from "vue";
import { onMounted } from "vue";
import { useAuthStore } from "@/stores/auth";
import settingApi from "@/api/settingApi";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";

const auth = useAuthStore();
const user = computed(() => auth.user);

// 비밀번호 입력 필드를 ref로 선언
const currentPw = ref("");
const newPw = ref("");
const newPwCheck = ref("");

// 실시간 메시지를 보여줄 ref
const passwordMessage = ref("");
const isPasswordMatch = ref(false); // 비밀번호 일치 여부
const isPasswordValid = ref(false); // 비밀번호 유효성 여부

onMounted(async () => {
  try {
    await auth.fetchUser(auth.userId); // 사용자 정보를 가져오는 메서드 (구현 필요)
  } catch (error) {
    console.error("사용자 정보 로드 실패:", error);
  } finally {
    isUserLoading.value = false;
  }
});

// 비밀번호 비교하는 함수
watch([newPw, newPwCheck], () => {
  // 비밀번호 길이 체크
  isPasswordValid.value = newPw.value.length >= 8;

  if (newPwCheck.value.length === 0) {
    passwordMessage.value = ""; // 비밀번호 확인란이 비어있을 때는 메시지 출력 안 함
  } else if (newPw.value === newPwCheck.value) {
    passwordMessage.value = isPasswordValid.value
      ? "비밀번호가 일치합니다."
      : "비밀번호는 8자리 이상이어야 합니다.";
    isPasswordMatch.value = isPasswordValid.value; // 유효성도 체크
  } else {
    passwordMessage.value = "비밀번호가 일치하지 않습니다.";
    isPasswordMatch.value = false;
  }
});

// 비밀번호 변경 함수
const changePassword = async () => {
  console.log("user: ", user.value);
  const formData = {
    currentPw: currentPw.value,
    newPw: newPw.value,
    userId: user.value.userId,
  };

  try {
    console.log("전송할 formData: ", formData);
    const response = await settingApi.changePassword(formData);
    console.log("Password change success:", response);
    alert("비밀번호가 성공적으로 변경되었습니다.");
  } catch (error) {
    console.error("Password change failed:", error.response.data);
    alert(`${error.response.data}`);
  }
};
</script>

<template>
  <div class="container">
    <div class="custom-spacer"></div>
    <div class="row justify-content-center">
      <div class="mx-auto mx-lg-0 col-lg-7 d-flex flex-column">
        <div class="card">
          <!-- 카드 헤더: 제목 -->
          <div class="pb-0 card-header text-center bg-transparent">
            <h4 class="font-weight-bolder">Change your Song-E Pay Password</h4>
          </div>

          <!-- 카드 본문 -->
          <div class="card-body">
            <form role="form" @submit.prevent="changePassword">
              <!-- 현재 비번 입력 필드 -->
              <label for="current-pw" class="form-label"
                >First, enter your current password</label
              >
              <argon-input
                id="current-pw"
                type="text"
                placeholder="current password"
                aria-label="current-pw"
                v-model="currentPw"
              />

              <!-- 새 비번 입력 필드 -->
              <label for="new-pw" class="form-label"
                >Next, enter your New password</label
              >
              <argon-input
                id="new-pw"
                type="text"
                placeholder="new password"
                aria-label="new-pw"
                v-model="newPw"
              />

              <!-- 새 비번 입력 확인 필드 -->
              <label for="new-pw-check" class="form-label"
                >Again, enter your New password</label
              >
              <argon-input
                id="new-pw-check"
                type="text"
                placeholder="new password check"
                aria-label="new-pw-check"
                v-model="newPwCheck"
              />

              <!-- 비밀번호 일치 여부 메시지 출력 -->
              <div
                class="password-message"
                :style="{ color: isPasswordMatch ? 'green' : 'red' }"
              >
                {{ passwordMessage }}
              </div>

              <!-- 다음 버튼 -->
              <div class="text-center">
                <argon-button
                  :disabled="!isPasswordMatch"
                  fullWidth
                  color="success"
                  variant="gradient"
                  class="my-4 mb-2"
                  >confirm</argon-button
                >
              </div>
            </form>
          </div>
        </div>
      </div>
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
