<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import paymentApi from "@/api/paymentApi";
import Swal from "sweetalert2";

const auth = useAuthStore();
const user = computed(() => auth.user);

const route = useRoute();
const reservationData = ref(null);
const router = useRouter();

onMounted(async () => {
  // jQuery 및 iamport 초기화
  const scriptJQuery = document.createElement("script");
  scriptJQuery.src = "http://code.jquery.com/jquery-latest.min.js";
  document.head.appendChild(scriptJQuery);

  const scriptIamport = document.createElement("script");
  scriptIamport.src = "https://cdn.iamport.kr/js/iamport.payment-1.2.0.js";
  document.head.appendChild(scriptIamport);

  scriptIamport.onload = () => {
    // Iamport 초기화
    window.IMP.init("imp74103424"); // 'iamport'를 가맹점 식별코드로 변경
  };

  reservationData.value = {
    accommodationName: route.query.accommodationName,
    checkInDate: route.query.checkInDate,
    checkOutDate: route.query.checkOutDate,
    guestCount: Number(route.query.guestCount),
    totalPrice: Number(route.query.totalPrice),
  };
});

const requestPay = async () => {
  const paymentData = {
    userId: user.value.userId,
    name: reservationData.value.accommodationName,
    checkInDate: reservationData.value.checkInDate,
    checkOutDate: reservationData.value.checkOutDate,
    amount: reservationData.value.totalPrice,
  };

  const result = await Swal.fire({
    title: "Payment Confirmation",
    text: "Do you want to proceed with the payment?",
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "Yes",
    cancelButtonText: "No",
    reverseButtons: true,
  });

  // if (confirm("결제하시겠습니까?")) {
  if (result.isConfirmed) {
    try {
      const response = await paymentApi.accommodationPayment(paymentData);
      // alert("결제가 성공적으로 완료되었습니다."); // 결제 성공 메시지
      Swal.fire({
        title: "Payment Success",
        text: "Payment has been successfully completed.",
        icon: "success",
        confirmButtonText: "OK",
      }).then(() => {
        router.push({ name: "SchedulePage" });
      });
    } catch (error) {
      console.error("결제 오류:", error);
      // alert(error.response.data || "결제에 실패했습니다. 다시 시도해 주세요."); // 결제 실패 메시지
      Swal.fire({
        title: "Payment Failed",
        text: "Payment failed. Please try again.",
        icon: "error",
        confirmButtonText: "Close",
      });
    }
  } else {
    // alert("결제가 취소되었습니다.");
    Swal.fire({
      title: "Payment Cancelled",
      text: "Payment has been cancelled.",
      icon: "warning",
      confirmButtonText: "Close",
    });
  }
};

const kakaoPay = async () => {
  console.log("Reservation Data:", reservationData.value);
  console.log("User Data:", user.value);
  window.IMP.request_pay(
    {
      pg: "kakaopay", // PG사 설정
      pay_method: "kakaopay", // 결제 방법
      merchant_uid: "merchant_" + new Date().getTime(), // 주문 번호
      name: reservationData.value.accommodationName, // 상품 이름
      amount: reservationData.value.totalPrice, // 결제 가격
      custom_data: {
        begin_date: reservationData.value.checkInDate,
        end_date: reservationData.value.checkOutDate,
      },
      buyer_email: user.value.userId,
      buyer_name: user.value.firstName + user.value.lastName, // 구매자 이름
      buyer_tel: user.value.phoneNo, // 구매자 연락처
      // buyer_addr: '서울특별시 강남구 삼성동', // 구매자 주소
      // buyer_postcode: '123-456' // 구매자 우편번호
    },
    function (response) {
      if (response.success) {
        console.log("결제 성공", response);
        paymentApi.accommodationKakaoPayment(response);
      } else {
        // 결제 실패 시 로직
        console.log("결제 실패", response);
      }
    }
  );
};

const formatDate = (dateString) => {
  const options = {
    year: "numeric",
    month: "long",
    day: "numeric",
    locale: "en-US",
  }; // 영어 포맷으로 변경
  return new Date(dateString).toLocaleDateString("en-US", options);
};

const formatPrice = (price) => {
  return new Intl.NumberFormat("ko-KR", {
    style: "currency",
    currency: "KRW",
  }).format(price);
};

const goBack = () => {
  router.go(-1); // 이전 페이지로 이동
};
</script>

<template>
  <div class="container mt-5">
    <h1 class="mb-4">Payment Information</h1>
    <div v-if="reservationData" class="card">
      <div class="card-body">
        <h5 class="card-title">
          <span class="arrow" @click="goBack">&lt;</span> Reservation Details
        </h5>
        <ul class="list-group list-group-flush">
          <li class="list-group-item">
            <strong>Accommodation Name:</strong>
            {{ reservationData.accommodationName }}
          </li>
          <li class="list-group-item">
            <strong>Check-in Date:</strong>
            {{ formatDate(reservationData.checkInDate) }}
          </li>
          <li class="list-group-item">
            <strong>Check-out Date:</strong>
            {{ formatDate(reservationData.checkOutDate) }}
          </li>
          <li class="list-group-item">
            <strong>Guest Count:</strong>
            {{ reservationData.guestCount }} guests
          </li>
          <li class="list-group-item">
            <strong>Total Price:</strong>
            {{ formatPrice(reservationData.totalPrice) }}
          </li>
        </ul>
        <!-- 결제 유저 정보 추가 -->
        <h5 class="mt-4">User Information</h5>
        <ul class="list-group list-group-flush">
          <li class="list-group-item">
            <strong>Name:</strong> {{ user.firstName }} {{ user.lastName }}
            <!-- 유저 이름 -->
          </li>
          <li class="list-group-item">
            <strong>Email:</strong> {{ user.userId }}
            <!-- 유저 이메일 -->
          </li>
          <li class="list-group-item">
            <strong>Phone:</strong> {{ user.phoneNo }}
            <!-- 유저 전화번호 -->
          </li>
        </ul>
      </div>
    </div>
    <div v-else class="alert alert-danger mt-3">
      Failed to load reservation information.
    </div>
    <div class="card-footer bg-transparent border-0">
      <button class="btn btn-primary mt-3" @click="requestPay">Payment</button>
      <button class="btn btn-primary mt-3" @click="kakaoPay">
        Pay with Kakao
      </button>
    </div>
  </div>
</template>

<style scoped>
.card,
.card-footer {
  max-width: 500px;
  margin: 0 auto;
}

.card-footer {
  padding: 0;
}

.btn {
  width: 100%;
}

.arrow {
  margin-right: 0.5em;
  cursor: pointer;
}

.card-title {
  font-size: 1.5em;
}
</style>
