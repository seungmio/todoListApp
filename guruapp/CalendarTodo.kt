package com.example.guruapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class CalendarTodo : AppCompatActivity() {
    lateinit var calendarView: CalendarView
    lateinit var edtTodo: EditText
    lateinit var btnTodo: Button
    lateinit var recyclerView: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    //lateinit var str_name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_todo)

        //id
        val intent = intent
        var str_name = intent.getStringExtra("loginID").toString()


        //acticity_main.xml 요소 연결
        edtTodo = findViewById(R.id.edtTodo)
        btnTodo = findViewById(R.id.btnTodo)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        //calendarView
        calendarView = findViewById(R.id.calendarView)
        //Calendar 인스턴스 생성
        val calendar = Calendar.getInstance()

        //Calendar 날짜를 오늘 날짜로 설정
        calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE)
        )

        //calendarView 날짜 적용
        calendarView.setDate(
            calendar.timeInMillis,
            false,
            false
        )

        //오늘 날짜 저장
        var today = Calendar.getInstance().apply {

            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))

        }.timeInMillis

        today = getIgnoredTimeDays(today)
        setupRecyclerview(str_name, today.toString())

        //캘린더 날짜 선택 시 리스너
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            var date = calendar.timeInMillis
            date = getIgnoredTimeDays(date)
            //Toast.makeText(this, ""+date.toString(), Toast.LENGTH_SHORT).show()

            setupRecyclerview(str_name, date.toString())
            //Toast.makeText(this, "" + year + "/" + (month + 1) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();

            //"등록" 버튼 클릭 시 addWork() 함수 실행
            btnTodo.setOnClickListener {
                val handler = DBHelper(this)
                var str_todo: String = edtTodo.text.toString()
                //var timestamp = getIgnoredTimeDays(System.currentTimeMillis())

                if (str_todo.isNotEmpty()) {
                    val status = handler.addTodo(Work(0, str_name, str_todo, false, date.toString()))
                    if(status > -1){
                        setupRecyclerview(str_name, date.toString())
                        edtTodo.setText(null)
                    }
                }
            }
        }
    }

    //시간, 분, 초, 밀리초 제외시키기
    fun getIgnoredTimeDays(time: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis= time

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    //체크박스 함수
    fun markCompleted(work: Work){
        val handler = DBHelper(this)
        val status = handler.updateTodo(Work(work.colId ,work.id, work.title, work.isChecked, work.timestamp))
        setupRecyclerview(work.id, work.timestamp)
    }

    //item 삭제 함수
    fun deleteWork(work: Work) {
        val handler = DBHelper(this)
        val status = handler.deleteTodo(work)

        if(status > -1){
            setupRecyclerview(work.id, work.timestamp)
        }
    }

    //item 수정 함수
    fun updateWork(work: Work){
        //Dialog를 띄움
        val updateDialog = Dialog(this, R.style.ThemeDialog)
        updateDialog.setCancelable(false)

        updateDialog.setContentView(R.layout.dialog_update_todo)
        val etUpdateTodo = updateDialog.findViewById<TextInputEditText>(R.id.etUpdateTodo)
        etUpdateTodo.setText(edtTodo.text)

        //"update" 버튼 클릭 시 수행
        val btnUpdate = updateDialog.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.btnUpdate)
        btnUpdate.setOnClickListener {
            val text = etUpdateTodo.text.toString()
            val handler = DBHelper(this)

            if (text.isNotEmpty()) {
                val status = handler.updateTodo(Work(work.colId, work.id, text, work.isChecked, work.timestamp))

                if (status > -1){
                    setupRecyclerview(work.id, work.timestamp)
                    updateDialog.dismiss()
                }
            }
            else{
                Toast.makeText(applicationContext, "Todo text can not empty", Toast.LENGTH_SHORT).show()
            }
        }

        //"cancel" 버튼 클릭 시 수행
        val btnUpdateCancel = updateDialog.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.btnUpdateCancel)
        btnUpdateCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.gotoMyPage -> {
                //str_name=intent.getStringExtra("loginID").toString()
                val intent2=intent
                val str_name=intent2.getStringExtra("loginID")

                val intent= Intent(this,MyPage::class.java)

                //Toast.makeText(this,"아이디 : $str_name ", Toast.LENGTH_SHORT).show()
                intent.putExtra("loginID",str_name)

                startActivity(intent)
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerview(id: String, timestamp: String) {
        val handler = DBHelper(this)
        var adapter = MainRvAdapter(this, handler.getAllTodos(id, timestamp))

        //recyclerView를 비워준다.
        adapter.clear()
        recyclerView.adapter = adapter

        if(handler.getAllTodos(id, timestamp).size > 0){
            //recyclerView를 채운다.
            adapter = MainRvAdapter(
                this,
                handler.getAllTodos(id, timestamp)
            )
            //상하로 보여지는 layoutmanager
            layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager

            recyclerView.adapter = adapter
        }
    }
}