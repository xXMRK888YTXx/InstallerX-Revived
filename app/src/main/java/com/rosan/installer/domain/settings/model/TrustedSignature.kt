// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.domain.settings.model

data class TrustedSignature(
    val id: Long = 0L,
    val name: String,
    val sha256: String,
    val addedAt: Long
)
