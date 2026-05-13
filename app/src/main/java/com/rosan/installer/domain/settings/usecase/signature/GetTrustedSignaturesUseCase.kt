// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.domain.settings.usecase.signature

import com.rosan.installer.domain.settings.model.TrustedSignature
import com.rosan.installer.domain.settings.repository.TrustedSignatureRepository
import kotlinx.coroutines.flow.Flow

class GetTrustedSignaturesUseCase(
    private val repository: TrustedSignatureRepository
) {
    operator fun invoke(): Flow<List<TrustedSignature>> = repository.getAll()
}
