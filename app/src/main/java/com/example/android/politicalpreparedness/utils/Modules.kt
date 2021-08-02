package com.example.android.politicalpreparedness.utils

import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.repo.BaseRepo
import com.example.android.politicalpreparedness.repo.Repository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ElectionsViewModel(get())
    }
    viewModel {
        RepresentativeViewModel(get())
    }
    viewModel {
        VoterInfoViewModel(get(), androidApplication())
    }
}

val repoModule = module {
    single {
        Repository() as BaseRepo
    }
}