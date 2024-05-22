package com.example.cinevote


import androidx.room.Room
import com.example.cinevote.data.database.Room.CineVoteDatabase
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.screens.cinema.cinemaVm
import com.example.cinevote.screens.details.DetailsVM
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
    single {
        Room.databaseBuilder(
            get(),
            CineVoteDatabase::class.java,
            "todo-list"
        ).build()
    }


    single { FilmRepository(get<CineVoteDatabase>().FilmDAO()) }
    viewModel {  SignupViewModel() }

    viewModel {  LoginViewModel(get()) }
    viewModel {  cinemaVm() }
    viewModel {  SettingsVm() }
    viewModel {  OutNowVM() }
    viewModel {  HomeVM(get()) }
    viewModel {  DetailsVM(get()) }
    viewModel {  ExpandVM(get()) }
}