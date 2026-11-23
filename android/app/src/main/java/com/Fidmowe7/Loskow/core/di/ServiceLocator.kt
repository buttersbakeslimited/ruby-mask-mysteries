package com.Fidmowe7.Loskow.core.di

import android.content.Context
import com.Fidmowe7.Loskow.data.local.CasePrefs
import com.Fidmowe7.Loskow.data.local.SettingsPrefs
import com.Fidmowe7.Loskow.data.repository.CaseRepositoryImpl
import com.Fidmowe7.Loskow.data.repository.SettingsRepositoryImpl
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.repository.SettingsRepository

object ServiceLocator {

    private var caseRepo: CaseRepository? = null
    private var settingsRepo: SettingsRepository? = null
    private var viewModelFactory: AppViewModelFactory? = null

    fun init(context: Context) {
        if (viewModelFactory != null) return
        val application = context.applicationContext
        val cases = CaseRepositoryImpl(CasePrefs(application))
        val settings = SettingsRepositoryImpl(SettingsPrefs(application))
        caseRepo = cases
        settingsRepo = settings
        viewModelFactory = AppViewModelFactory(cases, settings)
    }

    val caseRepository: CaseRepository
        get() = requireNotNull(caseRepo)

    val settingsRepository: SettingsRepository
        get() = requireNotNull(settingsRepo)

    val factory: AppViewModelFactory
        get() = requireNotNull(viewModelFactory)
}
