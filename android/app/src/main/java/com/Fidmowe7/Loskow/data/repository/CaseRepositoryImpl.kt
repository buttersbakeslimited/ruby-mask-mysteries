package com.Fidmowe7.Loskow.data.repository

import com.Fidmowe7.Loskow.data.local.CasePrefs
import com.Fidmowe7.Loskow.domain.model.CaseState
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CaseRepositoryImpl(private val prefs: CasePrefs) : CaseRepository {

    private val backing = MutableStateFlow(prefs.read())

    override val state: StateFlow<CaseState> = backing.asStateFlow()

    override fun update(transform: (CaseState) -> CaseState) {
        val next = transform(backing.value)
        backing.value = next
        prefs.write(next)
    }

    override fun reset() {
        prefs.clear()
        backing.value = prefs.read()
    }
}
