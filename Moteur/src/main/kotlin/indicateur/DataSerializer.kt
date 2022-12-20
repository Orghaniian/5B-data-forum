package indicateur

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DateSerializer", PrimitiveKind.STRING)
    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    init {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }
    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(sdf.format(value.time))
    }

    override fun deserialize(decoder: Decoder): Date {
        return sdf.parse(decoder.decodeString())
    }
}