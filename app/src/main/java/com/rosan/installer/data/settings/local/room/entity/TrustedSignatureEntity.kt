// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.data.settings.local.room.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "trusted_signatures",
    indices = [Index(value = ["sha256"], unique = true)]
)
data class TrustedSignatureEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "sha256")
    val sha256: String,
    @ColumnInfo(name = "added_at")
    val addedAt: Long = System.currentTimeMillis()
)
