// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.data.settings.repository

import com.rosan.installer.data.settings.local.room.dao.TrustedSignatureDao
import com.rosan.installer.data.settings.local.room.entity.TrustedSignatureEntity
import com.rosan.installer.domain.settings.model.TrustedSignature
import com.rosan.installer.domain.settings.repository.TrustedSignatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrustedSignatureRepositoryImpl(
    private val dao: TrustedSignatureDao
) : TrustedSignatureRepository {

    override fun getAll(): Flow<List<TrustedSignature>> =
        dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun isTrusted(sha256: String): Boolean =
        dao.getBySha256(sha256) != null

    override suspend fun add(name: String, sha256: String) {
        dao.insert(
            TrustedSignatureEntity(
                name = name,
                sha256 = sha256
            )
        )
    }

    override suspend fun delete(sha256: String) {
        dao.deleteBySha256(sha256)
    }

    private fun TrustedSignatureEntity.toDomain() = TrustedSignature(
        id = id,
        name = name,
        sha256 = sha256,
        addedAt = addedAt
    )
}
