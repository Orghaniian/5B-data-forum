<script lang="ts">
	import "carbon-components-svelte/css/all.css"
	import data from'$lib/assets/indicateurs.json'
	import '@carbon/styles/css/styles.css'
	import '@carbon/charts/styles.css'
	import Chart from '$lib/Chart.svelte'

	import { DataTable, Theme, ExpandableTile } from "carbon-components-svelte"

	//TODO change title bar and scrollbar style
	//TODO set min window size
	//TODO lazy load indicators

	let theme = "g90";
</script>
<Theme bind:theme />

<Chart file={data}/>
<br>
<br>
<br>

<ExpandableTile>
	<h2 slot='above'>Proposition d'indicateur initiale</h2>
	<div slot='below'>
		<br>
		<DataTable
			headers={[
    { key: "name", value: "Indicateurs" },
    { key: "what", value: "Quoi" },
    { key: "forWho", value: "Pour qui" },
    { key: "objective", value: "Objectif d’analyse" },
    { key: "visu", value: "Visualisation" },
    { key: "visuDescription", value: "Exemple visuel" },
  ]}
			rows={[
    {
			name: "Activité par utilisateur",
			what: "Activité des utilisateurs, affichée par utilisateur sur une période donnée",
			forWho: "Aux professeurs pour déterminer l’engagement des élèves et mesurer son évolution",
			objective: "Permet aux professeur de réagir à une baisse d’activité, déterminer ce qui favorise l’activité des utilisateurs afin de l’encourager et féliciter les plus actifs",
			visu: "Nombre des différentes interactions dans le temps",
			visuDescription: "Un graphique avec en abscisse le temps et en ordonné une valeur d’activité pondérée à partir des messages envoyés, messages vus, de la fréquence de connexion, des réponses/citations/etc.",
    }
  ]}
		/>
	</div>
</ExpandableTile>

<style>
	:global(*) {
			font-family: "Cascadia Mono", ui-monospace;
	}
	:global(body) {
			padding: 24px;
	}
</style>