package com.example.guruapp

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.net.URI
import java.util.*
import java.util.jar.Manifest

class MyPage : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var imageView:ImageView
    lateinit var edtID:TextView
    lateinit var btnImage:Button
    lateinit var btnMain:Button
    lateinit var btnPW:Button
    private val REQUEST_READ_EXTERNAL_STORAGE=1000
    lateinit var str_name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        imageView=findViewById(R.id.imageView)
        edtID=findViewById(R.id.edtID)
        btnImage=findViewById(R.id.btnImage)
        btnMain=findViewById(R.id.btnMain)
        btnPW=findViewById(R.id.btnPW)
       // val STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val intent=intent
        var setImage:Int=0
        str_name=intent.getStringExtra("loginID").toString()
        edtID.text=str_name

        btnImage.setOnClickListener {

            if(setImage.equals(0)) {
                imageView.setImageResource(R.drawable.character2)
                setImage++
            }
            else {
                imageView.setImageResource(R.drawable.character)
                setImage--
            }

/*
            if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
            )

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                ) {
                    var dlg = AlertDialog.Builder(this)
                    dlg.setTitle("권한이 필요한 이유")
                    dlg.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다.")
                    dlg.setPositiveButton("확인") { dialog, which ->
                        ActivityCompat.requestPermissions(
                                this@MyPage,
                                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                                REQUEST_READ_EXTERNAL_STORAGE
                        )
                    }
                    dlg.setNegativeButton("취소", null)
                    dlg.show()
                } else {
                    ActivityCompat.requestPermissions(
                            this@MyPage,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE
                    )
                } else { //권한 허용
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                //val intent=Intent(Intent.ACTION_GET_CONTENT)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                intent.action=Intent.ACTION_GET_CONTENT
                startActivity(intent)
*/
*/

           Toast.makeText(this,"이미지 변경 완료", Toast.LENGTH_SHORT).show()
        }

        btnPW.setOnClickListener {
            val intent=Intent(this,Change_PW::class.java)
            startActivity(intent)
        }

        btnMain.setOnClickListener {
            val intent=Intent(this,CalendarTodo::class.java)
            intent.putExtra("loginID", str_name)
            startActivity(intent)
        }
    }


}
