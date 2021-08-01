package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repo.BaseRepo
import kotlinx.coroutines.launch

class ElectionsViewModel(private val repo: BaseRepo): ViewModel() {

    private val _upComingElections = MutableLiveData<ArrayList<Election>>()
    private val _savedElections = MediatorLiveData<ArrayList<Election>>()
    val upComingElections: LiveData<ArrayList<Election>>
        get() = _upComingElections
    val savedElections: LiveData<ArrayList<Election>>
        get() = _savedElections

    init {
        viewModelScope.launch {
            _upComingElections.value = repo.getUpcomingElection()
            _savedElections.addSource(repo.getSavedElections(), _savedElections::setValue)
        }
    }

    companion object {
        fun getDirection(election: Election): NavDirections {
            return ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division)
        }
    }
}