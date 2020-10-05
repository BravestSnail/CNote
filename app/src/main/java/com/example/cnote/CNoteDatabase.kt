package com.example.cnote

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CNoteDatabase (val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version){

    private val createNoteTable = "create table Notes (" +
            "id integer primary key autoincrement," +
            "title text," +
            "content text)"

    private val creatPasswordTable = "create table Passwords (" +
            "id integer primary key autoincrement," +
            "title text," +
            "account text" +
            "password text" +
            "uri text" +
            "explain text)"

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL(createNoteTable)
            db.execSQL(creatPasswordTable)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}