package com.example.cnote

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class NoteAdapter(val context: Context, var noteList: MutableList<Note>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val noteTitle: TextView = view.findViewById(R.id.cnoteTitleTV)
        val noteContent: TextView = view.findViewById(R.id.cnoteContentTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_note_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]
        holder.noteTitle.text = note.title
        holder.noteContent.text = note.content
        //注册Recyclerview里子控件的点击事件,并再次启动AddNoteActivity
        holder.itemView.setOnClickListener{
            AddNoteActivity.actionStart(MyApplication.context, noteList[position])
        }
        //注册Recyclerview的长按事件，并提示是否删除该便签
        holder.itemView.setOnLongClickListener {
            Snackbar.make(it,"确定删除此便签？", Snackbar.LENGTH_LONG)
                .setAction("确定"){
                    val deleteNote = noteList[position]
                    db.delete(Notes, "title = ?", arrayOf(noteList[position].title))
                    noteList.remove(deleteNote)
                    notifyItemRemoved(position)
                }
                .show()
            Log.d("test1", "setonlongclicklistener ok")
            true
        }
    }

    override fun getItemCount() = noteList.size
}