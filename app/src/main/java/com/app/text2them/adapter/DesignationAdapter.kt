package com.app.text2them.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.text2them.R
import com.app.text2them.fragment.DesignationListFragment
import com.app.text2them.models.DepartmentModel.Department
import com.app.text2them.models.DesignationModel.Designation

class DesignationAdapter(
    private val requireActivity: FragmentActivity,
    private val designationList: List<Designation>,
    private val designationListFragment: DesignationListFragment
) : RecyclerView.Adapter<DesignationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_designation, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: Designation = designationList[position]

        holder.txtDesignation.text = data.Name

//        holder.imgEdit.setOnClickListener {
//            departmentListFragment.editUser(data.id)
//        }
//
//        holder.imgDelete.setOnClickListener {
//            departmentListFragment.deleteConfirmDialog(data.id, position)
//        }
    }

    override fun getItemCount(): Int {
        return designationList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtDesignation: AppCompatTextView = itemView.findViewById(R.id.txtDesignation)
        var imgEdit: AppCompatImageView = itemView.findViewById(R.id.imgEdit)
        var imgDelete: AppCompatImageView = itemView.findViewById(R.id.imgDelete)
    }
}