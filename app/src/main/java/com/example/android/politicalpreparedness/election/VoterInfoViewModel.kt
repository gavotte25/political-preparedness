package com.example.android.politicalpreparedness.election

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repo.BaseRepo
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val repo: BaseRepo, app: Application) : AndroidViewModel(app) {
    private var voterInfo: AdministrationBody? = null
    private val _election = MutableLiveData<Election>()
    private val _isSaved = MutableLiveData<Boolean>(false)
    val election: LiveData<Election>
        get() = _election
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    fun getVoterInfo(electionId: Int, division: Division) {
        viewModelScope.launch {
            _election.postValue(repo.getSavedElection(electionId))
            if(_election.value == null) {
                _election.postValue(repo.getRemoteElection(electionId))
                _isSaved.postValue(false)
            } else {
                _isSaved.postValue(true)
            }
            voterInfo = repo.getVoterInfo(electionId, division)
        }
    }

    fun onClickVotingLocations() {
        voterInfo?.votingLocationFinderUrl?.let {
            startActivity(getApplication(), Intent(Intent.ACTION_VIEW, Uri.parse(it)), null)
        }
    }

    fun onClickBallotInformation() {
        voterInfo?.ballotInfoUrl?.let {
            startActivity(getApplication(), Intent(Intent.ACTION_VIEW, Uri.parse(it)), null)
        }
    }

    fun saveOrRemoveElection() {
        viewModelScope.launch {
            _election.value?.let {
                if (_isSaved.value == true) {
                    repo.deleteElection(it.id)
                    _isSaved.postValue(false)
                } else {
                    repo.saveElection(it)
                    _isSaved.postValue(true)
                }
            }

        }
    }

}