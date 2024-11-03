<script setup>
import { computed, ref, reactive, watchEffect } from 'vue';
import { onBeforeMount, onMounted, onBeforeUnmount } from 'vue';
import { useStore } from 'vuex';

import setNavPills from '@/assets/js/nav-pills.js';
import setTooltip from '@/assets/js/tooltip.js';
import ArgonButton from '@/components/templates/ArgonButton.vue';
import settingApi from '@/api/settingApi';
import { useAuthStore } from '@/stores/auth';

import Swal from 'sweetalert2';
import SecondPasswordModal from '@/components/modal/SecondPasswordModal.vue';
// import Set2nd from './Set2nd.vue';
import Modal from '../../components/modal/Modal.vue';
import Set2ndModal from './Set2ndModal.vue';
import SetAccountModal from './SetAccountModal.vue';
import { useWebSocket } from "@/utils/websocket";

//i18n
import { useI18n } from "vue-i18n";
const { t } = useI18n();

const auth = useAuthStore();
const isLogin = computed(() => auth.isLogin);
const user = computed(() => auth.user);

const isSecondarySet = ref(false);
const isAccountSet = ref(false);

const profilePic = ref(null);

const emit = defineEmits(['password-verified', 'password-changed', 'account-changed', 'close']);

const { disconnect } = useWebSocket();

//2차 비밀번호 관련 기능
const showSet2ndModal = ref(false);
const openSet2ndModal = () => {
    showSet2ndModal.value = true;
};
const closeSet2ndModal = () => {
    showSet2ndModal.value = false;
};
const handlePasswordChanged = async () => {
    showSet2ndModal.value = false;
    Swal.fire({
        title: t('swal--title-success'),
        text: t('Secondary password change was successful.'),
        icon: 'success',
    }).then(() => {
        window.location.reload();
    });
};

const showModal = ref(false);
const openModal = () => {
    showModal.value = true;
};
const closeModal = () => {
    showModal.value = false;
};
const handlePasswordVerified = () => {
    showModal.value = false;
    openSet2ndModal();
}

const showAcntModal = ref(false);
const openAcntModal = () => {
    showAcntModal.value = true;
};
const closeAcntModal = () => {
    showAcntModal.value = false;
};
const handleAcntVerified = async () => {
    showAcntModal.value = false;
    Swal.fire({
        title: t('swal--title-success'),
        text: t('Account registration was successful.'),
        icon: 'success',
    }).then(() => {
        window.location.reload();
    });
}

const userInfo = reactive({
    userId: '',
    address: '',
    postCode: '',
    countryCode: '',
    accountNo: '',
    secondPwd: '',
    profilePic: null,
});

watchEffect(() => {
    if (user.value) {
        userInfo.userId = auth.userId;
        userInfo.address = user.value.address;
        userInfo.postCode = user.value.postCode;
        userInfo.countryCode = user.value.countryCode;
        userInfo.secondPwd = user.value.secondPwd;
        userInfo.accountNo = user.value.accountNo;
    }
});

const originalProfilePic = ref(null);

onMounted(async () => {
    if (auth.userId) {
        await auth.fetchUser(auth.userId);
        originalProfilePic.value = user.value?.profilePic;
    } else {
        console.error('사용자 ID를 찾을 수 없습니다.');
    }
    console.log('프로필 사용자 : ', user.value);
});

const triggerFileInput = () => {
    if (profilePic.value) {
        profilePic.value.value = ''; // 파일 입력 필드 초기화
    }
    profilePic.value.click();
};

const cancelProfilePic = () => {
    user.value.profilePic = originalProfilePic.value;
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
        Swal.fire({
            title: '성공!',
            text: '프로필 업데이트 성공',
            icon: 'success',
            confirmButtonText: '확인',
        });

        window.location.reload();
    } catch (error) {
        console.error('프로필 업데이트 실패:', error.response.data);
        Swal.fire({
            title: '실패',
            html: `프로필 업데이트 실패`,
            icon: 'error',
            confirmButtonText: '확인',
        });
    }
};

const logout = (e) => {
    // 로그아웃
    auth.logout();
    disconnect();
    window.location.href = '/main';
};

// 회원탈퇴 클릭 이벤트 핸들러
const handleWithdraw = () => {
    Swal.fire({
        title: '정말로 회원탈퇴 하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '확인',
        cancelButtonText: '취소',
    }).then((result) => {
        if (result.isConfirmed) {
            // 사용자가 확인을 클릭했을 경우 탈퇴 처리
            console.log('탈퇴 : ', user.value.userId);
            settingApi.delete(user.value.userId).then(() => {
                auth.logout();
                Swal.fire({
                    title: '탈퇴 확인',
                    text: '회원탈퇴가 완료되었습니다.',
                    icon: 'success',
                    confirmButtonText: '확인',
                });
            });
        } else {
            Swal.fire({
                title: '취소됨',
                text: '회원탈퇴가 취소되었습니다.',
                icon: 'info',
                confirmButtonText: '확인',
            });
        }
    });
};

