package com.app.text2them.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.text2them.R
import com.app.text2them.fragment.DepartmentListFragment
import com.app.text2them.models.DepartmentModel.Department

class DepartmentAdapter(
    private val requireActivity: FragmentActivity,
    private val departmentList: List<Department>,
    private val departmentListFragment: DepartmentListFragment
) : RecyclerView.Adapter<DepartmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_department, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: Department = departmentList[position]


        holder.txtDepartment.text = data.Name

//        holder.imgEdit.setOnClickListener {
//            departmentListFragment.editUser(data.id)
//        }

        holder.imgDelete.setOnClickListener {
            departmentListFragment.deleteConfirmDialog(data.id, position)
        }
    }

    override fun getItemCount(): Int {
        return departmentList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtDepartment: AppCompatTextView = itemView.findViewById(R.id.txtDepartment)
        var imgEdit: AppCompatImageView = itemView.findViewById(R.id.imgEdit)
        var imgDelete: AppCompatImageView = itemView.findViewById(R.id.imgDelete)
    }
}