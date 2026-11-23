package com.Fidmowe7.Loskow.presentation.app

import androidx.lifecycle.ViewModel
import com.Fidmowe7.Loskow.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = ArrayList<Screen>()

    private val current = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = current.asStateFlow()

    fun navigate(target: Screen) {
        val now = current.value
        if (now != Screen.Splash) {
            backStack.add(now)
        }
        current.value = target
    }

    fun replace(target: Screen) {
        current.value = target
    }

    fun resetTo(target: Screen) {
        backStack.clear()
        current.value = target
    }

    fun back(): Boolean {
        if (backStack.isEmpty()) return false
        val previous = backStack.removeAt(backStack.lastIndex)
        current.value = previous
        return true
    }
}
