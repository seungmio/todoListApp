package com.example.guruapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

//TodoList DB
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "todos_db"
        private const val TABLE_TODOS = "todos"

        private const val KEY_COLID = "_colId"
        private const val KEY_ID = "_id"
        private const val KEY_TEXT = "text"
        private const val KEY_STATUS = "status"
        private const val KEY_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TODOS_TABLE = (
                "CREATE TABLE $TABLE_TODOS($KEY_COLID INTEGER PRIMARY KEY, $KEY_ID TEXT ,$KEY_TEXT TEXT, $KEY_STATUS BOOLEAN, $KEY_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)"
                )
        db?.execSQL(CREATE_TODOS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TODOS")
        onCreate(db)
    }

    /*
    function
     */

    fun addTodo(todo: Work): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(KEY_ID, todo.id)
        contentValues.put(KEY_TEXT, todo.title)
        contentValues.put(KEY_STATUS, todo.isChecked)
        contentValues.put(KEY_TIMESTAMP, todo.timestamp)

        val success = db.insert(TABLE_TODOS, null, contentValues)
        db.close()

        return success
    }

    @SuppressLint("Range")
    fun getAllTodos(id: String, timestamp: String): ArrayList<Work>{
        val allTodos: ArrayList<Work> = ArrayList<Work>()
        //val selectQuery = "SELECT * FROM $TABLE_TODOS"
        val selectQuery = "SELECT * FROM $TABLE_TODOS WHERE _id ='" + id+ "' AND timestamp ='" + timestamp+ "';"
        val db = this.readableDatabase

        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery,null)
        }
        catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var colId: Int
        var id: String
        var title: String
        var isChecked: Boolean
        var timestamp: String

        if(cursor.moveToFirst()){
            do {
                colId = cursor.getInt(cursor.getColumnIndex(KEY_COLID))
                id = cursor.getString(cursor.getColumnIndex(KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(KEY_TEXT))
                isChecked = cursor.getInt(cursor.getColumnIndex(KEY_STATUS)) == 1
                timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))

                val todo = Work(colId = colId ,id = id, title = title, isChecked = isChecked, timestamp = timestamp)
                allTodos.add(todo)

            }while(cursor.moveToNext())
        }

        db.close()
        return allTodos
    }

    fun updateTodo(todo: Work): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, todo.id)
        contentValues.put(KEY_TEXT, todo.title)
        contentValues.put(KEY_STATUS, todo.isChecked)
        contentValues.put(KEY_TIMESTAMP, todo.timestamp)

        val success = db.update(TABLE_TODOS, contentValues, "$KEY_COLID = ${todo.colId}", null)

        db.close()
        return success
    }

    fun deleteTodo(todo: Work): Int{
        val db = this.writableDatabase

        val success = db.delete(TABLE_TODOS, "$KEY_COLID = ${todo.colId}", null)

        db.close()
        return success
    }

}