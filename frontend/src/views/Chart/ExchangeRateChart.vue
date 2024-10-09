<template>
  <div class="chart-container">
    <div :id="chartId" ref="chartContainer" class="chart"></div>
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, defineProps, onUnmounted } from "vue";
import * as XLSX from "xlsx";

// Props
const props = defineProps({
  chartId: {
    type: String,
    required: true,
  },
  period: {
    type: String,
    required: true,
  },
  chartType: {
    type: String,
    required: true,
  },
});

// Chart container references
const chartContainer = ref(null);
const errorMessage = ref(null);
let chart = null;
let data = null;
let options = null;

const loadExcelAndDrawChart = (months) => {
  const excelFilePath =
    props.chartType === "to" ? "/csv/USDKRW.xlsx" : "/csv/KRWUSD.xlsx";

  fetch(excelFilePath)
    .then((response) => {
      if (!response.ok) throw new Error("Excel 파일을 찾을 수 없습니다");
      return response.arrayBuffer();
    })
    .then((buffer) => {
      const workbook = XLSX.read(buffer, { type: "array" });
      const sheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[sheetName];
      const jsonData = XLSX.utils.sheet_to_json(worksheet);

      // 현재 날짜와 과거 날짜 계산
      const today = new Date();
      const pastDate = new Date();
      pastDate.setMonth(today.getMonth() - months);

      // 날짜로 데이터 필터링
      const filteredData = jsonData.filter((row) => {
        const rowDate = new Date(row.day);
        return rowDate >= pastDate && rowDate <= today;
      });

      // 차트 데이터 준비
      const chartData = [["날짜", "환율"]];
      filteredData.forEach((row) => {
        chartData.push([row.day, row.exchange_rate]);
      });

      // 구글 차트 그리기
      google.charts.load("current", { packages: ["corechart"] });
      google.charts.setOnLoadCallback(() => {
        data = google.visualization.arrayToDataTable(chartData);

        options = {
          curveType: "function",
          legend: { position: "bottom" },
          backgroundColor: { fill: "transparent" },
          hAxis: {
            textPosition: "none", // x축 라벨 제거
            gridlines: { color: "transparent" }, // x축 눈금선 제거
          },
          chartArea: { width: "80%", height: "70%" }, // 차트 영역 크기 조정
          legend: { position: "none" },
        };

        chart = new google.visualization.LineChart(chartContainer.value);
        drawChart(); // 초기 차트 그리기

        // 창 크기 변경 시 차트 다시 그리기
        window.addEventListener("resize", drawChart);
      });

      errorMessage.value = null; // 이전 오류 메시지 제거
    })
    .catch((error) => {
      console.error("Excel 파일 로딩 오류:", error);
      errorMessage.value =
        error.message || "환율 데이터를 불러오는데 실패했습니다.";
    });
};

// 차트 그리기 함수
const drawChart = () => {
  if (chart && chartContainer.value && data && options) {
    const containerWidth = chartContainer.value.offsetWidth;
    const containerHeight = chartContainer.value.offsetHeight;

    options.width = containerWidth;
    options.height = containerHeight;

    chart.draw(data, options);
  }
};

// 컴포넌트 언마운트 시 이벤트 리스너 제거
onUnmounted(() => {
  window.removeEventListener("resize", drawChart);
});

// period prop 변경 감시
watch(
  () => props.period,
  (newPeriod) => {
    const monthsMap = { "1y": 12, "6m": 6, "3m": 3, "1m": 1 };
    const months = monthsMap[newPeriod] || 12; // 기본값 1년
    loadExcelAndDrawChart(months);
  }
);

// 컴포넌트 마운트 시 실행
onMounted(() => {
  const monthsMap = { "1y": 12, "6m": 6, "3m": 3, "1m": 1 };
  const months = monthsMap[props.period] || 12; // 기본값 1년
  loadExcelAndDrawChart(months);
});
</script>

<style scoped>
.chart-container {
  position: relative;
  width: 100%;
  height: 330px;
}

.chart {
  width: 100%;
  height: 100%;
}

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