const body = document.getElementsByTagName('body')[0];

const store = useStore();

onMounted(() => {
    store.state.isAbsolute = true;
    setNavPills();
    setTooltip();
});

onBeforeMount(() => {
    store.state.imageLayout = 'profile-overview';
    store.state.showNavbar = false;
    store.state.showFooter = true;
    store.state.hideConfigButton = true;
    body.classList.add('profile-overview');
});

onBeforeUnmount(() => {
    store.state.isAbsolute = false;
    store.state.imageLayout = 'default';
    store.state.showNavbar = true;
    store.state.showFooter = true;
    store.state.hideConfigButton = false;
    body.classList.remove('profile-overview');
});
</script>
<template>
    <main>

        <div class="container-fluid">
            <div class="page-header min-height-300 bg-yellow" style="margin-right: -34%; margin-left: -34%">
                <!-- <span class="mask bg-gradient-success opacity-6"></span> -->
            </div>
            <div class="card shadow-lg mt-n6 page-size">
                <div class="card-body p-3">
                    <div class="row gx-4">
                        <div class="col-auto">
                            <!-- 유저 프로필 이미지 -->
                            <div class="avatar avatar-xl position-relative">
                                <img :src="user?.profilePic" alt="profile_image"
                                    class="profile-img shadow-sm w-100 border-radius-lg" @click="triggerFileInput" />
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
                                <h5 class="mb-1">{{ user?.userId }}</h5>
                            </div>
                        </div>
                        <div class="mx-auto mt-3 col-lg-4 col-md-6 my-sm-auto ms-sm-auto me-sm-0 btn-logout"
                            style="margin-right: 40px !important">
                            <div class="nav-wrapper position-relative end-0">
                                <a class="px-0 py-1 mb-0" data-bs-toggle="tab" href="javascript:;" role="tab"
                                    aria-selected="false" @click.prevent="logout">
                                    <span class="ms-1">{{
                                        $t('profile--button-logout')
                                        }}</span>
                                    <!-- <i class="fa-solid fa-right-from-bracket" style="margin-left: 5px;"></i> -->
                                    <i class="fa-solid fa-angles-right" style="margin-left: 5px"></i>
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
                            <p class="text-uppercase text-sm">
                                {{ $t('profile--personalInfoTitle') }}
                            </p>
                            <div class="row">
                                <div class="col-md-6">
                                    <label for="last-name" class="form-control-label">{{
                                        $t('profile--lastNameLabel')
                                    }}</label>
                                    <input class="form-control info-input" id="last-name" :value="user?.firstName"
                                        disabled />
                                </div>
                                <div class="col-md-6">
                                    <label for="first-name" class="form-control-label">{{
                                        $t('profile--firstNameLabel')
                                    }}</label>
                                    <input class="form-control info-input" id="first-name" :value="user?.lastName"
                                        disabled />
                                </div>
                                <div class="col-md-6">
                                    <label for="birthday" class="form-control-label">{{
                                        $t('profile--birthdayLabel')
                                    }}</label>
                                    <input class="form-control info-input" id="birthday" :value="user?.birthday"
                                        disabled />
                                </div>
                                <div class="col-md-6">
                                    <label for="phone" class="form-control-label">{{ $t('profile--phoneLabel')
                                        }}</label>
                                    <input class="form-control info-input" id="phone" :value="user?.phoneNo" disabled />
                                </div>
                            </div>
                            <br />
                            <p class="text-uppercase text-sm">
                                {{ $t('profile--personaladdress') }}
                            </p>
                            <form @submit.prevent="updateProfile">
                                <div class="row">
                                    <div class="col-md-6 input-address">
                                        <label for="address" class="form-control-label">{{
                                            $t('profile--addressLabel')
                                        }}</label>
                                        <input class="form-control" id="address" v-model="userInfo.address" />
                                    </div>
                                    <div class="col-md-6">
                                        <label for="postal-code" class="form-control-label">{{
                                            $t('profile--postalCodeLabel')
                                        }}</label>
                                        <input class="form-control" id="postal-code" v-model="userInfo.postCode" />
                                    </div>
                                    <div class="col-md-6">
                                        <label for="country" class="form-control-label">{{
                                            $t('profile--countryLabel')
                                        }}</label>
                                        <select id="country" class="form-select no-arrow" v-model="userInfo.countryCode"
                                            disabled>
                                            <option value="0">
                                                {{ $t('country_kr') }}
                                            </option>
                                            <option value="1">
                                                {{ $t('country_us') }}
                                            </option>
                                            <option value="2">
                                                {{ $t('country_id') }}
                                            </option>
                                            <option value="3">
                                                {{ $t('country_vn') }}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="card-header pb-0" style="padding-right: 0">
                                    <div class="d-flex align-items-center">
                                        <argon-button color="success" size="sm" class="ms-auto">{{
                                            $t('profile--button-settings')
                                        }}</argon-button>
                                    </div>
                                </div>
                            </form>
                            <hr class="horizontal dark" />
                            <!-- <hr /> -->
                            <p class="text-uppercase text-sm">
                                {{ $t('profile--privacySecurityTitle') }}
                            </p>
                            <ul class="navbar-nav nav-fill">
                                <li class="pvt-item">
                                    <a v-if="userInfo.accountNo === null || userInfo.accountNo === ''" class="pvt-link"
                                        @click="openAcntModal">
                                        <div class="pvt-icon">
                                            <i class="fa-solid fa-money-check-dollar"></i>
                                        </div>
                                        <span>{{
                                            $t('profile--accountLabel')
                                            }}</span>
                                        <span class="text-danger ms-3"><i class="fas fa-exclamation"></i> Requires a
                                            one-time initial setup.</span>

                                        <i class="fa-solid fa-angle-right"></i>
                                    </a>
                                    <div v-else class="d-flex">
                                        <div class="pvt-icon">
                                            <i class="fa-solid fa-money-check-dollar"></i>
                                        </div>
                                        <div>{{
                                            $t('profile--accountLabel')
                                        }}</div>
                                        <strong class="ms-3">{{ userInfo.accountNo }}</strong>

                                    </div>
                                </li>
                                <li class="pvt-item">
                                    <a class="pvt-link" href="/change-password">
                                        <div class="pvt-icon">
                                            <i class="ni ni-lock-circle-open"></i>
                                        </div>
                                        <span>{{
                                            $t('profile--changePasswordLabel')
                                            }}</span>
                                        <i class="fa-solid fa-angle-right"></i>
                                    </a>
                                </li>
                                <li class="pvt-item">
                                    <a v-if="userInfo.secondPwd === null || userInfo.secondPwd === ''" class="pvt-link"
                                        @click="openSet2ndModal">
                                        <div class="pvt-icon">
                                            <i class="fa-solid fa-user-lock"></i>
                                        </div>
                                        <span>{{
                                            $t(
                                                'profile--secondaryPasswordLabel'
                                            )
                                        }}</span>
                                        <span class="text-danger ms-3"><i class="fas fa-exclamation"></i> Requires a
                                            one-time initial setup.</span>

                                        <i class="fa-solid fa-angle-right"></i>
                                    </a>
                                    <a v-else class="pvt-link" @click="openModal">
                                        <div class="pvt-icon">
                                            <i class="fa-solid fa-user-lock"></i>
                                        </div>
                                        <span>{{
                                            $t(
                                                'profile--secondaryPasswordLabel'
                                            )
                                        }}</span>

                                        <i class="fa-solid fa-angle-right"></i>
                                    </a>
                                </li>
                            </ul>
                            <hr class="horizontal dark" />
                            <!-- <hr /> -->
                            <ul class="navbar-nav nav-fill">
                                <!-- <li class="pvt-item">
                  <div class="pvt-icon">
                    <i
                      class="fa-solid fa-circle-half-stroke fa-flip-horizontal"
                    ></i>
                  </div>
                  <span>{{ $t("profile--themeLabel") }}</span>
                  <input type="checkbox" id="toggle" hidden />
                  <label for="toggle" class="toggleSwitch">
                    <span class="toggleButton"></span>
                  </label>
                </li> -->
                                <li class="pvt-item" @click="handleWithdraw">
                                    <a class="pvt-link">
                                        <div class="pvt-icon">
                                            <i class="fa-regular fa-circle-xmark fa-lg"></i>
                                        </div>
                                        <span>{{
                                            $t('profile--deleteAccountLabel')
                                            }}</span>
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
        <SecondPasswordModal v-if="showModal" @close="closeModal" @password-verified="handlePasswordVerified" />
        <Set2ndModal v-if="showSet2ndModal" @close="closeSet2ndModal" @password-changed="handlePasswordChanged" />
        <!-- <SetAccountModal v-if="showAcntModal" @close="closeAcntModal" @account-changed="handleAcntVerified" />
        <SetAccountModal /> -->
        <!-- <SetAccountModal v-if="showAcntModal" @close="closeAcntModal" @account-changed="handleAcntVerified" /> -->
        <SetAccountModal v-if="showAcntModal" :userId="userInfo.userId" @close="closeAcntModal"
            @account-changed="handleAcntVerified" />

    </main>


</template>
<style scoped>
.no-arrow {
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;
    background-image: none;
}

.no-arrow::-ms-expand {
    display: none;
}

.no-arrow:disabled {
    background-color: transparent;
}

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
    background-color: #d9d9d9;
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