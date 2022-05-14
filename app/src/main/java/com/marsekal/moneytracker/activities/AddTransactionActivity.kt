package com.marsekal.moneytracker.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marsekal.moneytracker.DataClass
import com.marsekal.moneytracker.DatabaseHandler
import com.marsekal.moneytracker.R
import com.marsekal.moneytracker.activities.ViewExpensesActivity
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionActivity : AppCompatActivity() {
    var databaseHandler: DatabaseHandler? = null

    lateinit var mAlertDialog: AlertDialog
    lateinit var cal: Calendar
    lateinit var passedMonth: String
    var dataClass: DataClass? = null

    var view: ViewExpensesActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        databaseHandler = DatabaseHandler(this)

        initTodayDate()

        chooseDateBtn.setOnClickListener {
            val dialog = DatePickerDialog(
                this,
                mDateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }

        //buttons click
        addEntryBtn.setOnClickListener {
            addNewEntry(
                expenseName.text.toString(),
                expenseAmount.text.toString(),
                tvDate.text.toString()
            )
        }

    }
    private fun initTodayDate() {
        cal = Calendar.getInstance()
        val mYear = cal.get(Calendar.YEAR)
        val mMonth = cal.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.US)
        val mDay = cal.get(Calendar.DAY_OF_MONTH)

        var strDate: String = mDay.toString() + "-" + mMonth.toString() + "-" + mYear.toString()
        tvDate.setText(strDate)
        passedMonth = mMonth

    }

    //Listener
    val mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val myFormat = "d-MMM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvDate.text = sdf.format(cal.time)
        passedMonth = cal.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.US)

    }

    private fun addNewEntry(expenseName: String, expenseAmount: String, expenseDate: String) {

        if (expenseName.equals("") || expenseAmount.equals("") || expenseDate.equals("")) {
            Toast.makeText(this, "Fill all Fields", Toast.LENGTH_SHORT).show();
        } else {
            //create and fill Object
            val entry: DataClass = DataClass()
            var success: Boolean = false

            entry.expenseName = expenseName
            entry.expenseAmount = expenseAmount
            entry.expenseDate = expenseDate

            success = databaseHandler!!.addExpense(entry)
            if (success) {
                Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
                finish()
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

    }
}