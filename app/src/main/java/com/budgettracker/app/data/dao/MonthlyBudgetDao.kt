package com.budgettracker.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.budgettracker.app.data.model.MonthlyBudget
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyBudgetDao {
    @Insert
    suspend fun insertBudget(budget: MonthlyBudget)

    @Update
    suspend fun updateBudget(budget: MonthlyBudget)

    @Query("SELECT * FROM monthly_budgets WHERE yearMonth = :yearMonth")
    fun getBudgetForMonth(yearMonth: String): Flow<MonthlyBudget?>

    @Query("SELECT * FROM monthly_budgets ORDER BY yearMonth DESC")
    fun getAllBudgets(): Flow<List<MonthlyBudget>>

    @Query("DELETE FROM monthly_budgets WHERE yearMonth = :yearMonth")
    suspend fun deleteBudgetForMonth(yearMonth: String)

    @Query("SELECT EXISTS(SELECT 1 FROM monthly_budgets WHERE yearMonth = :yearMonth)")
    suspend fun budgetExistsForMonth(yearMonth: String): Boolean

    @Query("SELECT budgetLimit FROM monthly_budgets WHERE yearMonth = :yearMonth LIMIT 1")
    suspend fun getBudgetLimit(yearMonth: String): Double?
}
