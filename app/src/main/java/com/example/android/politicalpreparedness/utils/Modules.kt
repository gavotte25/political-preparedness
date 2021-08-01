package com.example.android.politicalpreparedness

import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.repo.BaseRepo
import com.example.android.politicalpreparedness.repo.Repository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ElectionsViewModel(get())
    }
    viewModel {
        RepresentativeViewModel(get())
    }
}

val repoModule = module {
    single {
        Repository() as BaseRepo
    }
}