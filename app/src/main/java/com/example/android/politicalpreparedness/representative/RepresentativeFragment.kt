package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.utils.setNewValue
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
        val stateList = arrayListOf<String>() // TODO: add some data here
    }

    private val viewModel by viewModel<RepresentativeViewModel>()
    private lateinit var binding: FragmentRepresentativeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentRepresentativeBinding>(inflater, R.layout.fragment_representative, container, false)
        binding.lifecycleOwner = this

        binding.representativeRecycler.adapter = RepresentativeListAdapter(RepresentativeListener {  })
        binding.state.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, stateList)
        binding.buttonSearch.setOnClickListener{
            val address = getAddressFromUi()
            viewModel.updateAddress(address)
            viewModel.getRepresentatives()
        }
        binding.buttonLocation.setOnClickListener{
            val location = getLocation()
            val address = geoCodeLocation(location)
            viewModel.updateAddress(address)
            viewModel.getRepresentatives()
        }
        viewModel.address.observe(viewLifecycleOwner, Observer{

        })
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            requestPermission()
            true
        } else {
            //TODO: Request Location permissions
            false
        }
    }

    private fun requestPermission() {
        // TODO
    }

    private fun isPermissionGranted() : Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return false
    }

    private fun getLocation(): Location {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        return Location("TODO")
    }

    private fun getAddressFromUi(): Address{
        return Address(
                binding.addressLine1.text.toString(),
                binding.addressLine2.text.toString(),
                binding.city.text.toString(),
                binding.state.selectedItem as String,
                binding.zip.text.toString()
        )
    }

    private fun updateAddressViews() {
        viewModel.address.value?.apply {
            binding.addressLine1.setText(this.line1)
            binding.addressLine2.setText(this.line2)
            binding.city.setText(this.city)
            binding.state.setNewValue(this.state)
            binding.zip.setText(this.zip)
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}