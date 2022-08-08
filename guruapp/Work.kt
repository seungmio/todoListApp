package com.example.guruapp

import java.io.Serializable

//data class
//할 일, 체크박스 체크 여부
class Work  (
    var colId: Int,
    var id: String,
    var title: String,
    var isChecked: Boolean,
    var timestamp: String
)