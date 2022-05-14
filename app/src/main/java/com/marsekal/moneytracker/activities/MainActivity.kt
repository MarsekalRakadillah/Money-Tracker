package com.marsekal.moneytracker.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.marsekal.moneytracker.DataClass
import com.marsekal.moneytracker.DatabaseHandler
import com.marsekal.moneytracker.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mAlertDialog: AlertDialog
    lateinit var cal: Calendar
    lateinit var passedMonth: String

    var databaseHandler: DatabaseHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHandler = DatabaseHandler(this)
        initTodayDate()

        addNewExpense.setOnClickListener {
            val intent = Intent(this@MainActivity, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        viewExpenses.setOnClickListener {
            //get all data
            var entryArrayList = databaseHandler!!.getAllUsers()
            //filter according to current month
//                cal = Calendar.getInstance()
//                val mMonth = cal.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.US)
            var filteredArrList = arrayListOf<DataClass>()
            for (item in entryArrayList) {
                if (item.expenseDate.contains(passedMonth)) {
                    filteredArrList.add(item)
                }
            }
            val intent = Intent(this@MainActivity, ViewExpensesActivity::class.java)
            val args = Bundle()
            args.putSerializable("ARRAYLIST", filteredArrList as Serializable)
            intent.putExtra("BUNDLE", args)
            startActivity(intent)
        }



        viewExpensesByMonth.setOnClickListener {
            val intent = Intent(this@MainActivity, ExpensesByMonthActivity::class.java)
            startActivity(intent)
        }

        //Listener
        val mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "d-MMM-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            selectedDateTextView.text = sdf.format(cal.time)
            passedMonth = cal.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.US)

        }
    }
    private fun initTodayDate() {
        cal = Calendar.getInstance()
        val mYear = cal.get(Calendar.YEAR)
        val mMonth = cal.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.US)
        val mDay = cal.get(Calendar.DAY_OF_MONTH)

        var strDate: String = mDay.toString() + "-" + mMonth.toString() + "-" + mYear.toString()
        selectedDateTextView.setText(strDate)
        passedMonth = mMonth

    }




    private fun showMyDialog() {


//        expenseNameTxtView.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
//            }
//        })
    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            R.id.myMenu -> {
                showConfirmDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showConfirmDialog() {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Clear Database")
        builder.setMessage("Do you Want to Clear All Database?")
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    databaseHandler!!.deleteAllEntries()
                    Toast.makeText(this@MainActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
                DialogInterface.BUTTON_NEGATIVE -> {}
            }
        }
        builder.setPositiveButton("YES",dialogClickListener)
        builder.setNegativeButton("NO",dialogClickListener)
        dialog = builder.create()
        dialog.show()
    }
}