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

     companion object {
         fun fromString(string: String) : Type = when(string) {
             "Connexion" -> Login
             "Poster un nouveau message" -> Post
             "Répondre à un message" -> Response
             else -> throw IllegalArgumentException("$string is not a valid value")
         }
     }
}