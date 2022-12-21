import indicateur.Activity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import java.io.File
import kotlinx.serialization.json.*
import model.Trace
import indicateur.Result
import java.util.*

private val json = Json { prettyPrint = true }
private const val defaultOuputFileName = "indicateurs.json"

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    print("Chemin du fichier de données: ")
    var path = readln().trimQuotes()

    val file = File(path)
    val data = Json.decodeFromStream<List<Trace>>(file.inputStream())

    val result = processData(data)

    print("Chemin du fichier dans lequel seront stockés les indicateurs: ")
    path = readln().trimQuotes().ifEmpty { defaultOuputFileName }

    var output = File(path)

    if(output.exists() && output.isDirectory) output = File(path, defaultOuputFileName)

    if(output.extension == "") output = File(output.name + ".json")

    json.encodeToStream(result, output.outputStream())//26976 lines
}

fun processData(data: List<Trace>): Result {
    val result = Result(mutableListOf(), mutableMapOf())
    data.forEach{ trace ->
        result.indicateurs.getOrPut(trace.user) { mutableListOf() }.also { activities ->
            activities.find { it.date.isSameDay(trace.date) }?.let {
                it.value++
            } ?: activities.add(Activity(trace.date, 1))
        }
    }

    result.indicateurs.forEach { (_, activities) -> activities.sortBy { it.date } }

    result.indicateurs.forEach { (_, activities) ->
        activities.forEach { activity ->
            result.mean.find { it.date == activity.date }?.let {
                it.value++
            } ?: result.mean.add(Activity(activity.date, 1))
        }
    }

    result.mean.forEach {
        it.value /= result.indicateurs.size
    }

    return result
}

fun String.trimQuotes() = replace(Regex("^\"|\"$"), "")

fun Date.isSameDay(other: Date): Boolean {
    val cal1 = Calendar.getInstance().also { it.time = this }
    val cal2 = Calendar.getInstance().also { it.time = other }

    return  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
            &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
}