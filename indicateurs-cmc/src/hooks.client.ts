import { appWindow, LogicalSize } from '@tauri-apps/api/window'

appWindow.setMinSize(new LogicalSize(850, 630)).then(r => console.log(r))