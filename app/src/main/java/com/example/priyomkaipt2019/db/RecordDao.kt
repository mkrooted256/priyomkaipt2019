package com.example.priyomkaipt2019.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.priyomkaipt2019.db.Record

@Dao
interface RecordDao {
    @Query("SELECT uid, name_p, name_i, name_b  FROM records ORDER BY timestamp DESC")
    fun getAll(): List<Record>

    @Query("SELECT * FROM records WHERE name_p LIKE :name_p AND name_i LIKE :name_i")
    fun findByName(name_p: String, name_i: String): List<RecordDetails>

    @Query("SELECT * FROM records WHERE uid = :uid LIMIT 1")
    fun findByUid(uid : Int): RecordDetails?

    @Insert
    fun insert(vararg records: RecordDetails)

    @Delete
    fun delete(vararg records: RecordDetails)
}