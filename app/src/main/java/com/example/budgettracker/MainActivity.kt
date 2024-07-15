package com.example.budgettracker

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var expenseName: EditText
    private lateinit var expenseAmount: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var addButton: Button
    private lateinit var totalTextView: TextView
    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    private var totalExpenses: Double = 0.0
    private val expenses = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expenseName = findViewById(R.id.expenseName)
        expenseAmount = findViewById(R.id.expenseAmount)
        categorySpinner = findViewById(R.id.categorySpinner)
        addButton = findViewById(R.id.addButton)
        totalTextView = findViewById(R.id.totalTextView)
        expensesRecyclerView = findViewById(R.id.expensesRecyclerView)

        // Set up Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.category_items,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Spinner Listener
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@MainActivity, "Selected: $item", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // RecyclerView setup
        expenseAdapter = ExpenseAdapter(expenses)
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)
        expensesRecyclerView.adapter = expenseAdapter

        // Button Listener
        addButton.setOnClickListener {
            addExpense()
        }
    }

    private fun addExpense() {
        val name = expenseName.text.toString()
        val amount = expenseAmount.text.toString().toDoubleOrNull()
        val category = categorySpinner.selectedItem.toString()

        if (name.isEmpty() || amount == null) {
            Toast.makeText(this, "Please enter a valid expense name and amount", Toast.LENGTH_SHORT).show()
            return
        }

        val expense = Expense(name, amount, category)
        expenses.add(expense)
        expenseAdapter.notifyItemInserted(expenses.size - 1)

        totalExpenses += amount
        totalTextView.text = "Total: $$totalExpenses"

        Toast.makeText(this, "Added $name to $category", Toast.LENGTH_SHORT).show()

        // Clear input fields
        expenseName.text.clear()
        expenseAmount.text.clear()
        categorySpinner.setSelection(0)
    }
}
