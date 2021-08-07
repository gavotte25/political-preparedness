package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repo.BaseRepo
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repo: BaseRepo): ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    private var _address = MutableLiveData<Address>()
    private var _checkEmptyResultForUserAcknowledgeFlag = false // If it's true and _representatives is empty, a toast will popup
    val representatives: LiveData<List<Representative>>
        get() = _representatives
    val address: LiveData<Address>
        get() = _address
    val checkEmptyResultForUserAcknowledgeFlag: Boolean
        get() = _checkEmptyResultForUserAcknowledgeFlag
    
    fun getRepresentatives() {
        viewModelScope.launch {
            _address.value?.let {
                _checkEmptyResultForUserAcknowledgeFlag = true
                _representatives.postValue(repo.getRepresentatives(it))
            }
        }
    }

    fun resetFlag() {
        _checkEmptyResultForUserAcknowledgeFlag = false
    }

    fun updateAddress(address: Address) {
        this._address.value = address
    }
}
