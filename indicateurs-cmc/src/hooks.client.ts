import { appWindow, LogicalSize } from '@tauri-apps/api/window'

// noinspection JSIgnoredPromiseFromCall
appWindow.setMinSize(new LogicalSize(850, 650))