package com.example.cnote

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class PasswordAdapter(val context: Context, var passwordList: MutableList<Password>) :
    RecyclerView.Adapter<PasswordAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val passwordTitle: EditText = view.findViewById(R.id.passwordTitleET)
        val passwordAccount: EditText = view.findViewById(R.id.passwordAccountET)
        val password: EditText = view.findViewById(R.id.passwordET)
        val passwordUri: EditText = view.findViewById(R.id.passwordUriET)
        val passwordExplain: EditText = view.findViewById(R.id.passwordExplainET)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_password_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val passwordPA = passwordList[position]
        holder.passwordTitle.setText(passwordPA.title)
        holder.passwordAccount.setText(passwordPA.account)
        holder.password.setText(passwordPA.password)
        holder.passwordUri.setText(passwordPA.uri)
        holder.passwordExplain.setText(passwordPA.explain)
    }

    override fun getItemCount() = passwordList.size
}