
<template>
    <div class="password-page page-size">
        <div class="page-size password-container">
            <form class="password-box">
                <label for="password1" class="form-control-label">비밀번호</label>
                <input class="form-control pwd-input" name="password" id="password1" style="margin-bottom: 5px;">
                <label for="password2" class="form-control-label">비밀번호 확인</label>
                <input class="form-control pwd-input" name="password" id="password2">
                <span class="btn btn-pwd">확인</span>
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

    .btn-pwd {
        position: absolute;
        bottom: 0%;
        right: 5%;
    }
</style>