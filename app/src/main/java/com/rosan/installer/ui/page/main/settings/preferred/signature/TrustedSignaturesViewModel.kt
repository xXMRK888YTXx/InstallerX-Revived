// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2025-2026 InstallerX Revived contributors
package com.rosan.installer.ui.page.main.settings.preferred.signature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosan.installer.domain.settings.usecase.signature.GetTrustedSignaturesUseCase
import com.rosan.installer.domain.settings.usecase.signature.ManageTrustedSignatureUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrustedSignaturesViewModel(
    private val getTrustedSignatures: GetTrustedSignaturesUseCase,
    private val manageTrustedSignature: ManageTrustedSignatureUseCase
) : ViewModel() {

    val state: StateFlow<TrustedSignaturesViewState> = getTrustedSignatures()
        .map { TrustedSignaturesViewState(signatures = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TrustedSignaturesViewState()
        )

    fun dispatch(action: TrustedSignaturesViewAction) {
        when (action) {
            is TrustedSignaturesViewAction.Add -> viewModelScope.launch {
                manageTrustedSignature.add(action.name, action.sha256)
            }

            is TrustedSignaturesViewAction.Delete -> viewModelScope.launch {
                manageTrustedSignature.delete(action.sha256)
            }
        }
    }
}
