package com.example.cinevote


import com.example.cinevote.screens.auth.viewModel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//val Context.dataStore by preferencesDataStore("theme")
val appModule = module {
    //single { get<Context>().dataStore }
    //single { ThemeRepository(get()) }
    viewModel { LoginViewModel() }
}