package com.example.guruapp

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Change_PW : AppCompatActivity(){

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var edtId: EditText
    lateinit var edtOriginal:EditText
    lateinit var edtPW: EditText
    lateinit var edtPW2: EditText
    lateinit var btnReg: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_pw)

        edtId=findViewById(R.id.edtID)
        edtOriginal=findViewById(R.id.edtOriginal)
        edtPW=findViewById(R.id.edtPW)
        edtPW2=findViewById(R.id.edtPW2)
        btnReg=findViewById(R.id.btnReg)

        dbManager= DBManager(this,"idDB",null,1)

        btnReg.setOnClickListener {
            var str_pw:String=edtPW.text.toString()
            var str_pw_compare:String=edtPW2.text.toString()


            if(str_pw.equals(str_pw_compare)) {
                sqlitedb = dbManager.writableDatabase
                sqlitedb.execSQL("UPDATE idDB SET password='"+edtPW.text.toString() +"' WHERE id='" +edtId.text.toString()+"';")
                sqlitedb.close ()

                Toast.makeText(this,"비밀번호 변경 완료. 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()

                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this,"비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}