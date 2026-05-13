// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.domain.engine.usecase

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.rosan.installer.data.engine.parser.SignatureUtils
import com.rosan.installer.domain.engine.repository.AppIconRepository
import com.rosan.installer.domain.settings.model.NamedPackage

/**
 * Use case to find all installed applications that share the same signature hash.
 */
class GetAppsWithSignatureUseCase(
    private val context: Context,
    private val appIconRepo: AppIconRepository
) {

    suspend operator fun invoke(targetSignatureHash: String?): List<NamedPackage> {
        if (targetSignatureHash == null) return emptyList()

        val pm = context.packageManager
        val apps = mutableListOf<NamedPackage>()

        // Use the same flags as SignatureUtils to ensure consistency
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PackageManager.GET_SIGNING_CERTIFICATES
        } else {
            @Suppress("DEPRECATION")
            PackageManager.GET_SIGNATURES
        }

        val installedPackages = pm.getInstalledPackages(flags)

        for (packageInfo in installedPackages) {
            val signatureHash = SignatureUtils.getInstalledAppSignatureHash(context, packageInfo.packageName)
            if (signatureHash == targetSignatureHash) {
                val label = packageInfo.applicationInfo?.loadLabel(pm)?.toString() ?: packageInfo.packageName
                val icon = appIconRepo.getIcon(
                    sessionId = AppIconRepository.SETTINGS_APP_LIST,
                    packageName = packageInfo.packageName,
                    entityToInstall = null,
                    userId = 0,
                    iconSizePx = 128,
                    preferSystemIcon = true
                )
                apps.add(NamedPackage(label, packageInfo.packageName, icon))
            }
        }

        return apps.sortedBy { it.name }
    }
}
