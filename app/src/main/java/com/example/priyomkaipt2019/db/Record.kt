package com.example.priyomkaipt2019.db

import androidx.room.*
import java.util.*


data class Record(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name_p") val name_p: String?,
    @ColumnInfo(name = "name_i") val name_i: String?,
    @ColumnInfo(name = "name_b") val name_b: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long?
)
