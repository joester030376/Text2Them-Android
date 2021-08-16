package com.app.text2them.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.app.text2them.R
import com.app.text2them.models.ChatModelSingle.Sendmessage

class ChatAdapterOneWay(
    private val chatListOneWay: List<Sendmessage>,
) :
    RecyclerView.Adapter<ChatAdapterOneWay.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_chat_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: Sendmessage = chatListOneWay[position]

        holder.linearRight.visibility = View.VISIBLE
        holder.linearLeft.visibility = View.GONE
        holder.txtRight.text = data.Message
        holder.txtRightDate.text = data.CreatedDateTimeStr

    }

    override fun getItemCount(): Int {
        return chatListOneWay.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtLeft: AppCompatTextView = itemView.findViewById(R.id.txtLeft)
        var txtRight: AppCompatTextView = itemView.findViewById(R.id.txtRight)
        var linearLeft: RelativeLayout = itemView.findViewById(R.id.linearLeft)
        var linearRight: RelativeLayout = itemView.findViewById(R.id.linearRight)
        var txtLeftDate: AppCompatTextView = itemView.findViewById(R.id.txtLeftDate)
        var txtRightDate: AppCompatTextView = itemView.findViewById(R.id.txtRightDate)
    }
}
