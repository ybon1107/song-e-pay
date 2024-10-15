<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Chart from 'chart.js/auto';
import adminApi from "@/api/adminApi";

const statistics = ref(null);
const detailedStats = ref({});
let genderChart = null;
let ageChart = null;

const fetchData = async () => {
    try {
        const response = await adminApi.getStatistics();
        console.log(response);
        statistics.value = response.data;
        processData();
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};

const processData = () => {
    const allGenderData = statistics.value.gender.find(item => item.country_code === "all");
    detailedStats.value = {
        'Total Users': allGenderData.total_users,
        'Women': allGenderData.woman,
        'Men': allGenderData.man,
    };

    statistics.value.age.all.forEach(item => {
        detailedStats.value[`Age ${item.age_group.replace('age_', '').replace('_', '-')}`] = item.user_count;
    });
};

const initCharts = () => {
    initGenderChart();
    initAgeChart();
};

const initGenderChart = () => {
    const ctx = document.getElementById('genderChart');
    const genderData = statistics.value.gender.find(item => item.country_code === "all");

    genderChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Women', 'Men'],
            datasets: [{
                data: [genderData.woman, genderData.man],
                backgroundColor: ['#FF6384', '#36A2EB'],
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Gender Distribution'
                }
            }
        }
    });
};

const initAgeChart = () => {
    const ctx = document.getElementById('ageChart');
    const ageData = statistics.value.age.all;

    ageChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ageData.map(item => item.age_group.replace('age_', '').replace('_', '-')),
            datasets: [{
                label: 'User Count',
                data: ageData.map(item => item.user_count),
                backgroundColor: '#4BC0C0',
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Age Distribution'
                }
            }
        }
    });
};

onMounted(async () => {
    await fetchData();
    initCharts();
});
</script>

<template>
    <div class="container mt-5">
        <h1 class="mb-4">User Statistics Dashboard</h1>

        <div class="row">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Gender Distribution</h5>
                        <div class="chart-container">
                            <canvas id="genderChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Age Distribution</h5>
                        <div class="chart-container">
                            <canvas id="ageChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Detailed Statistics</h5>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Category</th>
                                    <th>Value</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(value, key) in detailedStats" :key="key">
                                    <td>{{ key }}</td>
                                    <td>{{ value }}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.chart-container {
    position: relative;
    height: 300px;
    /* 원하는 높이로 조정하세요 */
    width: 100%;
    display: flex;
    justify-content: center;
}
</style>