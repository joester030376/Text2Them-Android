package com.app.text2them.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.app.text2them.R
import kotlinx.android.synthetic.main.dialog_change_number.*
import java.util.*

class ChangeNumberDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_change_number)

        val v = Objects.requireNonNull(window)!!.decorView
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window!!.setGravity(Gravity.CENTER)
        v.setBackgroundResource(android.R.color.transparent)
        setCanceledOnTouchOutside(false)
        setCancelable(true)

        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}