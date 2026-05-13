// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.data.settings.local.room.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.rosan.installer.data.settings.local.room.entity.TrustedSignatureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrustedSignatureDao {
    @Query("SELECT * FROM trusted_signatures ORDER BY added_at DESC")
    fun getAll(): Flow<List<TrustedSignatureEntity>>

    @Query("SELECT * FROM trusted_signatures WHERE sha256 = :sha256 LIMIT 1")
    suspend fun getBySha256(sha256: String): TrustedSignatureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TrustedSignatureEntity): Long

    @Delete
    suspend fun delete(entity: TrustedSignatureEntity)

    @Query("DELETE FROM trusted_signatures WHERE sha256 = :sha256")
    suspend fun deleteBySha256(sha256: String)
}
