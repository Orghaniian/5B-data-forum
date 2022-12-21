<script lang="ts">
	import 'carbon-components-svelte/css/all.css'
	import { LineChart } from '@carbon/charts-svelte'
	import type { LineChartOptions } from '@carbon/charts/interfaces'
	import { ChartTheme, ScaleTypes, ZoomBarTypes } from '@carbon/charts/interfaces'

	export let file

	interface DataEntry {
		group: string
		value: number
		date: string
	}


	const meanData: Array<DataEntry> = file.mean.map((entry) => ({group: "mean", ...entry}))

	const usersData: Array<DataEntry> = Object.keys(file.indicateurs).map(
		(group) => file.indicateurs[group].map((entry) => ({group, ...entry}))
	).flat()


	const data = meanData.concat(usersData)


	const options: LineChartOptions = {
		title: "Activité par utilisateur",
		axes: {
			bottom: {
				title: "Temps",
				mapsTo: "date",
				scaleType: ScaleTypes.TIME
			},
			left: {
				title: "Activité",
				mapsTo: "value",
				scaleType: ScaleTypes.LINEAR
			}
		},
		curve: "curveMonotoneX",
		height: "600px",
		animations:true,
		theme: ChartTheme.G90,
		resizable: true,
		zoomBar: {
			top: {
				enabled: true,
				type: ZoomBarTypes.GRAPH_VIEW
			},
		}
	}

	console.log(data)
</script>

<LineChart
	theme="g90"
	{data}
	{options}
/>
