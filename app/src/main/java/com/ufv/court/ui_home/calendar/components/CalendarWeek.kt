package com.ufv.court.ui_home.calendar.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ufv.court.ui_home.calendar.CalendarHorizontalDivisor
import com.ufv.court.ui_home.calendar.CalendarViewModel
import com.ufv.court.ui_home.calendar.CalendarWeek

fun Modifier.weekModifier(): Modifier = this
    .fillMaxWidth()
    .padding(horizontal = 8.dp)

@Composable
fun FirstWeekOfMonthItem(
    modifier: Modifier = Modifier,
    firstWeek: () -> CalendarWeek,
    index: Int,
    onDayClick: (String) -> Unit
) {
    Row(modifier = modifier) {
        val firstWeekSize = firstWeek().days.size
        if (firstWeekSize < CalendarViewModel.NUMBER_OF_DAYS_IN_A_WEEK) {
            val weight = (CalendarViewModel.NUMBER_OF_DAYS_IN_A_WEEK - firstWeekSize).toFloat()
            Spacer(modifier = Modifier.weight(weight))
        }
        firstWeek().days.forEach { day ->
            DayItem(day = { day }) {
                onDayClick(day.number)
            }
        }
    }
}

@Composable
fun MiddleWeekOfMonthItem(
    modifier: Modifier = Modifier,
    week: () -> CalendarWeek,
    onDayClick: (String) -> Unit,
    index: Int
) {
    CalendarHorizontalDivisor()
    Row(modifier = modifier) {
        week().days.forEach { day ->
            DayItem(day = { day }) {
                onDayClick(day.number)
            }
        }
    }
}

@Composable
fun LastWeekOfMonthItem(
    modifier: Modifier = Modifier,
    lastWeek: () -> CalendarWeek,
    onDayClick: (String) -> Unit,
    index: Int
) {
    Row(modifier = modifier) {
        val lastWeekSize = lastWeek().days.size
        lastWeek().days.forEach { day ->
            DayItem(day = { day }) {
                onDayClick(day.number)
            }
        }
        if (lastWeekSize < CalendarViewModel.NUMBER_OF_DAYS_IN_A_WEEK) {
            val weight = (CalendarViewModel.NUMBER_OF_DAYS_IN_A_WEEK - lastWeekSize).toFloat()
            Spacer(modifier = Modifier.weight(weight))
        }
    }
}
