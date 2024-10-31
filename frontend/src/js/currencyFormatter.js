
import { COUNTRY_CODE } from '@/constants/countryCode';


// 숫자 포맷팅 함수 추가
const formatNumber = (num) => {
    if (!num) return '';
    // 입력값이 숫자이면 포맷, 아니면 그대로 반환
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};

const reFormatNumber = (num) => {
    if (num !== null) {
        return num.toString().replace(/[^\d.]/g, '');
    }
    return num;
}

const exchangeCurrency = (amount, rate) => {
    // const num = reFormatNumber(amount);
    return Math.round(amount * rate);
}
// const exchangeCurrencyRevert = (amount, rate) => {
//     console.log(amount, rate);
//     console.log(Math.floor(amount / rate));
//     return Math.floor(amount / rate);
// }

// 통화 포맷팅 함수
// const formatCurrency = (value, locale, currency) => {
//     return new Intl.NumberFormat(locale, {
//         style: 'currency',
//         currency: currency,
//         currencyDisplay: 'code'
//     }).format(value);
// };


export default {
    formatNumber,
    reFormatNumber,
    exchangeCurrency,
    // exchangeCurrencyRevert
}