package com.app.text2them.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.text2them.R
import com.app.text2them.fragment.MessageQueueFragment
import com.app.text2them.models.MSGQueueModel.Data

class MSGQueueAdapter(
    private val requireActivity: FragmentActivity,
    private val msgQueueList: List<Data>,
    private val messageQueueFragment: MessageQueueFragment
) : RecyclerView.Adapter<MSGQueueAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_msg_queue, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: Data = msgQueueList[position]

        holder.txtName.text = data.Keyword
        holder.txtMsgContent.text = data.MessageContent
        holder.txtDate.text = data.MessageDate

        holder.btnAccept.setOnClickListener {
            messageQueueFragment.acceptApi(data.CampaignGUID,data.ReplyFrom,position)
        }
    }

    override fun getItemCount(): Int {
        return msgQueueList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtName: AppCompatTextView = itemView.findViewById(R.id.txtName)
        var txtMsgContent: AppCompatTextView = itemView.findViewById(R.id.txtMsgContent)
        var txtDate: AppCompatTextView = itemView.findViewById(R.id.txtDate)
        var btnAccept: AppCompatTextView = itemView.findViewById(R.id.btnAccept)
    }
}