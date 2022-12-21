import indicateur.Activity
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.File
import kotlinx.serialization.json.*
import model.Trace
import indicateur.Result
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

private val json = Json { prettyPrint = true }
private const val DEFAULT_OUTPUT_NAME = "indicateurs.json"

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    print("Chemin du fichier de données: ")
    val path = readln().trimQuotes()

    val file = File(path)
    val data = Json.decodeFromStream<List<Trace>>(file.inputStream())

    val result = processData(data)

    readOutputPath().outputStream().use { out ->
        json.encodeToStream(result, out)
    }
}

fun processData(data: List<Trace>): Result {
    val result = Result(mutableListOf(), mutableMapOf())
    data.forEach{ trace ->
        result.indicateurs.getOrPut(trace.user) { mutableListOf() }.also { activitiesOfUser ->
            activitiesOfUser.find { it.date == trace.date.toLocalDate() }?.let {
                it.value++
            } ?: activitiesOfUser.add(Activity(trace.date.toLocalDate(), 1f))
        }
    }

    result.indicateurs.forEach { (_, activities) -> activities.sortBy { it.date } }

    result.indicateurs.forEach { (_, activities) ->
        activities.forEach { activity ->
            result.mean.find { it.date == activity.date }?.let {
                it.value++
            } ?: result.mean.add(Activity(activity.date, 1f))
        }
    }

    result.mean.forEach {
        it.value /= result.indicateurs.size.toFloat()
    }

    return result
}

fun readOutputPath(): File {
    print("Chemin du fichier dans lequel seront stockés les indicateurs: ")
    val path = readln().trimQuotes().ifEmpty { DEFAULT_OUTPUT_NAME }

    var output = File(path)

    if(output.exists() && output.isDirectory) output = File(path, DEFAULT_OUTPUT_NAME)

    if(output.extension == "") output = File(output.name + ".json")

    return output
}

fun String.trimQuotes() = replace(Regex("^\"|\"$"), "")

fun Date.toLocalDate(zone: ZoneId = ZoneId.systemDefault()): LocalDate = toInstant().atZone(zone).toLocalDate()

fun Trace.toActivity(): Activity = Activity(date.toLocalDate(), 1f)