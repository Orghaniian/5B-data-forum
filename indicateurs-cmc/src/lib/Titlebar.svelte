<script lang="ts">
	import { appWindow } from '@tauri-apps/api/window'
	import { DataTable, Modal } from 'carbon-components-svelte'

	import About from "carbon-icons-svelte/lib/Help.svelte"

	let isMaximized = false
	appWindow.isMaximized().then(r => isMaximized = r)

	function maximize() {
		if (isMaximized) appWindow.unmaximize().then(() => isMaximized = false)
		else appWindow.maximize().then(() => isMaximized = true)
	}

	let open = false
	export let title: string
</script>

<div on:click={appWindow.startDragging} data-tauri-drag-region class="titlebar">
	<div on:click={() => (open = true)} class="titlebar-button" id="titlebar-about" on:keydown on:keyup on:keypress>
		<About />
	</div>
	<span on:click={appWindow.startDragging} class='title noselect'>{title}</span>
	<div on:click={appWindow.minimize} class="titlebar-button" id="titlebar-minimize" on:keydown on:keyup on:keypress>
		<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
			<rect width="10" height="1" x="3" y="8" fill-rule="evenodd"/>
		</svg>
	</div>
	<div on:click={maximize} class="titlebar-button" id="titlebar-maximize" on:keydown on:keyup on:keypress>
		{#if isMaximized}
			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
				<g fill-rule="evenodd">
					<path d="M5,8 L5,7 L7,7 L7,1 L1,1 L1,3 L0,3 L0,0 L8,0 L8,8 L5,8 Z" transform="translate(5 3)"/>
					<path d="M0,0 L8,0 L8,8 L0,8 L0,0 Z M1,1 L1,7 L7,7 L7,1 L1,1 Z" transform="translate(3 5)"/>
				</g>
			</svg>
		{:else}
			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
				<path fill-rule="evenodd" d="M3,3 L13,3 L13,13 L3,13 L3,3 Z M4,4 L4,12 L12,12 L12,4 L4,4 Z"/>
			</svg>
		{/if}
	</div>
	<div on:click={appWindow.close} class="titlebar-button" id="titlebar-close" on:keydown on:keyup on:keypress>
		<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
			<g fill-rule="evenodd" transform="translate(3 3)">
				<rect width="13" height="1" x="-1.5" y="4.5" transform="rotate(45 5 5)"/>
				<rect width="13" height="1" x="-1.5" y="4.5" transform="scale(-1 1) rotate(45 0 -7.071)"/>
			</g>
		</svg>
	</div>
</div>

<Modal size="lg" passiveModal bind:open modalHeading="Proposition d'indicateur initiale" on:open on:close style='padding: 0'>
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
			objective: "Permet aux professeur de réagir à une baisse d’activité, déterminer ce qui favorise l’activité des utilisateurs afin de l’encourager et féliciter les plus actifs.\nL'ajout d'évènements permet de repérer les causes des variations d'activité.",
			visu: "Nombre des différentes interactions dans le temps",
			visuDescription: "Un graphique avec en abscisse le temps et en ordonné une valeur d’activité pondérée à partir des messages envoyés, messages vus, de la fréquence de connexion, des réponses/citations/etc.",
    }
  ]}
	/>
</Modal>

<style>
    .titlebar {
        height: 30px;
        background: #1D2021;
        user-select: none;
        display: flex;
        justify-content: flex-end;
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
				z-index: 1000;
    }
    .titlebar-button {
        display: inline-flex;
        justify-content: center;
        align-items: center;
        width: 30px;
        height: 30px;
				cursor: pointer;
    }
		.titlebar-button > svg {
        fill: white;
		}
    .titlebar-button:hover {
        background:#303436;
    }

		.title {
				position: absolute;
				right: 50%;
				top: 50%;
				transform: translateX(50%) translateY(-50%);
		}

		#titlebar-about {
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
		}
</style>