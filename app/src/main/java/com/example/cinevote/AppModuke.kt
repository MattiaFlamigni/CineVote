package com.example.cinevote


import androidx.room.Room
import com.example.cinevote.data.database.Room.CineVoteDatabase
import com.example.cinevote.data.database.Room.MIGRATION_1_2
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.data.repository.reviewRepository
import com.example.cinevote.screens.cinema.cinemaVm
import com.example.cinevote.screens.details.DetailsVM
import com.example.cinevote.screens.expandView.ExpandVM
import com.example.cinevote.screens.home.HomeVM
import com.example.cinevote.screens.login.LoginViewModel
import com.example.cinevote.screens.outNow.OutNowVM
import com.example.cinevote.screens.review.reviewVM
import com.example.cinevote.screens.settings.SettingsVm
import com.example.cinevote.screens.signUp.SignupViewModel
import com.example.cinevote.screens.wishList.WishListVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//val Context.dataStore by preferencesDataStore("theme")
val appModule = module {
    //single { get<Context>().dataStore }
    single {
        Room.databaseBuilder(
            androidContext(),
            CineVoteDatabase::class.java,
            "cinevote_database"
        )
            .fallbackToDestructiveMigration()  // Cancella il database esistente se non esiste una migrazione appropriata
            .build()
    }


    single { FilmRepository(get<CineVoteDatabase>().FilmDAO()) }
    single { reviewRepository(get<CineVoteDatabase>().ReviewDAO())}
    viewModel {  SignupViewModel() }

    viewModel {  LoginViewModel(get()) }
    viewModel {  cinemaVm() }
    viewModel {  SettingsVm() }
    viewModel {  OutNowVM() }
    viewModel {  HomeVM(get()) }
    viewModel {  DetailsVM(get()) }
    viewModel {  ExpandVM(get()) }
    viewModel {  reviewVM(get()) }
    viewModel {  WishListVM(get()) }
}