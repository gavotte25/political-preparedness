package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repo.Repository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repo: Repository): ViewModel() {

    private val _representatives = MutableLiveData(ArrayList<Representative>())
    private var _address = MutableLiveData<Address>()
    val representatives: LiveData<ArrayList<Representative>>
        get() = _representatives
    val address: LiveData<Address>
        get() = _address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
    // TODO: this likely needs to be changed
    fun getRepresentatives() {
        viewModelScope.launch {
            _address.value?.let { repo.getRepresentatives(it) }
        }
    }

    fun updateAddress(address: Address) {
        this._address.value = address
    }
}
