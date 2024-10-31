<template>
    <nav style="
        background-color: #f6f2ef;
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        z-index: 1000;
        height:70px;
      ">
        <div class="container h-100 w-100 d-flex align-items-center justify-content-between">
            <a href="/" class="mb-0 text-white text-decoration-none">
                <img src="/images/song.png" alt="Song E Pay" class="logo-img d-block d-md-none" />

                <h4 class="d-none d-md-block mb-0">
                    <img class="ld-none d-md-block mb-0" src="../../../images/3D_SongE.png" alt="Illustration"
                        style="height: 40px; width: auto" />
                </h4>
            </a>

            <div>
                <a href="/register/legal" class="text-decoration-none me-3 text-bold">Sign Up</a>
                <a href="/login" class="btn btn-primary mb-0">Log in</a>
            </div>
        </div>
    </nav>
    <!-- 왼쪽 화면 -->
    <div class="section-container-beige">
        <h3 class="mb-4 mobile-btn text-center font_2">
            Make traveling in Korea more convenient<br />with our easy to use E-wallet service
        </h3>
        <div class="d-flex justify-content-between flex-column flex-md-row align-items-start">
            <!-- 왼쪽 섹션 -->

            <div class="left-section-main d-flex flex-column mt-2 col-md-6">
                <h3 class="mb-4 text-nowrap web-btn font_2">
                    Make traveling in Korea more convenient<br />with our easy to use E-wallet service
                </h3>

                <p class="col-md-9 mb-4">
                    For foreigners traveling to South Korea, we provide secure and fast
                    payment and currency exchange services. From checking live exchange
                    rates to sending money to friends, you can easily access all your
                    financial services in one place.
                </p>
                <!-- 모바일 버전 버튼 -->
                <a href="/login" class="btn mobile-btn col-md-3 mb-4">Get Started</a>
                <!-- 웹 버전 버튼 -->
                <a href="/login" class="btn web-btn col-md-5 mb-4">Get Started</a>

                <!-- 아이콘 섹션 -->
                <div class="icon-item d-flex flex-wrap mb-3">
                    <div class="icon-item col-md-4 col-12 d-flex align-items-center">
                        <div style="position: relative; width: 50px; height: 50px">
                            <img src="/images/main1.png" alt="Hand holding icon" style="
                    position: absolute;
                    top: 50%;
                    left: 50%;
                    transform: translate(-50%, -50%);
                    width: 30px;
                  " />
                        </div>
                        <div class="ms-2 web-btn">
                            <div style="font-size: 0.85rem">Speed &</div>
                            <div style="font-size: 0.85rem">Security</div>

                        </div>
                        <div class="mobile-btn">Speed & Security</div>
                    </div>
                    <div class="icon-item col-md-4 col-12 d-flex align-items-center">
                        <div style="position: relative; width: 50px; height: 50px">
                            <img src="/images/main2.png" alt="Hand holding icon" style="
                    position: absolute;
                    top: 50%;
                    left: 50%;
                    transform: translate(-50%, -50%);
                    width: 30px;
                  " />
                        </div>
                        <div class="ms-2 web-btn">
                            <div style="font-size: 0.85rem">Easy &</div>
                            <div style="font-size: 0.85rem">Simple</div>
                        </div>
                        <div class="mobile-btn">Easy & Simple</div>
                    </div>
                    <div class="icon-item col-md-4 col-12 d-flex align-items-center">
                        <div style="position: relative; width: 50px; height: 50px">
                            <img src="/images/main3.png" alt="Hand holding icon" style="
                    position: absolute;
                    top: 50%;
                    left: 50%;
                    transform: translate(-50%, -50%);
                    width: 30px;
                  " />
                        </div>
                        <div class="ms-2 web-btn">
                            <div style="font-size: 0.85rem">Local &</div>
                            <div style="font-size: 0.85rem">Global</div>
                        </div>
                        <div class="mobile-btn">Local & Global</div>
                    </div>
                </div>
            </div>

            <!-- 오른쪽 섹션 -->
            <div class="right-section-main d-flex justify-content-center mt-2 col-md-6 align-self-end">
                <img class="main-img" src="../../../images/mainpage-1.png" alt="Illustration"
                    style="max-width: 100%; height: auto" />
            </div>
        </div>
    </div>

    <section class="curved-section">
        <svg viewBox="0 0 1440 200" xmlns="http://www.w3.org/2000/svg" style="transform: scaleX(-1)">
            <path fill="#f6f2ef" fill-opacity="1" d="M0,150L1440,90L1440,0L0,0Z"></path>
        </svg>
    </section>

    <div class="section-container-green">

        <!-- 두 번째 섹션 -->
        <div class="row align-items-center">
            <div class="col-md-7">
                <div class="row mb-4">
                    <div class="col-12 mobile-btn">
                        <div class="d-flex flex-column align-items-center text-center">
                            <div class="small-img-container me-2">
                                <img src="/images/quote.png" class="small-img">
                            </div>
                            <h3>
                                Your essential travel partner be your essential travel partner
                            </h3>
                            <div class="small-img-container">
                                <img src="/images/quote-right.png" class="small-img">
                            </div>
                        </div>
                    </div>

                    <div class="col-10 web-btn">
                        <div class="d-flex">
                            <div class="small-img-container me-2">
                                <img src="/images/quote.png" class="small-img">
                            </div>
                            <h3 class="mt-3">
                                Your essential travel partner
                            </h3>
                        </div>
                        <div class="d-flex justify-content-end">
                            <h3 class="text-end">be your essential travel partner</h3>
                            <div class="small-img-container ms-2 mt-2">
                                <img src="/images/quote-right.png" class="small-img">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-5">
                <div class="row justify-content-center">
                    <div class="col">
                        <p>Make travel finance easier, smarter in Korea</p>
                        <p style="text-align: justify;">
                            Simplify your finances Song-E Pay provides foreigners traveling to Korea with
                            real-time exchange rates, automatic currency conversion, automatic
                            currency conversion, and easy money transfer. Exchange money and make
                            payments easily Manage your Pay Money account and bank map service to
                            make your local financial Make your life easier with automatic trip
                            logs based on your payment history Calendar makes it easy to organize
                            your memories.
                        </p>
                    </div>
                </div>
            </div>
        </div>


    </div>


    <div class="section-container-beige">
        <!-- 실시간 환율 조회 섹션 -->
        <div class="d-flex flex-wrap mb-3 gap-4">
            <!-- What We Offer 섹션 -->
            <h2 class="font_2">About Song-E Pay!</h2>
            <div class="col-md-9 mb-4">
                From live exchange rates to automated conversions and alerts, we're
                here to help you find the best deals and make your currency exchange
                quick and easy. Stay on top of currency fluctuations and get the best
                exchange rate for your money.
            </div>

            <div class="row">
                <div class="col-lg-3 col-md-6 pe-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="image-container">
                                <img class="flex_item_image" src="../../../images/image1.png" alt="실시간 환율 조회" />
                            </div>
                        </div>
                    </div>
                    <div class="text-container">
                        <h5 class="font_5">
                            Get live currency exchange rates
                        </h5>
                        <p class="lh-sm text-sm">
                            Get the latest exchange rates anytime before, during, or after your
                            trip, and make the best currency conversions as exchange rates
                            fluctuate.
                        </p>
                    </div>
                    <div class="p-3 mobile-btn"></div>
                </div>

                <div class="col-lg-3 col-md-6 px-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="image-container">
                                <img class="flex_item_image" src="../../../images/image2.png" alt="자동 환전 서비스" />
                            </div>
                        </div>
                    </div>
                    <div class="text-container">
                        <h5 class="font_5">
                            Automatic currency exchange service
                        </h5>
                        <p class="lh-sm text-sm">
                            We'll automatically complete your conversion at the rate you want.
                            Take the guesswork out of currency exchange.
                        </p>
                    </div>
                    <div class="p-3 mobile-btn"></div>
                </div>

                <div class="col-lg-3 col-md-6 px-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="image-container">
                                <img class="flex_item_image" src="../../../images/image3.png" alt="환율 알림 서비스" />
                            </div>
                        </div>
                    </div>
                    <div class="text-container">
                        <h5 class="font_5">
                            Exchange Rate Alert Service
                        </h5>
                        <p class="lh-sm text-sm">
                            Get instant notifications when your desired exchange rate is reached so you can make your
                            conversion at
                            the optimal time.
                        </p>
                    </div>
                    <div class="p-3 mobile-btn"></div>
                </div>

                <div class="col-lg-3 col-md-6 ps-4">
                    <div class="card">
                        <div class="card-body">
                            <div class="image-container">
                                <img class="flex_item_image" src="../../../images/image4.png" alt="No 수수료! 자유로운 환전" />
                            </div>
                        </div>
                    </div>
                    <div class="text-container">
                        <h5 class="font_5">
                            No fees! Free currency exchange
                        </h5>
                        <p class="lh-sm text-sm">
                            We'll give you a Pay Money account to manage the money you exchange. Exchange money anytime,
                            anywhere,
                            without worrying about fees.
                        </p>
                    </div>
                    <div class="p-3 mobile-btn"></div>
                </div>
            </div>
        </div>
    </div>


    <!-- 세 번째 섹션 -->
    <section class="section-container-brown">
        <div class="web-btn">
            <h3 class="font_2 color-light-beige">
                Pay Money account & Send money
            </h3>
            <h3 class="font_2 color-light-beige ms-5">between Korean won-only accounts</h3>
        </div>
        <div class="mobile-btn">
            <h3 class="font_2 color-light-beige text-center mb-4">Pay Money account & Send money between Korean won-only
                accounts</h3>
        </div>

        <div class="d-flex justify-content-between align-items-center flex-column flex-md-row mb-3 color-beige">
            <div class="mesh-left col-md-7 d-flex flex-column px-3">
                <p class="fw-bold fs-5 mb-1">Manage your money in one place!</p>
                <p style="text-align: justify;" class="lh-sm">
                    Manage multiple currencies easily with a Pay Money account. Store your
                    currency conversions and make quick payments and transfers whenever
                    you need them.
                </p>
                <p class="fw-bold fs-5 mb-1">Easily send money in Korean won!</p>
                <p style="text-align: justify;" class="lh-sm">
                    Easily send money to friends locally with a dedicated Korean Won
                    account. No bank account required, all you need is Pay Money to send
                    money quickly and securely with just a few clicks.
                </p>
                <p class="fw-bold fs-5 mb-1">
                    Quick and easy payments and transfers on the go!
                </p>
                <p style="text-align: justify;" class="lh-sm">
                    Smartly manage your currency conversion and have it at your fingertips
                    when you need it. Pay and send money in KRW instantly, even in Korea,
                    without worrying about exchange rates.
                </p>
            </div>
            <div class="mesh-right col-md-5 col-12 d-flex justify-content-end align-items-center">
                <img src="../../../images/Coin.png" alt="송금 이미지" class="img-fluid"
                    style="max-width: 80%; height: auto; object-fit: cover" />
            </div>
        </div>
    </section>
    <!-- 곡선 -->
    <div class="curve-container" style="background-color:#f6f2ef ">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320" class="curve">
            <path fill="#342826" fill-opacity="1"
                d="M0,320L48,288C96,256,192,192,288,165.3C384,139,480,149,576,176C672,203,768,245,864,256C960,267,1056,245,1152,218.7C1248,192,1344,160,1392,144L1440,128L1440,0L1392,0C1344,0,1248,0,1152,0C1056,0,960,0,864,0C768,0,672,0,576,0C480,0,384,0,288,0C192,0,96,0,48,0L0,0Z">
            </path>
        </svg>
    </div>

    <!-- 네 번째 섹션 -->
    <section class="section-container-beige">
        <div class="d-flex justify-content-between align-items-center flex-column flex-md-row">
            <div class="mesh-right col-md-5 col-12 d-flex justify-content-center align-items-center mb-4">
                <img src="../../../images/Map.png" alt="지도 이미지" class="img-fluid"
                    style="max-width: 100%; height: auto" />
            </div>
            <div class="mesh-left col-md-7 d-flex flex-column px-3">
                <h2 class="font_2 mb-4 text-start">Map of bank branches</h2>
                <p>
                    Easily find a Kookmin Bank location near you. <br />
                    Use our map to find your nearest branch and get your financial
                    needs taken care of quickly.
                </p>
            </div>
        </div>
    </section>
    <!-- 다섯 번째 섹션 -->
    <section class="section-container-beige">
        <div class="lastpage d-flex justify-content-between align-items-center flex-column gap-3">
            <div class="d-flex">
                <div class="small-img-container me-2">
                    <img src="/images/quote.png" class="small-img">
                </div>
                <h2 class="text-center font_2">
                    Prepare for Your Trip to Korea <br />With Our Song-E pay
                </h2>
                <div class="small-img-container ms-2">
                    <img src="/images/quote-right.png" class="small-img">
                </div>
            </div>
            <a href="/login" class="btn">get started</a>
        </div>
    </section>
