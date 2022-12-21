
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
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.UsageError

private val json = Json { prettyPrint = true }

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) = object: CliktCommand() {
    override fun run() {
        println("Database connexion informations")
        val hostname = prompt("Hostname", "localhost")
        val port = prompt("Port", "3306") {
            if(!it.all(Char::isDigit)) throw UsageError("The port can only contains digits")
            else it
        }
        val databaseName = prompt("Database Name", "traceforumsql")
        val user = prompt("Username", "root")
        val password = prompt("Password", "")

        val url = "jdbc:mysql://$hostname:$port/$databaseName"

        println("Connecting to $url")
        Database.connect(url, user = user!!, password = password ?: "", driver = "com.mysql.cj.jdbc.Driver")
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
}.main(args)

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