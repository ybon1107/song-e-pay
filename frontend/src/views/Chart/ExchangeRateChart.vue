<template>
  <div class="chart-container">
    <canvas :id="chartId" ref="chartContainer"></canvas>
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, defineProps } from "vue";
import * as XLSX from "xlsx";
import { Chart, registerables } from "chart.js/auto";

// Register Chart.js components
Chart.register(...registerables);

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
let chartInstance = null;

const loadExcelAndDrawChart = (months) => {
  const excelFilePath =
    props.chartType === "to" ? "/csv/USDKRW.xlsx" : "/csv/KRWUSD.xlsx";

  fetch(excelFilePath)
    .then((response) => {
      if (!response.ok) throw new Error("Excel file not found");
      return response.arrayBuffer();
    })
    .then((buffer) => {
      const workbook = XLSX.read(buffer, { type: "array" });
      const sheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[sheetName];
      const jsonData = XLSX.utils.sheet_to_json(worksheet);

      // Get current date and calculate past date
      const today = new Date();
      const pastDate = new Date();
      pastDate.setMonth(today.getMonth() - months);

      // Filter data by date
      const filteredData = jsonData.filter((row) => {
        const rowDate = new Date(row.day);
        return rowDate >= pastDate && rowDate <= today;
      });

      // Prepare data for the chart
      const labels = filteredData.map((row) => row.day);
      const exchangeRates = filteredData.map((row) => row.exchange_rate);

      // Clear existing chart if it exists
      if (chartInstance) {
        chartInstance.destroy();
      }

      // Draw the chart
      const ctx = chartContainer.value.getContext("2d");
      const chartData = {
        labels: labels,
        datasets: [
          {
            label: props.chartType === "to" ? "USD to KRW" : "KRW to USD",
            data: exchangeRates,
            borderColor: "rgba(75, 192, 192, 1)",
            backgroundColor: "rgba(75, 192, 192, 0.2)",
            borderWidth: 1,
          },
        ],
      };

      const chartOptions = {
        responsive: true,
        scales: {
          x: {
            beginAtZero: false,
            ticks: {
              display: false, // x축 레이블 숨기기
            },
          },
          y: { beginAtZero: false },
        },
      };

      chartInstance = new Chart(ctx, {
        type: "line",
        data: chartData,
        options: chartOptions,
      });

      errorMessage.value = null; // Clear any previous error
    })
    .catch((error) => {
      console.error("Error loading Excel file:", error);
      errorMessage.value =
        error.message || "Failed to load exchange rate data.";
    });
};

// Watch for changes in the period prop
watch(
  () => props.period,
  (newPeriod) => {
    const monthsMap = { "1y": 12, "6m": 6, "3m": 3, "1m": 1 };
    const months = monthsMap[newPeriod] || 12; // Default to 1 year if not found
    loadExcelAndDrawChart(months);
  }
);

// Mounted lifecycle hook
onMounted(() => {
  const monthsMap = { "1y": 12, "6m": 6, "3m": 3, "1m": 1 };
  const months = monthsMap[props.period] || 12; // Default to 1 year if not found
  loadExcelAndDrawChart(months);
});
</script>

<style scoped>
.chart-container {
  position: relative;
  width: 100%;
}

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
