package com.app.text2them.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.app.text2them.R
import com.app.text2them.models.DepartmentModel.Department
import com.app.text2them.models.DesignationModel.Designation

class DesignationSpinnerAdapter (
    private val context: FragmentActivity?,
    private val designationList: List<Designation>?,
) :
    BaseAdapter() {
    override fun getCount(): Int {
        return designationList!!.size + 1
    }

    override fun getItem(position: Int): Any {
        return designationList!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var list: Designation? = null
        try {
            if (position > 0) {
                list = designationList!![position - 1]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        @SuppressLint("ViewHolder")
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.spinner_list, parent, false)
        val tvCatName: AppCompatTextView = view.findViewById(R.id.tvSpinnerVehicleType)
        if (position == 0) {
            tvCatName.text = context!!.getString(R.string.selectDesignation)
            tvCatName.isEnabled = false
            tvCatName.isClickable = false
            tvCatName.isFocusable = false
            tvCatName.setTextColor(ContextCompat.getColor(context!!, R.color.fontColor))
        } else {
            if (list != null) tvCatName.text = list.Name
            tvCatName.setTextColor(ContextCompat.getColor(context!!, R.color.fontColor))
        }
        return view
    }
}