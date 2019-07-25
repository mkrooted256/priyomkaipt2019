package com.example.priyomkaipt2019.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "records")
data class RecordDetails(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name_p") val name_p: String?,
    @ColumnInfo(name = "name_i") val name_i: String?,
    @ColumnInfo(name = "name_b") val name_b: String?,

    @ColumnInfo(name = "envelope_address") val envelope_address: String?,
    @ColumnInfo(name = "phone")            val phone: String?,
    @ColumnInfo(name = "tg")               val tg: String?,
    @ColumnInfo(name = "form_json")        val form_json: String?,

    @ColumnInfo(name = "timestamp") val timestamp: Long?
)
