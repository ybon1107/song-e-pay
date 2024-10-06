<template>
  <header class="p-3 bg-dark text-white">
    <div class="container">
      <div class="d-flex align-items-center justify-content-between">
        <a href="/" class="mb-2 mb-lg-0 text-white text-decoration-none">
          <!-- <img src="/favicon.png" alt="Song E Pay" class="logo-img d-none d-md-block"> -->
          <img src="/favicon.png" alt="Song E Pay" class="logo-img d-block d-md-none" />
          <!-- <h1 class="text-white"> Song E Pay</h1> -->
          <h4 class="text-white d-none d-md-block mb-0">Song E Pay</h4>
          <!-- <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap"><use xlink:href="#bootstrap"/></svg> -->
        </a>

        <div>
          <a href="/register/legal" class="text-white text-decoration-none me-3 text-bold">회원가입</a>
          <a href="/login" class="btn btn-primary mb-0">로그인</a>
        </div>
      </div>
    </div>
  </header>
  <!-- 왼쪽 화면 -->
  <div class="section-container-beige">
    <!-- 모바일 버전 버튼 -->
    <a href="https://leeyebon7929.wixsite.com/my-site-1" class="btn mobile-btn col-md-3 mb-4">Get Started</a>
    <div class="d-flex justify-content-between flex-column flex-md-row align-items-start">
      <!-- 왼쪽 섹션 -->
      <div class="left-section-main d-flex flex-column mt-2 col-md-6">
        <h3 class="col-md-9 mb-4">
          한국에서의 여행을 더 편리하게, 간편한 전자지갑 서비스로
        </h3>
        <p class="col-md-9 mb-4">
          한국을 여행하는 외국인들을 위해, 안전하고 빠른 결제 및 환전 서비스를
          제공합니다. 실시간 환율 조회부터 친구에게 송금까지, 모든 금융 서비스를
          한 곳에서 쉽게 이용하세요.
        </p>

        <!-- 웹 버전 버튼 -->
        <a href="https://leeyebon7929.wixsite.com/my-site-1" class="btn web-btn col-md-3 mb-4">시작하기</a>

        <!-- 아이콘 섹션 -->
        <div class="icon-item d-flex flex-wrap mt-3">
          <div class="icon-item col-md-3 d-flex align-items-center">
            <div style="position: relative; width: 50px; height: 50px">
              <!-- Rectangle -->
              <svg width="50" height="50">
                <rect x="5" y="5" width="40" height="40" rx="10" ry="10" fill="#1D0E03" />
              </svg>

              <!-- Icon -->
              <i class="fa-solid fa-shield-halved"
                style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); font-size: 23px; color: #fff6ef"></i>
            </div>

            <div class="ms-2">
              <div style="font-size: 12px">Speed &</div>
              <div style="font-size: 12px">Security</div>
            </div>
          </div>
          <div class="icon-item col-md-3 d-flex align-items-center">
            <div style="position: relative; width: 50px; height: 50px">
              <!-- Circle -->
              <svg width="50" height="50">
                <rect x="5" y="5" width="40" height="40" rx="10" ry="10" fill="#1D0E03" />
              </svg>

              <!-- Icon -->
              <i class="fa-solid fa-hand-holding"
                style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); font-size: 23px; color: #fff6ef"></i>
            </div>
            <div class="ms-2">
              <div style="font-size: 12px">Convenient &</div>
              <div style="font-size: 12px">Simple</div>
            </div>
          </div>
          <div class="icon-item col-md-3 d-flex align-items-center">
            <div style="position: relative; width: 50px; height: 50px">
              <!-- Circle -->
              <svg width="50" height="50">
                <rect x="5" y="5" width="40" height="40" rx="10" ry="10" fill="#1D0E03" />
              </svg>

              <!-- Icon -->
              <i class="fa-solid fa-earth-americas"
                style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); font-size: 23px; color: #fff6ef"></i>
            </div>
            <div class="ms-2">
              <div style="font-size: 12px">Global &</div>
              <div style="font-size: 12px">Local</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 오른쪽 섹션 -->
      <div class="right-section-main d-flex justify-content-center mt-2 col-md-6 align-self-end">
        <img class="main-img" src="../../../public/images/mainpage-1.png" alt="Illustration"
          style="max-width: 100%; height: auto" />
      </div>
    </div>
  </div>

  <section class="curved-section">
    <svg viewBox="0 0 1440 200" xmlns="http://www.w3.org/2000/svg" style="transform: scaleX(-1)">
      <path fill="#fff6ef" fill-opacity="1" d="M0,150L1440,90L1440,0L0,0Z"></path>
    </svg>
  </section>
  <div class="section-container-green">
    <!-- 두 번째 섹션 -->
    <div class="d-flex justify-content-between flex-column flex-md-row">
      <div class="left-section d-flex align-items-center mt-2 col-md-5">
        <h3 class="col-md-8">
          "여행의 필수 파트너가<br />
          되어 드립니다"
        </h3>
      </div>
      <div class="right-section d-flex align-items-center p-5 mt-2 col-md-7">
        <p class="col-md-12">
          여행의 금융을 간편하게, 한국에서 더 스마트하게<br />
          Song-E Pay는 한국을 여행하는 외국인에게 실시간 환율 조회, 자동 환전,
          간편한 송금 등 다양한 금융 기능을 제공합니다. 환전과 결제를 쉽게
          관리할 수 있는 페이 머니 계좌와 은행 지도 서비스로 현지에서의 금융
          업무를 간편하게 해결하세요. 결제 내역을 기반으로 자동 기록되는 여행
          캘린더로 소중한 추억도 손쉽게 관리할 수 있습니다.
        </p>
      </div>
    </div>
  </div>
  <div class="section-container-beige">
    <!-- 실시간 환율 조회 섹션 -->

    <div class="d-flex flex-wrap mb-3">
      <!-- What We Offer 섹션 -->
      <div class="mt-2 col-md-12">
        <h2 class="font_2 mb-4">Song-E Pay는!</h2>
        <p class="font_8 mb-7">
          실시간 환율 조회부터 자동 환전, 알림 서비스까지! 언제든지 유리한 환전
          기회를 포착하고, 쉽고 빠르게 환전할 수 있도록 도와드립니다. 환율
          변동을 놓치지 않고, 원하는 환율에 맞춰 최적의 환전을 경험해보세요.
        </p>
      </div>
      <div class="flex-fill-column me-4">
        <div class="image-container">
          <img class="flex_item_image" style="border: 2px solid; border-radius: 2cap"
            src="https://static.wixstatic.com/media/c837a6_07cdd24cb9e84acdb41d8c3568867354~mv2.jpg/v1/fill/w_284,h_376,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/Artboard%2048%20copy%204_5x-100.jpg"
            alt="실시간 환율 조회" />
        </div>
        <div class="text-container">
          <h5 class="font_5 pt-3 pb-2 pl-0 pr-0">실시간 환율 조회</h5>
          <p>
            여행 전, 중, 후 언제든지 최신 환율을 확인하고, 환율 변동에 맞춰
            최적의 환전을 진행하세요.
          </p>
        </div>
      </div>
      <div class="flex-fill-column mx-4">
        <div class="image-container">
          <img class="flex_item_image" style="border: 2px solid; border-radius: 2cap"
            src="https://static.wixstatic.com/media/c837a6_ef6bbe6808334282ab6c74203e782f28~mv2.jpg/v1/fill/w_284,h_374,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/Artboard%2048%20copy%207_5x-100.jpg"
            alt="자동 환전 서비스" />
        </div>
        <div class="text-container">
          <h5 class="font_5 pt-3 pb-2 pl-0 pr-0">자동 환전 서비스</h5>
          <p>
            환율이 가장 좋을 때 자동으로 환전을 완료해 드립니다. 손쉬운 환전으로
            환율 고민을 덜어보세요.
          </p>
        </div>
      </div>
      <div class="flex-fill-column mx-4">
        <div class="image-container">
          <img class="flex_item_image" style="border: 2px solid; border-radius: 2cap"
            src="https://static.wixstatic.com/media/c837a6_070e2f97109e4b4ba2e079e8f7f18f4f~mv2.jpg/v1/fill/w_284,h_374,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/Artboard%2048%20copy%205_5x-100.jpg"
            alt="환율 알림 서비스" />
        </div>
        <div class="text-container">
          <h5 class="font_5 pt-3 pb-2 pl-0 pr-0">환율 알림 서비스</h5>
          <p>
            원하는 환율에 도달하면 즉시 알림을 받아 최적의 시기에 환전을 할 수
            있습니다.
          </p>
        </div>
      </div>
      <div class="flex-fill-column mx-4">
        <div class="image-container">
          <img class="flex_item_image" style="border: 2px solid; border-radius: 2cap"
            src="https://static.wixstatic.com/media/c837a6_1f658b7b98b846d4865a783d52c8b43c~mv2.jpg/v1/fill/w_284,h_374,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/Artboard%2048%20copy%206_5x-100.jpg"
            alt="No 수수료! 자유로운 환전" />
        </div>
        <div class="text-container">
          <h5 class="font_5 pt-3 pb-2 pl-0 pr-0">No 수수료! 자유로운 환전</h5>
          <p>
            환전한 금액을 관리할 수 있는 페이 머니 계좌를 제공해 드립니다.
            수수료 걱정 없이 언제 어디서든 환전하세요.
          </p>
        </div>
      </div>
    </div>
  </div>
  <!-- 세 번째 섹션 -->
  <section class="section-container-brown">
    <div class="d-flex justify-content-between align-items-center flex-column flex-md-row mb-3">
      <div class="mesh-left col-md-5 d-flex flex-column">
        <h2 class="font_2 mb-0">페이 머니 계좌</h2>
        <h2 class="font_2 mb-4">& 한국 원화 전용 계좌 간 송금</h2>
        <p class="fw-bold fs-5">환전한 금액, 한 곳에서 관리!</p>
        <p>
          페이 머니 계좌를 통해 다양한 통화를 간편하게 관리하세요. 환전한 금액을
          저장하고, 필요할 때 언제든 빠르게 결제와 송금이 가능합니다.
        </p>
        <p class="fw-bold fs-5">간편한 한국 원화 송금!</p>
        <p>
          한국 원화 전용 계좌를 통해 현지에서 손쉽게 친구에게 송금하세요. 은행
          계좌 없이도, 페이 머니만 있으면 몇 번의 클릭으로 빠르고 안전하게
          송금이 가능합니다.
        </p>
        <p class="fw-bold fs-5">쉽고 빠른 결제와 송금 여행 중에도 걱정 없이!</p>
        <p>
          환전한 금액을 스마트하게 관리하고, 필요한 순간 간편하게 사용할 수 있는
          기능을 제공합니다. 한국 내에서도, 환율 걱정 없이 바로 원화로 결제하고
          송금하세요.
        </p>
      </div>
      <div class="mesh-right col-md-7 col-12 d-flex justify-content-center align-items-center">
        <img src="../../../public/images/Coin.png" alt="송금 이미지" class="img-fluid"
          style="max-width: 160%; height: auto; object-fit: cover" />
      </div>
    </div>
  </section>
  <!-- 곡선 -->
  <div class="curve-container" style="background-color: #f8f9fa">
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320" class="curve">
      <path fill="#B0AB99" fill-opacity="1"
        d="M0,320L48,288C96,256,192,192,288,165.3C384,139,480,149,576,176C672,203,768,245,864,256C960,267,1056,245,1152,218.7C1248,192,1344,160,1392,144L1440,128L1440,0L1392,0C1344,0,1248,0,1152,0C1056,0,960,0,864,0C768,0,672,0,576,0C480,0,384,0,288,0C192,0,96,0,48,0L0,0Z">
      </path>
    </svg>
  </div>
  <!-- 네 번째 섹션 -->
  <section class="section-container-beige">
    <div class="d-flex justify-content-between align-items-center flex-column flex-md-row mb-3">
      <div class="mesh-right col-md-5 col-12 d-flex justify-content-center align-items-center">
        <img src="../../../public/images/Map.png" alt="지도 이미지" class="img-fluid"
          style="max-width: 100%; height: auto" />
      </div>
      <div class="mesh-left col-md-6 d-flex flex-column">
        <h2 class="font_2 mb-4 text-start">은행 지도</h2>
        <p>
          가까운 국민은행 위치를 손쉽게 찾아보세요. <br />
          지도를 통해 가장 가까운 지점을 확인하고, <br />필요한 금융 업무를
          신속하게 처리할 수 있습니다.
        </p>
      </div>
    </div>
    <div class="lastpage d-flex justify-content-between align-items-center flex-column mb-3">
      <h2 class="title text-center">Prepare for Your Trip to Korea With Our SongEpay</h2>
      <br />
      <a href="/home" class="btn">get started</a>
      <br />
    </div>
  </section>
