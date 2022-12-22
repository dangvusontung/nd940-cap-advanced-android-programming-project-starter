package com.example.android.politicalpreparedness.representative

import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.BR
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.Result
import com.example.android.politicalpreparedness.base.SingleLiveEvent
import com.example.android.politicalpreparedness.network.RemoteRepository
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import java.util.*
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    private val application: MyApplication,
    private val remoteRepository: RemoteRepository
) : BaseViewModel() {

    private val _representatives =
        MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    val locationButtonClickEvent = SingleLiveEvent<Unit>()
    val representativeButtonClickEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<String>()
    val fetchDataFailureEvent = SingleLiveEvent<Unit>()

    @get:Bindable
    var addressLine1: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.addressLine1)
            }
        }

    @get:Bindable
    var addressLine2: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.addressLine2)
            }
        }

    @get:Bindable
    var city: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.city)
            }
        }

    @get:Bindable
    var state: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.state)
            }
        }

    @get:Bindable
    var zip: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.zip)
            }
        }

    //TODO: Establish live data for representatives and address
    fun representativeSearch() {
        representativeButtonClickEvent.value = Unit
    }

    //TODO: Create function get address from geo location
    fun locationButtonClick() {
        locationButtonClickEvent.value = Unit
    }

    fun updateLocation(location: Location) {
        val address = geoCodeLocation(location)
        addressLine1 = address.line1
        addressLine2 = address.line2 ?: ""
        city = address.city
        state = address.state
        zip = address.zip
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(application, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1).map { address ->
            Address(
                line1 = address.thoroughfare ?: "",
                line2 = address.subThoroughfare ?: "",
                city = address.locality ?: "",
                state = address.adminArea ?: "",
                zip = address.postalCode ?: ""
            )
        }.first()
    }

    fun validateAndFetchRepresentative() {
        if (addressLine1.isEmpty()) {
            errorEvent.value = application.getString(R.string.address_line_1_empty)
            return
        }

        if (city.isEmpty()) {
            errorEvent.value = application.getString(R.string.city_empty)
            return
        }

        if (state.isEmpty()) {
            errorEvent.value = application.getString(R.string.state_empty)
            return
        }

        if (zip.isEmpty()) {
            errorEvent.value = application.getString(R.string.zip_empty)
            return
        }

        fetchRepresentative()
    }

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */


    //TODO: Create function to get address from individual fields
    private fun fetchRepresentative() {
        viewModelScope.launch {
           loadingEvent.postValue(true)

            val address = Address(
                line1 = addressLine1,
                line2 = addressLine2,
                city = city,
                state = state,
                zip = zip,
            ).toFormattedString()
            val result = remoteRepository.getRepresentatives(address)
            if (result is Result.Success) {
                val officials = result.data.officials
                val offices = result.data.offices
                val representatives = mutableListOf<Representative>()
                offices.forEach { item ->
                    representatives.addAll(item.getRepresentatives(officials))
                }
                _representatives.value = representatives
            } else {
                errorEvent.value = application.getString(R.string.cannot_get_representatives)
                fetchDataFailureEvent.value = Unit
            }

            loadingEvent.postValue(false)
        }
    }

}
