<script setup>
import { defineProps } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps({
    no: {
        type: Number,
        required: true
    },
    name: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    thumbnail: {
        type: String,
        required: true
    },
});

const goToAccommodationDetail = () => {
  router.push({ name: 'Accommodation', params: { no: props.no } });
};
</script>

<template>
    <div class="card">
        <img :src="thumbnail" class="card-img-top" :alt="name">
        <div class="card-body" @click="goToAccommodationDetail">
            <div class="card-title">
                <h5>{{ name }}</h5>
            </div>
            <div class="card-text">
                <p>{{ description }}</p>
            </div>
        </div>
    </div>
</template>

<style scoped>
.card {
    cursor: pointer;
}
</style>
