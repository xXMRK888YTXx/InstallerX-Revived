// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.domain.settings.usecase.signature

import com.rosan.installer.domain.settings.repository.TrustedSignatureRepository

class ManageTrustedSignatureUseCase(
    private val repository: TrustedSignatureRepository
) {
    suspend fun add(name: String, sha256: String) {
        repository.add(name, sha256)
    }

    suspend fun delete(sha256: String) {
        repository.delete(sha256)
    }
}
