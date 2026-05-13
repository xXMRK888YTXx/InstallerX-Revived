// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.ui.page.main.settings.preferred.signature

sealed interface TrustedSignaturesViewAction {
    data class Add(val name: String, val sha256: String) : TrustedSignaturesViewAction
    data class Delete(val sha256: String) : TrustedSignaturesViewAction
}
