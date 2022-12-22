package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.BR
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseBindingFragment
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.RemoteRepository
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.google.android.gms.location.LocationServices
import java.util.*

class DetailFragment :
    BaseBindingFragment<RepresentativeViewModel, FragmentRepresentativeBinding>() {

    companion object {
        //TODO: Add Constant for Location request
        const val REQUEST_LOCATION_CODE = 372
        const val TAG = "DetailFragment"
    }

    //TODO: Declare ViewModel
    override val viewModel: RepresentativeViewModel by lazy {
        RepresentativeViewModelFactory(
            MyApplication.instance, RemoteRepository.getInstance()
        ).create(
            RepresentativeViewModel::class.java
        )
    }
    private lateinit var representativeListAdapter: RepresentativeListAdapter

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(
                    requireContext(), R.string.location_permission_required, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkLocationPermissions() {
        return if (isPermissionGranted()) {
            getLocation()
        } else {
            //TODO: Request Location permissions
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE
        )
    }

    private fun isPermissionGranted(): Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation.addOnSuccessListener {
            Log.d(TAG, "getLocation: $it")
            it?.let {
                viewModel.updateLocation(it)
            } 
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun getLayoutId(): Int = R.layout.fragment_representative

    override fun getViewModelBindingVariable(): Int = BR.viewModel

    override fun initView() {
        //TODO: Establish bindings
        binding.viewModel = viewModel

        //TODO: Define and assign Representative adapter
        representativeListAdapter =
            RepresentativeListAdapter(
                representativeListener = RepresentativeListener {

                })
        //TODO: Populate Representative adapter
        binding.rvRepresentative.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = representativeListAdapter
        }
    }

    override fun initViewModel() {

    }

    override fun setEventListeners() {
        super.setEventListeners()
        viewModel.locationButtonClickEvent.observe(viewLifecycleOwner) {
            checkLocationPermissions()
        }

        viewModel.representativeButtonClickEvent.observe(viewLifecycleOwner) {
            hideKeyboard()
            viewModel.validateAndFetchRepresentative()
        }

        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.state = p0?.getItemAtPosition(p2) as String? ?: ""
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d(TAG, "onNothingSelected")
            }
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        //TODO: Establish button listeners for field and location search
        viewModel.representatives.observe(viewLifecycleOwner) {
            binding.listPlaceholder.visibility = View.GONE
            representativeListAdapter.submitList(it)
        }

        viewModel.fetchDataFailureEvent.observe(viewLifecycleOwner) {
            binding.listPlaceholder.visibility = View.VISIBLE
        }
    }
}