<script setup>
import ArgonInput from '@/components/templates/ArgonInput.vue';
import DefaultInfoCard from '@/views/Cards/AccountsCard.vue';
import ArgonAmountInput from '@/components/yb_templates/ArgonAmountInput.vue';
import ArgonButton from '@/components/templates/ArgonButton.vue';
import SecondPassword from '@/views/MyAccounts/SecondPassword.vue';
import { ref, onMounted, computed, nextTick, watch } from 'vue';
import myaccountApi from '../../api/myaccountApi';

const emit = defineEmits(['password-verified', 'close']);

const selectedAsset = ref('Song-E Money'); // 기본적으로 Song-E Money가 선택됨
const activeTab = ref('deposit'); // 기본적으로 충전 탭이 선택됨
const depositAmount = ref('');
const exchangeAmount = ref('');
const refundAmount = ref('');
const transferAmount = ref('');
const reExchangeAmount = ref('');
const customerunit = ref('USD'); //나라 설정에 따라 변경되게끔
const songEMoneyBalance = ref(0); // Song-E Money의 잔액
const wonEMoneyBalance = ref(0); // Won-E Money의 잔액
const sendEmail = ref('');
const sendEmailConfirm = ref('');
const formattedSongEMoneyBalance = computed(
    () =>
        `${customerunit.value} ${formatNumber(songEMoneyBalance.value.toFixed(2))}`
);
const formattedWonEMoneyBalance = computed(
    () => `KRW ${formatNumber(wonEMoneyBalance.value.toFixed(2))}`
);
const ecRate = ref(1); // To store the fetched exchange rate
const showModal = ref(false);
let currentAction = ref('');
onMounted(() => {
    fetchBalances();
});

const fetchBalances = () => {
    myaccountApi.fetchkrwAccountBalance('1234').then((balance) => {
        wonEMoneyBalance.value = balance;
    });

    myaccountApi.fetchsongeAccountBalance('1234').then((balance) => {
        songEMoneyBalance.value = balance;
    });
};
const isValidAmount = (amount) => {
    return amount && !isNaN(amount) && parseFloat(amount) > 0;
};

const openModal = () => {
    showModal.value = true;
    currentAction.value = activeTab.value;
};

const closeModal = () => {
    showModal.value = false;
};

// 비밀번호가 확인되었을 때 호출되는 함수
const handlePasswordVerified = async () => {
    showModal.value = false; // 모달 숨김
    switch (currentAction.value) {
        case 'deposit':
            await deposit(); // deposit이 완료될 때까지 기다림
            await fetchBalances(); // 잔액을 다시 가져옴
            break;
        case 'exchange':
            await exchange(); // exchange가 완료될 때까지 기다림
            await fetchBalances();
            break;
        case 'refund':
            await refund(); // refund가 완료될 때까지 기다림
            await fetchBalances();
            break;
        case 'transfer':
            await transfer(); // transfer가 완료될 때까지 기다림
            await fetchBalances();
            break;
        case 'reExchange':
            await reExchange(); // reExchange가 완료될 때까지 기다림
            await fetchBalances();
            break;
    }
};

