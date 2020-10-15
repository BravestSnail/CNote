package com.example.cnote

val CNoteDb = "CNote.db"
val Notes = "Notes"
val index = "index"
val cnoteOfTitle = "title"
val cnoteOfContent = "content"
const val AgainStart = 2
const val FragStart = 1
val dbHelper = CNoteDatabase(MyApplication.context, CNoteDb, 1)
val db = dbHelper.writableDatabase
val Passwords = "Passwords"
val Ptitle = "title"
val Paccount = "account"
val Ppassword = "password"
val Puri = "uri"
val Pexplain = "explain"