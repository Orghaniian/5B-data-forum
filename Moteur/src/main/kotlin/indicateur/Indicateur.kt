package indicateur

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Result(var mean: MutableList<Activity>, var indicateurs: MutableMap<String, MutableList<Activity>>)

@Serializable
data class Activity(@Serializable(with = LocalDateSerializer::class)val date: LocalDate, var value: Float)