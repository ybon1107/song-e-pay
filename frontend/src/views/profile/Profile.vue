<script setup>
import { computed, ref, reactive } from "vue";
import { onBeforeMount, onMounted, onBeforeUnmount } from "vue";
import { useStore } from "vuex";

import setNavPills from "@/assets/js/nav-pills.js";
import setTooltip from "@/assets/js/tooltip.js";
// import ProfileCard from "@/components/viewsComponents/ProfileCard.vue";
// import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";

import settingApi from '@/api/settingApi';
import { useAuthStore } from '@/stores/auth';

const auth = useAuthStore();
const isLogin = computed(() => auth.isLogin);
const userNo = computed(() => auth.userNo);
const user = computed(() => auth.user);

console.log("profile isLogin : ", isLogin)
console.log("profile userNo : ", userNo)
console.log("profile user : ", user)

const profilePic = ref(null);

const userInfo = reactive({
  userNo: auth.user.userNo,
  address: auth.user.address,
  postCode: auth.user.postCode,
  countryCode: auth.user.countryCode,
  profilePic: null,
});

const originalProfilePic = ref(auth.user.profilePic);

const triggerFileInput = () => {
  if (profilePic.value) {
    profilePic.value.value = ''; // 파일 입력 필드 초기화
  }
  profilePic.value.click();
};

const cancelProfilePic = () => {
  user.value.profilePic = originalProfilePic.value
  userInfo.profilePic = null; // 선택된 파일 제거
  if (profilePic.value) {
    profilePic.value.value = ''; // 파일 입력 필드 초기화
  }
};

const handleFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    userInfo.profilePic = file;
    console.log('선택된 파일:', file.name);

    // 파일 미리보기 생성
    const reader = new FileReader();
    reader.onload = (e) => {
      user.value.profilePic = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const updateProfile = async () => {
  try {
    await settingApi.updateProfile(userInfo);
    console.log('프로필 업데이트 성공');
    alert('프로필 업데이트 성공');
  } catch (error) {
    console.error('프로필 업데이트 실패:', error.response.data);
    alert('프로필 업데이트 실패');
  }
};

const logout = (e) => {
  // 로그아웃
  auth.logout();
  window.location.href = '/';
};

// 회원탈퇴 클릭 이벤트 핸들러
const handleWithdraw = () => {
  const confirmed = confirm("정말로 회원탈퇴 하시겠습니까?");

  if (confirmed) {
    // 사용자가 확인을 클릭했을 경우 특정 주소로 이동
    console.log("탈퇴 : ", user.value.userNo)
    settingApi.delete(user.value.userNo);
    auth.logout();
  }
};

const body = document.getElementsByTagName("body")[0];

const store = useStore();

onMounted(() => {
  store.state.isAbsolute = true;
  setNavPills();
  setTooltip();
});

onBeforeMount(() => {
  store.state.imageLayout = "profile-overview";
  store.state.showNavbar = false;
  store.state.showFooter = true;
  store.state.hideConfigButton = true;
  body.classList.add("profile-overview");
});
</script>
<template>
  <main>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
      integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA=="
      crossorigin="anonymous" referrerpolicy="no-referrer" />
    <div class="container-fluid">
      <div class="page-header min-height-300 bg-yellow" style="
          margin-right: -34%;
          margin-left: -34%;
        ">
        <!-- <span class="mask bg-gradient-success opacity-6"></span> -->
      </div>
      <div class="card shadow-lg mt-n6 page-size">
        <div class="card-body p-3">
          <div class="row gx-4">
            <div class="col-auto">
              <!-- 유저 프로필 이미지 -->
              <div class="avatar avatar-xl position-relative">
                <img :src="user.profilePic" alt="profile_image" class="profile-img shadow-sm w-100 border-radius-lg"
                  @click="triggerFileInput" />
                <input type="file" class="form-control visually-hidden" ref="profilePic" id="avatar"
                  accept="image/png, image/jpeg" @change="handleFileChange" />
                <button v-if="userInfo.profilePic" @click="cancelProfilePic" class="btn cancel-btn">
                  <i class="fa-solid fa-x"></i>
                </button>
              </div>


            </div>
            <div class="col-auto my-auto">
              <div class="h-100">
                <!-- 유저 닉네임 나오는 곳 -->
                <h5 class="mb-1">{{ user.userId }}</h5>
              </div>
            </div>
            <div class="mx-auto mt-3 col-lg-4 col-md-6 my-sm-auto ms-sm-auto me-sm-0 btn-logout"
              style="margin-right: 40px !important;">
              <div class="nav-wrapper position-relative end-0">
                <a class="px-0 py-1 mb-0 nav-link" data-bs-toggle="tab" href="javascript:;" role="tab"
                  aria-selected="false" @click.prevent="logout">
                  <span class="ms-1">logout</span>
                  <!-- <i class="fa-solid fa-right-from-bracket" style="margin-left: 5px;"></i> -->
                  <i class="fa-solid fa-angles-right" style="margin-left: 5px;"></i>
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="py-4 container-fluid">
      <div class="">
        <div class="col-md-8 page-size">
          <div class="card">
            <div class="card-body">
              <p class="text-uppercase text-sm">Personal information</p>
              <div class="row">
                <div class="col-md-6">
                  <label for="last-name" class="form-control-label">이름</label>
                  <input class="form-control info-input" id="last-name" :value="user.firstName" disabled />
                </div>
                <div class="col-md-6">
                  <label for="first-name" class="form-control-label">성</label>
                  <input class="form-control info-input" id="first-name" :value="user.lastName" disabled />
                </div>
                <div class="col-md-6">
                  <label for="birthday" class="form-control-label">생일</label>
                  <input class="form-control info-input" id="birthday" :value="user.birthday" disabled />
                </div>
                <div class="col-md-6">
                  <label for="phone" class="form-control-label">전화번호</label>
                  <input class="form-control info-input" id="phone" :value="user.phoneNo" disabled />
                </div>
              </div>
              <br />
              <p class="text-uppercase text-sm">Personal address</p>
              <form @submit.prevent="updateProfile">
                <div class="row">
                  <div class="col-md-6 input-address">
                    <label for="address" class="form-control-label">주소</label>
                    <input class="form-control" id="address" v-model="userInfo.address" />
                  </div>
                  <div class="col-md-6">
                    <label for="postal-code" class="form-control-label">우편번호</label>
                    <input class="form-control" id="postal-code" v-model="userInfo.postCode" />
                  </div>
                  <div class="col-md-6">
                    <label for="country" class="form-control-label">국가</label>
                    <select id="country" class="form-select" v-model="userInfo.countryCode">
                      <option value="0">한국</option>
                      <option value="1">미국</option>
                      <option value="2">인도네시아</option>
                      <option value="3">베트남</option>
                    </select>
                  </div>
                </div>
                <div class="card-header pb-0" style="padding-right: 0;">
                  <div class="d-flex align-items-center">
                    <argon-button color="success" size="sm" class="ms-auto">Settings</argon-button>
                  </div>
                </div>
              </form>
              <hr class="horizontal dark" />
              <!-- <hr /> -->
              <p class="text-uppercase text-sm">Privacy and security</p>
              <ul class="navbar-nav nav-fill">
                <li class="pvt-item">
                  <a class="pvt-link">
                    <div class="pvt-icon"><i class="fa-solid fa-file-invoice"></i></div>
                    <span>계좌</span>
                    <i class="fa-solid fa-angle-right"></i>
                  </a>
                </li>
                <li class="pvt-item">
                  <a class="pvt-link" href="/change-password">
                    <div class="pvt-icon"><i class="fa-solid fa-lock"></i></div>
                    <span>비밀번호 변경</span>
                    <i class="fa-solid fa-angle-right"></i>
                  </a>
                </li>
                <li class="pvt-item">
                  <a class="pvt-link">
                    <div class="pvt-icon"><i class="fa-solid fa-lock"></i></div>
                    <span>2차 비밀번호</span>
                    <i class="fa-solid fa-angle-right"></i>
                  </a>
                </li>
              </ul>
              <hr class="horizontal dark" />
              <!-- <hr /> -->
              <ul class="navbar-nav nav-fill">
                <li class="pvt-item">
                  <div class="pvt-icon"><i class="fa-solid fa-circle-half-stroke fa-flip-horizontal"></i></div>
                  <span>테마</span>
                  <input type="checkbox" id="toggle" hidden>
                  <label for="toggle" class="toggleSwitch">
                    <span class="toggleButton"></span>
                  </label>
                </li>
                <li class="pvt-item" @click="handleWithdraw">
                  <a class="pvt-link">
                    <div class="pvt-icon"><i class="fa-regular fa-circle-xmark fa-lg"></i></div>
                    <span>회원탈퇴</span>
                    <i class="fa-solid fa-angle-right"></i>
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <!-- <div class="col-md-4">
          <profile-card />
        </div> -->
      </div>
    </div>
  </main>
</template>
<style>
.btn-logout {
  width: fit-content !important;
}

.page-size {
  width: 80% !important;
  margin: 0 auto;
}

.info-input:disabled {
  background-color: white !important;
}

.pvt-item {
  margin: 10px 0;
  display: flex;
  align-items: center;
}

.pvt-link {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  cursor: pointer;
  text-decoration: none;
}

.pvt-link:hover {
  color: black;
}

.pvt-link>i:last-child {
  margin-left: auto;
}

.pvt-icon {
  border-radius: 50%;
  background-color: #D9D9D9;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
}

.pvt-icon>i {
  font-size: 20px;
  color: black;
}

.toggleSwitch {
  width: 50px;
  height: 25px;
  display: block;
  position: relative;
  border-radius: 30px;
  background-color: #fff;
  box-shadow: 0 0 16px 3px rgba(0 0 0 / 15%);
  cursor: pointer;
  margin: 0 0 0 auto !important;
}

.toggleSwitch .toggleButton {
  width: 20px;
  height: 20px;
  position: absolute;
  top: 50%;
  left: 4px;
  transform: translateY(-50%);
  border-radius: 50%;
  background: black;
}

#toggle:checked~.toggleSwitch {
  background: black;
}

#toggle:checked~.toggleSwitch .toggleButton {
  left: calc(100% - 25px);
  background: #fff;
}

.toggleSwitch,
.toggleButton {
  transition: all 0.2s ease-in;
}

.input-address {
  width: 100% !important;
}

.profile-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.avatar {
  position: relative;
}

.cancel-btn {
  position: absolute;
  top: 0px;
  right: 0px;
  font-size: 12px;
  background-color: white;
  border-radius: 50%;
  padding: 0.1rem 0.4rem !important;
  border: 1px solid #ccc;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: rgb(117, 118, 118) !important;
  color: white !important;
}

.cancel-btn:hover,
.cancel-btn:focus,
.cancel-btn:active {
  margin: 0;
  transform: none !important;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
