<template>
    <div class='chart-container'>
        <chart height='100%' width='100%'></chart>
    </div>
</template>

<script>
    import Chart from '@/components/Charts/lineMarker'

    export default {
        name: 'lineChart',
        components: { Chart }
    }
</script>

<style scoped>
    .chart-container{
        position: absolute;
        margin-top: -80px;
        margin-left: -40px;
        width: 100%;
        height: 100%;
    }
</style>

