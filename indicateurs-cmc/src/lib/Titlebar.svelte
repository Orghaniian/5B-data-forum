<script lang="ts">
	import { appWindow } from '@tauri-apps/api/window'

	let isMaximized = false
	appWindow.isMaximized().then(r => isMaximized = r)

	function maximize() {
		if (isMaximized) appWindow.unmaximize().then(() => isMaximized = false)
		else appWindow.maximize().then(() => isMaximized = true)
	}
</script>

<div on:click={appWindow.startDragging} data-tauri-drag-region class="titlebar">
	<span class='title'>Indicateurs CMC</span>
	<div on:click={appWindow.minimize} class="titlebar-button" id="titlebar-minimize">
		<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
			<rect width="10" height="1" x="3" y="8" fill-rule="evenodd"/>
		</svg>
	</div>
	<div on:click={maximize} class="titlebar-button" id="titlebar-maximize">
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
	<div on:click={appWindow.close} class="titlebar-button" id="titlebar-close">
		<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
			<g fill-rule="evenodd" transform="translate(3 3)">
				<rect width="13" height="1" x="-1.5" y="4.5" transform="rotate(45 5 5)"/>
				<rect width="13" height="1" x="-1.5" y="4.5" transform="scale(-1 1) rotate(45 0 -7.071)"/>
			</g>
		</svg>
	</div>
</div>

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


</style>