package com.Fidmowe7.Loskow.domain.repository

import com.Fidmowe7.Loskow.domain.model.CaseState
import kotlinx.coroutines.flow.StateFlow

interface CaseRepository {
    val state: StateFlow<CaseState>
    fun update(transform: (CaseState) -> CaseState)
    fun reset()
}
