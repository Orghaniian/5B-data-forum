package model

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Trace (
    val user: String,
    @Serializable(with = DateSerializer::class)
    val date: Date,
    val type: Type
)

enum class Type {
    Login,
    Post,
    Response;
}