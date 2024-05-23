package com.example.minisofascore.data.remote

import com.example.minisofascore.data.models.Incident
import com.example.minisofascore.data.models.IncidentType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class IncidentDeserializer : JsonDeserializer<Incident> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Incident {
        if (json != null && context != null) {
            when (json.asJsonObject.get("type").asString) {
                IncidentType.CARD.toString().lowercase() -> return context.deserialize(json, Incident.Card::class.java)
                IncidentType.GOAL.toString().lowercase() -> return context.deserialize(json, Incident.Goal::class.java)
                IncidentType.PERIOD.toString().lowercase() -> return context.deserialize(json, Incident.Period::class.java)
            }
        }
        throw JsonParseException("Invalid incident type.")
    }
}