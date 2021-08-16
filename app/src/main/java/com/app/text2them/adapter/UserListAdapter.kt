package com.app.text2them.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
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
            holder.txtActive.visibility = View.VISIBLE
            holder.txtDiactive.visibility = View.GONE
            holder.txtActive.text="Active"
            //holder.imgStatus.background = ContextCompat.getDrawable(requireActivity, R.drawable.right_tick)
        } else {
            holder.txtActive.visibility = View.GONE
            holder.txtDiactive.visibility = View.VISIBLE
            holder.txtDiactive.text="Deactive"
            //holder.imgStatus.background = ContextCompat.getDrawable(requireActivity, R.drawable.cross_tick)
        }

        holder.imgView.setOnClickListener {
            usersFragment.getUserDetailsApi(data.id)
        }

        holder.imgEdit.setOnClickListener {
            usersFragment.editUser(data.id)
        }

        holder.imgDelete.setOnClickListener {
            usersFragment.deleteConfirmDialog(data.id, position)
        }

        holder.txtEditNumber.setOnClickListener {
            usersFragment.changeNumberDialog(data.id)
        }
    }

    override fun getItemCount(): Int {
        return staffList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtName: AppCompatTextView = itemView.findViewById(R.id.txtName)
        var txtActive: AppCompatTextView = itemView.findViewById(R.id.txtActive)
        var txtDiactive: AppCompatTextView = itemView.findViewById(R.id.txtDiactive)
        var txtDepartment: AppCompatTextView = itemView.findViewById(R.id.txtDepartment)
        var txtDesignation: AppCompatTextView = itemView.findViewById(R.id.txtDesignation)
        var txtPhoneNumber: AppCompatTextView = itemView.findViewById(R.id.txtPhoneNumber)
        var txtEditNumber: AppCompatTextView = itemView.findViewById(R.id.txtEditNumber)
        var imgView: AppCompatImageView = itemView.findViewById(R.id.imgView)
        var imgEdit: AppCompatImageView = itemView.findViewById(R.id.imgEdit)
        var imgDelete: AppCompatImageView = itemView.findViewById(R.id.imgDelete)
    }
}