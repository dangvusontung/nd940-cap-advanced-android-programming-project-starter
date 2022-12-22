package com.example.android.politicalpreparedness.base

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.navigator.NavigationCommand
import com.example.android.politicalpreparedness.navigator.Navigator

abstract class BaseViewModel : ViewModel(), Observable {
    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    val loadingEvent by lazy { SingleLiveEvent<Boolean>() }
    private var _navigator: Navigator? = null
    val navigateEvent by lazy { SingleLiveEvent<NavigationCommand>() }
    val navigator get() = _navigator

    fun setNavigator(navigator: Navigator) {
        _navigator = navigator
    }

    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

    fun navigate(navigationCommand: NavigationCommand) {
        navigateEvent.value = navigationCommand
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }
}