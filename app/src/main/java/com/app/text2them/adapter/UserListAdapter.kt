package com.app.text2them.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.text2them.R
import com.app.text2them.fragment.UsersFragment
import com.app.text2them.models.UserListModel.Staffmember

class UserListAdapter(
    private val requireActivity: FragmentActivity,
    private val staffList: List<Staffmember>,
    private val usersFragment: UsersFragment
) : RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_users_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: Staffmember = staffList[position]

        holder.txtName.text = data.Name
        holder.txtDepartment.text = data.DepartmentName
        holder.txtDesignation.text = data.DesignationName
        holder.txtPhoneNumber.text = data.PhoneNumber
        if (data.IsActive) {
            holder.txtStatus.text = "Active"
        } else {
            holder.txtStatus.text = "Inactive"
        }
    }

    override fun getItemCount(): Int {
        return staffList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtName: AppCompatTextView = itemView.findViewById(R.id.txtName)
        var txtDepartment: AppCompatTextView = itemView.findViewById(R.id.txtDepartment)
        var txtDesignation: AppCompatTextView = itemView.findViewById(R.id.txtDesignation)
        var txtPhoneNumber: AppCompatTextView = itemView.findViewById(R.id.txtPhoneNumber)
        var txtStatus: AppCompatTextView = itemView.findViewById(R.id.txtStatus)
    }
}