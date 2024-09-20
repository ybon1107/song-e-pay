<script setup>
import { onBeforeMount, onMounted, onBeforeUnmount } from "vue";
import { useStore } from "vuex";

import setNavPills from "@/assets/js/nav-pills.js";
import setTooltip from "@/assets/js/tooltip.js";
// import ProfileCard from "@/components/viewsComponents/ProfileCard.vue";
// import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";

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
onBeforeUnmount(() => {
  store.state.isAbsolute = false;
  store.state.imageLayout = "default";
  store.state.showNavbar = true;
  store.state.showFooter = true;
  store.state.hideConfigButton = false;
  body.classList.remove("profile-overview");
});
</script>
<template>
  <main>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <div class="container-fluid">
      <div
        class="page-header min-height-300"
        style="
          background-image: url(&quot;https://images.unsplash.com/photo-1531512073830-ba890ca4eba2?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1920&q=80&quot;);
          margin-right: -24px;
          margin-left: -34%;
        "
      >
        <span class="mask bg-gradient-success opacity-6"></span>
      </div>
      <div class="card shadow-lg mt-n6 page-size">
        <div class="card-body p-3">
          <div class="row gx-4">
            <div class="col-auto">
              <div class="avatar avatar-xl position-relative">
                <!-- 유저 프로필 이미지 -->
                <img
                  src="../assets/img/team-1.jpg"
                  alt="profile_image"
                  class="shadow-sm w-100 border-radius-lg"
                />
              </div>
            </div>
            <div class="col-auto my-auto">
              <div class="h-100">
                <!-- 유저 닉네임 나오는 곳 -->
                <h5 class="mb-1">유저 닉네임</h5>
              </div>
            </div>
            <div
              class="mx-auto mt-3 col-lg-4 col-md-6 my-sm-auto ms-sm-auto me-sm-0 btn-logout"
              style="margin-right: 40px !important;"
            >
              <div class="nav-wrapper position-relative end-0">
                <a
                      class="px-0 py-1 mb-0 nav-link"
                      data-bs-toggle="tab"
                      href="javascript:;"
                      role="tab"
                      aria-selected="false"
                    >
                      <!-- <svg
                        class="text-dark"
                        width="16px"
                        height="16px"
                        viewBox="0 0 40 40"
                        version="1.1"
                        xmlns="http://www.w3.org/2000/svg"
                        xmlns:xlink="http://www.w3.org/1999/xlink"
                      >
                        <title>settings</title>
                        <g
                          stroke="none"
                          stroke-width="1"
                          fill="none"
                          fill-rule="evenodd"
                        >
                          <g
                            transform="translate(-2020.000000, -442.000000)"
                            fill="#FFFFFF"
                            fill-rule="nonzero"
                          >
                            <g transform="translate(1716.000000, 291.000000)">
                              <g transform="translate(304.000000, 151.000000)">
                                <polygon
                                  class="color-background"
                                  opacity="0.596981957"
                                  points="18.0883333 15.7316667 11.1783333 8.82166667 13.3333333 6.66666667 6.66666667 0 0 6.66666667 6.66666667 13.3333333 8.82166667 11.1783333 15.315 17.6716667"
                                />
                                <path
                                  class="color-background"
                                  d="M31.5666667,23.2333333 C31.0516667,23.2933333 30.53,23.3333333 30,23.3333333 C29.4916667,23.3333333 28.9866667,23.3033333 28.48,23.245 L22.4116667,30.7433333 L29.9416667,38.2733333 C32.2433333,40.575 35.9733333,40.575 38.275,38.2733333 L38.275,38.2733333 C40.5766667,35.9716667 40.5766667,32.2416667 38.275,29.94 L31.5666667,23.2333333 Z"
                                  opacity="0.596981957"
                                />
                                <path
                                  class="color-background"
                                  d="M33.785,11.285 L28.715,6.215 L34.0616667,0.868333333 C32.82,0.315 31.4483333,0 30,0 C24.4766667,0 20,4.47666667 20,10 C20,10.99 20.1483333,11.9433333 20.4166667,12.8466667 L2.435,27.3966667 C0.95,28.7083333 0.0633333333,30.595 0.00333333333,32.5733333 C-0.0583333333,34.5533333 0.71,36.4916667 2.11,37.89 C3.47,39.2516667 5.27833333,40 7.20166667,40 C9.26666667,40 11.2366667,39.1133333 12.6033333,37.565 L27.1533333,19.5833333 C28.0566667,19.8516667 29.01,20 30,20 C35.5233333,20 40,15.5233333 40,10 C40,8.55166667 39.685,7.18 39.1316667,5.93666667 L33.785,11.285 Z"
                                />
                              </g>
                            </g>
                          </g>
                        </g>
                      </svg> -->
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
        <div class="col-md-8 page-size" >
          <div class="card">
            <div class="card-body">
              <p class="text-uppercase text-sm">Personal information</p>
              <div class="row">
                <div class="col-md-6">
                  <label for="last-name" class="form-control-label"
                    >이름</label
                  >
                  <input class="form-control info-input" id="last-name"  value="믿어"  disabled/>
                </div>
                <div class="col-md-6">
                  <label for="first-name" class="form-control-label"
                    >성</label
                  >
                  <input class="form-control info-input" id="first-name" value="핑"  disabled/>
                </div>
                <div class="col-md-6">
                  <label for="birthday" class="form-control-label"
                    >생일</label
                  >
                  <input class="form-control info-input" id="birthday" value="2024-09-05" disabled/>
                </div>
                <div class="col-md-6">
                  <label for="phone" class="form-control-label"
                    >전화번호</label
                  >
                  <input class="form-control info-input" id="phone" value="010-1234-5678" disabled/>
                </div>
              </div>
              <br />
              <p class="text-uppercase text-sm">Personal address</p>
              <div class="row">
                <div class="col-md-6">
                  <label for="address" class="form-control-label"
                    >주소</label
                  >
                  <input class="form-control" id="address" value="서울시 광진구"/>
                </div>
                <div class="col-md-6">
                  <label for="city" class="form-control-label"
                    >도시</label
                  >
                  <input class="form-control" id="city" value="서울"/>
                </div>
                <div class="col-md-6">
                  <label for="postal-code" class="form-control-label"
                    >우편번호</label
                  >
                  <input class="form-control" id="postal-code" value="123456"/>
                </div>
                <div class="col-md-6">
                  <label for="country" class="form-control-label"
                    >국가</label
                  >
                  <input class="form-control" id="country" value="한국"/>
                </div>
              </div>
              <div class="card-header pb-0" style="padding-right: 0;">
              <div class="d-flex align-items-center">
                <argon-button color="success" size="sm" class="ms-auto"
                  >Settings</argon-button
                >
              </div>
            </div>
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
              <li class="pvt-item">
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

  .pvt-link > i:last-child {
    margin-left: auto;
  }

  .pvt-icon{
    border-radius: 50%;
    background-color: #D9D9D9;
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 10px;
  }

  .pvt-icon > i {
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

  #toggle:checked ~ .toggleSwitch {
    background: black;
  }

  #toggle:checked ~ .toggleSwitch .toggleButton {
    left: calc(100% - 25px);
    background: #fff;
  }

  .toggleSwitch, .toggleButton {
    transition: all 0.2s ease-in;
  }


</style>