</template>

<script setup>
import { useStore } from "vuex";
import { onBeforeMount, computed } from "vue";

const store = useStore();

onBeforeMount(() => {
  store.state.hideConfigButton = true;
  store.state.showNavbar = false;
  store.state.showSidenav = false;
  store.state.showFooter = false;
});
</script>
<style scoped>
.flex-fill-column {
  flex-basis: 18%;
}

.section-container-beige {
  background: linear-gradient(to bottom, #f8f9fa, #fff6ef);
  padding-top: 8rem;
  padding-left: 13rem;
  padding-right: 8rem;
  padding-bottom: 6rem;
}

.section-container-green {
  background-color: #ffeb69;
  padding-left: 13rem;
  padding-right: 8rem;
  padding-bottom: 6rem;
}

.section-container-brown {
  background-color: #b0ab99;
  color: #f4f2f2;
  padding-top: 8rem;
  padding-left: 13rem;
  padding-right: 8rem;
  padding-bottom: 6rem;
}

.curved-section {
  background-color: #ffeb69;
  padding: 0;
  margin-top: -20px;
  /* 섹션 사이에 자연스럽게 연결되도록 설정 */
  overflow: hidden;
}

.curved-section svg {
  display: block;
  width: 100%;
  height: auto;
}

.image-container img {
  max-width: 245px;
  /* Adjust the value as needed */
  height: auto;
  /* Maintain aspect ratio */
}

.web-btn {
  display: block;
  /* 웹에서는 보임 */
}

.mobile-btn {
  display: none;
  /* 모바일에서는 안 보임 */
}

/* 버튼 */
.btn {
  padding: 10px 20px;
  background-color: #ffeb69;
  /* 버튼 배경색 */
  color: #fff;
  /* 텍스트 색상 */
  text-align: center;
  text-decoration: none;
  border-radius: 15px;
  /* 모서리 둥글게 */
  font-size: 16px;
}

/* Mobile styles */
@media (max-width: 768px) {

  /* Adjust the max-width value as needed for your breakpoint */
  .section-container-beige {
    background: linear-gradient(to bottom, #f8f9fa, #fff6ef);
    padding-top: 5rem;
    padding-left: 5rem;
    padding-right: 5rem;
    padding-bottom: 5rem;
  }

  .section-container-green {
    background-color: #ffeb69;
    padding-left: 5rem;
    padding-right: 5rem;
    padding-bottom: 5rem;
    padding-top: 5rem;
  }

  .mobile-btn {
    display: block;
    /* 모바일에서는 보임 */
    order: 1;
    /* 첫 번째로 오도록 설정 */
  }

  .web-btn {
    display: none;
    /* 웹 버튼은 모바일에서 숨김 */
  }

  /* 모바일에서 이미지와 나머지 요소 순서 조정 */
  .right-section-main {
    order: 1;
    /* 이미지 두 번째로 */
  }

  .left-section-main {
    order: 2;
    /* 나머지 컨텐츠 순서대로 */
  }
}

.logo-img {
  height: 40px;
  width: auto;
}

.font_2 {
  font-weight: bolder;
}
</style>
