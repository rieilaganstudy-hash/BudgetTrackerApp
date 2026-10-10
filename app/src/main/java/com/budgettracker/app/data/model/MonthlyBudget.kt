package com.budgettracker.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.YearMonth

@Entity(tableName = "monthly_budgets")
data class MonthlyBudget(
    @PrimaryKey
    val yearMonth: String,
    val budgetLimit: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromYearMonth(yearMonth: YearMonth, limit: Double): MonthlyBudget {
            return MonthlyBudget(
                yearMonth = yearMonth.toString(),
                budgetLimit = limit
            )
        }
    }
}
