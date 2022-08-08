package com.example.guruapp

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*

//title, 체크 여부를 어느 View에 넣을 것인지 연결해주는 역할을 하는 class
class MainRvAdapter(
    val context: Context,
    private val todoList: ArrayList<Work>
    ):
    RecyclerView.Adapter<MainRvAdapter.ViewHolder>() {

    //recyclerview 초기화를 위한 함수
    fun clear() {
        todoList.clear()
        notifyDataSetChanged()
    }

    //항목에 사용할 View 생성, ViewHolder 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRvAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_list, parent, false)
        return ViewHolder(v)
    }

    //항목 뷰에 데이터 연결
    override fun onBindViewHolder(holder: MainRvAdapter.ViewHolder, position: Int) {
        val todo = todoList[position]

        holder.tvTodoItem.text = todo.title
        holder.cbCheck.isChecked = todo.isChecked

        // todo가 완료된 상태라면 글자색 변경 후 취소선 추가
        if(todo.isChecked) {
            holder.tvTodoItem.apply {
                setTextColor(Color.GRAY)
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }
        } else {
            // 완료 상태가 아니라면 글자색 복구, 취소선 없앰
            holder.tvTodoItem.apply {
                setTextColor(Color.BLACK)
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }
        }
        
        //delbtn이 눌렸을 때 todoList[position]을 전달
        holder.ivDel.setOnClickListener {
            if (context is CalendarTodo){
                context.deleteWork(todo)
            }
        }

        //editbtn이 눌렸을 때
        holder.ivEdit.setOnClickListener {
            if (context is CalendarTodo){
                context.updateWork(todo)
            }
        }

        //체크박스가 눌렸을 때
        holder.cbCheck.setOnClickListener {
            if (context is CalendarTodo){
                context.markCompleted(Work(todo.colId, todo.id, todo.title, !todo.isChecked, todo.timestamp))
            }
        }
    }

    //아이템 개수를 가져온다.
    override fun getItemCount(): Int {
        return todoList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvTodoItem = itemView.findViewById<TextView>(R.id.tvTodoItem)
        val cbCheck = itemView.findViewById<CheckBox>(R.id.cbCheck)
        val ivDel = itemView.findViewById<ImageView>(R.id.ivDel)
        val ivEdit = itemView.findViewById<ImageView>(R.id.ivEdit)

    }
}