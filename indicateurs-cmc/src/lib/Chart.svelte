<script lang="ts">
	import 'carbon-components-svelte/css/all.css'
	import { LineChart } from '@carbon/charts-svelte'
	import { open } from '@tauri-apps/api/dialog'
	import { readTextFile } from '@tauri-apps/api/fs'

	import {
		ChartTheme,
		type LineChartOptions,
		ScaleTypes,
		TickRotations,
		ToolbarControlTypes,
		type ToolbarOptions,
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
	import type DateThreshold from '$lib/DateThreshold'
	import { Button, FluidForm, TextInput } from 'carbon-components-svelte'
	import { DateInput, type Locale, localeFromDateFnsLocale } from 'date-picker-svelte'
	import ColorPicker from '$lib/ColorPicker.svelte'
	import Add from 'carbon-icons-svelte/lib/Add.svelte'


	export let file: Data
	let data: ChartDataEntry[] = parseData(file)

	const isFullScreenEnabled = typeof document !== 'undefined' && (document['fullscreenEnabled'] ||
			document['webkitFullscreenEnabled'] || document['mozFullScreenEnabled'] || document['msFullscreenEnabled']);

	const loadFile = async () => {
		open({title: "Indicateurs", filters: [{name: "json", extensions: ["json"]}]}).then(async (filePath) => {
			if(typeof filePath !== "string") return console.log("Aucun fichier sélectionné");
			const json = await readTextFile(filePath)
			data = parseData(JSON.parse(json))
		})
	}

	const toolbar: ToolbarOptions = {
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
				clickFunction: loadFile,
				text: "Charger des indicateurs",
				iconSVG: {
					content: "<svg id=\"icon\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 32 32\"><defs><style>.cls-1{fill:none;}</style></defs><title>document--import</title><polygon points=\"28 19 14.83 19 17.41 16.41 16 15 11 20 16 25 17.41 23.59 14.83 21 28 21 28 19\"/><path d=\"M24,14V10a1,1,0,0,0-.29-.71l-7-7A1,1,0,0,0,16,2H6A2,2,0,0,0,4,4V28a2,2,0,0,0,2,2H22a2,2,0,0,0,2-2V26H22v2H6V4h8v6a2,2,0,0,0,2,2h6v2Zm-8-4V4.41L21.59,10Z\"/><rect id=\"_Transparent_Rectangle_\" data-name=\"&lt;Transparent Rectangle&gt;\" class=\"cls-1\" width=\"32\" height=\"32\"/></svg>"
				},
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

	let dateTresholds: DateThreshold[] = [
	]

	$: options = {
		title: "Activité par utilisateur",
		axes: {
			bottom: {
				title: "Date",
				mapsTo: "date",
				scaleType: ScaleTypes.TIME,
				thresholds: dateTresholds,
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
		toolbar
	} as LineChartOptions


	// ------ add event --------
	const locale: Locale = localeFromDateFnsLocale(fr)
	let minDate: Date = new Date(data.reduce((min, other) => min.date < other.date ? min : other).date)
	let maxDate: Date = new Date(data.reduce((max, other) => max.date > other.date ? max : other).date)

	let newDate: Date = new Date((minDate.getTime() + maxDate.getTime()) / 2)
	let newLabel: string
	let newColor: string

	const addDateThreshold = () => {
		dateTresholds = [...dateTresholds, {
			fillColor: newColor,
			label: newLabel,
			value: newDate
		}]
	}
</script>
<LineChart
	{data}
	{options}
/>

<div class='new_event'>

	<div class='row' style='align-items: center'>
		<h3>Ajouter un évènement</h3>
		<DateInput
			bind:value={newDate}
			min={minDate}
			max={maxDate}
			locale={locale}
			closeOnSelection={true}
			format='yyyy-MM-dd'
		/>
		<ColorPicker bind:value={newColor} />
	</div>
	<br>
	<div class='row'>
		<FluidForm>
			<TextInput bind:value={newLabel} labelText="Nom de l'évènement" placeholder="ex: exercice de rédaction"/>
		</FluidForm>
		<Button on:click={addDateThreshold} iconDescription="Ajouter" disabled={!newLabel} icon={Add} kind="tertiary"/>
	</div>
</div>


<!--<ContextMenu>-->
<!--	<ContextMenuOption-->
<!--		indented-->
<!--		labelText="Zoom +"-->
<!--		icon={ZoomIn}-->
<!--	/>-->
<!--	<ContextMenuOption indented kind="danger" labelText="Delete" />-->
<!--</ContextMenu>-->


<style>
	:global(:root) {
      --date-picker-foreground: #f7f7f7;
      --date-picker-background: #1D2021;
      --date-picker-selected-background: rgba(255, 255, 255, 0.1);
      --date-picker-highlight-border: rgba(255, 255, 255, .7);
      --date-picker-highlight-shadow: transparent;
	}

	.new_event {
      margin-top: 24px;
			display: flex;
			flex-direction: column;
	}

	.new_event > .row {
      display: flex;
      flex-direction: row;
			justify-content: space-between;
	}

	:global(.bx--form--fluid) {
			flex-grow: 1;
			margin-right: 24px;
	}

	:global(.bx--btn__icon) {
      width: 	2rem !important;
      height: 2rem !important;
	}
</style>