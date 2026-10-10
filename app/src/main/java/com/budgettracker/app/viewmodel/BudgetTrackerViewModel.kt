package com.budgettracker.app.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.budgettracker.app.data.database.BudgetTrackerDatabase
import com.budgettracker.app.data.model.GroceryExpense
import com.budgettracker.app.data.repository.BudgetRepository
import com.budgettracker.app.data.repository.BudgetStatus
import com.budgettracker.app.data.repository.GroceryCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class BudgetTrackerViewModel(
    application: Application
) : ViewModel() {
    private val database = BudgetTrackerDatabase.getInstance(application)
    private val repository = BudgetRepository(database.groceryExpenseDao(), database.monthlyBudgetDao())

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    private val _budgetText = MutableStateFlow("")
    private val _expenseName = MutableStateFlow("")
    private val _expenseAmount = MutableStateFlow("")
    private val _expenseDate = MutableStateFlow(LocalDate.now().toString())
    private val _expenseDescription = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow(GroceryCategories.getAll().first())

    val uiState: StateFlow<BudgetTrackerUiState> = combine(
        _currentMonth,
        _budgetText,
        _expenseName,
        _expenseAmount,
        _expenseDate,
        _expenseDescription,
        _selectedCategory,
        repository.getExpensesForMonth(_currentMonth.value),
        repository.getRemainingBudgetForMonth(_currentMonth.value)
    ) { values ->
        val month = values[0] as YearMonth
        val budgetText = values[1] as String
        val name = values[2] as String
        val amount = values[3] as String
        val date = values[4] as String
        val description = values[5] as String
        val category = values[6] as String
        val expenses = values[7] as List<GroceryExpense>
        val budgetStatus = values[8] as BudgetStatus

        BudgetTrackerUiState(
            currentMonth = month,
            budgetText = budgetText,
            expenseName = name,
            expenseAmount = amount,
            expenseDate = date,
            expenseDescription = description,
            selectedCategory = category,
            expenses = expenses,
            budgetLimit = budgetStatus.budgetLimit,
            totalSpent = budgetStatus.totalSpending,
            remainingBudget = budgetStatus.remaining,
            isOverBudget = budgetStatus.isOverBudget
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetTrackerUiState())

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return BudgetTrackerViewModel(application) as T
                }
            }
    }

    init {
        viewModelScope.launch {
            repository.getBudgetForMonth(_currentMonth.value).collect { budget ->
                if (budget != null) {
                    _budgetText.value = budget.budgetLimit.toString()
                }
            }
        }
    }

    fun onBudgetTextChanged(value: String) {
        _budgetText.value = value
    }

    fun saveBudget() {
        viewModelScope.launch {
            val amount = _budgetText.value.toDoubleOrNull() ?: return@launch
            repository.setBudgetForMonth(_currentMonth.value, amount)
        }
    }

    fun onExpenseNameChanged(value: String) {
        _expenseName.value = value
    }

    fun onExpenseAmountChanged(value: String) {
        _expenseAmount.value = value
    }

    fun onExpenseDateChanged(value: String) {
        _expenseDate.value = value
    }

    fun onExpenseDescriptionChanged(value: String) {
        _expenseDescription.value = value
    }

    fun onCategorySelected(value: String) {
        _selectedCategory.value = value
    }

    fun addExpense() {
        viewModelScope.launch {
            val name = _expenseName.value.trim()
            if (name.isBlank()) return@launch
            val amount = _expenseAmount.value.toDoubleOrNull() ?: return@launch
            val date = runCatching { LocalDate.parse(_expenseDate.value) }.getOrElse { LocalDate.now() }

            repository.addExpense(
                name = name,
                amount = amount,
                category = _selectedCategory.value,
                date = date,
                description = _expenseDescription.value.trim()
            )

            _expenseName.value = ""
            _expenseAmount.value = ""
            _expenseDate.value = LocalDate.now().toString()
            _expenseDescription.value = ""
        }
    }

    fun deleteExpense(expense: GroceryExpense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }
}

data class BudgetTrackerUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val budgetText: String = "",
    val expenseName: String = "",
    val expenseAmount: String = "",
    val expenseDate: String = LocalDate.now().toString(),
    val expenseDescription: String = "",
    val selectedCategory: String = GroceryCategories.getAll().first(),
    val expenses: List<GroceryExpense> = emptyList(),
    val budgetLimit: Double = 0.0,
    val totalSpent: Double = 0.0,
    val remainingBudget: Double = 0.0,
    val isOverBudget: Boolean = false
)
