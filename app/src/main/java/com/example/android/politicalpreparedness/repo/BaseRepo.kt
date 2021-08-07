package com.example.android.politicalpreparedness.repo

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative

interface BaseRepo {
    fun getSavedElections(): LiveData<List<Election>>
    suspend fun getUpcomingElection(): List<Election>
    suspend fun getRepresentatives(address: Address): List<Representative>
    suspend fun getVoterInfo(electionId: Int, division: Division): AdministrationBody?
    suspend fun getSavedElection(electionId: Int): Election?
    suspend fun saveElection(election: Election)
    suspend fun deleteElection(electionId: Int)
    suspend fun getRemoteElection(electionId: Int): Election?
}