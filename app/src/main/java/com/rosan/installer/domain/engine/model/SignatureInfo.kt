// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2023-2026 iamr0s, InstallerX Revived contributors
package com.rosan.installer.domain.engine.model

import android.os.Parcelable

import com.rosan.installer.domain.settings.model.NamedPackage

data class SignatureInfo(
    val sha1: String,
    val sha256: String,
    val md5: String,
    val issuer: String,
    val subject: String,
    val expiration: Long,
    val notBefore: Long,
    val algorithm: String,
    val isLoadingApps: Boolean = false,
    val appsWithSameSignature: List<NamedPackage> = emptyList()
)
