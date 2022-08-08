package com.example.guruapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var dbManager:DBManager
    lateinit var sqlDB:SQLiteDatabase
    lateinit var loginID:EditText
    lateinit var loginPW:EditText
    lateinit var btnLogin:Button
    lateinit var btnAddPage:Button
    var dbID:String=""
    var dbPW:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginID=findViewById(R.id.loginID)
        loginPW=findViewById(R.id.loginPW)
        btnLogin=findViewById(R.id.btnLogin)
        btnAddPage=findViewById(R.id.btnAddPage)

        btnLogin.setOnClickListener {
            var log_id:String=loginID.text.toString()
            var log_pw:String=loginPW.text.toString()

            dbManager= DBManager(this,"idDB",null,1)
            sqlDB=dbManager.readableDatabase


            var cursor : Cursor
            cursor=sqlDB.rawQuery("SELECT * FROM idDB WHERE id ='"+log_id+"';",null)

            if(cursor.moveToNext()) {
                dbID=cursor.getString(0)
                dbPW=cursor.getString(1)
            }

            if(log_id.equals(dbID) && log_pw.equals(dbPW)){ //id,pw DB 내 존재
                val intent= Intent(this,CalendarTodo::class.java)
                intent.putExtra("loginID",log_id)

                startActivity(intent)
            }
            else if(!(log_id.equals(dbID))) { //id DB에 존재하지 않을 때
                Toast.makeText(this,"해당하는 ID가 없습니다. \n 회원 가입 창으로 이동합니다.", Toast.LENGTH_SHORT).show()

                val intent= Intent(this,IDreg::class.java)
                startActivity(intent)
            }
            else { //pw 잘못 입력시
                Toast.makeText(this,"비밀번호 오류. \n 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show()
            }

            cursor.close()
            sqlDB.close()
            dbManager.close()
        }

        btnAddPage.setOnClickListener {
            val intent= Intent(this,IDreg::class.java)
            startActivity(intent)

        }
    }
}