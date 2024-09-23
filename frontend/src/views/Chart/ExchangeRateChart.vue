<template>
  <div class="chart-container">
    <canvas :id="chartId" ref="chartContainer"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
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
});

// Chart container references
const chartContainer = ref(null);

// Function to load Excel and draw chart
const loadExcelAndDrawChart = (chartType) => {
  const excelFilePath =
    chartType === "to" ? "/csv/USDKRW.xlsx" : "/csv/KRWUSD.xlsx"; // File path based on chart type

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

      // Map data for chart
      const labels = jsonData.map((row) => row["day"]);
      const exchangeRates = jsonData.map((row) => row["exchange_rate"]);

      // Get chart context
      const ctx = chartContainer.value.getContext("2d");

      // Initialize Chart
      new Chart(ctx, {
        type: "line",
        data: {
          labels: labels,
          datasets: [
            {
              label:
                chartType === "to"
                  ? "1-Year Exchange Rate (USD/KRW)"
                  : "1-Year Exchange Rate (KRW/USD)",
              data: exchangeRates,
              borderColor: "rgba(75, 192, 192, 1)",
              backgroundColor: "rgba(75, 192, 192, 0.2)",
              borderWidth: 1,
            },
          ],
        },
        options: {
          responsive: true,
          scales: {
            x: {
              ticks: {
                display: false, // Hide x-axis labels
              },
            },
            y: {
              beginAtZero: false,
            },
          },
        },
      });
    })
    .catch((error) => {
      console.error("Error loading Excel file:", error);
    });
};

// On component mount
onMounted(() => {
  // Draw chart based on the chart type passed through props
  loadExcelAndDrawChart(props.chartId.includes("to") ? "to" : "from");
});
</script>

<style scoped>
.chart-container {
  position: relative;
  /* height: 40vh; */
  width: 100%;
}
</style>
