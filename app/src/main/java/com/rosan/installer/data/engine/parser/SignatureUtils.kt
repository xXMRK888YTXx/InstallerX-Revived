// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.data.engine.parser

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import timber.log.Timber
import java.security.MessageDigest

import com.rosan.installer.domain.engine.model.SignatureInfo
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * A utility object for extracting and hashing application signatures.
 * REFACTORED to handle unconventional file paths like file descriptors.
 */
object SignatureUtils {

    private const val HASH_ALGORITHM_SHA256 = "SHA-256"
    private const val HASH_ALGORITHM_SHA1 = "SHA-1"
    private const val HASH_ALGORITHM_MD5 = "MD5"

    /**
     * Gets the signature hash from an APK file path.
     *
     * @param context The application context.
     * @param apkPath The absolute path to the APK file, which can be a regular path or a file descriptor path.
     * @return The SHA-256 hash of the first signature, or null if it fails.
     */
    fun getApkSignatureHash(context: Context, apkPath: String): String? {
        return try {
            val packageInfo = getPackageArchiveInfoFromPath(context, apkPath)
            val signature = getFirstSignature(packageInfo)
            signature?.let { hashSignature(it, HASH_ALGORITHM_SHA256) }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get signature hash from APK: $apkPath")
            null
        }
    }

    fun getApkSignatureInfo(context: Context, apkPath: String): SignatureInfo? {
        return try {
            val packageInfo = getPackageArchiveInfoFromPath(context, apkPath)
            val signature = getFirstSignature(packageInfo)
            signature?.let { extractSignatureInfo(it) }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get signature info from APK: $apkPath")
            null
        }
    }

    private fun extractSignatureInfo(signature: Signature): SignatureInfo? {
        return try {
            val certBytes = signature.toByteArray()
            val cf = CertificateFactory.getInstance("X509")
            val cert = cf.generateCertificate(certBytes.inputStream()) as X509Certificate

            SignatureInfo(
                sha1 = hashBytes(certBytes, HASH_ALGORITHM_SHA1),
                sha256 = hashBytes(certBytes, HASH_ALGORITHM_SHA256),
                md5 = hashBytes(certBytes, HASH_ALGORITHM_MD5),
                issuer = cert.issuerX500Principal.name,
                subject = cert.subjectX500Principal.name,
                expiration = cert.notAfter.time,
                notBefore = cert.notBefore.time,
                algorithm = cert.sigAlgName
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to extract X509 info from signature")
            null
        }
    }

    /**
     * Gets the signature hash from an already installed application.
     *
     * @param context The application context.
     * @param packageName The package name of the installed app.
     * @return The SHA-256 hash of the first signature, or null if not found or an error occurs.
     */
    fun getInstalledAppSignatureHash(context: Context, packageName: String): String? {
        return try {
            val packageInfo = getInstalledPackageInfo(context, packageName)
            val signature = getFirstSignature(packageInfo)
            signature?.let { hashSignature(it, HASH_ALGORITHM_SHA256) }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.d("Package not found, can't get signature: $packageName")
            null
        } catch (e: Exception) {
            Timber.e(e, "Failed to get signature hash for installed package: $packageName")
            null
        }
    }

    private fun hashSignature(signature: Signature, algorithm: String): String {
        return hashBytes(signature.toByteArray(), algorithm)
    }

    private fun hashBytes(bytes: ByteArray, algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm)
        val hashBytes = digest.digest(bytes)
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    // Dedicated function to get PackageInfo from an APK file path.
    @Suppress("DEPRECATION")
    private fun getPackageArchiveInfoFromPath(context: Context, apkPath: String): PackageInfo? {
        val pm = context.packageManager
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PackageManager.GET_SIGNING_CERTIFICATES
        } else {
            PackageManager.GET_SIGNATURES
        }
        // This method correctly handles ANY valid file path, including file descriptors.
        return pm.getPackageArchiveInfo(apkPath, flags)
    }

    // Dedicated function to get PackageInfo from an installed package name.
    @Suppress("DEPRECATION")
    private fun getInstalledPackageInfo(context: Context, packageName: String): PackageInfo? {
        val pm = context.packageManager
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PackageManager.GET_SIGNING_CERTIFICATES
        } else {
            PackageManager.GET_SIGNATURES
        }
        return pm.getPackageInfo(packageName, flags)
    }

    /**
     * Safely extracts the first signature from a PackageInfo object.
     * Handles both modern SigningInfo and legacy Signature[] arrays.
     */
    private fun getFirstSignature(packageInfo: PackageInfo?): Signature? {
        packageInfo ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val signingInfo = packageInfo.signingInfo
            if (signingInfo?.hasMultipleSigners() == true) {
                signingInfo.apkContentsSigners?.firstOrNull()
            } else {
                signingInfo?.signingCertificateHistory?.firstOrNull()
            }
        } else {
            @Suppress("DEPRECATION")
            packageInfo.signatures?.firstOrNull()
        }
    }
}