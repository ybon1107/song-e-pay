<script setup>
import { ref, onMounted, computed, watchEffect } from 'vue';
import exchangeReservationApi from '../../api/exchangeReservationApi';
import Swal from 'sweetalert2';
//user
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);
const userId = computed(() => auth.userId);
// const userId = computed(() => auth.user.userId);

//i18n
import { useI18n } from 'vue-i18n';
const { t } = useI18n();

// 자동 환전 내역을 저장할 ref
const autoConditions = ref([]);

// 환율 알림 내역을 저장할 ref
const alertConditions = ref([]);

// 자동 환전 예약 데이터를 가져오는 함수
const fetchAutoExchange = async () => {
    try {
        const response = await exchangeReservationApi.autoExchange(userId.value);
        if (response.status === 200) {
            // 응답 데이터가 존재하면 autoConditions에 저장
            const reservations = response.data;
            autoConditions.value = reservations;
        }
    } catch (error) {
        console.error('자동 환전 데이터를 가져오는 중 오류 발생:', error);
    }
}

// 환율 알림 데이터를 가져오는 함수
const fetchAlertConditions = async () => {
    try {
        const response = await exchangeReservationApi.alertConditions(userId.value);
        if (response.status === 200) {
            // 응답 데이터가 존재하면 alertConditions에 최대 2개 저장
            const alerts = response.data.slice(0, 2); // 최대 2개만 가져옴
            alertConditions.value = alerts;
            console.log('Fetched alert conditions:', alertConditions.value);
            // fetchAlertConditions(); // 알림을 다시 가져와서 업데이트
        }
    } catch (error) {
        console.error('환율 알림 데이터를 가져오는 중 오류 발생:', error);
    }
};

const deleteAlertCondition = async (resNo) => {
    try {
        console.log('삭제할 예약 번호:', resNo);
        if (resNo) {
            const response = await exchangeReservationApi.deleteConditions(resNo);
            if (response.status === 200) {
                Swal.fire({
                    title: t('myAccount--Alert-swal-suceess'),
                    text: t('myAccount--Alert-swal-setSuccess'),
                    icon: 'success',
                });
                fetchAlertConditions(); // 알림을 다시 가져와서 업데이트
                fetchAutoExchange(); // 예약을 다시 가져와서 업데이트
            }
            else {
                throw new Error();
            }
        }
    } catch (error) {
        console.error('알림 설정 삭제 중 오류 발생:', error);
        Swal.fire({
            title: t('myAccount--Alert-swal-fail'),
            text: t('myAccount--error-notificationAlert'),
            icon: 'error',
        });
    }
};
const confirmDelete = (resNo) => {
    Swal.fire({
        title: t('myAccount--Alert-swal-requestDelete'),
        text: t('myAccount--Alert-swal-warningDeleteAlert'),
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: t('myAccount--Alert-swal-deleteAlert'),
        cancelButtonText: t('transaction_states_cancelled'),
        buttonsStyling: true,
    }).then((result) => {
        if (result.isConfirmed) {
            deleteAlertCondition(resNo);
        }
    });
};

watchEffect(() => {
    if (user.value) {
        fetchAutoExchange();
        fetchAlertConditions();
    }
});

</script>

<template>
    <div>
        <span @click="$router.push('/exchange-rate')" class="alert-text me-3 mb-0" role="button" tabindex="0">
            {{ $t('myAccount--header-autoExchange') }}
            <i class="fa-solid fa-angle-right alert-icon"></i>
        </span>
    </div>
    <div class="card">
        <div class="card-body">
            <div class="d-grid gap-3">
                <div>
                    <label for="autoCondition" class="form-label">{{ $t('myAccount--autoExchange-title') }}</label>
                    <ul v-if="autoConditions.length > 0" class="list-group">
                        <li class="list-group-item">
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="d-flex gap-3 flex-column-min">
                                    <div>{{ $t('myAccount--exchangeRate-baseCurrency') }}: {{
                                        autoConditions[0]?.baseCode
                                        }}</div>
                                    <div>{{ $t('myAccount--exchangeRate-targetCurrency') }}: {{
                                        autoConditions[0]?.targetCode }}</div>
                                    <div>{{ $t('myAccount--exchangeRate-targetExchangeRate') }}: {{
                                        autoConditions[0]?.targetExchange
                                        }}
                                    </div>
                                    <div>{{ $t('myAccount--exchangeRate-targetKrw') }}: {{ autoConditions[0]?.targetKrw
                                        }}</div>
                                </div>
                                <div class="cursor-pointer" @click="confirmDelete(autoConditions[0]?.resNo)">
                                    <i class="ni ni-fat-remove text-danger"></i>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <p v-else style="margin-left: 0.5rem">{{ $t('myAccount--autoExchange-noReservationAuto') }}</p>
                </div>

                <div>
                    <label for="alertConditions" class="form-label">{{ $t('myAccount--exchangeRate-settingHistory')
                        }}</label>
                    <ul v-if="alertConditions.length > 0" class="list-group">
                        <li v-for="condition in alertConditions" :key="condition.resNo" class="list-group-item">
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="d-flex gap-3 flex-column-min">
                                    <div>{{ $t('myAccount--exchangeRate-baseCurrency') }}: {{ condition.baseCode }}
                                    </div>
                                    <div>{{ $t('myAccount--exchangeRate-targetCurrency') }}: {{ condition.targetCode }}
                                    </div>
                                    <div>{{ $t('myAccount--exchangeRate-targetExchangeRate') }}: {{
                                        condition.targetExchange }}</div>
                                </div>
                                <div class="cursor-pointer" @click="confirmDelete(condition.resNo)">
                                    <i class="ni ni-fat-remove text-danger"></i>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <p v-if="alertConditions.length === 0" style="margin-left: 0.5rem">{{
                        t('myAccount--exchangeRate-noReservationAlert') }}</p>
                </div>
            </div>
        </div>
    </div>
</template>