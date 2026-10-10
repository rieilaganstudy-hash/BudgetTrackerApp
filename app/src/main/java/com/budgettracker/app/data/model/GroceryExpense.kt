package com.budgettracker.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "grocery_expenses")
data class GroceryExpense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val amount: Double,
    val category: String,
    val date: LocalDate,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
