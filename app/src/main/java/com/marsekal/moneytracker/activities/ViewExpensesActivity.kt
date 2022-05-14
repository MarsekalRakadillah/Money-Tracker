package com.marsekal.moneytracker.activities

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.marsekal.moneytracker.DataClass
import com.marsekal.moneytracker.DatabaseHandler
import com.marsekal.moneytracker.R
import com.marsekal.moneytracker.adapter.CustomAdapter
import kotlinx.android.synthetic.main.activity_view_expenses.*


class ViewExpensesActivity : AppCompatActivity(), AdapterView.OnItemLongClickListener{

    lateinit var entriesArrayList: ArrayList<DataClass>
    lateinit var dataList: List<DataClass>

    var databaseHandler: DatabaseHandler? = null

    private lateinit var customAdapter: CustomAdapter
    var dataClass: DataClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_expenses)

        databaseHandler = DatabaseHandler(this)

        var str = intent.getStringExtra("comeFromExpensesByMonth")
        if (!TextUtils.isEmpty(str)) {
            if (str.equals("yes")) {

                var incomingMonth = intent.getStringExtra("monthName")
                var entryArrayList = databaseHandler!!.getAllUsers()
                //filter according to current month
                var filteredArrList = arrayListOf<DataClass>()
                for (item in entryArrayList) {
                    if (item.expenseDate.contains(incomingMonth.toString())) {
                        filteredArrList.add(item)
                    }
                }
                entriesArrayList = filteredArrList
                setupRecylerView()
                showData(dataList)
                calculateTotal(entriesArrayList)

            }
        } else {
            val intent = intent
            val args = intent.getBundleExtra("BUNDLE")
            if (args != null) {
                entriesArrayList = args.getSerializable("ARRAYLIST") as ArrayList<DataClass>
            }

            setupRecylerView()
            calculateTotal(entriesArrayList)
        }

    }

    private fun calculateTotal(entriesArrayList: ArrayList<DataClass>) {
        var total: Int = 0
        for (item in entriesArrayList) {
            total = total + item.expenseAmount.toInt()
        }
        totalExpenses.text = "= " + total.toString()
    }

    private fun setupRecylerView() {
        customAdapter= CustomAdapter(arrayListOf(), object : CustomAdapter.OnAdapterListener {
            override fun onClick(result: DataClass) {
//                tvPbResponse.text = result.title
//                Toast.makeText(applicationContext, result.title,
//                        Toast.LENGTH_SHORT).show()
//                startActivity(
//                        Intent(applicationContext, HomeDetailActivity::class.java)
//                                .putExtra("intent_anoun", result.anoun)
//                                .putExtra("intent_date", result.date)
//                                .putExtra("intent_title", result.title)
//                                .putExtra("intent_body", result.body)
//                )
            }
        })

        viewExpensesListView.apply {
            layoutManager= LinearLayoutManager(applicationContext)
            adapter= customAdapter

        }

    }

    private fun showData(datas : List<DataClass>) {
        val results = datas
        customAdapter.setData(results)
        for (data in results) {

        }
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        var txt: TextView = view!!.findViewById(R.id.row_id)
        var id = txt.text
        showMyDialog(id.toString(), position)

        return true;
    }

//    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//
//        var trimmedExpense = row_expenseName.text.toString().substring(0,3)
//        val i = Intent(this, AddTransactionActivity::class.java)
//        i.putExtra("comeFromExpenses", position)
//        startActivity(i)
//
//    }

    private fun showMyDialog(id: String, position: Int) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Remove Expense")
        builder.setMessage("Do you Want to Delete this Expense?")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    deleteNow(id, position)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                }
            }
        }
        builder.setPositiveButton("YES", dialogClickListener)
        builder.setNegativeButton("NO", dialogClickListener)
        dialog = builder.create()
        dialog.show()
    }

    private fun deleteNow(id: String, position: Int) {
        var success: Boolean = false
        success = databaseHandler!!.deleteEntry(id)
        if (success) {
            Toast.makeText(this@ViewExpensesActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            customAdapter.notifyDataSetChanged()
            calculateAgain()
        } else {
            Toast.makeText(this@ViewExpensesActivity, "Error Occurred", Toast.LENGTH_SHORT).show();
        }

    }

    private fun calculateAgain() {
        var total: Int = 0
        for (item in entriesArrayList) {
            total = total + item.expenseAmount.toInt()
        }
        totalExpenses.text = "= " + total.toString()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.my_second_menu, menu)
        var myMenuItem = menu!!.findItem(R.id.menuTotal)
        myMenuItem.title = entriesArrayList.size.toString()
        return super.onCreateOptionsMenu(menu)
    }


}
