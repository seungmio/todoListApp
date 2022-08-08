package com.example.guruapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class IDreg  : AppCompatActivity()  {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var edtId:EditText
    lateinit var edtPW:EditText
    lateinit var edtPW2:EditText
    lateinit var btnReg:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addid)

        edtId=findViewById(R.id.edtID)
        edtPW=findViewById(R.id.edtPW)
        edtPW2=findViewById(R.id.edtPW2)
        btnReg=findViewById(R.id.btnReg)

        dbManager= DBManager(this,"idDB",null,1)

        btnReg.setOnClickListener {
            var str_id:String=edtId.text.toString()
            var str_pw:String=edtPW.text.toString()
            var str_pw_compare:String=edtPW2.text.toString()

            if(str_pw.equals(str_pw_compare)) {
                sqlitedb = dbManager.writableDatabase
                sqlitedb.execSQL ("INSERT INTO idDB VALUES ('" + str_id + "','" + str_pw + "')")
                sqlitedb.close ()

                Toast.makeText(this,"회원 가입이 완료 되었습니다.", Toast.LENGTH_SHORT).show()

                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this,"비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}