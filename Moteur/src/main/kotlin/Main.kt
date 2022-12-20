import indicateur.Activity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import java.io.File
import kotlinx.serialization.json.*
import model.Trace
import indicateur.Result

private val json = Json { prettyPrint = true }

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {
    val path = if(args.isEmpty()) {
        print("Chemin du fichier de données: ")
        readln()
    } else args.first()

    val file = File(path)
    val data = Json.decodeFromStream<List<Trace>>(file.inputStream())

    val result = processData(data)

    val output = File("indicateurs.json")

    json.encodeToStream(result, output.outputStream())
}

fun processData(data: List<Trace>) : Result {
    val result = Result(mutableListOf(), mutableMapOf())
    data.forEach{ trace ->
        result.indicateurs.getOrPut(trace.user) { mutableListOf() }.also { activities ->
            activities.find { it.date == trace.date }?.let {
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