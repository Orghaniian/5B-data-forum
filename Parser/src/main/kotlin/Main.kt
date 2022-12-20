import kotlinx.serialization.ExperimentalSerializationApi
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.time
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlinx.serialization.json.*
import java.io.File

private val json = Json { prettyPrint = true }

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {
    Database.connect("jdbc:mysql://localhost:3306/traceforumsql", user = "root", driver = "com.mysql.cj.jdbc.Driver")

    val results = transaction {
        Transitions.slice(Transitions.date, Transitions.time, Transitions.title, Transitions.user).select(
            Transitions.title eq "Connexion"
                or (Transitions.title eq "Poster un nouveau message")
                or (Transitions.title eq "Répondre à un message")
        ).map { row ->
            Trace(
                row[Transitions.user],
                Date.from(LocalDateTime.of(row[Transitions.date], row[Transitions.time]).atZone(ZoneId.systemDefault()).toInstant()),
                Type.fromString(row[Transitions.title])
            )
        }
    }

    File("data.json").outputStream().use { out ->
        json.encodeToStream(results, out)
    }
}

object Transitions : Table("transition") {
    val id = long("IDTran")
    val user = varchar("Utilisateur", 70)
    val title = varchar("Titre",120)
    val attribut = varchar("Attribut", 200)
    val date = date("Date")
    val time = time("Heure")
    val delay = time("Delai")
    val ref = long("RefTran")
    val comment = varchar("Commentaire", 100)
}