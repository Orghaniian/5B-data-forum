import { appWindow, LogicalSize } from '@tauri-apps/api/window'

appWindow.setMinSize(new LogicalSize(850, 650)).then(r => console.log(r))