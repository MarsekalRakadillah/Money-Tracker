package com.marsekal.moneytracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marsekal.moneytracker.DataClass
import com.marsekal.moneytracker.R
import kotlinx.android.synthetic.main.row_expenses.view.*

class CustomAdapter(
        var listData: ArrayList<DataClass>,
        val listener:OnAdapterListener
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_expenses, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = listData[position]
        holder.view.row_expenseName.text = data.expenseName
        holder.view.row_expenseAmount.text = data.expenseAmount
        holder.view.row_expenseDate.text = data.expenseDate

        holder.itemView.setOnClickListener{
            listener.onClick(data)
        }
    }

    override fun getItemCount() = listData.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface OnAdapterListener {
        fun onClick(data: DataClass)
    }

    fun setData(data: List<DataClass>){
        listData.clear()
        listData.addAll(data)
        notifyDataSetChanged()
    }

}