package com.app.text2them.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.app.text2them.R
import com.app.text2them.fragment.SendMessageUserFragment
import com.app.text2them.models.MessageToUserModel.UserListData

class SendMessageToUserAdapter(
    private val userType: Int,
    private val userList: List<UserListData>,
    private val sendMessageUserFragment: SendMessageUserFragment
) : RecyclerView.Adapter<SendMessageToUserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_send_message_to_user, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userListData: UserListData = userList[position]

        holder.txtName.text = userListData.Name
        if (userType==1){
            holder.txtCompanyName.text = userListData.Department
        }else{
            holder.txtCompanyName.text = userListData.CompanyName
        }

        holder.txtNumber.text = userListData.phonenumber

        holder.chkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sendMessageUserFragment.addValue(userListData.NetworkEmailID, userListData.phonenumber)
            } else {
                sendMessageUserFragment.removeValue(userListData.NetworkEmailID, userListData.phonenumber)
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtName: AppCompatTextView = itemView.findViewById(R.id.txtName)
        var txtCompanyName: AppCompatTextView = itemView.findViewById(R.id.txtCompanyName)
        var txtNumber: AppCompatTextView = itemView.findViewById(R.id.txtNumber)
        var chkBox: CheckBox = itemView.findViewById(R.id.chkBox)
    }
}