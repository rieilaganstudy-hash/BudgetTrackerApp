package com.budgettracker.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.budgettracker.app.data.model.GroceryExpense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface GroceryExpenseDao {
    @Insert
    suspend fun insertExpense(expense: GroceryExpense): Long

    @Update
    suspend fun updateExpense(expense: GroceryExpense)

    @Delete
    suspend fun deleteExpense(expense: GroceryExpense)

    @Query("SELECT * FROM grocery_expenses WHERE strftime('%Y-%m', date) = :yearMonth ORDER BY date DESC")
    fun getExpensesByMonth(yearMonth: String): Flow<List<GroceryExpense>>

    @Query("SELECT * FROM grocery_expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<GroceryExpense>>

    @Query("SELECT category, SUM(amount) as totalAmount, COUNT(*) as itemCount FROM grocery_expenses WHERE strftime('%Y-%m', date) = :yearMonth GROUP BY category ORDER BY totalAmount DESC")
    fun getExpensesByCategoryForMonth(yearMonth: String): Flow<List<CategoryExpenseSummary>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM grocery_expenses WHERE strftime('%Y-%m', date) = :yearMonth")
    fun getTotalSpendingForMonth(yearMonth: String): Flow<Double>

    @Query("SELECT * FROM grocery_expenses WHERE id = :id")
    suspend fun getExpenseById(id: Int): GroceryExpense?

    @Query("DELETE FROM grocery_expenses WHERE strftime('%Y-%m', date) = :yearMonth")
    suspend fun deleteExpensesByMonth(yearMonth: String)

    @Query("SELECT * FROM grocery_expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<GroceryExpense>>
}

data class CategoryExpenseSummary(
    val category: String,
    val totalAmount: Double,
    val itemCount: Int
)
