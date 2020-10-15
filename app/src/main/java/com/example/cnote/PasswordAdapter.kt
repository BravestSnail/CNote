package com.example.cnote

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_password_item.view.*
import kotlinx.android.synthetic.main.password_dialog_item.view.*

class PasswordAdapter(val context: Context, var passwordList: MutableList<Password>) :
    RecyclerView.Adapter<PasswordAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.passwordTitleTV)
        val account: TextView = view.findViewById(R.id.passwordAccountTV)
        val password: TextView = view.findViewById(R.id.passwordTV)
        val uri: TextView = view.findViewById(R.id.passwordUriTV)
        val explain: TextView = view.passwordExplainTV
        val accountBtn: Button = view.passwordAccountBtn
        val passwordBtn: Button = view.passwordBtn
        val uriBtn: Button = view.passwordUriBtn
        val explainBtn: Button = view.passwordExplainBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_password_item, parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("ServiceCast")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val passwordPA = passwordList[position]
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
        Log.d("tagg", ClipboardManager::class.java.toString())
//        设置各个TextView的内容
        holder.title.setText(passwordPA.title)
        holder.account.setText(passwordPA.account)
        holder.password.setText(passwordPA.password)
        holder.uri.setText(passwordPA.uri)
        holder.explain.setText(passwordPA.explain)
        //设置fragment_password_item里按钮的点击事件，复制相应的数据到剪切板
        holder.accountBtn.setOnClickListener {
            val clip: ClipData = ClipData.newPlainText("simple text", passwordPA.account)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "账号复制成功", Toast.LENGTH_SHORT).show()
        }
        holder.passwordBtn.setOnClickListener {
            val clip: ClipData = ClipData.newPlainText("simple text", passwordPA.password)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "密码复制成功", Toast.LENGTH_SHORT).show()
        }
        holder.uriBtn.setOnClickListener {
            val clip: ClipData = ClipData.newPlainText("simple text", passwordPA.uri)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "链接复制成功", Toast.LENGTH_SHORT).show()
        }
        holder.explainBtn.setOnClickListener {
            val clip: ClipData = ClipData.newPlainText("simple text", passwordPA.explain)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "说明复制成功", Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnLongClickListener {
            //设置长按删除密签
            Snackbar.make(it,"确定删除此密签？", Snackbar.LENGTH_LONG)
                .setAction("确定"){
                    val deleteNote = passwordList[position]
                    db.delete(Passwords, "title = ?", arrayOf(passwordList[position].title))
                    passwordList.remove(deleteNote)
                    notifyItemRemoved(position)
                }
                .show()
            true
        }
        //设置点击修改密签事件
        val view: View = View.inflate(context, R.layout.password_dialog_item, null)
        val alert = context?.let { PasswordFragment().creatAlertDialog(context, view) }
        view.passwordTitleOfDialog.setText(passwordPA.title)
        view.passwordAccountOfDialog.setText(passwordPA.account)
        view.passwordPasswordOfDialog.setText(passwordPA.password)
        view.passwordUriOfDialog.setText(passwordPA.uri)
        view.passwordExplainOfDialog.setText(passwordPA.explain)
        PasswordFragment().setButtonOnClick(alert, view, this, true, passwordPA.title, position)
        holder.itemView.setOnClickListener {
            alert?.show()
        }
    }

    override fun getItemCount() = passwordList.size
}