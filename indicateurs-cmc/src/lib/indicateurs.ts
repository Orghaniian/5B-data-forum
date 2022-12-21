import type ChartDataEntry from '$lib/ChartDataEntry'

export interface Activity {
	value: number
	date: string
}

export interface Data {
	mean: Activity[],
	indicateurs: {
		[key: string]: Activity[]
	}
}

export function parseData(file: Data): ChartDataEntry[] {
	const meanData: Array<ChartDataEntry> = file.mean.map((entry) => ({user: "Moyenne", ...entry}))

	const usersData: Array<ChartDataEntry> = Object.keys(file.indicateurs).map(
		(user) => file.indicateurs[user]?.map((entry) => ({user, ...entry}))
	).flat().filter((element) => element != undefined) as Array<ChartDataEntry>

	return meanData.concat(usersData)
}