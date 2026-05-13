// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.domain.settings.repository

import com.rosan.installer.domain.settings.model.TrustedSignature
import kotlinx.coroutines.flow.Flow

interface TrustedSignatureRepository {
    fun getAll(): Flow<List<TrustedSignature>>
    suspend fun isTrusted(sha256: String): Boolean
    suspend fun add(name: String, sha256: String)
    suspend fun delete(sha256: String)
}