const formatNumber = (num) => {
    // 입력값이 숫자이면 포맷, 아니면 그대로 반환
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

let processAfterBalance = computed(() => {
    let balance = 0;
    // 충전일 경우 잔액 증가
    if (activeTab.value === 'deposit') {
        balance =
            songEMoneyBalance.value + parseFloat(depositAmount.value || 0);
    }
    // 환전일 경우 잔액 감소
    if (activeTab.value === 'exchange') {
        balance =
            songEMoneyBalance.value - parseFloat(exchangeAmount.value || 0);
    }
    // 환불일 경우 잔액 감소
    else if (activeTab.value === 'refund') {
        balance = songEMoneyBalance.value - parseFloat(refundAmount.value || 0);
    }

    // 계산된 숫자를 포맷하여 반환
    return formatNumber(balance.toFixed(2)); // 소수점 두 자릿수까지 표시
});

let processAfterWonBalance = computed(() => {
    let wonBalance = 0;

    // 충전일 경우 잔액 증가
    if (activeTab.value === 'transfer') {
        wonBalance =
            wonEMoneyBalance.value - parseFloat(transferAmount.value || 0);
    }

    // 환급일 경우 잔액 감소
    else if (activeTab.value === 'reExchange') {
        wonBalance =
            wonEMoneyBalance.value - parseFloat(reExchangeAmount.value || 0);
    }

    // 계산된 숫자를 포맷하여 반환
    return formatNumber(wonBalance.toFixed(2)); // 소수점 두 자릿수까지 표시
});

// 자산 선택
const selectAsset = (asset) => {
    selectedAsset.value = asset;
    // Song-E Money 선택 시 기본 탭을 deposit로 설정, Won-E Money 선택 시 기본 탭을 transfer로 설정
    activeTab.value = asset === 'Song-E Money' ? 'deposit' : 'transfer';
};

// 충전 함수 정의
const deposit = async () => {
    const userNo = '1';
    const accountNo = '123'; // 실제 계좌 번호 사용
    const songNo = '1234'; // 송이 페이 계좌 번호
    const krwNo = '1234';
    const amount = depositAmount.value; // 충전하려는 금액
    // 송이 페이머니 충전 요청
    const response = await myaccountApi.deposit({
        amount,
        accountDTO: {
            accountNo,
        },
        songAccountDTO: {
            songNo,
        },
        historyDTO: {
            userNo,
            songNo,
            krwNo,
            typeCode: 3, //거래 코드 충전 3
            stateCode: 1,
            historyContent: 'SongE money 충전',
            amount,
        },
    });

    if (response != '') {
        console.log(response);
    }

    // 충전 후 입력 초기화
    depositAmount.value = ''; // 충전 금액 초기화
};

// 환전 처리
const exchange = async () => {
    const userNo = '1';
    const krwNo = '1234'; // krw 계좌 번호 사용
    const songNo = '1234'; // 송이 페이 계좌 번호
    const exchangeRate = ecRate.value;
    const amount = exchangeAmount.value; // 환전하려는 금액
    const response = await myaccountApi.exchange({
        amount,
        exchangeRate,
        songAccountDTO: {
            songNo,
        },
        krwAccountDTO: {
            krwNo,
        },
        historyDTO: {
            userNo,
            songNo,
            krwNo,
            typeCode: 5,
            stateCode: 1,
            historyContent: 'SongE money -> WonE money',
            amount,
            exchangeRate,
        },
    });

    if (response != '') {
        console.log(response);
    }

    exchangeAmount.value = ''; // 환전 후 입력 초기화
};

// 환불 처리
const refund = async () => {
    const userNo = '1';
    const accountNo = '123'; // 실제 계좌 번호 사용
    const songNo = '1234'; // 송이 페이 계좌 번호
    const krwNo = '1234'; // krw 계좌 번호 사용
    const amount = refundAmount.value; // 충전하려는 금액
    // 송이 페이머니 충전 요청
    const response = await myaccountApi.refund({
        amount,
        accountDTO: {
            accountNo,
        },
        songAccountDTO: {
            songNo,
        },
        historyDTO: {
            userNo,
            songNo,
            krwNo,
            typeCode: 4,
            stateCode: 1,
            historyContent: 'SongE money -> My Account',
            amount,
        },
    });

    if (response != '') {
        console.log(response);
    }
    refundAmount.value = ''; // 환불 후 입력 초기화
};

// 송금 처리
const transfer = async () => {
    const userNo = '1';
    const songNo = '1234'; // 송이 페이 계좌 번호
    const krwNo = '1234'; // krw 계좌 번호 사용
    const target_krwNo = '1234'; // 보내는 사람 페이 계좌 번호
    const amount = transferAmount.value; // 충전하려는 금액
    // 송이 페이머니 충전 요청
    const response = await myaccountApi.transfer({
        amount,
        target_krwNo,
        krwAccountDTO: {
            krwNo,
        },
        historyDTO: {
            userNo,
            songNo,
            krwNo,
            typeCode: 2, //거래 코드 충전 2
            stateCode: 1,
            historyContent: `나의 WonE money -> ${sendEmail}`,
            amount,
        },
    });

    if (response != '') {
        console.log(response);
    }
    transferAmount.value = ''; // 송금 후 입력 초기화
};

// 환급 처리
const reExchange = async () => {
    const userNo = '1';
    const krwNo = '1234'; // krw 계좌 번호 사용
    const songNo = '1234'; // 송이 페이 계좌 번호
    const exchangeRate = ecRate.value;
    const amount = reExchangeAmount.value; // 환급하려는 금액
    const response = await myaccountApi.reExchange({
        amount,
        exchangeRate,
        krwAccountDTO: {
            krwNo,
        },
        songAccountDTO: {
            songNo,
        },
        historyDTO: {
            userNo,
            songNo,
            krwNo,
            typeCode: 6,
            stateCode: 1,
            historyContent: 'WonE money -> SongE money',
            amount,
            exchangeRate,
        },
    });

    if (response != '') {
        console.log(response);
    }
    reExchangeAmount.value = ''; // 환급 후 입력 초기화
};

const emailConfirm = () => {
    //이메일 확인 버튼 눌렀을 시 회원 이메일 혹은 비회원 이메일 뜨게 하기
};

// 받는 금액 계산
const receivedAmount = computed(() => {
    if (selectedAsset.value === 'Song-E Money') {
        return ((exchangeAmount.value * 1) / ecRate.value).toFixed(2);
    } else if (selectedAsset.value === 'Won-E Money') {
        return ((reExchangeAmount.value * 1) / ecRate.value).toFixed(2);
    }
});
</script>

<template>
    <div class="container-fluid" style="width: 80%">
        <h1>My account</h1>
        <SecondPassword
            v-if="showModal"
            @close="closeModal"
            @password-verified="handlePasswordVerified"
        />
        <div class="assets-list">
            <!-- Song-E Money 카드 -->
            <DefaultInfoCard
                title="Song-E Money"
                :value="formattedSongEMoneyBalance"
                img-src="/images/song-e-money.png"
                img="/images/america.png"
                @click="selectAsset('Song-E Money')"
                :class="{ selected: selectedAsset === 'Song-E Money' }"
            />

            <!-- Won-E Money 카드 -->
            <DefaultInfoCard
                title="Won-E Money"
                :value="formattedWonEMoneyBalance"
                img-src="images/won-e-money.png"
                img="/images/korea.png"
                @click="selectAsset('Won-E Money')"
                :class="{ selected: selectedAsset === 'Won-E Money' }"
            />
        </div>

        <div class="card">
            <!-- Song-E Money 선택 시 -->
            <template v-if="selectedAsset === 'Song-E Money'">
                <nav class="nav flex-column flex-sm-row">
                    <a
                        class="flex-sm-fill text-sm-center nav-link"
                        :class="{ active: activeTab === 'deposit' }"
                        @click="activeTab = 'deposit'"
                        aria-current="page"
                    >
                        충전
                    </a>
                    <a
                        class="flex-sm-fill text-sm-center nav-link"
                        :class="{ active: activeTab === 'exchange' }"
                        @click="activeTab = 'exchange'"
                    >
                        환전
                    </a>
                    <a
                        class="flex-sm-fill text-sm-center nav-link"
                        :class="{ active: activeTab === 'refund' }"
                        @click="activeTab = 'refund'"
                    >
                        환불
                    </a>
                </nav>
            </template>

            <!-- Won-E Money 선택 시 -->
            <template v-if="selectedAsset === 'Won-E Money'">
                <nav class="nav flex-column flex-sm-row">
                    <a
                        class="flex-sm-fill text-sm-center nav-link"
                        :class="{ active: activeTab === 'transfer' }"
                        @click="activeTab = 'transfer'"
                        aria-current="page"
                    >
                        송금
                    </a>
                    <a
                        class="flex-sm-fill text-sm-center nav-link"
                        :class="{ active: activeTab === 'reExchange' }"
                        @click="activeTab = 'reExchange'"
                    >
                        환급
                    </a>
                </nav>
            </template>

            <!-- Song-E Money의 탭 내용 -->
            <div class="card-body" v-if="selectedAsset === 'Song-E Money'">
                <div
                    v-if="activeTab === 'deposit'"
                    class="tab-pane fade show active"
                >
                    <p>충전 금액</p>
                    <ArgonAmountInput
                        v-model="depositAmount"
                        placeholder="금액을 입력하세요"
                        :unit="customerunit"
                    />
                    <p>
                        충전계좌:
                        {{
                            selectedAsset === 'Song-E Money'
                                ? '내 계좌'
                                : 'KRW 계좌'
                        }}
                    </p>
                    <p>
                        거래 후 잔액: {{ processAfterBalance }}
                        {{ customerunit }}
                    </p>
                    <argon-button
                        type="submit"
                        color="success"
                        size="lg"
                        class="w-100"
                        @click="openModal"
                        :disabled="!isValidAmount(depositAmount)"
                        >충전하기</argon-button
                    >
                </div>

                <div v-if="activeTab === 'exchange'">
                    <p>현재 환율</p>
                    <p>1KRW = {{ ecRate }} {{ customerunit }}</p>
                    <p>보내는 금액</p>
                    <ArgonAmountInput
                        v-model="exchangeAmount"
                        placeholder="얼마를 환전할까요?"
                        :unit="customerunit"
                        :selectedAsset="selectedAsset"
                        :songEMoneyBalance="songEMoneyBalance"
                        :activeTab="activeTab"
                    />
                    <p>받는 금액</p>
                    <p>{{ receivedAmount }} KRW</p>
                    <p>
                        환급계좌:
                        {{
                            selectedAsset === 'Song-E Money'
                                ? '내 계좌'
                                : 'KRW 계좌'
                        }}
                    </p>
                    <p>
                        거래 후 잔액: {{ processAfterBalance }}
                        {{ customerunit }}
                    </p>
                    <argon-button
                        type="submit"
                        color="success"
                        size="lg"
                        class="w-100"
                        @click="openModal"
                        :disabled="!isValidAmount(exchangeAmount)"
                        >환전하기</argon-button
                    >
                </div>

                <div v-if="activeTab === 'refund'">
                    <p>환불할 금액을 입력하세요</p>
                    <ArgonAmountInput
                        v-model="refundAmount"
                        placeholder="얼마를 환불할까요?"
                        :unit="customerunit"
                        :selectedAsset="selectedAsset"
                        :songEMoneyBalance="songEMoneyBalance"
                        :activeTab="activeTab"
                    />
                    <p>
                        환불계좌:
                        {{
                            selectedAsset === 'Song-E Money'
                                ? '내 계좌'
                                : 'KRW 계좌'
                        }}
                    </p>
                    <p>
                        거래 후 잔액: {{ processAfterBalance }}
                        {{ customerunit }}
                    </p>
                    <argon-button
                        type="submit"
                        color="success"
                        size="lg"
                        class="w-100"
                        @click="openModal"
                        :disabled="!isValidAmount(refundAmount)"
                        >환불하기</argon-button
                    >
                </div>
            </div>

            <!-- Won-E Money의 탭 내용 -->
            <div class="card-body" v-if="selectedAsset === 'Won-E Money'">
                <div v-if="activeTab === 'transfer'">
                    <div class="text-btn">
                        <p style="margin-top: 1rem; margin-bottom: 1rem">
                            보낼 이메일
                        </p>
                        <button class="action-btn2" @click="emailConfirm">
                            이메일 확인하기
                        </button>
                    </div>
                    <ArgonInput
                        v-model="sendEmail"
                        placeholder="받는 분의 이메일을 입력하세요"
                    />
                    <p>이메일 확인</p>
                    <ArgonInput
                        v-model="sendEmailConfirm"
                        placeholder="이메일을 다시 입력하세요"
                    />
                    <p>송금할 금액을 입력하세요</p>
                    <ArgonAmountInput
                        v-model="transferAmount"
                        placeholder="얼마를 송금할까요?"
                        unit="KRW"
                        :selectedAsset="selectedAsset"
                        :wonEMoneyBalance="wonEMoneyBalance"
                        :activeTab="activeTab"
                    />
                    <p>송금 후 잔액: {{ processAfterWonBalance }} KRW</p>
                    <argon-button
                        type="submit"
                        color="success"
                        size="lg"
                        class="w-100"
                        @click="openModal"
                        :disabled="!isValidAmount(transferAmount)"
                        >송금하기</argon-button
                    >
                </div>

                <div v-if="activeTab === 'reExchange'">
                    <p>현재 환율</p>
                    <p>1{{ customerunit }} = {{ ecRate }} KRW</p>
                    <p>보내는 금액</p>
                    <ArgonAmountInput
                        v-model="reExchangeAmount"
                        placeholder="얼마를 환급할까요?"
                        unit="KRW"
                        :selectedAsset="selectedAsset"
                        :wonEMoneyBalance="wonEMoneyBalance"
                        :activeTab="activeTab"
                    />
                    <p>받는 금액</p>
                    <p>{{ receivedAmount }} USD</p>
                    <p>거래 후 잔액: {{ processAfterWonBalance }} KRW</p>
                    <argon-button
                        type="submit"
                        color="success"
                        size="lg"
                        class="w-100"
                        @click="openModal"
                        :disabled="!isValidAmount(reExchangeAmount)"
                        >환급하기</argon-button
                    >
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* 기본 탭 스타일 */
.nav-link {
    cursor: pointer;
    color: #555;
    padding: 10px 15px;
    border-radius: 4px;
    transition: background-color 0.3s ease;
}

/* 탭 활성화 스타일 */
.nav-link.active {
    background-color: #2cce89;
    color: white;
}

/* 비활성화된 탭 스타일 */
.nav-link:hover {
    background-color: #e9ecef;
}

/* 기타 스타일 */
.assets-list {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    width: 100%; /* 카드를 가로로 전체 너비에 맞추기 */
    margin-bottom: 20px; /* 탭과의 간격 설정 */
}

.text-btn {
    display: flex;
    align-items: center; /* 세로 중앙 정렬 */
    gap: 10px; /* p 태그와 버튼 사이의 간격을 설정 (필요에 따라 조정) */
}
</style>