</template>

<script setup>
import { useStore } from "vuex";
import { onBeforeMount } from "vue";

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
    background: #f6f2ef;
    padding-top: 7rem;
    padding-left: 9rem;
    padding-right: 9rem;
    padding-bottom: 6rem;
}

.section-container-green {
    background-color: #fae55f;
    padding-top: 6rem;
    padding-left: 9rem;
    padding-right: 9rem;
    padding-bottom: 6rem;
}

.section-container-brown {
    background-color: #342826;
    color: #f4f2f2;
    padding-top: 9rem;
    padding-left: 9rem;
    padding-right: 9rem;
    padding-bottom: 6rem;
}

.curved-section {
    background-color: #fae55f;
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

.image-container {
    display: flex;
    justify-content: center;
}

.image-container img {
    max-width: 150px;
    /* Adjust the value as needed */
    height: auto;
    /* Maintain aspect ratio */
}

.web-btn {
    display: block;
    /* 웹에서는 보임 */
    max-width: 100%;
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
    color: #342826;
    /* 텍스트 색상 */
    text-align: center;
    text-decoration: none;
    border-radius: 15px;
    /* 모서리 둥글게 */
    font-size: 16px;
}

.btn:hover {
    background-color: #fae55f;
}

.small-img-container {
    position: relative;
    width: 50px;
    height: 50px;
}

.small-img {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 30px;
}

.font_5 {
    font-size: 1rem;
    padding-top: 2rem;
}

.text-container {
    padding-left: 0.5rem;
    padding-right: 0.5rem;
}

/* Mobile styles */
@media (max-width: 768px) {

    /* Adjust the max-width value as needed for your breakpoint */
    .section-container-beige {
        padding-top: 5rem;
        padding-left: 2rem;
        padding-right: 2rem;
        padding-bottom: 5rem;
    }

    .section-container-green {
        padding-left: 2rem;
        padding-right: 2rem;
        padding-bottom: 5rem;
        padding-top: 5rem;
    }

    .section-container-brown {
        padding-left: 2rem;
        padding-right: 2rem;
        padding-bottom: 5rem;
        padding-top: 5rem;
    }

    .mobile-btn {
        display: block;
        /* 모바일에서는 보임 */
        order: 3;
        /*첫 번째로 오도록 설정*/
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
    height: 50px;
    width: auto;
}

.font_2 {
    font-weight: bolder;
    color: #342826;
}


.color-dark-brown {
    color: #342826;
}

.color-brown {
    color: #7b4632;
}

.color-green {
    color: #536438;
}

.color-red {
    color: #d21c23;
}

.color-beige {
    color: #d8c6ba;
}

.color-light-beige {
    color: #f6f2ef;
}

.color-dark-beige {
    color: #8e7f78;
}
</style>