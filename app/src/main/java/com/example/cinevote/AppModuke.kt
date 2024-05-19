package com.example.cinevote


import com.example.cinevote.screens.cinema.cinemaVm
import com.example.cinevote.screens.expandView.ExpandVM
import com.example.cinevote.screens.home.HomeVM
import com.example.cinevote.screens.login.LoginViewModel
import com.example.cinevote.screens.outNow.OutNowVM
import com.example.cinevote.screens.settings.SettingsVm
import com.example.cinevote.screens.signUp.SignupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//val Context.dataStore by preferencesDataStore("theme")
val appModule = module {
    //single { get<Context>().dataStore }
    single {  }
    viewModel {  SignupViewModel() }

    viewModel {  LoginViewModel() }
    viewModel {  cinemaVm() }
    viewModel {  SettingsVm() }
    viewModel {  OutNowVM() }
    viewModel {  HomeVM() }
    viewModel {  ExpandVM() }
}