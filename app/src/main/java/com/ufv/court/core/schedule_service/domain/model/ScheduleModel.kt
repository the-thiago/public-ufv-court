package com.ufv.court.core.schedule_service.domain.model

data class ScheduleModel(
    val id: String = "",
    val ownerId: String = "",
    val hourStart: Int = -1,
    val hourEnd: Int = -1,
    val title: String = "",
    val description: String = "",
    val scheduleType: String = "",
    val freeSpaces: Int = 0,
    val hasFreeSpace: Boolean = false,
    val cancelled: Boolean = false,
    val timeInMillis: Long = -1L, // millis
    val participantsId: List<String> = listOf()
)
