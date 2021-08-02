package com.example.android.politicalpreparedness.repo

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative

class Repository: BaseRepo {
    // TODO: implement methods
    override fun getSavedElections(): LiveData<ArrayList<Election>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRemoteElection(electionId: Int): Election? {
        TODO("Not yet implemented")
    }

    override suspend fun getUpcomingElection(): ArrayList<Election> {
        TODO("Not yet implemented")
    }

    override suspend fun getRepresentatives(address: Address): ArrayList<Representative> {
        TODO("Not yet implemented")
    }

    override suspend fun getVoterInfo(electionId: Int, division: Division): AdministrationBody {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedElection(electionId: Int): Election? {
        TODO("Not yet implemented")
    }

    override suspend fun saveElection(election: Election) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteElection(electionId: Int) {
        TODO("Not yet implemented")
    }
}