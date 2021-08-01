package com.example.android.politicalpreparedness.repo

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative

interface BaseRepo {
    // TODO: add methods
    fun getSavedElections(): LiveData<ArrayList<Election>>
    suspend fun getUpcomingElection(): ArrayList<Election>
    suspend fun getRepresentatives(address: Address): ArrayList<Representative>
}