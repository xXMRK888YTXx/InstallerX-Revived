// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.domain.settings.usecase.signature

import com.rosan.installer.domain.settings.repository.TrustedSignatureRepository

class IsSignatureTrustedUseCase(
    private val repository: TrustedSignatureRepository
) {
    suspend operator fun invoke(sha256: String?): Boolean {
        if (sha256 == null) return false
        return repository.isTrusted(sha256)
    }
}
