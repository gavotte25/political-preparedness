package com.example.android.politicalpreparedness.repo

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.representative.model.Representative
import retrofit2.HttpException
import java.lang.Exception
import java.net.UnknownHostException

class Repository(private val dao: ElectionDao, private val service: CivicsApiService): BaseRepo {

    override fun getSavedElections(): LiveData<List<Election>> {
        return dao.selectElections()
    }

    override suspend fun getRemoteElection(electionId: Int): Election? {
        return try {
            val response = service.getElections()
            response.elections.find { election -> election.id == electionId }
        } catch (ex: UnknownHostException) {
            ex.printStackTrace()
            null
        }
    }

    override suspend fun getUpcomingElection(): List<Election> {
        return try {
            val response = service.getElections()
            response.elections
        } catch (ex: UnknownHostException) {
            ex.printStackTrace()
            listOf()
        }

    }

    override suspend fun getRepresentatives(address: Address): List<Representative> {
        return try {
            val response = service.getRepresentatives(address.toFormattedString())
            val result = arrayListOf<Representative>()
            for (office in response.offices) {
                result.addAll(office.getRepresentatives(response.officials))
            }
            result
        } catch (ex: Exception) {
            if (ex is UnknownHostException || ex is HttpException) {
                ex.printStackTrace()
            } else {
                throw  ex
            }
            listOf()
        }

    }

    override suspend fun getVoterInfo(electionId: Int, division: Division): AdministrationBody? {
        val address = "${division.state} ${division.country}"
        var response: VoterInfoResponse? = null
        try {
            response = service.getVoterInfo(address, electionId.toLong())
        } catch (ex: Exception) {
            if (ex is HttpException || ex is UnknownHostException) {
                ex.printStackTrace()
            } else {
                throw ex
            }
        }
        response?.state?.let{
            return if(it.isNotEmpty()) {
                it.first().electionAdministrationBody
            } else {
                null
            }
        }
        return null
    }

    override suspend fun getSavedElection(electionId: Int): Election? {
        return dao.selectElection(electionId)
    }

    override suspend fun saveElection(election: Election) {
        dao.insertElection(election)
    }

    override suspend fun deleteElection(electionId: Int) {
        dao.deleteElection(electionId)
    }
}