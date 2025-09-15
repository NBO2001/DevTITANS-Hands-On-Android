package com.example.plaintext.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.plaintext.data.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PasswordDao : BaseDao<Password> {

    /**
     * Recovery all storage passwords
     */
    @Query("SELECT * FROM passwords")
    abstract fun getAllPasswords(): Flow<List<Password>>

    /**
     * Return the password with the given id
     */
    @Query("SELECT * FROM passwords WHERE id = :id")
    abstract fun get(id: Int): Password?
}