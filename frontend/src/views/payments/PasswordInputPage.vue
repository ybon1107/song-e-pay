<template>
    <div class="password-input-page">
        <h1 class="title">비밀번호 입력</h1>
        <p class="instruction">6자리 비밀번호를 입력해주세요</p>
        <div class="password-input-container">
            <input v-for="(digit, index) in password" :key="index" :ref="el => inputRefs[index] = el"
                v-model="password[index]" type="tel" maxlength="1" @input="onInput(index)"
                @keydown="onKeyDown($event, index)" class="password-digit">
        </div>
        <button @click="submitPassword" :disabled="!isPasswordComplete" class="btn btn-primary">
            제출
        </button>
    </div>
</template>

<script>
export default {
    data() {
        return {
            password: ['', '', '', '', '', ''],
            inputRefs: [],
        }
    },
    computed: {
        isPasswordComplete() {
            return this.password.every(digit => digit !== '')
        }
    },
    methods: {
        onInput(index) {
            if (this.password[index] && index < 5) {
                this.focusNextInput(index)
            }
        },
        onKeyDown(event, index) {
            if (event.key === 'Backspace' && !this.password[index] && index > 0) {
                this.focusPreviousInput(index)
            }
        },
        focusNextInput(index) {
            if (index < 5) {
                this.inputRefs[index + 1].focus()
            }
        },
        focusPreviousInput(index) {
            if (index > 0) {
                this.inputRefs[index - 1].focus()
            }
        },
        submitPassword() {
            if (this.isPasswordComplete) {
                const password = this.password.join('')
                // TODO: 여기에 비밀번호 제출 로직 추가
                console.log('Submitted password:', password)
            }
        }
    }
}
</script>

<style scoped>
.password-input-page {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    padding: 20px;
    background-color: #f5f5f5;
}

.title {
    font-size: 24px;
    margin-bottom: 10px;
}

.instruction {
    font-size: 16px;
    margin-bottom: 20px;
    color: #666;
}

.password-input-container {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
}

.password-digit {
    width: 40px;
    height: 50px;
    margin: 0 5px;
    text-align: center;
    font-size: 24px;
    border: 1px solid #ccc;
    border-radius: 5px;
    outline: none;
}

.password-digit:focus {
    border-color: #007bff;
}

/*
.submit-button {
    padding: 10px 20px;
    font-size: 18px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

.submit-button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}
*/
</style>