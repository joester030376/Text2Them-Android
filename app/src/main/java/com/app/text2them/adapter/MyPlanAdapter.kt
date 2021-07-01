package com.app.text2them.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.text2them.R
import com.app.text2them.models.MyPlanModel.Usersubscription
import com.app.text2them.utils.AppUtils

class MyPlanAdapter(
    private val requireActivity: FragmentActivity,
    private val planList: List<Usersubscription>,
) : RecyclerView.Adapter<MyPlanAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_my_plan, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: Usersubscription = planList[position]

        holder.txtPlanName.text = data.PlanName
        //holder.txtPlanEndDate.text = data.SubscriptionEndDate
        holder.txtPlanContact.text = data.ContactRangeStart.toString() + "To" + data.ContactRangeEnd

        if (data.SubscriptionEndDate != null) {

            var str = data.SubscriptionEndDate
            val oldValue = "T"
            val newValue = " "
            val output = str.replace(oldValue, newValue)
            holder.txtPlanEndDate.text = AppUtils.parseDate(output)
        }
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtPlanName: AppCompatTextView = itemView.findViewById(R.id.txtPlanName)
        var txtPlanEndDate: AppCompatTextView = itemView.findViewById(R.id.txtPlanEndDate)
        var txtPlanContact: AppCompatTextView = itemView.findViewById(R.id.txtPlanContact)
    }
}