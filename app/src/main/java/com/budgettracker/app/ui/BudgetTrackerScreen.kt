package com.budgettracker.app.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.budgettracker.app.data.model.GroceryExpense
import com.budgettracker.app.data.repository.GroceryCategories
import com.budgettracker.app.viewmodel.BudgetTrackerUiState
import com.budgettracker.app.viewmodel.BudgetTrackerViewModel
import java.time.YearMonth
import java.util.Currency
import java.util.Locale

@Composable
fun BudgetTrackerScreen(
    viewModel: BudgetTrackerViewModel = viewModel(
        factory = BudgetTrackerViewModel.provideFactory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderCard(uiState)

        InputForm(
            budgetValue = uiState.budgetText,
            onBudgetChange = viewModel::onBudgetTextChanged,
            onSaveBudget = viewModel::saveBudget,
            expenseName = uiState.expenseName,
            onExpenseNameChange = viewModel::onExpenseNameChanged,
            expenseAmount = uiState.expenseAmount,
            onExpenseAmountChange = viewModel::onExpenseAmountChanged,
            expenseDate = uiState.expenseDate,
            onExpenseDateChange = viewModel::onExpenseDateChanged,
            expenseDescription = uiState.expenseDescription,
            onExpenseDescriptionChange = viewModel::onExpenseDescriptionChanged,
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = viewModel::onCategorySelected,
            onAddExpense = viewModel::addExpense,
            currentMonth = uiState.currentMonth
        )

        ExpenseList(
            expenses = uiState.expenses,
            onDeleteExpense = viewModel::deleteExpense
        )
    }
}

@Composable
private fun HeaderCard(uiState: BudgetTrackerUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${uiState.currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${uiState.currentMonth.year}",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Text(
                text = "Budget Summary",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryColumn(label = "Spent", value = formatCurrency(uiState.totalSpent))
                SummaryColumn(label = "Remaining", value = formatCurrency(uiState.remainingBudget))
                SummaryColumn(label = "Limit", value = formatCurrency(uiState.budgetLimit))
            }
        }
    }
}

@Composable
private fun SummaryColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = label, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
private fun InputForm(
    budgetValue: String,
    onBudgetChange: (String) -> Unit,
    onSaveBudget: () -> Unit,
    expenseName: String,
    onExpenseNameChange: (String) -> Unit,
    expenseAmount: String,
    onExpenseAmountChange: (String) -> Unit,
    expenseDate: String,
    onExpenseDateChange: (String) -> Unit,
    expenseDescription: String,
    onExpenseDescriptionChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onAddExpense: () -> Unit,
    currentMonth: YearMonth
) {
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Monthly budget", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = budgetValue,
                        onValueChange = onBudgetChange,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        label = { Text("Budget amount") }
                    )

                    Button(onClick = onSaveBudget) {
                        Text("Save")
                    }
                }
            }
        }

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Add grocery expense", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                OutlinedTextField(
                    value = expenseName,
                    onValueChange = onExpenseNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Item name") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = expenseAmount,
                        onValueChange = onExpenseAmountChange,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        label = { Text("Amount") },
                        singleLine = true
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Category") },
                            singleLine = true,
                            readOnly = true,
                            trailingIcon = {
                                Text(
                                    text = "▾",
                                    modifier = Modifier
                                        .clickable { categoryMenuExpanded = true }
                                        .padding(end = 8.dp)
                                )
                            }
                        )

                        DropdownMenu(
                            expanded = categoryMenuExpanded,
                            onDismissRequest = { categoryMenuExpanded = false }
                        ) {
                            GroceryCategories.getAll().forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        onCategorySelected(category)
                                        categoryMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = expenseDate,
                    onValueChange = onExpenseDateChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Date (YYYY-MM-DD)") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = expenseDescription,
                    onValueChange = onExpenseDescriptionChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Notes (optional)") }
                )

                Button(
                    onClick = onAddExpense,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add expense")
                }
            }
        }
    }
}

@Composable
private fun ExpenseList(
    expenses: List<GroceryExpense>,
    onDeleteExpense: (GroceryExpense) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Recent purchases", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            if (expenses.isEmpty()) {
                Text(text = "No grocery purchases yet this month.", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(expenses) { expense ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F7FA), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(expense.name, fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = "${expense.category} • ${expense.date}",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                                if (expense.description.isNotBlank()) {
                                    Text(expense.description, fontSize = 12.sp, color = Color.DarkGray)
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = formatCurrency(expense.amount),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                IconButton(onClick = { onDeleteExpense(expense) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete expense")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatCurrency(value: Double): String {
    val symbol = try {
        Currency.getInstance(Locale.getDefault()).symbol
    } catch (e: Exception) {
        "$"
    }
    return "$symbol${String.format(Locale.getDefault(), "%.2f", value)}"
}
