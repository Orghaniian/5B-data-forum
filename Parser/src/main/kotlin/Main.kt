
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
private const val DEFAULT_OUTPUT_NAME = "data.json"

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) = object: CliktCommand() {
    override fun run() {
        println("Database connexion informations")
        val dbConfig: DatabaseConfig = DatabaseConfig.Builder.fromPrompt()

        println("Connecting to ${dbConfig.url}")
        dbConfig.connect()

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

        promptOutputPath().outputStream().use { out ->
            json.encodeToStream(results, out)
        }
    }

    fun promptOutputPath(): File {
        val path = prompt("Output file path", DEFAULT_OUTPUT_NAME) ?: DEFAULT_OUTPUT_NAME

        var output = File(path)

        if(output.exists() && output.isDirectory) output = File(path, DEFAULT_OUTPUT_NAME)

        if(output.extension == "") output = File(output.name + ".json")

        return output
    }

    inner class DatabaseConfig(
        val hostname: String,
        val port: String,
        val databaseName: String,
        val user: String?,
        val password: String?
    ) {

        val url: String by lazy { "jdbc:mysql://$hostname:$port/$databaseName" }

        fun connect(): Database = Database.connect(url, user = user!!, password = password ?: "", driver = "com.mysql.cj.jdbc.Driver")
        companion object Builder {
            fun fromPrompt(): DatabaseConfig = DatabaseConfig(
                hostname = prompt("Hostname", "localhost") ?: "localhost",
                port = prompt("Port", "3306") {
                    if(!it.all(Char::isDigit)) throw UsageError("The port can only contains digits")
                    else it
                } ?: "3306",
                databaseName = prompt("Database Name", "traceforumsql") ?: "traceforumsql",
                user = prompt("Username", "root"),
                password = prompt("Password", ""),
            )
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

