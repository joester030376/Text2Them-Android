package com.app.text2them.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.app.text2them.R

class UserTypeSpinAdapter(
    val context: Context?,
    private val userTypeList: List<String>?
) : BaseAdapter() {
    override fun getCount(): Int {
        return userTypeList!!.size + 1
    }

    override fun getItem(position: Int): Any {
        return userTypeList!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var list: String? = null
        try {
            if (position > 0) {
                list = userTypeList!![position - 1]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        @SuppressLint("ViewHolder")
        val view: View = LayoutInflater.from(context).inflate(R.layout.spinner_list, parent, false)
        val tvCatName: AppCompatTextView = view.findViewById(R.id.tvSpinnerVehicleType)
        if (position == 0) {
            tvCatName.text = context!!.getString(R.string.pleaseSelect)
            tvCatName.isEnabled = false
            tvCatName.isClickable = false
            tvCatName.isFocusable = false
            tvCatName.setTextColor(ContextCompat.getColor(context!!, R.color.gray))
        } else {
            if (list != null) tvCatName.text = list
            tvCatName.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        }
        return view
    }
}