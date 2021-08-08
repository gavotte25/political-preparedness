package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.utils.setNewValue
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.security.InvalidParameterException
import java.util.Locale
import kotlin.jvm.Throws

class DetailFragment : Fragment() {

    private val viewModel by viewModel<RepresentativeViewModel>()
    private lateinit var binding: FragmentRepresentativeBinding
    private val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            checkLocationSettingsAndLoadRepresentatives()
        }
    }
    private val locationSettingsRequest = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        if (checkAndRequestLocationPermissions()) {
            checkLocationSettingsAndLoadRepresentatives(false)
        }
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentRepresentativeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.representativeRecycler.adapter = RepresentativeListAdapter(RepresentativeListener {  })
        binding.representativeRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonSearch.setOnClickListener{
            try {
                hideKeyboard()
                val address = getAddressFromUi()
                viewModel.updateAddress(address)
                viewModel.getRepresentatives()
            } catch (ex: InvalidParameterException) {
                Toast.makeText(requireContext().applicationContext, ex.message,Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonLocation.setOnClickListener{
            hideKeyboard()
            if (checkAndRequestLocationPermissions()) {
                checkLocationSettingsAndLoadRepresentatives()
            }
        }
        binding.executePendingBindings()
        viewModel.address.observe(viewLifecycleOwner, {
            updateAddressViews()
        })
        viewModel.representatives.observe(viewLifecycleOwner, {
            (binding.representativeRecycler.adapter as RepresentativeListAdapter).submitList(it)
            if(viewModel.checkEmptyResultForUserAcknowledgeFlag && it.isEmpty()) {
                Toast.makeText(requireContext(), "No data is found", Toast.LENGTH_SHORT).show()
                viewModel.resetFlag()
            }
        })
        return binding.root
    }

    private fun checkAndRequestLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationSettingsAndLoadRepresentatives(resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply { priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireContext())
        val responseTask = settingsClient.checkLocationSettings(builder.build())
        responseTask.apply {
            addOnSuccessListener {
                val providerClient = FusedLocationProviderClient(requireContext())
                providerClient.lastLocation.addOnSuccessListener {
                    if (it == null) {
                        requestUpdateLocation(providerClient, locationRequest)
                    } else {
                        loadRepresentatives(it)
                    }
                }
            }
            addOnFailureListener {
                if (it is ResolvableApiException && resolve) {
                    try {
                        locationSettingsRequest.launch(IntentSenderRequest.Builder(it.resolution).build())
                    } catch (ex: IntentSender.SendIntentException) {
                        Toast.makeText(requireContext(), "Location should be turn on", Toast.LENGTH_SHORT).show()
                        ex.printStackTrace()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestUpdateLocation(client: FusedLocationProviderClient, request: LocationRequest) {
        Toast.makeText(requireContext(), "Getting location, please wait", Toast.LENGTH_SHORT).show()
        val callback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                p0?.let {
                    loadRepresentatives(it.lastLocation)
                }
            }
        }
        client.requestLocationUpdates(request, callback, Looper.myLooper())
    }

    private fun loadRepresentatives(location: Location) {
        try {
            val address = geoCodeLocation(location)
            viewModel.updateAddress(address)
            viewModel.getRepresentatives()
        } catch (ex: IOException) {
            Toast.makeText(requireContext(), "Please recheck your network", Toast.LENGTH_SHORT).show()
            ex.printStackTrace()
        }
    }

    @Throws(InvalidParameterException::class)
    private fun getAddressFromUi(): Address {
        val addressLine1 = binding.addressLine1.text.toString()
        val addressLine2 = binding.addressLine2.text.toString()
        val city = binding.city.text.toString()
        val zip = binding.zip.text.toString()
        val state = binding.state.selectedItem as String
        if (addressLine1.isEmpty()) {
            throw InvalidParameterException("Please fill in Address line 1")
        }
        if (city.isEmpty()) {
            throw InvalidParameterException("Please fill in City")
        }
        if (zip.isEmpty()) {
            throw InvalidParameterException("Please fill in Zip")
        }
        if (state == "State") {
            throw InvalidParameterException("Please select a valid state")
        }
        return Address(addressLine1, addressLine2, city, state, zip)
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
                    Address(
                            address.thoroughfare ?: "",
                            address.subThoroughfare ?: "",
                            address.locality ?: "",
                            address.adminArea ?: "",
                            address.postalCode ?: "")
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}