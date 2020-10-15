package com.example.cnote

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.android.synthetic.main.password_dialog_item.view.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val passwordList = ArrayList<Password>()

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //获取每日一言
//        val url = "https://api.uixsj.cn/hitokoto/get?type=hitokoto&code=json"
//        thread {
//            try {
//                val client = OkHttpClient()
//                val request = Request.Builder()
//                        .url(url)
//                        .build()
//                val response = client.newCall(request).execute()
//                val responseData = response.body?.string()
//                if(responseData != null){
//                    val gson = Gson()
//                    val yiyan = gson.fromJson(responseData, Yiyan::class.java)
//                    passwordYiyanTV.setText(yiyan.content)
//                }
//            } catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
        //获取每日一图
//        val url1 = "https://api.xygeng.cn/Bing/url/"
//        thread {
//            try {
//                val client = OkHttpClient()
//                val request = Request.Builder()
//                        .url(url1)
//                        .build()
//                val response = client.newCall(request).execute()
//                val responseData = response.body?.string()
//                if(responseData != null){
//                    val gson = Gson()
//                    val yitu = gson.fromJson(responseData, Yitu::class.java)
//                    Glide.with(MyApplication.context).load(yitu).into(passwordYituIV)
//                }
//            } catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        //从数据库查询password数据
        val cursor = db.query(Passwords, null, null, null, null, null, null)
        if (cursor.moveToNext()) {
            do {
                val title = cursor.getString(cursor.getColumnIndex(Ptitle))
                val account = cursor.getString(cursor.getColumnIndex(Paccount))
                val password = cursor.getString(cursor.getColumnIndex(Ppassword))
                val explain = cursor.getString(cursor.getColumnIndex(Pexplain))
                val uri = cursor.getString(cursor.getColumnIndex(Puri))
                passwordList.add(Password(title, account, password, uri, explain))
            } while (cursor.moveToNext())
        }
        cursor.close()
        //启动RecyclerView，并传入passwordList
        val recyclerView: RecyclerView
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        passwordRecyclerView.layoutManager = layoutManager
        val adapter = context?.let { PasswordAdapter(it, passwordList) }
        passwordRecyclerView.adapter = adapter
        //添加密码输入页面
        val view: View = View.inflate(context, R.layout.password_dialog_item, null)
        val alert = context?.let { creatAlertDialog(it, view) }
        passwordFab.setOnClickListener {
            alert?.show()
        }
        if(alert != null&&adapter != null){
            setButtonOnClick(alert, view, adapter, )
        }
    }

    override fun onPause() {
        super.onPause()
        //清空passwordList里的数据
        passwordList.clear()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
//    创建AlertDialog对象
    fun creatAlertDialog(context: Context, view: View): AlertDialog{
        val alert = AlertDialog.Builder(context).apply {
            setView(view)
            setCancelable(false)
        }.create()
        return alert
    }
//    设置确定、取消按钮的点击事件
    fun setButtonOnClick(alert: AlertDialog, view: View, adapter: PasswordAdapter,
                         isRestartAlertDialog: Boolean = false, previousTitle: String = "", position: Int = 0){
        view.passwordConfirmBtn.setOnClickListener {
            alert?.dismiss()
            //将数据存入数据库
            val title = view.passwordTitleOfDialog.text.toString()
            val account = view.passwordAccountOfDialog.text.toString()
            val password = view.passwordPasswordOfDialog.text.toString()
            val uri = view.passwordUriOfDialog.text.toString()
            val explain = view.passwordExplainOfDialog.text.toString()
            val value = ContentValues().apply {
                put(Ptitle, title)
                put(Paccount, account)
                put(Ppassword, password)
                put(Puri, uri)
                put(Pexplain, explain)
            }
//                重启的AlertDialog
            if (isRestartAlertDialog == true){
//                passwordList.(Password(title, account, password, uri, explain))
                db.update(Passwords, value, "title = ?", arrayOf(previousTitle))
                passwordList[position] = Password(title, account, password, uri, explain)
                adapter?.notifyItemChanged(position)
            }else{
                db.insert(Passwords, null, value)
                //为新增的数据添加一个adapter
                passwordList.add(Password(title, account, password, uri, explain))
                adapter?.notifyItemInserted(passwordList.size)
            }
        }
        view.passwordCancelBtn.setOnClickListener {
            //在点击取消后清空dialog的内容并关闭dialog
            view.passwordAccountOfDialog.setText("")
            view.passwordPasswordOfDialog.setText("")
            view.passwordUriOfDialog.setText("")
            view.passwordExplainOfDialog.setText("")
            alert?.dismiss()
        }
    }
}