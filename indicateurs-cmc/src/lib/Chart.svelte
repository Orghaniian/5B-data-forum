<script lang="ts">
	import 'carbon-components-svelte/css/all.css'
	import { LineChart } from '@carbon/charts-svelte'
	import {
		ChartTheme,
		type LineChartOptions,
		ScaleTypes,
		ToolbarControlTypes,
		ZoomBarTypes
	} from '@carbon/charts/interfaces'
	import fr from 'date-fns/locale/fr'

	export let file

	interface DataEntry {
		group: string
		value: number
		date: string
	}


	const meanData: Array<DataEntry> = file.mean.map((entry) => ({user: "Moyenne", ...entry}))

	const usersData: Array<DataEntry> = Object.keys(file.indicateurs).map(
		(user) => file.indicateurs[user].map((entry) => ({user, ...entry}))
	).flat()


	const data = meanData.concat(usersData)

	const isFullScreenEnabled =
		typeof document !== 'undefined' &&
		(document['fullscreenEnabled'] ||
			document['webkitFullscreenEnabled'] ||
			document['mozFullScreenEnabled'] ||
			document['msFullscreenEnabled']);

	const options: LineChartOptions = {
		title: "Activité par utilisateur",
		axes: {
			bottom: {
				title: "Date",
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
		height: "inherit",
		animations:true,
		theme: ChartTheme.G90,
		resizable: true,
		zoomBar: {
			top: {
				enabled: true,
				type: ZoomBarTypes.GRAPH_VIEW
			},
		},
		timeScale: {
			localeObject: fr
		},
		tooltip: {
			showTotal: false,
			groupLabel: ""
		},
		data: {
			groupMapsTo: "user"
		},
		toolbar: {
			numberOfIcons: 10,
			controls: [
				{
					type: ToolbarControlTypes.ZOOM_OUT
				},
				{
					type: ToolbarControlTypes.RESET_ZOOM
				},
				{
					type: ToolbarControlTypes.ZOOM_IN
				},
				{
					type: ToolbarControlTypes.CUSTOM,
					id: "load",
					clickFunction: () => null,
					text: "Charger des indicateurs"
				},
				{
					type: ToolbarControlTypes.SHOW_AS_DATATABLE,
				},
				...(isFullScreenEnabled
					? [
						{
							type: ToolbarControlTypes.MAKE_FULLSCREEN,
						},
					]
					: []),
				{
					text: "Exporter au format CSV",
					type: ToolbarControlTypes.EXPORT_CSV,
				},
				{
					text: "Exporter au format PNG",
					type: ToolbarControlTypes.EXPORT_PNG,
				},
				{
					text: "Exporter au format JPG",
					type: ToolbarControlTypes.EXPORT_JPG,
				},
			]
		}
	}
</script>

<LineChart
	theme="g90"
	{data}
	{options}
/>
