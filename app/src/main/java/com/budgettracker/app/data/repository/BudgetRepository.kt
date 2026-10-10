package com.budgettracker.app.data.repository

import com.budgettracker.app.data.dao.GroceryExpenseDao
import com.budgettracker.app.data.dao.MonthlyBudgetDao
import com.budgettracker.app.data.dao.CategoryExpenseSummary
import com.budgettracker.app.data.model.GroceryExpense
import com.budgettracker.app.data.model.MonthlyBudget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.YearMonth

class BudgetRepository(
    private val groceryExpenseDao: GroceryExpenseDao,
    private val monthlyBudgetDao: MonthlyBudgetDao
) {
    suspend fun addExpense(
        name: String,
        amount: Double,
        category: String,
        date: LocalDate,
        description: String = ""
    ): Long {
        val expense = GroceryExpense(
            name = name,
            amount = amount,
            category = category,
            date = date,
            description = description
        )
        return groceryExpenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: GroceryExpense) {
        groceryExpenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: GroceryExpense) {
        groceryExpenseDao.deleteExpense(expense)
    }

    fun getExpensesForMonth(yearMonth: YearMonth): Flow<List<GroceryExpense>> {
        return groceryExpenseDao.getExpensesByMonth(yearMonth.toString())
    }

    fun getTotalSpendingForMonth(yearMonth: YearMonth): Flow<Double> {
        return groceryExpenseDao.getTotalSpendingForMonth(yearMonth.toString())
    }

    fun getExpensesByCategory(yearMonth: YearMonth): Flow<List<CategoryExpenseSummary>> {
        return groceryExpenseDao.getExpensesByCategoryForMonth(yearMonth.toString())
    }

    suspend fun deleteExpensesForMonth(yearMonth: YearMonth) {
        groceryExpenseDao.deleteExpensesByMonth(yearMonth.toString())
    }

    suspend fun setBudgetForMonth(yearMonth: YearMonth, limit: Double) {
        val budget = MonthlyBudget.fromYearMonth(yearMonth, limit)
        if (monthlyBudgetDao.budgetExistsForMonth(yearMonth.toString())) {
            monthlyBudgetDao.updateBudget(budget.copy(updatedAt = System.currentTimeMillis()))
        } else {
            monthlyBudgetDao.insertBudget(budget)
        }
    }

    fun getBudgetForMonth(yearMonth: YearMonth): Flow<MonthlyBudget?> {
        return monthlyBudgetDao.getBudgetForMonth(yearMonth.toString())
    }

    suspend fun getBudgetLimitForMonth(yearMonth: YearMonth): Double? {
        return monthlyBudgetDao.getBudgetLimit(yearMonth.toString())
    }

    suspend fun deleteBudgetForMonth(yearMonth: YearMonth) {
        monthlyBudgetDao.deleteBudgetForMonth(yearMonth.toString())
    }

    fun getRemainingBudgetForMonth(yearMonth: YearMonth): Flow<BudgetStatus> {
        return combine(
            getBudgetForMonth(yearMonth),
            getTotalSpendingForMonth(yearMonth)
        ) { budget, spending ->
            val limit = budget?.budgetLimit ?: 0.0
            val remaining = limit - spending
            val percentage = if (limit > 0) spending / limit * 100 else 0.0
            BudgetStatus(
                budgetLimit = limit,
                totalSpending = spending,
                remaining = remaining,
                percentageUsed = percentage,
                isOverBudget = remaining < 0
            )
        }
    }
}

data class BudgetStatus(
    val budgetLimit: Double,
    val totalSpending: Double,
    val remaining: Double,
    val percentageUsed: Double,
    val isOverBudget: Boolean
)
