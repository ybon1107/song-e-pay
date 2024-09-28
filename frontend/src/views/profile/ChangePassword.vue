<script setup>
import { ref, computed } from "vue";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";
const currentPw = ref("");
const newPw = ref("");
const newPwCheck = ref("");

const isFormValid = computed(() => {
    // 추후에 비밀번호 형식 잘 지켰는지 확인하는 로직 추가
    return currentPw.value.trim() !== "" &&
        newPw.value.trim() !== "" &&
        newPwCheck.value.trim() !== "";
});
</script>

<template>
    <div class="container">
        <div class="row justify-content-center">
            <div class="mx-auto mx-lg-0 col-lg-7 d-flex flex-column">
                <div class="card">
                    <!-- 카드 헤더: 제목 -->
                    <div class="pb-0 card-header text-center bg-transparent">
                        <h4 class="font-weight-bolder">
                            Change your Song-E Pay Password
                        </h4>
                    </div>

                    <!-- 카드 본문 -->
                    <div class="card-body">
                        <form role="form">
                            <!-- 현재 비번 입력 필드 -->
                            <label for="current-pw" class="form-label">First, enter your current password</label>
                            <argon-input id="current-pw" type="text" placeholder="current password"
                                aria-label="current-pw" v-model="currentPw" />

                            <!-- 새 비번 입력 필드 -->
                            <label for="new-pw" class="form-label">Next, enter your New password</label>
                            <argon-input id="new-pw" type="text" placeholder="new password" aria-label="new-pw"
                                v-model="newPw" />

                            <!-- 새 비번 입력 확인 필드 -->
                            <label for="new-pw-check" class="form-label">Again, enter your New password</label>
                            <argon-input id="new-pw-check" type="text" placeholder="new password check"
                                aria-label="new-pw-check" v-model="newPwCheck" />
                            <!-- 다음 버튼 -->
                            <div class="text-center">
                                <argon-button :disabled="!isFormValid" fullWidth color="success" variant="gradient"
                                    class="my-4 mb-2" @click="handleNext">confirm</argon-button>
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