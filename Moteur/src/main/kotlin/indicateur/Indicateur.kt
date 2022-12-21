package indicateur

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Result(var mean: MutableList<Activity>, var indicateurs: MutableMap<String, MutableList<Activity>>)

@Serializable
data class Activity(@Serializable(with = DateSerializer::class)val date: Date, var value: Float)