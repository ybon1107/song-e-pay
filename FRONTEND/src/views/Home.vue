<template>
    <div>
        <base-header class="pb-6 pb-8 pt-5 pt-md-8">
            <b-row>
                <b-col xl="3" md="6">
                    <stats-card title="Total traffic" type="gradient-red" sub-title="350,897" icon="ni ni-active-40"
                        class="mb-4">

                        <template slot="footer">
                            <span class="text-success mr-2">3.48%</span>
                            <span class="text-nowrap">Since last month</span>
                        </template>
                    </stats-card>
                </b-col>
                <b-col xl="3" md="6">
                    <stats-card title="Total traffic" type="gradient-orange" sub-title="2,356" icon="ni ni-chart-pie-35"
                        class="mb-4">

                        <template slot="footer">
                            <span class="text-success mr-2">12.18%</span>
                            <span class="text-nowrap">Since last month</span>
                        </template>
                    </stats-card>
                </b-col>
            </b-row>
        </base-header>
    </div>
</template>

<script>
import StatsCard from '@/components/Cards/StatsCard';
export default {
    components: {
        StatsCard
    }
}
</script>