package com.dev.anhnd.mybase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private vararg val params: Any) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val listClazz = params.map { clazz -> clazz.javaClass }.toTypedArray()
        val constructor = modelClass.getDeclaredConstructor(*listClazz)
        return constructor.newInstance(*params)
    }
}
