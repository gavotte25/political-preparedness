package com.example.android.politicalpreparedness.repo

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative

class Repository: BaseRepo {
    // TODO: implement menthods
    override fun getSavedElections(): LiveData<ArrayList<Election>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUpcomingElection(): ArrayList<Election> {
        TODO("Not yet implemented")
    }

    override suspend fun getRepresentatives(address: Address): ArrayList<Representative> {
        TODO("Not yet implemented")
    }
}