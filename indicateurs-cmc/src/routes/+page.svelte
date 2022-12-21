<script lang="ts">
	import "carbon-components-svelte/css/all.css"
	import data from'$lib/assets/indicateurs.json'
	import '@carbon/styles/css/styles.css'
	import '@carbon/charts/styles.css'
	import Chart from '$lib/Chart.svelte'

	import { Theme } from "carbon-components-svelte"
	import Titlebar from '$lib/Titlebar.svelte'
	//TODO lazy load indicators
	//TODO custom theme

	let theme = "g90";
</script>
<Titlebar/>
<Theme bind:theme />

<div class='chart-wrapper'>
	<Chart file={data}/>
</div>
<br>
<br>
<br>

<span class='signature'>Julien Boulvert</span>
<style>
	:global(*) {
			font-family: "Cascadia Mono", ui-monospace;
			-ms-scrollbar-base-color: #0f62fe;
	}
	:global(body) {
      min-height: 100vh;
			height: 100vh;
			max-height: 100vh;
      padding: 24px 24px 32px;
  }
	.chart-wrapper {
      padding-top: 12px;
	}
	.signature {
			position: fixed;
			bottom: 8px;
			right: 8px;
			opacity: 30%;
	}

  :global(.noselect) {
      -webkit-touch-callout: none; /* iOS Safari */
      -webkit-user-select: none; /* Safari */
      -khtml-user-select: none; /* Konqueror HTML */
      -moz-user-select: none; /* Old versions of Firefox */
      -ms-user-select: none; /* Internet Explorer/Edge */
      user-select: none; /* Non-prefixed version, currently
                                  supported by Chrome, Edge, Opera and Firefox */
  }

	:global(.cds--overflow-menu--flip) {
			width: fit-content;
	}

	:global(.cds--overflow-menu-options__btn) {
			max-width: 15rem;
	}
</style>