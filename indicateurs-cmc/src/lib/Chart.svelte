<script lang="ts">
	import 'carbon-components-svelte/css/all.css'
	import { LineChart } from '@carbon/charts-svelte'
	import { open } from '@tauri-apps/api/dialog'
	import { readTextFile } from '@tauri-apps/api/fs'

	import {
		ChartTheme,
		type LineChartOptions,
		ScaleTypes,
		ToolbarControlTypes,
		ZoomBarTypes
	} from '@carbon/charts/interfaces'
	// import {
	// 	ContextMenu,
	// 	ContextMenuDivider,
	// 	ContextMenuGroup,
	// 	ContextMenuOption,
	// } from "carbon-components-svelte"
	// import ZoomIn from "carbon-icons-svelte/lib/ZoomIn.svelte";
	import fr from 'date-fns/locale/fr'
	import { type Data, parseData } from '$lib/indicateurs'
	import type ChartDataEntry from '$lib/ChartDataEntry'

	async function loadFile(): Promise<ChartDataEntry[]> {
		open({title: "Indicateurs", filters: [{name: "json", extensions: ["json"]}]}).then(async (filePath) => {
			if(typeof filePath !== "string") return console.log("Aucun fichier sélectionné");
			const json = await readTextFile(filePath)
			console.log(parseData(JSON.parse(json)).length)
			return parseData(JSON.parse(json))
		})
	}

	const isFullScreenEnabled =
		typeof document !== 'undefined' &&
		(document['fullscreenEnabled'] ||
			document['webkitFullscreenEnabled'] ||
			document['mozFullScreenEnabled'] ||
			document['msFullscreenEnabled']);

	export let file: Data

	let data: ChartDataEntry[] = parseData(file)

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
					clickFunction: async () => data = await loadFile(),
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

<!--TODO fix load new data-->
<LineChart
	theme="g90"
	{data}
	{options}
/>

<!--<ContextMenu>-->
<!--	<ContextMenuOption-->
<!--		indented-->
<!--		labelText="Zoom +"-->
<!--		icon={ZoomIn}-->
<!--	/>-->
<!--	<ContextMenuOption indented kind="danger" labelText="Delete" />-->
<!--</ContextMenu>-->



