package com.example.cnote

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_note_item.*

class AddNoteActivity : AppCompatActivity() {
    companion object{
        //这是一个启动AddNoteActivity的方法
        fun actionStart(context: Context, note: Note){
            val intent = Intent(context, AddNoteActivity::class.java).apply {
                putExtra(cnoteOfTitle, note.title)
                putExtra(cnoteOfContent, note.content)
                putExtra(index, AgainStart)
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        //设置toolbar
        setSupportActionBar(addNoteToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        //判断是否是再次启动AddNoteActivity，若是则为EditText设置初始文本
        if (AgainStart == intent.getIntExtra(index, 1)){
            val title = intent.getStringExtra(cnoteOfTitle)
            val content = intent.getStringExtra(cnoteOfContent)
            if (title.isNotEmpty()){
                addNoteTitle.setText(title)
                addNoteTitle.setSelection(title.length)
            }
            if (content.isNotEmpty()){
                addNoteContent.setText(content)
                addNoteContent.setSelection(content.length)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        //判断是否是再次启动AddNoteActivity，若是则更新数据库的数据
        val isNoteAgainStart = (intent.getIntExtra(index, 1) == AgainStart)
        val noteTitle = addNoteTitle.text.toString()
        val noteContent = addNoteContent.text.toString()
        val value = ContentValues().apply {
            if(noteTitle != "" || noteContent != ""){
                put("title", noteTitle)
                put("content", noteContent)
            }
        }
        if(isNoteAgainStart){
            val previousNoteTitle = intent.getStringExtra(cnoteOfTitle)
            db.update(Notes, value, "title = ?", arrayOf(previousNoteTitle))
        }else{
            db.insert("Notes", null, value)
        }
    }
}