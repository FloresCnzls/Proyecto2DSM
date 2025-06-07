package com.example.logindsm.model

data class EventModel(
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val location: String = "",
    val creatorId: String = "",
    val participants: List<String> = emptyList()
)
data class EventModelWithId(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val location: String = "",
    val creatorId: String = "",
    val participants: List<String> = emptyList()
) {
    companion object {
        fun from(docId: String, event: EventModel) = EventModelWithId(
            id = docId,
            title = event.title,
            description = event.description,
            date = event.date,
            location = event.location,
            creatorId = event.creatorId,
            participants = event.participants
        )
    }
}
