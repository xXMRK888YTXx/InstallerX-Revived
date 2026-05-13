// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.ui.page.main.settings.preferred.signature

import com.rosan.installer.domain.settings.model.TrustedSignature

data class TrustedSignaturesViewState(
    val signatures: List<TrustedSignature> = emptyList()
)
